package com.lubayi.fight.concurrent;

import java.util.concurrent.*;

/**
 * @author lubayi
 * @date 2025/7/11
 */
public class AboutBlockingQueue {
}
class ArrayBlockingQueueTest {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        executor.execute(new Producer(queue, 1));
        executor.execute(new Producer(queue, 2));

        executor.execute(new Consumer(queue, 1));
        executor.execute(new Consumer(queue, 2));

        Thread.sleep(10000);
        executor.shutdownNow();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }

    static class Producer implements Runnable {

        private final BlockingQueue<Integer> queue;

        private final int producerId;

        public Producer(BlockingQueue<Integer> queue, int producerId) {
            this.queue = queue;
            this.producerId = producerId;
        }

        @Override
        public void run() {
            try {
                int item = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep((long) (Math.random() * 1000));
                    int value = producerId * 100 + item++;

                    queue.put(value);
                    System.out.printf("[生产者%d] 生产: %d | 队列: %s%n", producerId, value, queue);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("[生产者%d] 停止生产%n", producerId);
        }
    }

    static class Consumer implements Runnable {

        private final BlockingQueue<Integer> queue;

        private final int consumerId;

        public Consumer(BlockingQueue<Integer> queue, int consumerId) {
            this.queue = queue;
            this.consumerId = consumerId;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Integer value = queue.take();

                    Thread.sleep(800);
                    System.out.printf("[消费者%d] 消费: %d | 队列: %s%n", consumerId, value, queue);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("[消费者%d] 停止消费%n", consumerId);
        }
    }

}

/*
BlockingQueue 继承自 Queue 的方法
1、插入元素
    boolean add(E e)    将元素添加到队列尾部，如果队列满了，则抛出异常 IllegalStateException。
    boolean offer(E e)  将元素添加到队列尾部，如果队列满了，则返回 false。
2、删除元素
    boolean remove(Object o)    从队列中删除元素，成功返回 true，失败返回 false。
    E poll()       检索并删除此队列的头部，如果此队列为空，则返回null。
3、查找元素
    E element()     检索但不删除此队列的头部，如果队列为空时则抛出 NoSuchElementException 异常。
    peek()      检索但不删除此队列的头部，如果此队列为空，则返回 null。

自定义方法
插入操作
void put(E e)   将元素添加到队列尾部，如果队列满了，则线程将阻塞直到有空间。
offer(E e, long timeout, TimeUnit unit)     将元素插入队列中，如果队列满了，则等待指定的时间，直到队列可用。
删除操作：
E take()     检索并删除此队列的头部，如有必要，则等待直到队列可用。
poll(long timeout, TimeUnit unit)      检索并删除此队列的头部，如果需要元素变得可用，则等待指定的等待时间。
 */