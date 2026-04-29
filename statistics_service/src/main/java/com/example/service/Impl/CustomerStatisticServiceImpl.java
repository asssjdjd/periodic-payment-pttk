package com.example.service.Impl;

import com.example.client.AccountClient;
import com.example.dto.ContractDto;
import com.example.dto.CustomerStatisticDto;
import com.example.dto.LoanPaymentScheduleDto;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.CustomerResponse;
import com.example.dto.response.StatisticCustomerResponse;
import com.example.entity.Contract;
import com.example.entity.Customer;
import com.example.entity.LoanPaymentSchedule;
import com.example.repository.ContractRepository;
//import com.example.repository.CustomerRepository;
import com.example.repository.LoanPaymentScheduleRepository;
import com.example.service.CustomerStatisticService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerStatisticServiceImpl implements CustomerStatisticService {

    private final ContractRepository contractRepository;

    private final LoanPaymentScheduleRepository loanPaymentScheduleRepository;

//    private final CustomerRepository customerRepository;

    private final AccountClient accountClient;

    private final ObjectMapper objectMapper;

    // OVERDUE, PENDING, PAID

    @Override
    public StatisticCustomerResponse getStatisticCustomerOutstandingDebt(Float maxDebt, Float minDebt, LocalDate endDate, LocalDate fromDate) {
//        List<Customer> customers = customerRepository.findAll();

        ResponseEntity<ApiResponse.Payload> response = accountClient.getAllCustomers();

        if (response == null || response.getBody() == null) {
            throw new RuntimeException("Inventory Service không trả về dữ liệu (Response Body null).");
        }

        ApiResponse.Payload payload = response.getBody();

        if (payload.getCode() != 200) {
            String errorMsg = payload.getMessage();
            throw new RuntimeException("Inventory từ chối yêu cầu. Lý do: " + errorMsg);
        }

        if (payload.getData() == null) {
            throw new RuntimeException("Data rỗng, không có gì để map!");
        }

        Object rawData = payload.getData();

        List<CustomerResponse> customerResponses = objectMapper.convertValue(
                rawData,
                new TypeReference<List<CustomerResponse>>() {}
        );

        List<Customer> customers = new ArrayList<>();

        for(CustomerResponse customerResponse : customerResponses) {
            Customer customer = Customer.builder()
                    .id(customerResponse.getId())
                    .fullName(customerResponse.getFullName())
                    .phoneNumber(customerResponse.getPhoneNumber())
                    .status(customerResponse.getStatus())
                    .build();
            customers.add(customer);
        }

        List<CustomerStatisticDto> customerStatisticDtos = new ArrayList<>();
        // 1. Chuyển đổi điều kiện lọc Float sang BigDecimal
        BigDecimal min = minDebt != null ? BigDecimal.valueOf(minDebt) : BigDecimal.ZERO;
        // Nếu maxDebt không truyền, cho nó một số rất lớn
        BigDecimal max = maxDebt != null ? BigDecimal.valueOf(maxDebt) : new BigDecimal("999999999999");

        for(Customer customer : customers) {
            List<ContractDto> contractDtos = getStatisticCustomerOutstandingDebtDetail(customer.getId());

            BigDecimal totalDebt = BigDecimal.ZERO;
            BigDecimal totalInterestRemaining = BigDecimal.ZERO;
            BigDecimal totalPrincipleRemaining = BigDecimal.ZERO;
            BigDecimal totalPenalty = BigDecimal.ZERO;
            BigDecimal totalOverdue = BigDecimal.ZERO;

            for(ContractDto contractDto : contractDtos) {
                totalDebt = totalDebt.add(getSafeValue(contractDto.getDebt()));
                totalInterestRemaining = totalInterestRemaining.add(getSafeValue(contractDto.getInterestRemaining()));
                totalPrincipleRemaining = totalPrincipleRemaining.add(getSafeValue(contractDto.getPrincipleRemaining()));
                totalPenalty = totalPenalty.add(getSafeValue(contractDto.getPenalty()));
                totalOverdue = totalOverdue.add(getSafeValue(contractDto.getOverdue()));
            }

            // 2. Logic Filter theo Dư Nợ (minDebt, maxDebt)
            // Lấy ra khách hàng có nợ nằm trong khoảng min - max
            CustomerStatisticDto customerStatisticDto = new CustomerStatisticDto();
            customerStatisticDto.setCustomerName(customer.getFullName());
            customerStatisticDto.setCustomerPhone(customer.getPhoneNumber());
            customerStatisticDto.setContracts(contractDtos);

                // 3. SỬA LỖI: Gán các tổng vào DTO
            customerStatisticDto.setTotalDebt(totalDebt);
            customerStatisticDto.setTotalInterestRemaining(totalInterestRemaining);
            customerStatisticDto.setTotalPrincipleRemaining(totalPrincipleRemaining);
            customerStatisticDto.setTotalPenalty(totalPenalty);
            customerStatisticDto.setTotalOverdue(totalOverdue);

            customerStatisticDtos.add(customerStatisticDto);

        }
        return new StatisticCustomerResponse(customerStatisticDtos);
    }

    private BigDecimal getSafeValue(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public List<ContractDto> getStatisticCustomerOutstandingDebtDetail(String customerId) {
        List<Contract> contracts = contractRepository.findByCustomerId(customerId);

        List<ContractDto> contractDtos = new ArrayList<>();

        for(Contract contract : contracts)
        {
            List<LoanPaymentSchedule> loanPaymentSchedules = findByContractIdOrderByTermNoAsc(contract.getId());
            List<LoanPaymentScheduleDto> loanPaymentScheduleDtos = new ArrayList<>();

            // map LoanPayment
            for(LoanPaymentSchedule loanPaymentSchedule : loanPaymentSchedules) {
                loanPaymentScheduleDtos.add(mapLoanPaymentScheduleDto(loanPaymentSchedule));
            }

            ContractDto contractDto = new ContractDto();

            contractDto.setSignDate(contract.getSignedDate());
            contractDto.setLoanPaymentSchedules(loanPaymentScheduleDtos);
            contractDto.setDebt(calculateDebt(loanPaymentScheduleDtos));
            contractDto.setPenalty(calculatePenalty(loanPaymentScheduleDtos));
            contractDto.setOverdue(calculateOverdueInterest(loanPaymentScheduleDtos));
            contractDto.setPrincipleRemaining(calculatePrincipleRemaining(loanPaymentScheduleDtos));
            contractDto.setInterestRemaining(calculateInterestRemaining(loanPaymentScheduleDtos));
            contractDtos.add(contractDto);
        }
        return contractDtos;
    }

    public List<LoanPaymentSchedule> findByContractIdOrderByTermNoAsc(String contractId) {
        return loanPaymentScheduleRepository.findByContractIdOrderByTermNoAsc(contractId);
    }

    public LoanPaymentScheduleDto mapLoanPaymentScheduleDto(LoanPaymentSchedule loanPaymentSchedule) {
        return LoanPaymentScheduleDto.builder()
                .termNo(loanPaymentSchedule.getTermNo())
                .dueDate(loanPaymentSchedule.getDueDate())
                .interestDue(loanPaymentSchedule.getInterestDue())
                .interestPaid(loanPaymentSchedule.getInterestPaid())
                .overdueInterest(loanPaymentSchedule.getOverdueInterest())
                .overdueInterestPaid(loanPaymentSchedule.getOverdueInterestPaid())
                .penaltyFee(loanPaymentSchedule.getPenaltyFee())
                .penaltyFeePaid(loanPaymentSchedule.getPenaltyFeePaid())
                .principalDue(loanPaymentSchedule.getPrincipalDue())
                .principlePaid(loanPaymentSchedule.getPrinciplePaid())
                .status(loanPaymentSchedule.getStatus())
                .build();
    }



    private BigDecimal calculateInterestRemaining(List<LoanPaymentScheduleDto> loanPaymentScheduleDtos) {
        BigDecimal totalInterestRemaining = BigDecimal.ZERO;
        for(LoanPaymentScheduleDto dto : loanPaymentScheduleDtos) {
            if("OVERDUE".equals(dto.getStatus()) || "PENDING".equals(dto.getStatus())) {
                BigDecimal remainingOfThisTerm = getSafeValue(dto.getInterestDue())
                        .subtract(getSafeValue(dto.getInterestPaid()));
                // SỬA LỖI GÁN LẠI
                totalInterestRemaining = totalInterestRemaining.add(remainingOfThisTerm);
            }
        }
        return totalInterestRemaining;
    }

    private BigDecimal calculatePrincipleRemaining(List<LoanPaymentScheduleDto> loanPaymentScheduleDtos) {
        BigDecimal totalPrincipleRemaining = BigDecimal.ZERO;
        for (LoanPaymentScheduleDto dto : loanPaymentScheduleDtos) {
            if("OVERDUE".equals(dto.getStatus()) || "PENDING".equals(dto.getStatus())) {
                BigDecimal termPrincipleRemaining = getSafeValue(dto.getPrincipalDue())
                        .subtract(getSafeValue(dto.getPrinciplePaid()));
                totalPrincipleRemaining = totalPrincipleRemaining.add(termPrincipleRemaining);
            }
        }
        return totalPrincipleRemaining;
    }

    private BigDecimal calculatePenalty(List<LoanPaymentScheduleDto> loanPaymentScheduleDtos) {
        BigDecimal totalPenalty = BigDecimal.ZERO;
        for(LoanPaymentScheduleDto dto : loanPaymentScheduleDtos) {
            if("OVERDUE".equals(dto.getStatus())) {
                BigDecimal remainingPenalty = getSafeValue(dto.getPenaltyFee())
                        .subtract(getSafeValue(dto.getPenaltyFeePaid()));
                totalPenalty = totalPenalty.add(remainingPenalty);
            }
        }
        return totalPenalty;
    }

    private BigDecimal calculateOverdueInterest(List<LoanPaymentScheduleDto> loanPaymentScheduleDtos) {
        BigDecimal totalOverdueInterest = BigDecimal.ZERO;
        for(LoanPaymentScheduleDto dto : loanPaymentScheduleDtos) {
            if("OVERDUE".equals(dto.getStatus())) {
                BigDecimal overdueInterest = getSafeValue(dto.getOverdueInterest())
                        .subtract(getSafeValue(dto.getOverdueInterestPaid()));
                totalOverdueInterest = totalOverdueInterest.add(overdueInterest);
            }
        }
        return totalOverdueInterest;
    }

    private BigDecimal calculateDebt(List<LoanPaymentScheduleDto> loanPaymentScheduleDtos) {
        BigDecimal principleRemaining = calculatePrincipleRemaining(loanPaymentScheduleDtos);
        BigDecimal interestRemaining = calculateInterestRemaining(loanPaymentScheduleDtos);
        BigDecimal penaltyRemaining = calculatePenalty(loanPaymentScheduleDtos);
        BigDecimal overdueRemaining = calculateOverdueInterest(loanPaymentScheduleDtos);

        return principleRemaining.add(interestRemaining).add(penaltyRemaining).add(overdueRemaining);
    }
}
