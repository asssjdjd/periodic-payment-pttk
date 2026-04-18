package com.example.service.impl;


import com.example.dto.LoanPaymentScheduleDTO;
import com.example.model.LoanPaymentSchedule;
import com.example.repository.LoanPaymentScheduleRepository;
import com.example.service.FinanceConstants;
import com.example.service.LoanPaymentScheduleState;
import com.example.service.ScheduleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OverdueStateImpl implements ScheduleState {

    private final LoanPaymentScheduleRepository scheduleRepository;


    @Override
    public BigDecimal pay(String loanPaymentScheduleId, BigDecimal amount) {
        LoanPaymentSchedule entity = scheduleRepository.findById(loanPaymentScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kỳ thanh toán với ID: " + loanPaymentScheduleId));
        log.info("[OverdueStateImpl] : Xử lý thanh toán cho kỳ QUÁ HẠN ID: {}, Số tiền nộp: {}", entity.getContractId(), amount);
        BigDecimal remaining = amount;

        // 1. Trừ tiền phạt (Penalty)
        // Tiền phạt = tiền phạt -tiền phạt đã đóng.
        BigDecimal penaltyOwed = entity.getPenaltyFee().subtract(entity.getPenaltyFeePaid());

        if (remaining.compareTo(penaltyOwed) >= 0) {
            entity.setPenaltyFeePaid(new BigDecimal(100000));
            remaining = remaining.subtract(penaltyOwed);
        } else {
            entity.setPenaltyFeePaid(entity.getPenaltyFeePaid().add(remaining));
            return BigDecimal.ZERO; // Hết tiền, dừng tại đây
        }

        // 1. Trừ tiền lãi quá hạn (OverdueInterestPaid)
        BigDecimal overdueInterestOwed= entity.getOverdueInterest().subtract(entity.getOverdueInterestPaid());

        if (remaining.compareTo(overdueInterestOwed) >= 0) {
            entity.setOverdueInterestPaid(entity.getOverdueInterest());
            remaining = remaining.subtract(overdueInterestOwed);
        } else {
            entity.setOverdueInterestPaid(entity.getOverdueInterestPaid().add(remaining));
            return BigDecimal.ZERO; // Hết tiền, dừng tại đây
        }

        // 2. Trừ lãi lãi định kỳ (Interest)
        BigDecimal interestOwed = entity.getInterestDue().subtract(entity.getInterestPaid());
        if (remaining.compareTo(interestOwed) >= 0) {
            entity.setInterestPaid(entity.getInterestDue());
            remaining = remaining.subtract(interestOwed);
        } else {
            entity.setInterestPaid(entity.getInterestPaid().add(remaining));
            return BigDecimal.ZERO;
        }

        // 3. Trừ gốc (Principal)
        BigDecimal principalOwed = entity.getPrincipalDue().subtract(entity.getPrinciplePaid());
        if (remaining.compareTo(principalOwed) >= 0) {
            entity.setPrinciplePaid(entity.getPrincipalDue());
            remaining = remaining.subtract(principalOwed);
            entity.setStatus("PAID"); // Đã trả hết cả gốc lẫn lãi phạt
        } else {
            entity.setPrinciplePaid(entity.getPrinciplePaid().add(remaining));
//            entity.setStatus("PARTIALLY_PAID"); vẫn là quá hạn
            remaining = BigDecimal.ZERO;
        }
        entity.setDueDate(LocalDateTime.now());
        scheduleRepository.save(entity);
        return remaining;
    }



}
