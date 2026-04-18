package com.example.repository;

import com.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN ImportOrderDetail iod ON p.id = iod.product.id " +
            "JOIN ImportOrder io ON iod.importOrder.id = io.id " +
            "WHERE io.supplier.id = :supplierId " +
            "AND p.deletedAt IS NULL")
    List<Product> findDistinctProductsBySupplierId(@Param("supplierId") String supplierId);
}