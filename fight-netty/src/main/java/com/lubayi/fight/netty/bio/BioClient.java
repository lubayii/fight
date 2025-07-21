package com.lubayi.fight.netty.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author lubayi
 * @date 2025/7/18
 */
public class BioClient {

    public static void main(String[] args) throws InterruptedException {
        Thread tom = new Thread(() -> {
            sendHello();
        }, "Tom");
        Thread jerry = new Thread(() -> {
            sendHello();
        }, "Jerry");
        tom.start();
        jerry.start();
        tom.join();
        jerry.join();
    }

    private static void sendHello() {
        try {
            // socket 连接第 1 种写法
            // Socket socket = new Socket("localhost", 8088);
            // socket 连接第 2 种写法
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 8088));
            OutputStream os = socket.getOutputStream();
            for (int i = 0; i < 10; i++) {
                String message = Thread.currentThread().getName() + " say hello" + i;
                os.write(message.getBytes());
                os.flush();
            }
            Thread.sleep(1000);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
