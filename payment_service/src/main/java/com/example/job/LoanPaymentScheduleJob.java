package com.example.job;

import com.example.dto.EventPayloads;
import com.example.model.Contract;
import com.example.model.LoanPaymentSchedule;
import com.example.model.OutboxEvent;
import com.example.repository.ContractRepository;
import com.example.repository.LoanPaymentScheduleRepository;
import com.example.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoanPaymentScheduleJob {

    private final LoanPaymentScheduleRepository scheduleRepository;
    private final ContractRepository contractRepository;

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    // Chạy mỗi 60,000 milliseconds (1 phút)
    @Scheduled(fixedRate = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void cleanupExpiredSlots() {
        LocalDate now = LocalDate.now();
        log.info("Bắt đầu quét các lịch thanh toán quá hạn lúc: {}", now);

        List<LoanPaymentSchedule> overdueSchedules = scheduleRepository.findOverdueSchedules(now);

        if (overdueSchedules.isEmpty()) {
            return; // Không có dữ liệu thì dừng luôn
        }

        List<OutboxEvent> outboxEvents = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (LoanPaymentSchedule schedule : overdueSchedules) {

            // Nếu đúng ngày nay thì bỏ
            if (schedule.getUpdatedAt().toLocalDate().equals(today)) {
                log.debug("Bỏ qua lịch {} vì đã cập nhật quá hạn trong ngày hôm nay", schedule.getScheduleId());
                continue;
            }

            Contract contract = contractRepository.findById(schedule.getContractId())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy contract với id : " + schedule.getContractId()));

            // 1. Chuyển trạng thái


            // 2. Tính toán số ngày quá hạn (Days Overdue)
            long overdueDays = ChronoUnit.DAYS.between(schedule.getDueDate(), now);

            // Nếu số ngày <= 0 (do chênh lệch giờ nhưng chưa qua ngày mới), bỏ qua tính lãi
            if (overdueDays > 0 ) {
                // Xử lý null an toàn cho các trường số tiền
                schedule.setStatus("OVERDUE");
                BigDecimal principalDue = getOrDefault(schedule.getPrincipalDue());
                BigDecimal principlePaid = getOrDefault(schedule.getPrinciplePaid());
                BigDecimal overdueRate = getOrDefault(contract.getOverdueInterestRate());

                // 3. Thực thi công thức: (principalDue - principlePaid) * rate * overdueDays
                BigDecimal remainingPrincipal = principalDue.subtract(principlePaid);

                // Tránh trường hợp âm (đã trả dư)
                if (remainingPrincipal.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal overdueInterest = remainingPrincipal
                            .multiply(overdueRate)
                            .multiply(BigDecimal.valueOf(overdueDays));

                    schedule.setOverdueInterest(overdueInterest);
                }
            }
            schedule.setPenaltyFee(contract.getPenaltyFee());

            try {
                // ==========================================
                // EVENT 3: Bắn sự kiện cập nhật lịch bị quá hạn
                // ==========================================
                EventPayloads.OverdueScheduleEvent overduePayload = new EventPayloads.OverdueScheduleEvent(
                        schedule.getScheduleId(), schedule.getOverdueInterest(), schedule.getPenaltyFee(), schedule.getStatus()
                );

                OutboxEvent event = new OutboxEvent();
                event.setAggregateId(schedule.getScheduleId());
                event.setAggregateType("OverDueLoanPaymentSchedule");
                event.setEventType("OverdueScheduleUpdateEvent");
                event.setPayload(objectMapper.writeValueAsString(overduePayload));
                event.setStatus("RECEIVED");
                // Job tự động nên không có transaction thanh toán của khách hàng
                event.setTransactionId(null);

                outboxEvents.add(event);
            } catch (Exception e) {
                log.error("Lỗi parse JSON trong Job", e);
                throw new RuntimeException("Lỗi tạo Outbox Event trong Job", e);
            }
        }
        scheduleRepository.saveAll(overdueSchedules);
        outboxEventRepository.saveAll(outboxEvents);

        log.info("Đã cập nhật {} lịch quá hạn và lưu {} Outbox Events", overdueSchedules.size(), outboxEvents.size());
    }

    private BigDecimal getOrDefault(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}