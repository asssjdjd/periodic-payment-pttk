package com.example.service.impl;

import com.example.dto.response.CustomerResponse;
import com.example.exception.ExceptionCode;
import com.example.exception.ResourceException;
import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import com.example.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true) // tăng hiệu quả đọc ghi
    public List<CustomerResponse> searchCustomersByName(String name) {
        log.info("[Customer Service] : Thực hiện tìm kiếm khách hàng với từ khóa : {}", name);
        List<Customer> customers = customerRepository.findByFullNameContainingIgnoreCase(name);

        if(customers.isEmpty()) {
            throw new ResourceException(ExceptionCode.USER_NOT_FOUND.getCode(),ExceptionCode.USER_NOT_FOUND.getMessage());
        }

        return customers.stream()
                .map(CustomerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Customer> validate(String name, String password) {
       return customerRepository.findByFullNameAndPassword(name,password);
    }


//    @Override
//    @Transactional(readOnly = true)
//    public List<CustomerResponse> searchCustomersByCccd(String cccd) {
//        log.info("[Customer Service] : Thực hiện tìm kiếm khách hàng với số CCCD chứa: {}", cccd);
//
//        if (cccd.isBlank()) {
//            throw new ResourceException(ExceptionCode.CCCD_NOT_FOUND.getCode(), ExceptionCode.CCCD_NOT_FOUND.getMessage());
//        }
//        // Gọi xuống DB lấy danh sách khớp
//        List<Customer> customers = customerRepository.findByFullrnameAndPassWord(cccd);
//
//        if(customers.isEmpty()) {
//            throw new ResourceException(ExceptionCode.USER_NOT_FOUND.getCode(),ExceptionCode.USER_NOT_FOUND.getMessage());
//        }
//
//        // Map từ Entity sang DTO
//        return customers.stream()
//                .map(CustomerResponse::fromEntity)
//                .collect(Collectors.toList());
//    }
}
