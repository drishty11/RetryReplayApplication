package com.retry.replay.RetryReplayApplication.retry.strategy;

import com.retry.replay.RetryReplayApplication.job.RetryableJob;
import com.retry.replay.RetryReplayApplication.retry.RetryStrategyHandler;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("CIRCUIT_BREAKER")
public class CircuitBreakerStrategy implements RetryStrategyHandler {

    private static final int FAILURE_THRESHOLD = 3;
    private static final long CIRCUIT_OPEN_TIMEOUT = 60; // seconds

    @Override
    public void handleRetry(JobExecutionContext context, Exception e, JobDataMap newMap) {
        try {
            int attempt = newMap.getInt("attempt");
            int maxAttempts = newMap.getInt("maxAttempts");

            if (attempt >= FAILURE_THRESHOLD) {
                System.out.println("🔌 Circuit breaker activated for job: " + newMap.getString("jobName") + ". Pausing further retries.");
                return;
            }

            long delay = CIRCUIT_OPEN_TIMEOUT;

            JobDetail newJob = JobBuilder.newJob(RetryableJob.class)
                    .withIdentity(UUID.randomUUID().toString(), context.getJobDetail().getKey().getGroup())
                    .usingJobData(newMap)
                    .build();

            Trigger retryTrigger = TriggerBuilder.newTrigger()
                    .startAt(DateBuilder.futureDate((int) delay, DateBuilder.IntervalUnit.SECOND))
                    .forJob(newJob)
                    .build();

            context.getScheduler().scheduleJob(newJob, retryTrigger);

        } catch (SchedulerException ex) {
            throw new RuntimeException(ex);
        }
    }
}