package com.lubayi.fight.concurrent.thread.pool;

/**
 * Author: lubayi
 * Date: 2025/11/5
 * Time: 21:28
 */
public class ThrowRejectHandle implements RejectHandle {

    @Override
    public void reject(Runnable rejectCommand, MyThreadPool threadPool) {
        throw new RuntimeException("阻塞队列满了！");
    }
}
