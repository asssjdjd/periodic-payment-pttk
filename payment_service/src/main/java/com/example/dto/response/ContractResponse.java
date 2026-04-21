package com.example.dto.response;


import com.example.dto.LoanPaymentScheduleDTO;
import com.example.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractResponse {
    private String id;
    private String code;
    private String status;
    private BigDecimal loanAmount;
    private LocalDate signedDate;
    private BigDecimal productPrice;
    private BigDecimal prepaidAmount;
    private List<LoanPaymentScheduleDTO> paymentSchedules; // Lấy từ bảng LoanOffer


}