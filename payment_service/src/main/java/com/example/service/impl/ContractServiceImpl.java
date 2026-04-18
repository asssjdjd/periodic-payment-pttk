package com.example.service.impl;


import com.example.dto.LoanPaymentScheduleDTO;
import com.example.dto.response.ContractResponse;
import com.example.model.Contract;
import com.example.model.LoanPaymentSchedule;
import com.example.repository.ContractRepository;
import com.example.repository.LoanPaymentScheduleRepository;
import com.example.service.ContractService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
//    private final CustomerRepository customerRepository;
    private final LoanPaymentScheduleRepository loanPaymentScheduleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ContractResponse> getActiveContractsByCustomerId(String customerId) {
        log.info("[Contract Service] Lấy danh sách hợp đồng ACTIVE cho khách hàng ID: {}", customerId);

        // 1. Kiểm tra khách hàng tồn tại (Dùng existsById để tối ưu hiệu năng)
//        if (!customerRepository.existsById(customerId)) {
//            throw new ResourceException(ExceptionCode.DATA_NOT_FOUND.getCode(), "Không tìm thấy khách hàng yêu cầu");
//        }

        // 2. Lấy dữ liệu đã được tuning
        List<Contract> contracts = contractRepository.findByCustomerIdAndStatus(customerId, "ACTIVE");

        List<Contract> results = new ArrayList<>();

        // Cập nhật trạng thái hợp đồng.
        List<ContractResponse> contractResponses = new ArrayList<>();

        for(Contract contract : contracts) {
            List<LoanPaymentSchedule> loanPaymentSchedules = loanPaymentScheduleRepository.findByContractId(contract.getId());

            boolean isActive = false;
            for(LoanPaymentSchedule loanPaymentSchedule : loanPaymentSchedules) {
                if(!"PAID".equalsIgnoreCase(loanPaymentSchedule.getStatus())) {
                    isActive = true;
                    break;
                }
            }
            if(isActive) {

                results.add(contract);
                List<LoanPaymentScheduleDTO> loanPaymentScheduleDtos = new ArrayList<>();

                BigDecimal principleDueRate = contract.getPrincipalDueRate();
                BigDecimal interestDueRate = contract.getInterestDueRate();
                BigDecimal overdueInterestRate = contract.getOverdueInterestRate();

                // map LoanPayment
                for(LoanPaymentSchedule loanPaymentSchedule : loanPaymentSchedules) {
                    loanPaymentScheduleDtos.add(LoanPaymentScheduleDTO.mapLoanPaymentScheduleDto(loanPaymentSchedule,principleDueRate,interestDueRate,overdueInterestRate));
                }

                ContractResponse contractResponse = ContractResponse.builder()
                        .id(contract.getId())
                        .code(contract.getCode())
                        .status("ACTIVE")
                        .loanAmount(contract.getLoanAmount())
                        .paymentSchedules(loanPaymentScheduleDtos)
                        .prepaidAmount(contract.getPrepaidAmount())
                        .productPrice(contract.getProductPrice())
                        .signedDate(contract.getSignedDate())
                        .build();

                contractResponses.add(contractResponse);
            }else {
                // nếu toàn bộ đã thanh toán uơdate lại
                contract.setStatus("COMPLETED");
                contractRepository.save(contract);
            }
        }

        // nếu rỗng trả về kết quả luôn
        if(results.isEmpty()) {
            return new ArrayList<>();
        }

        return contractResponses;
    }

}