package com.lubayi.fight.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lubayi
 * @date 2025/7/11
 */
public class AboutReentrantLock {

    private static final ReentrantLock reentrantLock = new ReentrantLock();

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                reentrantLock.lock();
                try {
                    count++;
                } finally {
                    // ReentrantLock 的锁必须在 finally 中手动释放
                    reentrantLock.unlock();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                reentrantLock.lock();
                try {
                    count++;
                } finally {
                    reentrantLock.unlock();
                }
            }
        });
        thread1.start();
        thread2.start();
        // join 方法等待线程执行完毕
        thread1.join();
        thread2.join();
        System.out.println(count);
    }

}