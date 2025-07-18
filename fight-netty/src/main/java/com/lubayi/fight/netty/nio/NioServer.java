package com.lubayi.fight.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
            ssc.bind(new InetSocketAddress("localhost", 8688));
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            Iterator<SelectionKey> iterator = (Iterator<SelectionKey>) selector.selectedKeys();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
