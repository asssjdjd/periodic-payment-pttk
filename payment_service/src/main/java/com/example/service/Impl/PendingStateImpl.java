package com.example.service.Impl;


import com.example.model.LoanPaymentSchedule;
import com.example.repository.LoanPaymentScheduleRepository;
import com.example.service.ScheduleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PendingStateImpl implements ScheduleState {

    private final OverdueStateImpl overdueState;
    private final LoanPaymentScheduleRepository scheduleRepository;


    @Override
    @Transactional
    public BigDecimal pay(String loanPaymentScheduleId, BigDecimal amount) {

        LoanPaymentSchedule entity = scheduleRepository.findById(loanPaymentScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kỳ thanh toán với ID: " + loanPaymentScheduleId));
        log.info("[PendingStateImpl] : Xử lý thanh toán cho kỳ ĐẾN HẠN ID: {}, Số tiền nộp: {}", entity.getContractId(), amount);

        BigDecimal remaining = amount;

        // 1. Trừ lãi trước
        BigDecimal interestOwed = entity.getInterestDue().subtract(entity.getInterestPaid());
        if (remaining.compareTo(interestOwed) >= 0) {
            entity.setInterestPaid(entity.getInterestDue());
            remaining = remaining.subtract(interestOwed);
        } else {
            entity.setInterestPaid(entity.getInterestPaid().add(remaining));
            entity.setStatus("PARTIALLY_PAID");
            return BigDecimal.ZERO;
        }

        // 2. Trừ gốc
        BigDecimal principalOwed = entity.getPrincipalDue().subtract(entity.getPrinciplePaid());
        if (remaining.compareTo(principalOwed) >= 0) {
            entity.setPrinciplePaid(entity.getPrincipalDue());
            remaining = remaining.subtract(principalOwed);
            entity.setStatus("PAID");
            entity.setDueDate(LocalDateTime.now());
        }else {
            entity.setPrinciplePaid(entity.getPrinciplePaid().add(remaining));
            remaining = BigDecimal.ZERO;
        }
;
        scheduleRepository.save(entity);
        return remaining;
    }
}
