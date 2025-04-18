package com.retry.replay.RetryReplayApplication.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaRetryListener {

    @KafkaListener(topics = "failed-transactions", groupId = "retry-group")
    public void listenRetryTransaction(String transactionId) {
        System.out.println("Retrying transaction: " + transactionId);
    }
}
