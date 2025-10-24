package com.lubayi.fight.netty.entry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author lubayi
 * @date 2025/10/24
 */
// 服务端启动类
public class MyServer {
    public static void main(String[] args) throws Exception {
        // 创建两个线程组 boosGroup、workerGroup
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务端的启动对象，设置参数
            /**
             * ServerBootstrap —— 创建服务端启动器的工厂类。
             * 创建启动器的步骤：设置 EventLoopGroup 线程组 -> 设置 channel 通道类型 -> 设置 option 参数
             * -> 设置 handler 流水线 -> 进行端口绑定 -> 启动 -> 等待通道关闭 -> 关闭 EventLoopGroup
             */
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 设置两个线程组 boosGroup 和 workerGroup
            bootstrap.group(boosGroup, workerGroup)
                    // 设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 给 pipeline 管道设置处理器
                            socketChannel.pipeline().addLast(new MyServerHandler());
                        }
                    });//给 workerGroup 的 EventLoop 对应的管道设置处理器

            System.out.println("服务端已经准备就绪...");
            // 绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(6666).sync();
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
