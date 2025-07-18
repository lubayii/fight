package com.lubayi.fight.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lubayi
 * @date 2025/7/18
 */
public class BioServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8088);
            while (true) {
                Socket socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
//                String content = new String(buffer);
                    String content = new String(buffer, 0, length);
                    System.out.println("客户端发送的消息内容为：" + content);
                }
                System.out.println("客户端断开连接");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
