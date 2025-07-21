package com.lubayi.fight.netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: lubayi
 * Date: 2025/7/19
 * Time: 13:46
 */
public class NettyServer {

    public static void main(String[] args) {
        Map<Channel, List<String>> db = new ConcurrentHashMap<>();
        ServerBootstrap serverBootstrap = new ServerBootstrap()
            .group(new NioEventLoopGroup(), new NioEventLoopGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
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
                                    String message = msg + "  Nice to meet you! \n";
                                    ctx.channel().writeAndFlush(message);
                                    ctx.fireChannelRead(msg);
                                }
                            })
                            .addLast(new SimpleChannelInboundHandler<String>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                    List<String> messageList = db.computeIfAbsent(ctx.channel(), k -> new ArrayList<>());
                                    messageList.add(msg);
                                }

                                @Override
                                public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                    System.out.println(ctx.channel() + " 注册了。。。");
                                }

                                @Override
                                public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                                    System.out.println(ctx.channel() + "解除注册了。。。");
                                }

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    System.out.println(ctx.channel() + "可以使用了。。。");
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                    List<String> messages = db.get(ctx.channel());
                                    System.out.println("输出db内容：" + messages);
                                }
                            })
                    ;
                }
            });
        ChannelFuture bindFuture = serverBootstrap.bind(8688);
        bindFuture.addListener(f -> {
            if (f.isSuccess()) {
                System.out.println("服务器成功监听端口：" + 8688);
            } else {
                System.out.println("服务器监听端口失败！ ");
            }
        });

    }

}
