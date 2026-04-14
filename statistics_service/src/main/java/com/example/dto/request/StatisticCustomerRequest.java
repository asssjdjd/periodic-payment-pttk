package com.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticCustomerRequest {
    private LocalDate fromDate;
    private LocalDate endDate;
    private Float minDebt;
    private Float maxDebt;
//    private String customerId;
}
