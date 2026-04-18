package com.example.controller;

import com.example.dto.response.ApiResponse;
import com.example.dto.response.CustomerResponse;
import com.example.dto.response.SuccessResponse;
import com.example.model.Customer;
import com.example.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    
    @GetMapping("/search")
    public ApiResponse searchCustomers(
            @RequestParam(name = "name", defaultValue = "") String name) {
        
        log.info("🔍 [User Service] Processing searchCustomers request - name: '{}'", name);
        
        try {
            List<CustomerResponse> result = customerService.searchCustomersByName(name);
            
            log.info(" [User Service] Search successful - found {} customers", result.size());
            
            // Bọc kết quả vào SuccessResponse theo chuẩn hệ thống
            return new SuccessResponse(result, "Lấy danh sách khách hàng thành công");
        } catch (Exception e) {
            log.error(" [User Service] Error searching customers: ", e);
            throw e;
        }
    }

    @GetMapping("/check")
    public ApiResponse validateCustomers(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "password", defaultValue = "") String password) {

        log.info("🔍 [User Service] Processing validate request - name: '{}'", name);

        try {
            List<Customer> result = customerService.validate(name,password);

            return new SuccessResponse(result, "Lấy danh sách khách hàng thành công");
        } catch (Exception e) {
            log.error(" [User Service] Error searching customers: ", e);
            throw e;
        }
    }

//    @GetMapping("/search-by-cccd")
//    public ApiResponse searchCustomersByCccd(
//            @RequestParam(name = "cccd", defaultValue = "") String cccd) {
//
//        log.info("🔍 [User Service] Processing searchCustomersByCccd request - cccd: '{}'", cccd);
//
//        try {
//            List<CustomerResponse> result = customerService.searchCustomersByCccd(cccd);
//
//            log.info("✅ [User Service] Search by CCCD successful - found {} customers", result.size());
//
//            return new SuccessResponse(result, "Tìm kiếm khách hàng theo CCCD thành công");
//        } catch (Exception e) {
//            log.error("❌ [User Service] Error searching customers by CCCD: ", e);
//            throw e;
//        }
//    }
}
