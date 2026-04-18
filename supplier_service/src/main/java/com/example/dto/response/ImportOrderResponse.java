package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportOrderResponse {
    private String id;
    private String supplierId;
    private String supplierName;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate importDate;
}
