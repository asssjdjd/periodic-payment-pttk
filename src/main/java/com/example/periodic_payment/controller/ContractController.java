package com.example.periodic_payment.controller;

import com.example.periodic_payment.dto.LoanPaymentScheduleDTO;
import com.example.periodic_payment.dto.response.*;
import com.example.periodic_payment.dto.resquest.PaymentRequest;
import com.example.periodic_payment.service.ContractService;
import com.example.periodic_payment.service.LoanTransactionPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;
    private final LoanTransactionPaymentService loanTransactionPaymentService;

    @GetMapping("/{customerId}/contracts/active")
    public ApiResponse getActiveContracts(@PathVariable Long customerId) {
        List<ContractResponse> result = contractService.getActiveContractsByCustomerId(customerId);
        return new SuccessResponse(result, "Truy vấn danh sách hợp đồng thành công");
    }

    @GetMapping("/{customerId}/schedule/{contractId}")
    public ApiResponse getLoanPaymentSchedule(@PathVariable Long customerId,@PathVariable Long contractId) {
        List<ContractResponse> result = contractService.getActiveContractsByCustomerId(customerId);

        Integer contract = Math.toIntExact(contractId);
        ContractResponse choosedContract = result.get(contract);
        Long contractRealId = choosedContract.getId();
        List<LoanPaymentScheduleDTO> loanPaymentScheduleDTOS = choosedContract.getPaymentSchedules();

        List<LoanPaymentScheduleResponse> loanPaymentScheduleResponse =  loanTransactionPaymentService.choosePaymentSchedule(loanPaymentScheduleDTOS,contractRealId);
        return new SuccessResponse(loanPaymentScheduleResponse, "Truy vấn danh sách hợp đồng thành công");
    }

    @PostMapping("/{customerId}/contracts/payment")
    public ApiResponse paymentLoanPaymentSchedule(
            @PathVariable Long customerId,
            @RequestBody PaymentRequest request) { // Request chứa scheduleId và amount

        // Kiểm tra tính hợp lệ cơ bản của số tiền
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return new ErrorResponse("Số tiền thanh toán phải lớn hơn 0");
        }

        try {
            LoanPaymentScheduleResponse response = loanTransactionPaymentService
                    .executePayment(request.getScheduleId(), request.getAmount());

            return new SuccessResponse(response, "Thanh toán kỳ hạn thành công");
        } catch (Exception e) {
            return new ErrorResponse("Lỗi hệ thống: " + e.getMessage());
        }
    }

}
