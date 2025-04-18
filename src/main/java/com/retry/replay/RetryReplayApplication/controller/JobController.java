package com.retry.replay.RetryReplayApplication.controller;

import com.retry.replay.RetryReplayApplication.dto.JobRequest;
import com.retry.replay.RetryReplayApplication.job.RetryableJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private Scheduler scheduler;

    @PostMapping("/schedule")
    public String scheduleJob(@RequestBody JobRequest request) throws Exception {
        JobDetail jobDetail = JobBuilder.newJob(RetryableJob.class)
                .withIdentity(request.getJobName(), request.getJobGroup())
                .usingJobData("attempt", 0)
                .usingJobData("jobName", request.getJobName())
                .usingJobData("strategy", request.getStrategy().name())
                .usingJobData("initialDelay", request.getInitialDelay())
                .usingJobData("maxAttempts", request.getMaxAttempts())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .forJob(jobDetail)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        return "Job Scheduled";
    }
}
