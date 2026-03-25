package com.example.service.impl;


import com.example.dto.LoanPaymentScheduleDTO;
import com.example.exception.ExceptionCode;
import com.example.exception.ResourceException;
import com.example.model.LoanPaymentSchedule;
import com.example.repository.LoanPaymentScheduleRepository;
import com.example.service.ScheduleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class PendingStateImpl implements ScheduleState {

    private final OverdueStateImpl overdueState;
    private final LoanPaymentScheduleRepository scheduleRepository;

    @Override
    public void recalculate(LoanPaymentScheduleDTO context) {
        if (LocalDate.now().isAfter(context.getDueDate())) {
            // Đã quá hạn -> Chuyển giao cho OverdueState xử lý
            overdueState.recalculate(context);
        }else {
            throw new ResourceException(ExceptionCode.LOAN_PAYMENT_SCHEDUE.getCode(),ExceptionCode.LOAN_PAYMENT_SCHEDUE.getMessage());
        }
    }

    @Override
    @Transactional
    public BigDecimal pay(Long loanPaymentScheduleId, BigDecimal amount) {

        LoanPaymentSchedule entity = scheduleRepository.findById(loanPaymentScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kỳ thanh toán với ID: " + loanPaymentScheduleId));
        log.info("[PendingStateImpl] : Xử lý thanh toán cho kỳ ĐẾN HẠN ID: {}, Số tiền nộp: {}", entity.getId(), amount);

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
        } else {
            entity.setPrinciplePaid(entity.getPrinciplePaid().add(remaining));
            entity.setStatus("PARTIALLY_PAID");
            remaining = BigDecimal.ZERO;
        }
//        entity.setDueDate(LocalDate.now());
        scheduleRepository.save(entity);
        return remaining;
    }
}
