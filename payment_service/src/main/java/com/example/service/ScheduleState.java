package com.example.service;


import com.example.dto.LoanPaymentScheduleDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface ScheduleState {
    @Transactional
    BigDecimal pay(String loanPaymentScheduleId, BigDecimal money,  BigDecimal penaltyFee);
}
