package com.retry.replay.RetryReplayApplication.retry.strategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IncidentService {

    public void createIncident(String transactionId, String reason, String correlationId) {
        // In real scenario, this could be a call to ServiceNow, Jira, or internal system
        log.error("[{}] Incident created for transaction {} due to {}", correlationId, transactionId, reason);
    }
}

