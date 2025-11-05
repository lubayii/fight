package com.lubayi.fight.concurrent.thread.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author lubayi
 * @date 2025/11/5
 */
public class MainDemo {

    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(2, 4, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), new DiscardRejectHandle());
        for (int i = 0; i < 7; i++) {
            final int fi = i;
            myThreadPool.execute(() -> {
                // 此任务为：3s之后，打印执行任务的线程
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + "   " + fi);
            });
        }

        System.out.println("主线程没有被阻塞");
    }

}
