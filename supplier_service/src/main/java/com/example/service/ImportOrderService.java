package com.example.service;

import com.example.dto.response.ImportOrderResponse;

import java.util.List;

public interface ImportOrderService {
    List<ImportOrderResponse> getImportOrdersByStatus(String status, String supplierName);
}