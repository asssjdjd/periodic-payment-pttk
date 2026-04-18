package com.example.service.Impl;

import com.example.dto.response.ImportOrderResponse;
import com.example.entity.ImportOrder;
import com.example.repository.ImportOrderRepository;
import com.example.service.ImportOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImportOrderServiceImpl implements ImportOrderService {

    private final ImportOrderRepository importOrderRepository;

//    @Override
//    public List<ImportOrderResponse> getImportOrdersByStatus(String status) {
//        List<ImportOrder> orders = importOrderRepository.findByStatusAndDeletedAtIsNull(status);
//
//        return orders.stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<ImportOrderResponse> getImportOrdersByStatus(String status, String supplierName) {
        // Gọi repository với tham số name
        List<ImportOrder> orders = importOrderRepository.findByStatusAndSupplierName(status, supplierName);

        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ImportOrderResponse mapToResponse(ImportOrder order) {
        return ImportOrderResponse.builder()
                .id(order.getId())
                .supplierId(order.getSupplier().getId())
                .supplierName(order.getSupplier().getName())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .importDate(order.getImportDate())
                .build();
    }
}
