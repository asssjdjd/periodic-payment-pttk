package com.example.service;

import com.example.dto.response.CustomerResponse;
import com.example.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    List<CustomerResponse> findAllCustomer(String name, String cccd);

    List<Customer> validate(String name, String password);

//    List<CustomerResponse> searchCustomersByCccd(String cccdKeyword);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerById(String id);
}
