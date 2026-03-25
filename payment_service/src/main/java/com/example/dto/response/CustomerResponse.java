//package com.example.dto.response;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class CustomerResponse {
//    private Long id;
//    private String fullName;
//    private String phoneNumber;
//    private String status;
//    private Integer creditScore;
//
//    private String cccd;
//
//    public static CustomerResponse fromEntity(Customer customer) {
//        return CustomerResponse.builder()
//                .id(customer.getId())
//                .fullName(customer.getFullName())
//                .phoneNumber(customer.getPhoneNumber())
//                .status(customer.getStatus())
//                .creditScore(customer.getCreditScore())
//                .cccd(customer.getCccd())
//                .build();
//    }
//}
