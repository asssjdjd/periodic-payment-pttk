package com.example.service;

import com.example.dto.response.CustomerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    List<CustomerResponse> searchCustomersByName(String nameKeyword);

    List<CustomerResponse> searchCustomersByCccd(String cccdKeyword);
}
