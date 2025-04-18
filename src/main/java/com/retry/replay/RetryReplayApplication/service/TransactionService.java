package com.retry.replay.RetryReplayApplication.service;

import com.retry.replay.RetryReplayApplication.model.RetryEvent;
import com.retry.replay.RetryReplayApplication.repository.RetryEventRepository;
import com.retry.replay.RetryReplayApplication.retry.RetryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class TransactionService {

    @Autowired
    private RetryService retryService;

    @Autowired
    private RetryEventRepository retryEventRepository;

    public void replay(String transactionId) {
        MDC.put("correlationId", UUID.randomUUID().toString());
        try {
            log.info("Replaying transaction with ID: {}", transactionId);
            retry(transactionId);  // You can call retry inside replay
        } catch (Exception e) {
            log.error("Replay failed for transaction ID: {}", transactionId, e);
        } finally {
            MDC.clear();
        }
    }

    public void retry(String transactionId) throws Exception {
        MDC.put("correlationId", UUID.randomUUID().toString());

        RetryTemplate retryTemplate = retryService.getFixedIntervalRetryTemplate(); // or getExponentialBackOffRetryTemplate

        retryTemplate.execute(context -> {
            int attempt = context.getRetryCount() + 1;
            log.info("Retry attempt {} for transaction ID: {}", attempt, transactionId);

            // Simulate the actual retry logic
            simulateExternalCall(transactionId);

            // If successful, log it
            log.info("Transaction {} processed successfully", transactionId);
            saveRetryEvent(transactionId, "SUCCESS", "Transaction processed successfully");

            return null;
        }, context -> {
            log.error("Retry failed for transaction ID: {}", transactionId);
            saveRetryEvent(transactionId, "FAILED", "Retry failed for transaction");
            // Optional: send email notification on failure
            return null;
        });

        MDC.clear();
    }

    private void simulateExternalCall(String transactionId) throws Exception {
        // You can replace this with actual logic (e.g., REST call, DB operation)
        if (Math.random() < 0.7) {
            throw new Exception("Simulated failure for transaction " + transactionId);
        }
    }

    private void saveRetryEvent(String transactionId, String status, String errorMessage) {
        RetryEvent retryEvent = new RetryEvent();
        retryEvent.setTransactionId(transactionId);
        retryEvent.setStatus(status);
        retryEvent.setMessage(errorMessage);
        retryEvent.setTimeStamp(LocalDateTime.now());

        // Save event in the repository
        retryEventRepository.save(retryEvent);
    }
}
