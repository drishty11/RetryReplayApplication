package com.retry.replay.RetryReplayApplication.util;

import java.util.UUID;

public class CorrelationIdUtil {

    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}

