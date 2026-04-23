package com.example.listener;

import com.example.dto.ContractEventDto;
import com.example.dto.PaymentEventDto;
import com.example.dto.UpdateLoanPaymentEventDto;
import com.example.entity.Contract;
import com.example.entity.InboxEvent;
import com.example.entity.LoanPaymentSchedule;
import com.example.repository.ContractRepository;
import com.example.repository.InboxEventRepository;
import com.example.repository.LoanPaymentScheduleRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventListener {

    private final ObjectMapper objectMapper;
    // Inject các Repository/Service của stats-service vào đây
    // private final StatsRepository statsRepository;
    private final InboxEventRepository inboxEventRepository;
    private final LoanPaymentScheduleRepository loanPaymentScheduleRepository;
    private final ContractRepository contractRepository;

    /**
     * Lắng nghe queue "test-outbox-queue"
     */
    @RabbitListener(queues = "update-loan-payment-schedule-queue")
    @Transactional
    public void handleLoanSchedulePaymentEvent(Message message, Channel channel,
                                               @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            String rawPayload = new String(message.getBody());
            log.info("[Stats Service] Nhận được tin nhắn gốc: {}", rawPayload);
            String uniqueEventId =  java.util.UUID.randomUUID().toString();

            // Bước 1: Luôn parse chuỗi gốc thành JsonNode
            JsonNode rootNode = objectMapper.readTree(rawPayload);
            JsonNode actualDataNode = rootNode;

            // Bước 2: Bóc tách nếu có bọc trong trường "payload"
            if (rootNode.has("payload")) {
                actualDataNode = rootNode.get("payload");
            }

            // Bước 3: QUAN TRỌNG - Nếu Node vẫn đang ở dạng String (do bị escape 2 lần), phải parse tiếp
            if (actualDataNode.isTextual()) {
                actualDataNode = objectMapper.readTree(actualDataNode.asText());
            }

            // Bước 4: Chuyển đổi sang DTO
            PaymentEventDto eventDto = objectMapper.treeToValue(actualDataNode, PaymentEventDto.class);


            log.info("[contractId : {}] ; [InterestPaid : {}]; [PrinciplePaid : {}]; [ScheduleId : {}]; [Status : {}] [AmountPaid : {}]; [penaltyFeePaid : {}] [overdueInterestPaid : {}]",
                    eventDto.getContractId(), eventDto.getInterestPaid(),
                    eventDto.getPrinciplePaid(), eventDto.getScheduleId(), eventDto.getStatus(), eventDto.getAmountPaid(), eventDto.getPenaltyFeePaid(), eventDto.getOverdueInterestPaid());

            InboxEvent inbox = InboxEvent.builder()
                    //.id(...) // Nếu bạn muốn tự set ID, không thì bỏ qua để database/hibernate tự lo
                    .eventId(uniqueEventId) // Truyền UUID vào đúng cột event_id
                    .payloadReceive(rawPayload)
                    .status("PENDING")
                    .processedAt(LocalDateTime.now())
                    .build();
            inboxEventRepository.save(inbox);


            // xử lý nghiệp vụ tại đây :

            // cập nhật lại
            LoanPaymentSchedule paymentSchedule = loanPaymentScheduleRepository.findById(eventDto.getScheduleId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy schedule hợp lệ"));
            paymentSchedule.setStatus(eventDto.getStatus());
            paymentSchedule.setInterestPaid(eventDto.getInterestPaid());
            paymentSchedule.setPrinciplePaid(eventDto.getPrinciplePaid());
            paymentSchedule.setPenaltyFeePaid(eventDto.getPenaltyFeePaid());
            paymentSchedule.setOverdueInterestPaid(eventDto.getOverdueInterestPaid());
            loanPaymentScheduleRepository.save(paymentSchedule);

            log.info("Cập nhật thành công LoanPaymentSchedule với type : Payment");

            inbox.setStatus("COMPLETED");
            inboxEventRepository.save(inbox);

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("[Stats Service] Lỗi xử lý tin nhắn. Delivery Tag: {}", deliveryTag, e);
            channel.basicNack(deliveryTag, false, false);
            throw new RuntimeException("Lỗi xử lý event, yêu cầu Rollback", e);
        }
    }

    @RabbitListener(queues = "update-contract-queue")
    @Transactional
    public void handleContractEvent(Message message, Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            String rawPayload = new String(message.getBody());
            log.info("[Stats Service] Nhận được tin nhắn gốc: {}", rawPayload);
            String uniqueEventId =  java.util.UUID.randomUUID().toString();

            // Bước 1: Luôn parse chuỗi gốc thành JsonNode
            JsonNode rootNode = objectMapper.readTree(rawPayload);
            JsonNode actualDataNode = rootNode;

            // Bước 2: Bóc tách nếu có bọc trong trường "payload"
            if (rootNode.has("payload")) {
                actualDataNode = rootNode.get("payload");
            }

            // Bước 3: QUAN TRỌNG - Nếu Node vẫn đang ở dạng String (do bị escape 2 lần), phải parse tiếp
            if (actualDataNode.isTextual()) {
                actualDataNode = objectMapper.readTree(actualDataNode.asText());
            }

            // Bước 4: Chuyển đổi sang DTO
            ContractEventDto contractEventDto = objectMapper.treeToValue(actualDataNode, ContractEventDto.class);


            log.info("[contractId : {}] ; [Status : {}] ",
                    contractEventDto.getContractId(), contractEventDto.getStatus());



            InboxEvent inbox = InboxEvent.builder()
                    //.id(...) // Nếu bạn muốn tự set ID, không thì bỏ qua để database/hibernate tự lo
                    .eventId(uniqueEventId) // Truyền UUID vào đúng cột event_id
                    .payloadReceive(rawPayload)
                    .status("PENDING")
                    .processedAt(LocalDateTime.now())
                    .build();
            inboxEventRepository.save(inbox);

            // xử lý nghiệp vụ tại đây :

            // cập nhật lại
            Contract contract = contractRepository.findById(contractEventDto.getContractId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy contract hợp lệ"));
            contract.setStatus(contractEventDto.getStatus());
            contractRepository.save(contract);

            log.info("Cập nhật thành công Contract với type : Completed");

            inbox.setStatus("COMPLETED");
            inboxEventRepository.save(inbox);

            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("[Stats Service] Lỗi xử lý tin nhắn. Delivery Tag: {}", deliveryTag, e);
            channel.basicNack(deliveryTag, false, false);
            throw new RuntimeException("Lỗi xử lý event, yêu cầu Rollback", e);
        }
    }

    @RabbitListener(queues = "update-overdue-schedule-queue")
    @Transactional
    public void handleLoanScheduleOverdueEvent(Message message, Channel channel,
                                               @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            String rawPayload = new String(message.getBody());
            log.info("[Stats Service] Nhận được tin nhắn gốc: {}", rawPayload);

            String uniqueEventId =  java.util.UUID.randomUUID().toString();

            // Bước 1: Luôn parse chuỗi gốc thành JsonNode
            JsonNode rootNode = objectMapper.readTree(rawPayload);
            JsonNode actualDataNode = rootNode;

            // Bước 2: Bóc tách nếu có bọc trong trường "payload"
            if (rootNode.has("payload")) {
                actualDataNode = rootNode.get("payload");
            }

            // Bước 3: QUAN TRỌNG - Nếu Node vẫn đang ở dạng String (do bị escape 2 lần), phải parse tiếp
            if (actualDataNode.isTextual()) {
                actualDataNode = objectMapper.readTree(actualDataNode.asText());
            }

            // Bước 4: Chuyển đổi sang DTO
            UpdateLoanPaymentEventDto eventDto = objectMapper.treeToValue(actualDataNode, UpdateLoanPaymentEventDto.class);


            log.info("[overdueInterest : {}] ; [penaltyFee : {}]; [status : {}]; [ScheduleId : {}]",
                    eventDto.getOverdueInterest(), eventDto.getPenaltyFee(), eventDto.getStatus(), eventDto.getScheduleId());

            InboxEvent inbox = InboxEvent.builder()
                    //.id(...) // Nếu bạn muốn tự set ID, không thì bỏ qua để database/hibernate tự lo
                    .eventId(uniqueEventId) // Truyền UUID vào đúng cột event_id
                    .payloadReceive(rawPayload)
                    .status("PENDING")
                    .processedAt(LocalDateTime.now())
                    .build();

            inboxEventRepository.save(inbox);

            // xử lý nghiệp vụ tại đây :

            // cập nhật lại
            LoanPaymentSchedule paymentSchedule = loanPaymentScheduleRepository.findById(eventDto.getScheduleId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy schedule hợp lệ"));
            paymentSchedule.setStatus(eventDto.getStatus());
            paymentSchedule.setPenaltyFee(eventDto.getPenaltyFee());
            paymentSchedule.setOverdueInterest(eventDto.getOverdueInterest());
            loanPaymentScheduleRepository.save(paymentSchedule);

            log.info("Cập nhật thành công LoanPaymentSchedule với type : Overdue");

            inbox.setStatus("COMPLETED");
            inboxEventRepository.save(inbox);

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("[Stats Service] Lỗi xử lý tin nhắn. Delivery Tag: {}", deliveryTag, e);
            channel.basicNack(deliveryTag, false, false);
            throw new RuntimeException("Lỗi xử lý event, yêu cầu Rollback", e);
        }
    }
}