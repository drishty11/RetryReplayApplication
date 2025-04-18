package com.retry.replay.RetryReplayApplication.controller;

import com.retry.replay.RetryReplayApplication.dto.ReplayRequest;
import com.retry.replay.RetryReplayApplication.dto.ReplayResponse;
import com.retry.replay.RetryReplayApplication.service.ReplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/replay")
public class ReplayController {

    @Autowired
    private ReplayService replayService;

    @PostMapping
    public ReplayResponse replay(@RequestBody ReplayRequest request) {
        return replayService.processReplay(request);
    }
}
