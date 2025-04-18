package com.retry.replay.RetryReplayApplication.retry.strategy;

import com.retry.replay.RetryReplayApplication.job.RetryableJob;
import com.retry.replay.RetryReplayApplication.retry.RetryStrategyHandler;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component("FIXED_INTERVAL")
public class FixedIntervalStrategy implements RetryStrategyHandler {

    @Override
    public void handleRetry(JobExecutionContext context, Exception e, JobDataMap newMap) {
        try {
            int attempt = newMap.getInt("attempt");
            int maxAttempts = newMap.getInt("maxAttempts");

            if (attempt >= maxAttempts) {
                System.out.println("🔁 Max retry attempts reached for job: " + newMap.getString("jobName"));
                return;
            }

            long delay = newMap.getLong("initialDelay");

            JobDetail newJob = JobBuilder.newJob(RetryableJob.class)
                    .withIdentity(UUID.randomUUID().toString(), context.getJobDetail().getKey().getGroup())
                    .usingJobData(newMap)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .startAt(Date.from(Instant.now().plusSeconds(delay)))
                    .forJob(newJob)
                    .build();

            context.getScheduler().scheduleJob(newJob, trigger);

            System.out.println("Scheduled retry for job: " + newMap.getString("jobName") + ", Attempt: " + attempt + ", Delay: " + delay + "s");

        } catch (SchedulerException ex) {
            throw new RuntimeException(ex);
        }
    }
}