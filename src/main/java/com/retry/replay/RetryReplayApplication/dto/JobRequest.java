package com.retry.replay.RetryReplayApplication.dto;

import com.retry.replay.RetryReplayApplication.model.RetryStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class JobRequest {
    private String jobName;
    private String jobGroup;
    private int maxAttempts;
    private long initialDelay;
    private RetryStrategy strategy;
    private boolean useKafka;
}

