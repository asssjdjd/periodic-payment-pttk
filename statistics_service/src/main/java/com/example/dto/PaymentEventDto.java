package com.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentEventDto implements Serializable {
    private String contractId;
    private String scheduleId;
    private BigDecimal amountPaid;
    private BigDecimal interestPaid;
    private BigDecimal principlePaid;
    private BigDecimal penaltyFeePaid;
    private BigDecimal overdueInterestPaid;
    @JsonProperty("scheduleStatus")
    private String status;

    private String transactionId;
}