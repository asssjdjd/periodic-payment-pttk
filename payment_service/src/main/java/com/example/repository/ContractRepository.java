package com.example.repository;

import com.example.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract,Long> {
    List<Contract> findAllByCustomerIdAndStatus(Long customerId, String status);

//    @Query("SELECT c FROM Contract c JOIN FETCH c.loanOffer WHERE c.customer.id = :customerId AND c.status = :status")
//    List<Contract> findActiveContractsByCustomerId(@Param("customerId") Long customerId, @Param("status") String status);

    @Query("SELECT DISTINCT c FROM Contract c " +
            "JOIN FETCH c.customer " +
            "JOIN FETCH c.user " +
            "JOIN FETCH c.loanOffer " +
            "WHERE c.customer.id = :customerId AND c.status = :status")
    List<Contract> findActiveContractsByCustomerId(@Param("customerId") Long customerId, @Param("status") String status);

    List<Contract> findAllByCustomerId(Long customerId);
}
