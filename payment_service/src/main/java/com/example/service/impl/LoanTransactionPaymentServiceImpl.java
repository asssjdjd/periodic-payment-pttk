package com.example.service.impl;


import com.example.dto.LoanPaymentScheduleDTO;
import com.example.dto.response.LoanPaymentScheduleResponse;
import com.example.exception.ExceptionCode;
import com.example.exception.ResourceException;
import com.example.model.LoanPaymentSchedule;
import com.example.repository.LoanPaymentScheduleRepository;
import com.example.service.LoanTransactionPaymentService;
import com.example.service.ScheduleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanTransactionPaymentServiceImpl implements LoanTransactionPaymentService {

    private final LoanPaymentScheduleRepository repo;

    private final PendingStateImpl pendingState;
    private final OverdueStateImpl overdueState;
    private final PartiallyPaidStateImpl partiallyPaidState;
//    private final ContractRepository contractRepository;

    @Override
    public List<LoanPaymentScheduleResponse> choosePaymentSchedule(List<LoanPaymentScheduleDTO> loanPaymentSchedules,Long contractId) {
        log.info("[LoanTransactionPaymentService] : The list of LoanPaymentSchedule is {}", loanPaymentSchedules);
        LocalDate today = LocalDate.now();

        List<LoanPaymentScheduleDTO> actives = new ArrayList<>();
        for(LoanPaymentScheduleDTO loanPaymentScheduleDTO : loanPaymentSchedules) {
            LocalDate dayDue = loanPaymentScheduleDTO.getDueDate();
            long daysOverdue = ChronoUnit.DAYS.between(today,dayDue);
//            if(daysOverdue <= 0) {
            actives.add(loanPaymentScheduleDTO);
//            }
        }

        if(actives.isEmpty()) {
            return null;
        }

        for (LoanPaymentScheduleDTO dto : actives) {
            try {
                ScheduleState scheduleState = createScheduleState(dto.getStatus());
                scheduleState.recalculate(dto);
            } catch (Exception e) {
                log.error("Error updating schedule ID {}: {}", dto.getId(), e.getMessage());
            }
        }

        List<LoanPaymentSchedule> updatedEntities = repo.findAllByContractId(contractId);
        List<LoanPaymentScheduleResponse> reals = updatedEntities.stream()
                .filter(entity -> !"PAID".equals(entity.getStatus())) // Chỉ bỏ PAID
                .map(entity -> {
                    return LoanPaymentScheduleResponse.fromEntity(LoanPaymentScheduleDTO.fromEntity(entity));
                })
                .collect(Collectors.toList());


//        List<LoanPaymentScheduleResponse> result = new ArrayList<>();
//        for(LoanPaymentScheduleResponse loanPaymentScheduleResponse : reals)  {
//            LocalDate due = loanPaymentScheduleResponse.getDueDate();
//            long dayBetween = ChronoUnit.DAYS.between(today,due);
//            log.info("[LoanTransactionPaymentServiceImpl] Day is {}" ,dayBetween);
//            result.add(loanPaymentScheduleResponse);
//            if(dayBetween > 0) {
//                break;
//            }
//        }
        return reals;
    }

    @Override
    public LoanPaymentScheduleResponse executePayment(Long scheduleId, BigDecimal amount) {
        log.info("[LoanTransactionPayment] Thực hiện thanh toán cho ID: {}, Số tiền: {}", scheduleId, amount);
        // 1. Kiểm tra số tiền hợp lệ
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResourceException(ExceptionCode.INVALID_INPUT.getCode(), "Số tiền thanh toán phải lớn hơn 0");
        }

        LoanPaymentSchedule entity = repo.findById(scheduleId)
                .orElseThrow(() -> new ResourceException(ExceptionCode.LOAN_PAYMENT_SCHEDUE_NOT_FOUND.getCode(), "Không tìm thấy lịch thanh toán"));


        if ("PAID".equals(entity.getStatus())) {
            throw new ResourceException(ExceptionCode.LOAN_PAYMENT_SCHEDUE_ALREADY_PAID.getCode(), "Kỳ hạn này đã được thanh toán hoàn tất");
        }

        ScheduleState state = createScheduleState(entity.getStatus());

        BigDecimal remainingMoney = state.pay(scheduleId, amount);

        LoanPaymentSchedule updatedEntity = repo.findById(scheduleId).get();
        // 3. LOGIC RECALCULATE SAU THANH TOÁN
        // Sau khi trả tiền, các con số Due (cần thu) đã thay đổi.
        // Ta cần gọi recalculate để cập nhật lại trạng thái dựa trên số dư nợ gốc/lãi còn lại.
        try {
            log.info("[LoanTransactionPayment] Tái tính toán lại dữ liệu sau thanh toán cho ID: {}", scheduleId);

            // Chuyển entity vừa cập nhật sang DTO để recalculate
            LoanPaymentScheduleDTO updatedDto = LoanPaymentScheduleDTO.fromEntity(entity);

            // Gọi lại chính State đó để recalculate (ví dụ: nếu trả gần hết, nó có thể chuyển từ OVERDUE sang PARTIALLY_PAID)
            state.recalculate(updatedDto);

            // Lưu lại kết quả sau khi recalculate
            entity = repo.save(entity);
        } catch (Exception e) {
            log.error("Lỗi khi tái tính toán sau thanh toán cho ID {}: {}", scheduleId, e.getMessage());
            // Tùy nghiệp vụ, có thể throw exception hoặc chỉ log error
        }

        log.info("[LoanTransactionPayment] Hoàn tất. Tiền dư: {}", remainingMoney);

        return LoanPaymentScheduleResponse.fromEntity(LoanPaymentScheduleDTO.fromEntity(updatedEntity));
    }

    private ScheduleState createScheduleState(String status) {
        return switch (status) {
            case "OVERDUE" -> overdueState;
            case "PARTIALLY_PAID" -> partiallyPaidState;
            case "PENDING" -> pendingState;
            default -> throw new ResourceException(
                    ExceptionCode.LOAN_PAYMENT_SCHEDUE.getCode(),
                    "Trạng thái lịch thanh toán không hợp lệ: " + status
            );
        };
    }
}
