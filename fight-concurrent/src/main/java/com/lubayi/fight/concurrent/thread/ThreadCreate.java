package com.lubayi.fight.concurrent.thread;

/**
 * 创建线程的方式
 * 1.继承 Thread 类创建线程
 * 2.实现 Runnable 接口创建线程
 * 3.通过 Callable 和 FutureTask 创建线程
 * 4.通过线程池创建线程
 * @author lubayi
 * @date 2025/7/10
 */
public class ThreadCreate {
    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        t1.run();
    }
}
// 1.继承 Thread 类创建线程
class MyThread extends Thread {
    @Override
    public void run() {
        // run() 方法中的内容，就是线程具体要执行的任务
        for (int i = 1; i <= 100; i++) {
            System.out.println(getName() + "喷雾喷了：" + i + " 下");
        }
    }
}
// 2.实现 Runnable 接口创建线程
class MyRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            System.out.println(Thread.currentThread().getName() + "喷雾喷了：" + i + " 下");
        }
    }
}
// 3.通过 Callable 和 FutureTask 创建线程