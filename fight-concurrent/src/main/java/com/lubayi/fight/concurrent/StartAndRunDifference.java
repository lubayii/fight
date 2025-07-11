package com.lubayi.fight.concurrent;

/**
 * @author lubayi
 * @date 2025/7/10
 */
public class StartAndRunDifference {

    public static void main(String[] args) {
        RunnableWay runnable = new RunnableWay();
        Thread thread1 = new Thread(runnable);

        System.out.println("【直接调用run()】开始......");
        // run()只是普通方法，直接调用不会创建新线程。
        thread1.run();

        System.out.println("【正确调用run()】开始......");
        Thread thread2 = new Thread(runnable);
        // start()会触发JVM创建新线程并在新线程中执行run()中的逻辑
        thread2.start();

        System.out.println("主线程继续执行...");
    }
}
class RunnableWay implements Runnable {
    @Override
    public void run() {
        // 打印当前线程名称
        System.out.println("执行线程为：" + Thread.currentThread().getName());

        // 模拟耗时操作
        for (int i = 1; i <= 3; i++) {
            try {
                // Thread.sleep() 使当前线程睡眠指定时间，即 使线程暂时停止执行，并不会释放锁。时间到后，线程会重新进入 RUNNABLE 状态。
                Thread.sleep(500); // 暂停500ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 线程工作中：" + i);
        }
    }
}