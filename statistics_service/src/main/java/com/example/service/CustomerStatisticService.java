package com.example.service;

import com.example.dto.response.StatisticCustomerResponse;

import java.time.LocalDate;
import java.util.List;

public interface CustomerStatisticService {

//    LocalDate fromDate = statisticCustomerRequest.getFromDate();
//    LocalDate endDate = statisticCustomerRequest.getEndDate();
//    Float minDebt = statisticCustomerRequest.getMinDebt();
//    Float maxDebt = statisticCustomerRequest.getMinDebt();
//    String customerId = statisticCustomerRequest.getCustomerId();
    public StatisticCustomerResponse getStatisticCustomerOutstandingDebt(Float maxDebt, Float minDebt , LocalDate endDate, LocalDate fromDate);
}
