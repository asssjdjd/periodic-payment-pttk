package com.example.controller;

import com.example.dto.response.ApiResponse;
import com.example.dto.response.CustomerResponse;
import com.example.dto.response.SuccessResponse;
import com.example.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    @GetMapping("/search")
    public ApiResponse searchCustomers(
            @RequestParam(name = "name", defaultValue = "") String name) {

        List<CustomerResponse> result = customerService.searchCustomersByName(name);

        // Bọc kết quả vào SuccessResponse theo chuẩn hệ thống
        return new SuccessResponse(result, "Lấy danh sách khách hàng thành công");
    }

    @GetMapping("/search-by-cccd")
    public ApiResponse searchCustomersByCccd(
            @RequestParam(name = "cccd", defaultValue = "") String cccd) {

        List<CustomerResponse> result = customerService.searchCustomersByCccd(cccd);

        return new SuccessResponse(result, "Tìm kiếm khách hàng theo CCCD thành công");
    }

}
