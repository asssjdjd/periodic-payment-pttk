package com.example.client;

import com.example.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping("/api/v1/customers")
    public ResponseEntity<ApiResponse.Payload> getAllCustomers();

}
