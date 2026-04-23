package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inbox_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InboxEvent extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "event_id")
    String eventId;

    @Column(name = "payload_receive",  columnDefinition = "jsonb",nullable = false)
    String payloadReceive;

    @Column(name = "status", nullable = false)
    String status;

    @Column(name = "processed_at",nullable = false)
    LocalDateTime processedAt;
}
