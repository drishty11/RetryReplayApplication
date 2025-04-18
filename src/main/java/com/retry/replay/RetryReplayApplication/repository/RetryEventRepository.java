package com.retry.replay.RetryReplayApplication.repository;

import com.retry.replay.RetryReplayApplication.model.RetryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetryEventRepository extends JpaRepository<RetryEvent, Long> {
    // Custom queries can go here if needed, e.g., find by transactionId
    List<RetryEvent> findByTransactionId(String transactionId);
}
