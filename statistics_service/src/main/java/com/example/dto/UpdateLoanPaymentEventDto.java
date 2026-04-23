package com.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateLoanPaymentEventDto implements Serializable {
    String scheduleId;
    BigDecimal overdueInterest;
    BigDecimal penaltyFee;
    String status;
}
