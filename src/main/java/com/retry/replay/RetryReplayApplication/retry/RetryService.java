package com.retry.replay.RetryReplayApplication.retry;

import com.retry.replay.RetryReplayApplication.model.RetryEvent;

import com.retry.replay.RetryReplayApplication.service.RetryEventService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service responsible for handling the retry logic.
 * It records retry attempts, successes, and failures, as well as provides retry strategies.
 */
@Service
@Slf4j
public class RetryService {

    @Autowired
    private RetryEventService retryEventService;

    private final Map<String, RetryEvent> retryEventStore = new ConcurrentHashMap<>();

    // Retrieve all retry events
    public List<RetryEvent> getRetryEvents() {
        // Convert the map to a list for easier handling in a controller or elsewhere
        return new ArrayList<>(retryEventStore.values());
    }

    /**
     * Retry a transaction with the given transaction ID.
     *
     * @param transactionId The ID of the transaction to retry.
     */
    public void retryTransaction(String transactionId) {
        int attemptCount = 1; // This should ideally be tracked dynamically (e.g., in the database)
        String status = "RETRY";
        String errorMessage = null;

        try {
            log.info("Retrying transaction with ID: {}", transactionId);

            // Perform retry logic here (for example, calling an external service)
            // Simulate the retry logic and its outcome (success or failure)
            boolean success = new Random().nextBoolean(); // For demo purposes

            // If retry is successful
            if (success) {
                status = "SUCCESS";
                log.info("Transaction {} retry successful", transactionId);
            } else {
                // Simulate failure
                status = "FAILED";
                errorMessage = "Simulated retry failure for transaction " + transactionId;
                log.warn("Transaction {} retry failed: {}", transactionId, errorMessage);
            }
        } catch (Exception e) {
            // In case of unexpected errors
            status = "FAILED";
            errorMessage = e.getMessage();
            log.error("Transaction {} retry failed due to error: {}", transactionId, errorMessage);
        } finally {
            // Log the retry event after retry attempt (successful or failed)
            retryEventService.logRetryEvent(transactionId, status, attemptCount, errorMessage);
        }
    }

    public RetryTemplate getFixedIntervalRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        FixedBackOffPolicy policy = new FixedBackOffPolicy();
        policy.setBackOffPeriod(5000);  // 5 seconds delay
        retryTemplate.setBackOffPolicy(policy);
        return retryTemplate;
    }

    public RetryTemplate getExponentialBackOffRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(1000);
        policy.setMultiplier(2.0);
        policy.setMaxInterval(30000); // Max 30 seconds delay
        retryTemplate.setBackOffPolicy(policy);
        return retryTemplate;
    }
}