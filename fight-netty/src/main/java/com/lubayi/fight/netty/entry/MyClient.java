package com.lubayi.fight.netty.entry;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author lubayi
 * @date 2025/10/24
 */
// 客户端启动类
public class MyClient {

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            // 创建 bootstrp 对象，配置参数
            // Bootstrap —— 创建客户端启动器的工厂类
            Bootstrap bootstrap = new Bootstrap();
            // 设置线程组
            bootstrap.group(eventExecutors)
                    // 设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    // 使用匿名内部类初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 添加客户端通道的处理器
                            socketChannel.pipeline().addLast(new MyClientHandler());
                        }
                    });

            System.out.println("客户端准备就绪...");
            // 连接服务器
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666).sync();
            // 对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 关闭线程组
            eventExecutors.shutdownGracefully();
        }
    }

}
/*
ChannelFuture 提供操作完成时一种异步通知的方式。
一般在Socket编程中，等待响应结果都是同步阻塞的，而 Netty 则不会造成阻塞，
因为 ChannelFuture 是采取类似观察者模式的形式进行获取结果。
 */