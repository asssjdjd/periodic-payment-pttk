package com.example.periodic_payment.service;

import com.example.periodic_payment.dto.response.CustomerResponse;
import com.example.periodic_payment.model.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface CustomerService {
    List<CustomerResponse> searchCustomersByName(String nameKeyword);

    List<CustomerResponse> searchCustomersByCccd(String cccdKeyword);
}
