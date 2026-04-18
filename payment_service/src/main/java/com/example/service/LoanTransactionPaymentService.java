package com.example.service;


import com.example.dto.LoanPaymentScheduleDTO;
import com.example.dto.response.LoanPaymentScheduleResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;

@Service

public interface LoanTransactionPaymentService {
   List<LoanPaymentScheduleResponse> choosePaymentSchedule(List<LoanPaymentScheduleDTO> loanPaymentSchedules, String contractId);

    @Transactional
    LoanPaymentScheduleResponse executePayment(String scheduleId, BigDecimal amount);

}
