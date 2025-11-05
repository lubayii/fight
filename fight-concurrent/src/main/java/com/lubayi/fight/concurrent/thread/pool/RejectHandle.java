package com.lubayi.fight.concurrent.thread.pool;

/**
 * Author: lubayi
 * Date: 2025/11/5
 * Time: 21:25
 */
public interface RejectHandle {

    void reject(Runnable rejectCommand, MyThreadPool threadPool);

}
