package com.example.repository;

import com.example.model.TransactionLoanPaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLoanPaymentScheduleRepository extends JpaRepository<TransactionLoanPaymentSchedule,String> {

}
