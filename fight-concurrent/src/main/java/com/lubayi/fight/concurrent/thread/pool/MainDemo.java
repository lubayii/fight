package com.lubayi.fight.concurrent.thread.pool;

/**
 * @author lubayi
 * @date 2025/11/5
 */
public class MainDemo {

    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool();
        for (int i = 0; i < 5; i++) {
            myThreadPool.execute(() -> {
                // 此任务为：3s之后，打印执行任务的线程
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName());
            });
        }

        System.out.println("主线程没有被阻塞");
    }

}
