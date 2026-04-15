package com.example.repository;

import com.example.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {
    //Tìm kiếm khách hàng theo tên, chưa từ khóa (LIKE %keyword%) và không phân biệt chữ hoa, chữ thường.
    List<Customer> findByFullNameContainingIgnoreCase(String keyword);

//    // THÊM HÀM MỚI: Tìm kiếm chứa chuỗi con của CCCD
//    List<Customer> findByCccdContaining(String cccdKeyword);

    List<Customer> findByFullNameAndPassword(String name,String password);
}
