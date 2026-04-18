package com.example.repository;

import com.example.entity.ImportOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportOrderRepository extends JpaRepository<ImportOrder, String> {
    List<ImportOrder> findByStatusAndDeletedAtIsNull(String status);

    // Tìm kiếm theo trạng thái và tên NCC (không phân biệt hoa thường)
    @Query("SELECT io FROM ImportOrder io " +
            "WHERE io.status = :status " +
            "AND (:name IS NULL OR LOWER(io.supplier.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND io.deletedAt IS NULL")
    List<ImportOrder> findByStatusAndSupplierName(@Param("status") String status, @Param("name") String name);
}