package com.example.periodic_payment.dto.resquest;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    private BigDecimal amount;
    private Long scheduleId;
}
