package com.example.controller;

import com.example.dto.response.ApiResponse;
import com.example.dto.response.CustomerResponse;
import com.example.dto.response.SuccessResponse;
import com.example.dto.resquest.SearchCustomerRequest;
import com.example.model.Customer;
import com.example.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    
    @PostMapping("/search")
    public ApiResponse searchCustomers(
            @RequestBody SearchCustomerRequest request) {
        
        log.info(" [User Service] Processing searchCustomers request - name: '{}'; cccd : {}", request.getName(), request.getCccd());
        
        try {
            List<CustomerResponse> result = customerService.findAllCustomer(request.getName(), request.getCccd());
            
            log.info(" [User Service] Search successful - found {} customers", result.size());
            
            // Bọc kết quả vào SuccessResponse theo chuẩn hệ thống
            return new SuccessResponse(result, "Lấy danh sách khách hàng thành công");
        } catch (Exception e) {
            log.error(" [User Service] Error searching customers: ", e);
            throw e;
        }
    }

    @GetMapping
    public ApiResponse getAllCustomers() {
        log.info(" [User Service] Processing getAllCustomers request");

        try {
            List<CustomerResponse> result = customerService.getAllCustomers();
            log.info(" [User Service] Lấy thành công {} khách hàng", result.size());
            return new SuccessResponse(result, "Lấy danh sách khách hàng thành công");
        } catch (Exception e) {
            log.error(" [User Service] Error getting all customers: ", e);
            throw e;
        }
    }

    // 2. Lấy thông tin khách hàng theo ID
    @GetMapping("/{id}")
    public ApiResponse getCustomerById(@PathVariable("id") String id) {
        log.info(" [User Service] Processing getCustomerById request - id: '{}'", id);

        try {
            CustomerResponse result = customerService.getCustomerById(id);
            log.info(" [User Service] Lấy thành công thông tin khách hàng id: {}", id);
            return new SuccessResponse(result, "Lấy thông tin khách hàng thành công");
        } catch (Exception e) {
            log.error(" [User Service] Error getting customer by id: ", e);
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
}
