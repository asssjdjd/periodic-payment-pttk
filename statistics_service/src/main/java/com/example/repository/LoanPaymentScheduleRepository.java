package com.example.repository;

import com.example.entity.LoanPaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanPaymentScheduleRepository extends JpaRepository<LoanPaymentSchedule,String> {
    List<LoanPaymentSchedule> findByContractId(String contractId);
    List<LoanPaymentSchedule> findByContractIdOrderByTermNoAsc(String contractId);
}
