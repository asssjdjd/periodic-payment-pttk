package com.example.controller;

import com.example.dto.response.ApiResponse;
import com.example.dto.response.ImportOrderResponse;
import com.example.dto.response.SuccessResponse;
import com.example.service.ImportOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/import-orders")
@RequiredArgsConstructor
@Slf4j
public class ImportOrderController {

    private final ImportOrderService importOrderService;

    // API 1: Get tất cả hợp đồng đã nhập kho (COMPLETED)
    @GetMapping("/completed")
    public ApiResponse getCompletedImportOrders(@RequestParam(value = "name", required = false) String name) {
        log.info("[ImportOrder Service] : Get COMPLETED orders with filter name: {}", name);
        List<ImportOrderResponse> response = importOrderService.getImportOrdersByStatus("COMPLETED", name);
        return new SuccessResponse(response, "Lấy danh sách đơn đã nhập kho thành công");
    }

    // API 2: Lấy đơn chờ xác nhận + Filter theo tên
    @GetMapping("/pending")
    public ApiResponse getPendingImportOrders(@RequestParam(value = "name", required = false) String name) {
        log.info("[ImportOrder Service] : Get PENDING orders with filter name: {}", name);
        List<ImportOrderResponse> response = importOrderService.getImportOrdersByStatus("PENDING", name);
        return new SuccessResponse(response, "Lấy danh sách đơn chờ xác nhận thành công");
    }
}