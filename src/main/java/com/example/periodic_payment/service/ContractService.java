package com.example.periodic_payment.service;

import com.example.periodic_payment.dto.response.ContractResponse;
import com.example.periodic_payment.model.Contract;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContractService {
    List<ContractResponse>getActiveContractsByCustomerId(Long customerId);
}
