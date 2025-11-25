package com.lubayi.fight.springsecurity.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author lubayi
 * @date 2025/11/25
 */
// 配置WebSocket
@Configuration("chatSocketConfig")
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册 WebSocketHandler(MyHandler)，用来处理WebSocket建立以及消息处理的类
        registry.addHandler(new MyHandler(), "/websocket/{INFO}")
                .setAllowedOrigins("*")
                // 注册WebSocketInterceptor拦截器，用来在客户端向服务端发起初次连接时，记录客户端拦截信息
                .addInterceptors(new WebSocketInterceptor());
    }

}
