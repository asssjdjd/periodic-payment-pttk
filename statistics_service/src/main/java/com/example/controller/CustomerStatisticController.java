package com.example.controller;

import com.example.dto.request.StatisticCustomerRequest;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.ErrorResponse;
import com.example.dto.response.StatisticCustomerResponse;
import com.example.dto.response.SuccessResponse;
import com.example.service.CustomerStatisticService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/")
@RequiredArgsConstructor
@Slf4j
public class CustomerStatisticController {

    private final CustomerStatisticService customerStatisticService;

    @PostMapping("customer/outstanding-debt/detail")
    public ApiResponse getStatisticCustomer(@RequestBody StatisticCustomerRequest statisticCustomerRequest) {

        LocalDate fromDate = statisticCustomerRequest.getFromDate();
        LocalDate endDate = statisticCustomerRequest.getEndDate();
        Float minDebt = statisticCustomerRequest.getMinDebt();
        Float maxDebt = statisticCustomerRequest.getMinDebt();
//        String customerId = statisticCustomerRequest.getCustomerId();

        log.info("[Statistic Service] Nhận được yêu cầu xem thông tin dư nợ khách hàng với minDebt : {}; maxDebt : {}; fromDate : {}; endDate :{}",
                minDebt,maxDebt,fromDate,endDate);

        StatisticCustomerResponse response = customerStatisticService.getStatisticCustomerOutstandingDebt(
                maxDebt,minDebt,endDate,fromDate);

        return new SuccessResponse(response,"Lấy thông tin dư nợ khách hàng thành công");
    }

}
