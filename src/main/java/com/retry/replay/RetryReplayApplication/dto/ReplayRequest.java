package com.retry.replay.RetryReplayApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplayRequest {
    private List<String> transactionIds;
    private String scope;              // e.g. SYSTEM_A, SYSTEM_B
    private String replayType;        // STATEFUL or STATELESS
    private boolean scheduleLater;
    private String scheduledTime;     // optional ISO 8601
}
