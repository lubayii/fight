package com.lubayi.fight.apply.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Author: lubayi
 * Date: 2025/11/24
 * Time: 06:06
 */
@Configuration
@EnableWebSocketMessageBroker // 使用此注解来标识使其能成为WebSocket的broker，即使用broker来处理消息。
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // //用来注册 Endpoint，"/gs-guide-websocket" 即为客户端尝试建立连接的地址。
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }

    // 实现 WebSocketMessageBrokerConfigurer 中的此方法，配置消息代理（broker）
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 启用 SimpleBroker，使得订阅到此 "topic" 前缀的客户端可以收到greeting消息
        registry.enableSimpleBroker("/topic");
        // 将 "app" 前缀绑定到 MessageMapping 注解指定的方法上。如"app/hello"被指定用greeting()方法来处理.
        registry.setApplicationDestinationPrefixes("/app");
    }
}
