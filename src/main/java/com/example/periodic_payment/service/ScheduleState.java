package com.example.periodic_payment.service;

import com.example.periodic_payment.dto.LoanPaymentScheduleDTO;
import com.example.periodic_payment.dto.response.ContractResponse;
import com.example.periodic_payment.model.LoanPaymentSchedule;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface ScheduleState {
    void recalculate(LoanPaymentScheduleDTO context);

    @Transactional
    BigDecimal pay(Long loanPaymentScheduleId, BigDecimal money);
}
