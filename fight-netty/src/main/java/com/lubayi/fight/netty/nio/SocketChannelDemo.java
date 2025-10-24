package com.lubayi.fight.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author lubayi
 * @date 2025/10/24
 */
public class SocketChannelDemo {

    public static void main(String[] args) throws Exception {
        // 获取 ServerSocketChannel——open()方法可以获取服务器的通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);
        // 绑定地址，端口号
        serverSocketChannel.bind(address);
        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        while (true) {
            // 获取 SocketChannel——客户端的连接通道
            SocketChannel socketChannel = serverSocketChannel.accept();
            Integer port = socketChannel.socket().getPort();
            System.out.println(port + "客户端已连接");
            while (socketChannel.read(byteBuffer) != -1) {
                String msg = port + "客户端发送的消息为：" + new String(byteBuffer.array());
                // 打印结果
                System.out.println(msg);
                // 清空缓冲区
                byteBuffer.clear();
            }
        }
    }

}
