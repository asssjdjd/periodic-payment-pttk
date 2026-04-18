package com.example.repository;


import com.example.model.LoanPaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanPaymentScheduleRepository extends JpaRepository<LoanPaymentSchedule,String> {

    List<LoanPaymentSchedule> findByContractId(String contractId);

    // Bỏ JOIN FETCH s.contract vì nó không tồn tại
    @Query("SELECT s FROM LoanPaymentSchedule s WHERE s.dueDate < :now AND s.status NOT IN ('PAID')")
    List<LoanPaymentSchedule> findOverdueSchedules(@Param("now") LocalDateTime now);
}
