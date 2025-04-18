package com.retry.replay.RetryReplayApplication.service;

import com.retry.replay.RetryReplayApplication.dto.ReplayRequest;
import com.retry.replay.RetryReplayApplication.dto.ReplayResponse;
import com.retry.replay.RetryReplayApplication.model.Transaction;
import com.retry.replay.RetryReplayApplication.retry.strategy.*;
import com.retry.replay.RetryReplayApplication.util.CorrelationIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReplayService {

    private final NotificationService notificationService;
    private final IncidentService incidentService;

    // Constructor Injection to enable Spring DI
    public ReplayService(NotificationService notificationService, IncidentService incidentService) {
        this.notificationService = notificationService;
        this.incidentService = incidentService;
    }

    public ReplayResponse processReplay(ReplayRequest request) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        List<Transaction> transactions = fetchTransactions(request.getTransactionIds());

        ReplayStrategy strategy = request.getReplayType().equalsIgnoreCase("STATEFUL")
                ? new StatefulReplay()
                : new StatelessReplay();

        List<String> failedTransactions = new ArrayList<>();
        for (Transaction tx : transactions) {
            if (validate(tx, request.getScope())) {
                try {
                    if (request.isScheduleLater()) {
                        scheduleReplay(tx, strategy, request.getScheduledTime(), correlationId);
                    } else {
                        strategy.replay(tx);
                        log.info("[{}] Replayed transaction: {}", correlationId, tx.getId());
                    }
                } catch (Exception e) {
                    log.error("[{}] Failed to replay transaction {}: {}", correlationId, tx.getId(), e.getMessage());
                    failedTransactions.add(tx.getId());
                    incidentService.createIncident(tx.getId(), e.getMessage(), correlationId);
                }
            } else {
                log.warn("[{}] Validation failed for transaction: {}", correlationId, tx.getId());
            }
        }

        String summary = failedTransactions.isEmpty()
                ? "All transactions replayed successfully."
                : "Some transactions failed: " + failedTransactions;

        notificationService.sendReplayInitiationEmail(correlationId, request);

        return new ReplayResponse(true, summary);
    }

    private List<Transaction> fetchTransactions(List<String> ids) {
        List<Transaction> list = new ArrayList<>();
        for (String id : ids) {
            list.add(new Transaction(id, "payload", true, "SYSTEM_A"));
        }
        return list;
    }

    private boolean validate(Transaction tx, String scope) {
        return tx.isValid() && tx.getSystem().equalsIgnoreCase(scope);
    }

    private void scheduleReplay(Transaction tx, ReplayStrategy strategy, String scheduledTime, String correlationId) {
        log.info("[{}] Scheduled replay at {} for transaction {}", correlationId, scheduledTime, tx.getId());
    }
}
