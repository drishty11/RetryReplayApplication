package com.retry.replay.RetryReplayApplication.retry.strategy;

import com.retry.replay.RetryReplayApplication.job.RetryableJob;
import com.retry.replay.RetryReplayApplication.retry.RetryStrategyHandler;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("EXPONENTIAL_BACKOFF")
public class ExponentialBackoffStrategy implements RetryStrategyHandler {

    @Override
    public void handleRetry(JobExecutionContext context, Exception e, JobDataMap newMap) {
        try {
            int attempt = newMap.getInt("attempt");
            long baseDelay = newMap.getLong("initialDelay");

            long delay = baseDelay * (1L << (attempt - 1)); // Exponential backoff

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

