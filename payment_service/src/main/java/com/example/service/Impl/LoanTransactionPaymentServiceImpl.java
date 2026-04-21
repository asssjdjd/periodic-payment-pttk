package com.example.service.Impl;


import com.example.dto.LoanPaymentScheduleDTO;
import com.example.dto.response.LoanPaymentScheduleResponse;
import com.example.exception.ExceptionCode;
import com.example.exception.ResourceException;
import com.example.model.Contract;
import com.example.model.LoanPaymentSchedule;
import com.example.model.TransactionLoanPaymentSchedule;
import com.example.repository.ContractRepository;
import com.example.repository.LoanPaymentScheduleRepository;
import com.example.repository.TransactionLoanPaymentScheduleRepository;
import com.example.service.LoanTransactionPaymentService;
import com.example.service.ScheduleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class  LoanTransactionPaymentServiceImpl implements LoanTransactionPaymentService {

    private final LoanPaymentScheduleRepository repo;
    private final TransactionLoanPaymentScheduleRepository loanPaymentScheduleRepository;
    private final ContractRepository contractRepository;

    private final PendingStateImpl pendingState;
    private final OverdueStateImpl overdueState;

    @Override
    public List<LoanPaymentScheduleResponse> choosePaymentSchedule(List<LoanPaymentScheduleDTO> loanPaymentSchedules,String contractId) {
        log.info("[LoanTransactionPaymentService] : The list of LoanPaymentSchedule is {}; contractId : {}", loanPaymentSchedules, contractId);
        List<LoanPaymentScheduleResponse> reals = new ArrayList<>();

        for(LoanPaymentScheduleDTO loanPaymentScheduleDTO : loanPaymentSchedules) {
            if(!"PAID".equals(loanPaymentScheduleDTO.getStatus())) {
                reals.add(LoanPaymentScheduleResponse.mapFromLoanPaymentDto(loanPaymentScheduleDTO));
            }
        }
        return reals;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoanPaymentScheduleResponse executePayment(String scheduleId, BigDecimal amount) {
        log.info("[LoanTransactionPayment] Thực hiện thanh toán cho ID: {}, Số tiền: {}", scheduleId, amount);

        // 1. Kiểm tra số tiền hợp lệ
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResourceException(ExceptionCode.INVALID_INPUT.getCode(), "Số tiền thanh toán phải lớn hơn 0");
        }

        LoanPaymentSchedule entity = repo.findById(scheduleId)
                .orElseThrow(() -> new ResourceException(ExceptionCode.LOAN_PAYMENT_SCHEDUE_NOT_FOUND.getCode(), "Không tìm thấy lịch thanh toán"));

        Contract contract = contractRepository.findById(entity.getContractId())
                .orElseThrow(() -> new ResourceException(ExceptionCode.FIND_CONTRACT_BY_SCHEDULE_NOT_FOUND.getCode(),
                        ExceptionCode.FIND_CONTRACT_BY_SCHEDULE_NOT_FOUND.getMessage()));;
        int maxTerm = contract.getLoanScheduleTermNumber();

        if ("PAID".equals(entity.getStatus())) {
            throw new ResourceException(ExceptionCode.LOAN_PAYMENT_SCHEDUE_ALREADY_PAID.getCode(), "Kỳ hạn này đã được thanh toán hoàn tất");
        }

        ScheduleState state = createScheduleState(entity.getStatus());

        BigDecimal penaltyFee = entity.getPenaltyFee();

        BigDecimal remainingMoney = state.pay(scheduleId, amount,penaltyFee);

        log.info("[LoanTransactionPayment] Hoàn tất. Tiền dư: {}", remainingMoney);
        LoanPaymentSchedule updatedEntity = repo.findById(scheduleId).get();

        TransactionLoanPaymentSchedule transactionLoanPaymentSchedule = TransactionLoanPaymentSchedule.builder()
                .amount(amount)
                .scheduleId(scheduleId)
                .paymentDate(LocalDate.now())
                .build();

        loanPaymentScheduleRepository.save(transactionLoanPaymentSchedule);
        log.info("[LoanTransactionPayment] Cập nhật bản ghi vào TransactionLoanPaymentSchedule ");

        if( entity.getTermNo() >= maxTerm) {
            log.info("[LoanTransactionPayment] [Đã thanh toán thành công toàn bộ hợp đồng : {}] [Tiến hành đóng hợp đồng]", contract.getCode());
            contract.setStatus("COMPLETED");
            contractRepository.save(contract);
        }

        return LoanPaymentScheduleResponse.mapFromLoanPaymentDto(LoanPaymentScheduleDTO.mapLoanPaymentScheduleDtoNotCalculate(updatedEntity));
    }

    private ScheduleState createScheduleState(String status) {
        return switch (status) {
            case "OVERDUE" -> overdueState;
            case "PENDING" -> pendingState;
            default -> throw new ResourceException(
                    ExceptionCode.LOAN_PAYMENT_SCHEDUE.getCode(),
                    "Trạng thái lịch thanh toán không hợp lệ: " + status
            );
        };
    }
}
