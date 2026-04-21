package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
public class OutboxEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    // Trong PostgreSQL, Jsonb được map tốt nhất qua các thư viện như Hibernate Types.
    // Nếu chưa cài thư viện, lưu tạm dưới dạng String.
    @Column(name = "payload", columnDefinition = "jsonb", nullable = false)
    private String payload;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, PUBLISHED, FAILED

    @Column(name = "transaction_loan_payment_schedule_id")
    private String transactionId;
}