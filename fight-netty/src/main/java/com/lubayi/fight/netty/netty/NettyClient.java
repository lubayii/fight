package com.lubayi.fight.netty.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Author: lubayi
 * Date: 2025/7/19
 * Time: 14:14
 */
public class NettyClient {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                        System.out.println(msg);
                                    }
                                })
                        ;
                    }
                });
        ChannelFuture connect = bootstrap.connect("localhost", 8688);
        connect.addListener(f -> {
            if (f.isSuccess()) {
                System.out.println("成功连接了8688服务器！");
                EventLoop eventLoop = connect.channel().eventLoop();
                eventLoop.scheduleAtFixedRate(() -> {
                    connect.channel().writeAndFlush("Hello, Netty " + System.currentTimeMillis() + "\n");
                }, 0, 10, TimeUnit.SECONDS);
            }
        });
    }

}
