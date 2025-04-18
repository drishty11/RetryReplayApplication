package com.retry.replay.RetryReplayApplication.retry;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

public interface RetryStrategyHandler {
    void handleRetry(JobExecutionContext context, Exception e, JobDataMap newMap);
}
