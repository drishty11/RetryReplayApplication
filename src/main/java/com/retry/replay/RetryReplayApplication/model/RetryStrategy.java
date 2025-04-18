package com.retry.replay.RetryReplayApplication.model;

public enum RetryStrategy {
    FIXED_INTERVAL,
    EXPONENTIAL_BACKOFF,
    CIRCUIT_BREAKER,
    JITTER
}

