package com.retry.replay.RetryReplayApplication.retry.strategy;

import com.retry.replay.RetryReplayApplication.model.Transaction;

public class StatefulReplay implements ReplayStrategy {
    @Override
    public void replay(Transaction transaction) {
        System.out.println("Replaying stateful transaction: " + transaction.getId());
        // include system context handling here
    }
}
