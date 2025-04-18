package com.retry.replay.RetryReplayApplication.retry.strategy;

import com.retry.replay.RetryReplayApplication.job.RetryableJob;
import com.retry.replay.RetryReplayApplication.retry.RetryStrategyHandler;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component("JITTER")
public class JitterStrategy implements RetryStrategyHandler {

    private final Random random = new Random();

    @Override
    public void handleRetry(JobExecutionContext context, Exception e, JobDataMap newMap) {
        try {
            int attempt = newMap.getInt("attempt");
            int maxAttempts = newMap.getInt("maxAttempts");

            if (attempt >= maxAttempts) {
                System.out.println("🔁 Max retry attempts reached for job: " + newMap.getString("jobName"));
                return;
            }

            long baseDelay = newMap.getLong("initialDelay");

            // Add jitter of ±50%
            long jitter = (long) (baseDelay * 0.5);
            long delay = baseDelay + random.nextInt((int) (2 * jitter + 1)) - jitter;

            if (delay < 1) delay = 1;

            JobDetail newJob = JobBuilder.newJob(RetryableJob.class)
                    .withIdentity(UUID.randomUUID().toString(), context.getJobDetail().getKey().getGroup())
                    .usingJobData(newMap)
                    .build();

            Trigger retryTrigger = TriggerBuilder.newTrigger()
                    .startAt(DateBuilder.futureDate((int) delay, DateBuilder.IntervalUnit.SECOND))
                    .forJob(newJob)
                    .build();

            context.getScheduler().scheduleJob(newJob, retryTrigger);

            System.out.println("Jitter retry scheduled for job: " + newMap.getString("jobName") +
                    ", Attempt: " + attempt + ", Delay: " + delay + "s");

        } catch (SchedulerException ex) {
            throw new RuntimeException(ex);
        }
    }
}