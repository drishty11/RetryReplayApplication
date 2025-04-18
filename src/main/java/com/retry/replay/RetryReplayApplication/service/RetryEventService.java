package com.retry.replay.RetryReplayApplication.service;

import com.retry.replay.RetryReplayApplication.model.RetryEvent;
import com.retry.replay.RetryReplayApplication.repository.RetryEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RetryEventService {

    @Autowired
    private RetryEventRepository retryEventRepository;

    public void logRetryEvent(String transactionId, String status, int attemptCount, String errorMessage) {
        RetryEvent event = new RetryEvent();
        event.setTransactionId(transactionId);
        event.setStatus(status);
        event.setTimeStamp(LocalDateTime.now());
        event.setAttemptCount(attemptCount);
        event.setErrorMessage(errorMessage);

        retryEventRepository.save(event);
    }

    public List<RetryEvent> getRetryEventsByTransactionId(String transactionId) {
        return retryEventRepository.findByTransactionId(transactionId);
    }
}