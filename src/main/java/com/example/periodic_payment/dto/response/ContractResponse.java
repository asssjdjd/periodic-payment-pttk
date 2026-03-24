package com.example.periodic_payment.dto.response;

import com.example.periodic_payment.dto.LoanPaymentScheduleDTO;
import com.example.periodic_payment.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
public class ContractResponse {
    private Long id;
    private String code;
    private String status;
    private BigDecimal loanAmount;
    private LocalDate signedDate;
    private BigDecimal productPrice;
    private BigDecimal prepaidAmount;

    private CustomerDTO customer;
    private UserDTO user;
    private LoanOfferDTO loanOffer;
    private Contract parentContract;
    private List<CollateralDTO> collaterals;
    private List<LoanPaymentScheduleDTO> paymentSchedules; // Lấy từ bảng LoanOffer

    public static ContractResponse fromEntity(Contract entity) {
        if (entity == null) return null;

        return ContractResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .status(entity.getStatus())
                .loanAmount(entity.getLoanAmount())
                .signedDate(entity.getSignedDate())
                .productPrice(entity.getProductPrice())
                .prepaidAmount(entity.getPrepaidAmount())

                // Map đối tượng đơn lẻ an toàn
                .customer(entity.getCustomer() != null ? CustomerDTO.fromEntity(entity.getCustomer()) : null)
                .user(entity.getUser() != null ? UserDTO.fromEntity(entity.getUser()) : null)
                .loanOffer(entity.getLoanOffer() != null ? LoanOfferDTO.fromEntity(entity.getLoanOffer()) : null)

                // Map danh sách bằng Stream API (Tránh lỗi Null và Proxy)
                .collaterals(Optional.ofNullable(entity.getCollaterals())
                        .map(list -> list.stream().map(CollateralDTO::fromEntity).collect(Collectors.toList()))
                        .orElse(List.of()))

                .paymentSchedules(Optional.ofNullable(entity.getPaymentSchedules())
                        .map(list -> list.stream().map(LoanPaymentScheduleDTO::fromEntity).collect(Collectors.toList()))
                        .orElse(List.of()))
                .build();
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class CustomerDTO {
        Long id;
        String fullName;
        String phoneNumber;
        Integer creditScore;
        String status;
        String cccd;

        public static CustomerDTO fromEntity(Customer customer) {
            return CustomerDTO.builder()
                    .id(customer.getId())
                    .fullName(customer.getFullName())
                    .phoneNumber(customer.getPhoneNumber())
                    .creditScore(customer.getCreditScore())
                    .status(customer.getStatus())
                    .cccd(customer.getCccd()).build();
        }
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserDTO {
        private Long id;
        private String userName;
        private String password;
        private String phoneNumber;
        private String email;
        private String name;

        public static UserDTO fromEntity(User user) {
            return UserDTO.builder()
                    .id(user.getId())
                    .userName(user.getUserName())
                    .password(user.getPassword())
                    .phoneNumber(user.getPhoneNumber())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class LoanOfferDTO {
        private Long id;
        private Long product;
        private String name;
        private BigDecimal interestRate;
        private BigDecimal penaltyRate;
        private BigDecimal overdueInterestRate;
        private BigDecimal overduePrincipleRate;
        private BigDecimal maxAmount;
        private Integer termMonths;

        public static LoanOfferDTO fromEntity(LoanOffer loanOffer) {
            return  LoanOfferDTO.builder()
                    .id(loanOffer.getId())
                    .product(loanOffer.getProduct().getId())
                    .name(loanOffer.getName())
                    .interestRate(loanOffer.getInterestRate())
                    .penaltyRate(loanOffer.getPenaltyRate())
                    .overdueInterestRate(loanOffer.getOverdueInterestRate())
                    .overduePrincipleRate(loanOffer.getOverduePrincipleRate())
                    .maxAmount(loanOffer.getMaxAmount())
                    .termMonths(loanOffer.getTermMonths())
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class CollateralDTO{
        private Long id;
        private String type;
        private String description;
        private BigDecimal valuationValue;

        public static CollateralDTO fromEntity(Collateral collateral) {
            return CollateralDTO.builder()
                    .id(collateral.getId())
                    .type(collateral.getType())
                    .description(collateral.getDescription())
                    .valuationValue(collateral.getValuationValue())
                    .build();
        }
    }

}