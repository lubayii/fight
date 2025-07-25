package com.lubayi.fight.concurrent.thread;

/**
 * @author lubayi
 * @date 2025/7/25
 */
public class AboutThreadState {

    public static void main(String[] args) throws InterruptedException {
        testNew();
        System.out.println("==========================");
        // 等同于操作系统中的就绪和运行
        testRunnable();
        System.out.println("==========================");
        /*
        jdk 注释：
        Thread state for a thread blocked waiting for a monitor lock.
        A thread in the blocked state is waiting for a monitor lock to enter a synchronized block/method or
        reenter a synchronized block/method after calling  Object.wait.
         */
        testBlocked();

    }

    private static void testNew() {
        Thread thread = new Thread();
        System.out.println(thread.getState());
    }

    private static void testRunnable() {
        Thread thread = new Thread();
        thread.start();
        System.out.println(thread.getState());
    }

    private static void testBlocked() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (AboutThreadState.class) {
                    System.out.println("t1获取到锁！状态为：" + Thread.currentThread().getState());
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("t2尝试获取锁！");
                synchronized (AboutThreadState.class) {
                    System.out.println("t2获取到锁！状态为：" + Thread.currentThread().getState());
                }
            }
        });
        t1.start();
        Thread.sleep(100);
        System.out.println("t1的状态为：" + t1.getState());
        t2.start();
        for(; ;) {
            System.out.println("t2的状态为：" + t2.getState());
            if (t2.getState() == Thread.State.TERMINATED) break;
        }

    }

}
