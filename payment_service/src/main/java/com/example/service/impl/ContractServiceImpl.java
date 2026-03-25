package com.example.service.impl;


import com.example.dto.response.ContractResponse;
import com.example.exception.ExceptionCode;
import com.example.exception.ResourceException;
import com.example.model.Contract;
import com.example.model.LoanPaymentSchedule;
import com.example.repository.ContractRepository;
import com.example.repository.CustomerRepository;
import com.example.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ContractResponse> getActiveContractsByCustomerId(Long customerId) {
        log.info("[Contract Service] Lấy danh sách hợp đồng ACTIVE cho khách hàng ID: {}", customerId);

        // 1. Kiểm tra khách hàng tồn tại (Dùng existsById để tối ưu hiệu năng)
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceException(ExceptionCode.DATA_NOT_FOUND.getCode(), "Không tìm thấy khách hàng yêu cầu");
        }

        // 2. Lấy dữ liệu đã được tuning
        List<Contract> contracts = contractRepository.findActiveContractsByCustomerId(customerId, "ACTIVE");
//        List<Contract> contracts = contractRepository.findAllByCustomerId(customerId);
        List<Contract> results = new ArrayList<>();
        for(Contract contract : contracts) {
            List<LoanPaymentSchedule> loanPaymentSchedules = contract.getPaymentSchedules();
            boolean isActive = false;
            for(LoanPaymentSchedule loanPaymentSchedule : loanPaymentSchedules) {
                if(!"PAID".equalsIgnoreCase(loanPaymentSchedule.getStatus())) {
                    isActive = true;
                }
            }
            if(isActive == true) {
                results.add(contract);
            }else {
                // nếu toàn bộ đã thanh toán uơdate lại
                contract.setStatus("DONE");
                contractRepository.save(contract);
            }
        }
        // 3. Chuyển đổi sang Response
        return results.stream()
                .map(ContractResponse::fromEntity)
                .collect(Collectors.toList());
    }
}