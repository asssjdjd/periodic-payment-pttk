package com.example.repository;

import com.example.entity.InboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboxEventRepository extends JpaRepository<InboxEvent,String> {
    boolean existsByEventId(String eventId);
}
