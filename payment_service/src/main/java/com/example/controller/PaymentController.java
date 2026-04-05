package com.example.controller;


import com.example.dto.LoanPaymentScheduleDTO;
import com.example.dto.response.*;
import com.example.dto.resquest.PaymentRequest;
import com.example.service.ContractService;
import com.example.service.LoanTransactionPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final ContractService contractService;
    private final LoanTransactionPaymentService loanTransactionPaymentService;

    @GetMapping("/{customerId}/contracts/active")
    public ApiResponse getActiveContracts(@PathVariable Long customerId) {
        log.info("📋 [Payment Service] Processing getActiveContracts request - customerId: {}", customerId);
        
        try {
            List<ContractResponse> result = contractService.getActiveContractsByCustomerId(customerId);
            log.info("✅ [Payment Service] getActiveContracts successful - found {} contracts", result.size());
            return new SuccessResponse(result, "Truy vấn danh sách hợp đồng thành công");
        } catch (Exception e) {
            log.error("❌ [Payment Service] Error getting active contracts: ", e);
            throw e;
        }
    }

    @GetMapping("/{customerId}/schedule/{contractId}")
    public ApiResponse getLoanPaymentSchedule(@PathVariable Long customerId,@PathVariable Long contractId) {
        log.info("📅 [Payment Service] Processing getLoanPaymentSchedule - customerId: {}, contractId: {}", 
                customerId, contractId);
        
        try {
            List<ContractResponse> result = contractService.getActiveContractsByCustomerId(customerId);

            Integer contract = Math.toIntExact(contractId);
            ContractResponse choosedContract = result.get(contract);
            List<LoanPaymentScheduleDTO> loanPaymentScheduleDTOS = choosedContract.getPaymentSchedules();
            Long contractRealId = choosedContract.getId();
            List<LoanPaymentScheduleResponse> loanPaymentScheduleResponse =  
                    loanTransactionPaymentService.choosePaymentSchedule(loanPaymentScheduleDTOS,contractRealId);

            log.info("✅ [Payment Service] getLoanPaymentSchedule successful - found {} schedules", 
                    loanPaymentScheduleResponse.size());
            return new SuccessResponse(loanPaymentScheduleResponse, "Truy vấn danh sách hợp đồng thành công");
        } catch (Exception e) {
            log.error("❌ [Payment Service] Error getting loan payment schedule: ", e);
            throw e;
        }
    }

    @PostMapping("/{customerId}/contracts/payment")
    public ApiResponse paymentLoanPaymentSchedule(
            @PathVariable Long customerId,
            @RequestBody PaymentRequest request) { // Request chứa scheduleId và amount

        log.info("💳 [Payment Service] Processing payment - customerId: {}, scheduleId: {}, amount: {}", 
                customerId, request.getScheduleId(), request.getAmount());
        
        // Kiểm tra tính hợp lệ cơ bản của số tiền
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("⚠️ [Payment Service] Invalid payment amount: {}", request.getAmount());
            return new ErrorResponse("Số tiền thanh toán phải lớn hơn 0");
        }

        try {
            LoanPaymentScheduleResponse response = loanTransactionPaymentService
                    .executePayment(request.getScheduleId(), request.getAmount());

            log.info("✅ [Payment Service] Payment successful - scheduleId: {}, amount: {}", 
                    request.getScheduleId(), request.getAmount());
            return new SuccessResponse(response, "Thanh toán kỳ hạn thành công");
        } catch (Exception e) {
            log.error("❌ [Payment Service] Payment error - scheduleId: {}, amount: {}", 
                    request.getScheduleId(), request.getAmount(), e);
            return new ErrorResponse("Lỗi hệ thống: " + e.getMessage());
        }
    }

}
