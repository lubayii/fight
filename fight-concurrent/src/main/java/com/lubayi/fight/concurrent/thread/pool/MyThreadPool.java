package com.lubayi.fight.concurrent.thread.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author lubayi
 * @date 2025/11/5
 */
public class MyThreadPool {

    private final int corePoolSize;

    private final int maxSize;

    private final int timeout;

    private final TimeUnit timeUnit;

    public final BlockingQueue<Runnable> blockingQueue;

    private final RejectHandle rejectHandle;

    public MyThreadPool(int corePoolSize, int maxSize, int timeout, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue, RejectHandle rejectHandle) {
        this.corePoolSize = corePoolSize;
        this.maxSize = maxSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.blockingQueue = blockingQueue;
        this.rejectHandle = rejectHandle;
    }

    class CoreThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable command = blockingQueue.take();
                    command.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class SupportThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable command = blockingQueue.poll(timeout, timeUnit);
                    if (command == null) {
                        break;
                    }
                    command.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " 线程结束了！");
        }
    }

    // 线程池应该有多少个线程
    List<Thread> coreList = new ArrayList<>();

    List<Thread> supportList = new ArrayList<>();

    // 问题：在判断长度和添加元素时，整个操作不是原子的，有线程安全问题 ---> 可以用原子操作、CAS的原子变量、加锁
    void execute(Runnable command) {
        // 1、我们判断 threadList 中有多少个元素，如果没到 corePoolSize，那么我们就创建线程
        if (coreList.size() < corePoolSize) {
            Thread thread = new CoreThread();
            coreList.add(thread);
            thread.start();
        }
        if (blockingQueue.offer(command)) {
            return;
        }
        // 2、当 offer 值为false时，说明任务队列已经满了，核心线程处理不过来
        if (coreList.size() + supportList.size() < maxSize) {
            Thread thread = new SupportThread();
            supportList.add(thread);
            thread.start();
        }
        if (!blockingQueue.offer(command)) {
            rejectHandle.reject(command, this);
        }
    }

    /*
    1、线程什么时候创建
    2、线程的 runnable 是什么，是我们提交的 command 吗
     */

    /*
    // 也可以实现，但存在两个问题：1、避免频繁创建线程，因为创建线程是非常消耗资源的；2、没有办法管理创建的线程
    new Thread(command).start();

    // 此处把每次创建好的线程，放在集合中进行管理，那这个线程可以复用吗？可以在集合里有线程的时候，用已经存在的线程执行command吗？
    // 不可以，一个线程的生命周期，从它被创建开始，到它完成自己所在的command之后，就会被销毁
    Thread thread = new Thread(command);
    threadList.add(thread);
    thread.start();
     */

    /*
    Thread thread = new Thread(() -> {

//        while (true) {
//            // 如果 commandList 是空，那 死循环 则是无谓地消耗 CPU 资源  ---> 所以用到为空则阻塞的容器【阻塞队列】
//            if (!commandList.isEmpty()) {
//                Runnable command = commandList.remove(0);
//                command.run();
//            }
//        }

        while (true) {
            try {
                Runnable command = blockingQueue.take();
                command.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
     }, "唯一线程");

        {
        thread.start();
        }
     */

}
