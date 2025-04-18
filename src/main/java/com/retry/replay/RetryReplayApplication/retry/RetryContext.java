package com.retry.replay.RetryReplayApplication.retry;

import com.retry.replay.RetryReplayApplication.model.RetryStrategy;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RetryContext {

    @Autowired
    private ApplicationContext context;

    public void scheduleRetry(JobExecutionContext executionContext, Exception e) {
        JobDataMap oldMap = executionContext.getJobDetail().getJobDataMap();
        int currentAttempt = oldMap.getInt("attempt");
        int nextAttempt = currentAttempt + 1;

        RetryStrategy strategy = RetryStrategy.valueOf(oldMap.getString("strategy"));
        RetryStrategyHandler handler = (RetryStrategyHandler) context.getBean(strategy.name());

        // Create a new JobDataMap with updated attempt
        JobDataMap newMap = new JobDataMap();
        newMap.put("jobName", oldMap.getString("jobName"));
        newMap.put("strategy", strategy.name());
        newMap.put("initialDelay", oldMap.getLong("initialDelay"));
        newMap.put("maxAttempts", oldMap.getInt("maxAttempts"));
        newMap.put("attempt", nextAttempt); // Incremented attempt here

        handler.handleRetry(executionContext, e, newMap);

    }
}

