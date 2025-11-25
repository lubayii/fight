package com.lubayi.fight.springsecurity.chat;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author lubayi
 * @date 2025/11/25
 */
// 实现握手拦截器
// HandshakeInterceptor 用来拦截客户端第一次连接服务端时的请求，即客户端连接 /webSocket/{INFO} 时，可以获取到对应INFO信息
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            String info = request.getURI().getPath().split("INFO=")[1];
            if (info != null && info.length() > 0) {
                JSONObject jsonObject = JSONObject.parseObject(info);
                String command = jsonObject.getString("command");
                if (MessageKey.ENTER_COMMAND.equals(command)) {
                    System.out.println("当前session的ID=" + jsonObject.getString("name"));
                    ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
                    HttpSession session = httpRequest.getServletRequest().getSession();
                    attributes.put(MessageKey.KEY_WEBSOCKET_USERNAME, jsonObject.getString("name"));
                    attributes.put(MessageKey.KEY_ROOM_ID, jsonObject.getString("roomId"));
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        System.out.println("进来webSocket的afterHandshake拦截器！");
    }

}
