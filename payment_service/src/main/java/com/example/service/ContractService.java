package com.example.service;

import com.example.dto.response.ContractResponse;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContractService {
    List<ContractResponse>getActiveContractsByCustomerId(Long customerId);
}
