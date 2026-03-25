package com.example.service;


import com.example.dto.LoanPaymentScheduleDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface ScheduleState {
    void recalculate(LoanPaymentScheduleDTO context);

    @Transactional
    BigDecimal pay(Long loanPaymentScheduleId, BigDecimal money);
}
