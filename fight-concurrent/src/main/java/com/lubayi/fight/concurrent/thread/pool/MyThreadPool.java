package com.lubayi.fight.concurrent.thread.pool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lubayi
 * @date 2025/11/5
 */
public class MyThreadPool {

    List<Runnable> commandList = new ArrayList<>();

    Thread thread = new Thread(() -> {
        while (true) {
            if (!commandList.isEmpty()) {

            }
        }
    });

    void execute(Runnable command) {
        commandList.add(command);
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

}
