package com.retry.replay.RetryReplayApplication.job;

import com.retry.replay.RetryReplayApplication.retry.RetryContext;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetryableJob implements Job {

    @Autowired
    private RetryContext retryContext;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = context.getJobDetail().getJobDataMap();
        String jobName = map.getString("jobName");
        int attempt = map.getInt("attempt");
        int maxAttempts = map.getInt("maxAttempts");

        System.out.println("Executing job: " + jobName + ", Attempt: " + attempt);

        try {
            // Simulate failure
            if (attempt < maxAttempts) {
                throw new RuntimeException("Simulated failure");
            }
            System.out.println("Job succeeded!");
        } catch (Exception e) {
            retryContext.scheduleRetry(context, e);
        }
    }
}



