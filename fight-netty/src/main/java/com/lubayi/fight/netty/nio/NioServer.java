package com.lubayi.fight.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author lubayi
 * @date 2025/7/18
 */
public class NioServer {

    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress("localhost", 8088));
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();  // 阻塞函数，监听事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel client = channel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        System.out.println(client.getRemoteAddress() + "连接了");
                    }
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int length = channel.read(byteBuffer);
                        if (length == -1) {
                            System.out.println("客户端断开了连接：" + channel.getRemoteAddress());
                            channel.close();
                        } else {
                            byteBuffer.flip();
                            byte[] buffer = new byte[byteBuffer.remaining()];
                            byteBuffer.get(buffer);
                            String message = new String(buffer);
                            System.out.println(message);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
