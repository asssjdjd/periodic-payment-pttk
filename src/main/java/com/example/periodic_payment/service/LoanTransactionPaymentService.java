package com.example.periodic_payment.service;

import com.example.periodic_payment.dto.LoanPaymentScheduleDTO;
import com.example.periodic_payment.dto.response.LoanPaymentScheduleResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;

@Service

public interface LoanTransactionPaymentService {
    List<LoanPaymentScheduleResponse> choosePaymentSchedule(List<LoanPaymentScheduleDTO> loanPaymentSchedules,Long contractId);

    @Transactional
    LoanPaymentScheduleResponse executePayment(Long scheduleId, BigDecimal amount);

}
