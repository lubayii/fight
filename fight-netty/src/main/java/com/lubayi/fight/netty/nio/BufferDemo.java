package com.lubayi.fight.netty.nio;

import java.nio.ByteBuffer;

/**
 * @author lubayi
 * @date 2025/10/24
 */
public class BufferDemo {

    /*
    创建 Buffer 的方式：JVM堆内内存块Buffer(非直接缓冲区)-HeapByteBuffer、堆外内存块Buffer(直接缓冲区)-DirectByteBuffer
     */
    public static void main(String[] args) {
        String msg = "nio, hello!测试";
        byte[] bytes = msg.getBytes();
        // 创建一个固定大小的buffer(返回的是HeapByteBuffer)；allocateDirect(返回的是DirectByteBuffer)
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 写入数据到 buffer 中
        byteBuffer.put(bytes);
        // 切换成读模式，关键一步。【缓存区是双向的，既可以往缓冲区写入数据，也可以从缓冲区读取数据。但是不能同时进行，需要切换】
        byteBuffer.flip();
        // 创建一个临时数组，用于存储获取到的数据
        byte[] tempByte = new byte[bytes.length];
        int i = 0;
        // 如果还有数据，就循环。循环判断条件
        while (byteBuffer.hasRemaining()) {
            // 获取 byteBuffer 中的数据
            byte b = byteBuffer.get();
            // 放到临时数组中
            tempByte[i++] = b;
        }
        System.out.println(new String(tempByte));
    }

}
