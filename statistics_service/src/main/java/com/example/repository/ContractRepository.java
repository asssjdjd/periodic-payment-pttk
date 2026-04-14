package com.example.repository;

import com.example.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract,String> {

    List<Contract> findByCustomerId(String customerId);

}
