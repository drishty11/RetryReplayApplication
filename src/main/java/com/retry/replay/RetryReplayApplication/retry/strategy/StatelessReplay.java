package com.retry.replay.RetryReplayApplication.retry.strategy;

import com.retry.replay.RetryReplayApplication.model.Transaction;

public class StatelessReplay implements ReplayStrategy {
    @Override
    public void replay(Transaction transaction) {
        System.out.println("Replaying stateless transaction: " + transaction.getId());
    }
}

