package com.example.periodic_payment.service.impl;

import com.example.periodic_payment.dto.LoanPaymentScheduleDTO;
import com.example.periodic_payment.model.LoanPaymentSchedule;
import com.example.periodic_payment.repository.LoanPaymentScheduleRepository;
import com.example.periodic_payment.service.FinanceConstants;
import com.example.periodic_payment.service.LoanPaymentScheduleState;
import com.example.periodic_payment.service.ScheduleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OverdueStateImpl implements ScheduleState {

    private final LoanPaymentScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public void recalculate(LoanPaymentScheduleDTO context) {

        Long paymentSchedulePaymentId = context.getId();
        LoanPaymentSchedule entity = scheduleRepository.findById(paymentSchedulePaymentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kỳ thanh toán với ID: " + paymentSchedulePaymentId));

        if (LoanPaymentScheduleState.PAID.name().equals(entity.getStatus())) {
            log.info("[OverdueStateImpl]; Kỳ thanh toán {} đã tất toán, bỏ qua tính toán.", paymentSchedulePaymentId);
            return;
        }

        LocalDate dayDue = context.getDueDate();
        LocalDate today = LocalDate.now();
        long daysOverdue = ChronoUnit.DAYS.between(dayDue,today);

        String contractCode = context.getContractCode();
        Long contractId = context.getContractId();

        Long loanPaymentScheduleId = context.getId();
        //   Quá hạn :
        if(daysOverdue > 0) {
            log.info("[OverdueStateImpl]; Tính toán lại cho hợp đồng : {} mã hợp đồng : {} , kỳ hợp đồng : {} ",contractId,contractCode,loanPaymentScheduleId);
            // phần trăm tien gốc quá hạn và lãi quá hạn (thông số)
            BigDecimal overduePrincipleRate = context.getOverduePrincipleRate();
            BigDecimal overdueInterestRate = context.getOverdueInterestRate();

            // Phí phạt do muộn hợp đồng
            BigDecimal penaltyFee = context.getPenaltyFee();

            // Tiền phạt của tiền gốc ( gốc chưa trả * rate * số ngày)

            // số tiền gốc còn nợ ( Tien gốc phải trả - tiền gốc đã trả).
            BigDecimal remainPrinciple = context.getPrincipalDue().subtract(context.getPrinciplePaid());
            // số tiền phạt của tiền gốc còn nợ = tiền gốc * phần trăm * số ngày
            BigDecimal overduePrinciple = remainPrinciple.multiply(overduePrincipleRate)
                    .multiply(BigDecimal.valueOf(daysOverdue))
                    .setScale(FinanceConstants.SCALE,FinanceConstants.ROUNDING);

            // Phạt trên Lãi (Lãi chưa trả * Rate * Số ngày)
            // Tiền lãi phải trả  hàng tháng :
            BigDecimal interestDueMoney = (context.getInterestRate().multiply(context.getPrincipalDue()));
            BigDecimal remainInterset = interestDueMoney.subtract(context.getInterestPaid());
            BigDecimal  overdueInterest = remainInterset.multiply(overdueInterestRate)
                    .multiply(BigDecimal.valueOf(daysOverdue))
                    .setScale(FinanceConstants.SCALE, FinanceConstants.ROUNDING);

            String status = "OVERDUE";
            BigDecimal penaltyDue = penaltyFee.add(overdueInterest).add(overduePrinciple);

            entity.setOverduePrinciple(overduePrinciple);
            entity.setOverdueInterest(overdueInterest);
            entity.setPenaltyDue(penaltyDue);
            entity.setPenaltyFee(penaltyFee);
            entity.setStatus(status);
            entity.setInterestDue(interestDueMoney);

            // sau khi trừ xong thì xóa đi
            entity.setPenaltyFeePaid(BigDecimal.ZERO);
            entity.setOverdueInterestPaid(BigDecimal.ZERO);
            entity.setOverduePrinciplePaid(BigDecimal.ZERO);


            scheduleRepository.save(entity);

            log.info("[OverdueStateImpl] Cập nhật lại hợp đồng với : tiền phạt cố định hàng tháng: {}; tiền phạt gốc quá hạn : {}; " +
                            "tiền phạt lãi quá hạn : {}; tổng số tiền phạt : {}; trạng thái hợp đồng : {}; số ngày phạt {}; Tiền gốc quá hạn : {}, Tiền lãi quá hạn {}"
                    ,penaltyFee,overduePrinciple,overdueInterest,penaltyDue,status,daysOverdue,remainPrinciple,interestDueMoney);
        }
    }

    @Override
    public BigDecimal pay(Long loanPaymentScheduleId, BigDecimal amount) {
        LoanPaymentSchedule entity = scheduleRepository.findById(loanPaymentScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kỳ thanh toán với ID: " + loanPaymentScheduleId));
        log.info("[OverdueStateImpl] : Xử lý thanh toán cho kỳ QUÁ HẠN ID: {}, Số tiền nộp: {}", entity.getId(), amount);
        BigDecimal remaining = amount;

        // 1. Trừ tiền phạt (Penalty)

        // Tiền phạt = tiền phạt -tiền phạt đã đóng.
        BigDecimal penaltyOwed = entity.getPenaltyDue().subtract(entity.getPenaltyFeePaid());

        if (remaining.compareTo(penaltyOwed) >= 0) {
            entity.setPenaltyFeePaid(entity.getPenaltyDue());
            entity.setOverdueInterestPaid(entity.getOverdueInterest());
            entity.setOverduePrinciplePaid(entity.getOverduePrinciple());
            remaining = remaining.subtract(penaltyOwed);
        } else {
            entity.setPenaltyFeePaid(entity.getPenaltyFeePaid().add(remaining));
            if(remaining.compareTo(entity.getOverdueInterest()) >= 0) {
                entity.setOverdueInterestPaid(entity.getOverdueInterest());
            }
            remaining = remaining.subtract(entity.getOverdueInterest());
            entity.setOverduePrinciplePaid(remaining);
            return BigDecimal.ZERO; // Hết tiền, dừng tại đây
        }

        // 2. Trừ lãi quá hạn/lãi định kỳ (Interest)
        BigDecimal interestOwed = entity.getInterestDue().subtract(entity.getInterestPaid());
        if (remaining.compareTo(interestOwed) >= 0) {
            entity.setInterestPaid(entity.getInterestDue());
            remaining = remaining.subtract(interestOwed);
        } else {
            entity.setInterestPaid(entity.getInterestPaid().add(remaining));
            return BigDecimal.ZERO;
        }

        // 3. Trừ gốc (Principal)
        BigDecimal principalOwed = entity.getPrincipalDue().subtract(entity.getPrinciplePaid());
        if (remaining.compareTo(principalOwed) >= 0) {
            entity.setPrinciplePaid(entity.getPrincipalDue());
            remaining = remaining.subtract(principalOwed);
            entity.setStatus("PAID"); // Đã trả hết cả gốc lẫn lãi phạt
        } else {
            entity.setPrinciplePaid(entity.getPrinciplePaid().add(remaining));
//            entity.setStatus("PARTIALLY_PAID"); vẫn là quá hạn
            remaining = BigDecimal.ZERO;
        }
        entity.setDueDate(LocalDate.now());
        scheduleRepository.save(entity);
        return remaining;
    }



}
