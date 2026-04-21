package com.example.service.search;

import com.example.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SearchStrategy {
    List<Customer> searchCustomer(String name, String cccd);
}
