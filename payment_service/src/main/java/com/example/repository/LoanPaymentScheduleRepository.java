package com.example.repository;


import com.example.model.LoanPaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanPaymentScheduleRepository extends JpaRepository<LoanPaymentSchedule,Long> {
    @Query("SELECT lps FROM LoanPaymentSchedule lps WHERE lps.contract.id = :contractId AND lps.status <> 'PAID'")
    List<LoanPaymentSchedule> findAllByContractIdAndNotPaid(@Param("contractId") Long contractId);
}
