package com.example.controller;

import com.example.dto.response.ApiResponse;
import com.example.dto.response.CustomerResponse;
import com.example.dto.response.SuccessResponse;
import com.example.dto.response.UserResponse;
import com.example.dto.resquest.SearchCustomerRequest;
import com.example.model.Customer;
import com.example.service.CustomerService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 2. Lấy thông tin khách hàng theo ID
    @GetMapping("/{id}")
    public ApiResponse getCustomerById(@PathVariable("id") String id) {
        log.info(" [User Service] Processing getCustomerById request - id: '{}'", id);

        try {
            UserResponse result = userService.getUserById(id);
            log.info(" [User Service] Lấy thành công thông tin khách hàng id: {}", id);
            return new SuccessResponse(result, "Lấy thông tin khách hàng thành công");
        } catch (Exception e) {
            log.error(" [User Service] Error getting customer by id: ", e);
            throw e;
        }
    }


}
