package com.retry.replay.RetryReplayApplication.retry.strategy;

import com.retry.replay.RetryReplayApplication.model.Transaction;

public interface ReplayStrategy {
    void replay(Transaction transaction);
}

