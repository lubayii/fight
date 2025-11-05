package com.lubayi.fight.concurrent.thread.pool;

/**
 * Author: lubayi
 * Date: 2025/11/5
 * Time: 21:29
 */
public class DiscardRejectHandle implements RejectHandle {

    @Override
    public void reject(Runnable rejectCommand, MyThreadPool threadPool) {
        threadPool.blockingQueue.poll();
        threadPool.execute(rejectCommand);
    }
}
