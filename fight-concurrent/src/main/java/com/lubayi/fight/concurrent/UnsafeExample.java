package com.lubayi.fight.concurrent;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author lubayi
 * @date 2025/7/14
 */
public class UnsafeExample {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        // 使用反射获取 Unsafe 实例
        Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafeField.get(null);

        // 分配堆外内存，返回内存地址
        long size = 1024;   // 内存大小
        long address = unsafe.allocateMemory(size);

        // 写入数据到堆外内存
        String dataToWrite = "Hello, this is testing direct memory!";
        byte[] dataBytes = dataToWrite.getBytes();
        for (int i = 0; i < dataBytes.length; i++) {
            unsafe.putByte(address + i, dataBytes[i]);
        }

        // 从堆外内存读取数据
        byte[] dataToRead = new byte[dataBytes.length];
        for (int i = 0; i < dataBytes.length; i++) {
            dataToRead[i] = unsafe.getByte(address + i);
        }

        System.out.println(new String(dataToRead));

        // 释放堆外内存
        unsafe.freeMemory(address);
    }

}
/*
Unsafe 是 CAS 的核心类，因为 Java 无法直接访问底层操作系统，而是通过本地（navite）方法来访问，不过尽管如此，
JVM还是开了一个后门，JDK 中有一个类 Unsafe，它提供了硬件级别的原子操作。

Unsafe 是 Java 中一个底层类，包含了很多基础的操作，比如数组操作、对象操作、内存操作、CAS操作、线程（park）操作、栅栏（Fence）操作，
JUC 包、一些三方框架都使用 Unsafe 类来保证并发安全。

Unsafe 类在 JDK 源码的多个类中用到，这个类提供了一些绕开 JVM 的更底层功能，基于它的实现可以提高效率。
但是，它是一把双刃剑：正如它的名字所示的那样，它是 Unsafe 的，它所分配的内存需要手动 free（不被 GC 回收）。
Unsafe 类，提供了 JNI 某些功能的简单替代：确保高效性的同时，使事情变得更简单。

Unsafe 类提供了硬件级别的原子操作，主要提供了以下功能：
1、通过 Unsafe 类可以分配内存，可以释放内存；
2、可以定位对象某字段的内存位置，也可以修改对象的字段值，即使它是私有的；
3、将线程进行挂起与恢复；
4、CAS 操作
 */