package com.example.dto.response;

import com.example.dto.CustomerStatisticDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticCustomerResponse {
   private List<CustomerStatisticDto> customerStatistics;
}
