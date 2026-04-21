package com.example.service.search.Impl;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import com.example.service.search.SearchStrategy;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
public class SearchByNameImpl implements SearchStrategy {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> searchCustomer(String name, String cccd) {
        log.info("[Account Service] [SearchByNameImpl] search by name : {}", name);
        List<Customer> result = new ArrayList<>();
        if(cccd == null && name != null) {
            result = customerRepository.findByFullNameContainingIgnoreCase(name);
        }
        return result;
    }
}
