package com.lubayi.fight.springsecurity.chat;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lubayi
 * @date 2025/11/25
 */
// 实现消息处理器
public class MyHandler implements WebSocketHandler {

    // key 为房间号；Map<String, WebSocketSession>的key为用户名称
    private static final Map<String, Map<String, WebSocketSession>> sUserMap = new HashMap<>(3);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("成功建立连接");
        String info = session.getUri().getPath().split("INFO=")[1];
        System.out.println(info);
        if (info != null && info.length() > 0) {
            JSONObject jsonObject = JSONObject.parseObject(info);
            String command = jsonObject.getString("command");
            String roomId = jsonObject.getString("roomId");
            if (MessageKey.ENTER_COMMAND.equals(command)) {
                Map<String, WebSocketSession> mapSession = sUserMap.computeIfAbsent(roomId, k -> new HashMap<>(3));
                mapSession.put(jsonObject.getString("name"), session);
                session.sendMessage(new TextMessage("当前房间在线人数" + mapSession.size() + "人"));
                System.out.println(session);
            }
        }
        System.out.println("当前在线人数：" + sUserMap.size());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(webSocketMessage.getPayload().toString());
        Message message = new Message(jsonObject.toString());
        System.out.println(message + ":来自" + session.getAttributes().get(MessageKey.KEY_WEBSOCKET_USERNAME) + "的消息");
        if (message.getName() != null && message.getCommand() != null) {
            switch (message.getCommand()) {
                case MessageKey.ENTER_COMMAND:
                    sendMessageToRoomUsers(message.getRoomId(),
                            new TextMessage("【" + getNameFromSession(session) + "】加入了房间，欢迎！"));
                    break;
                case MessageKey.MESSAGE_COMMAND:
                    if (message.getName().equals("all")) {
                        sendMessageToRoomUsers(message.getRoomId(),
                                new TextMessage(getNameFromSession(session) + "说：" + message.getInfo()));
                    } else {
                        sendMessageToUser(message.getRoomId(), message.getName(),
                                new TextMessage(getNameFromSession(session) + "悄悄对你说：" + message.getInfo()));
                    }
                    break;
                case MessageKey.LEAVE_COMMAND:
                    sendMessageToRoomUsers(message.getRoomId(),
                            new TextMessage("【" + getNameFromSession(session) + "】离开了房间，欢迎下次再来！"));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("连接出错！");
        if (session.isOpen()) {
            session.close();
        }
        Map<String, WebSocketSession> map = sUserMap.get(getRoomIdFromSession(session));
        if (map != null) {
            map.remove(getNameFromSession(session));
        }
    }

    // 用来处理当用户离开房间后的信息销毁工作
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("连接已关闭：" + closeStatus);
        Map<String, WebSocketSession> map = sUserMap.get(getRoomIdFromSession(session));
        if (map != null) {
            map.remove(getNameFromSession(session));
        }
    }

    private boolean sendMessageToRoomUsers(String roomId, TextMessage message) {
        if (roomId == null || sUserMap.get(roomId) == null) {
            return false;
        }
        boolean allSendSuccess = true;
        Collection<WebSocketSession> sessions = sUserMap.get(roomId).values();
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                allSendSuccess = false;
                e.printStackTrace();
            }
        }
        return allSendSuccess;
    }

    private boolean sendMessageToUser(String roomId, String name, TextMessage message) {
        if (roomId == null || name == null || sUserMap.get(roomId) == null) {
            return false;
        }
        WebSocketSession session = sUserMap.get(roomId).get(name);
        if (!session.isOpen()) {
            return false;
        }
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getNameFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get(MessageKey.KEY_WEBSOCKET_USERNAME);
    }

    private String getRoomIdFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get(MessageKey.KEY_ROOM_ID);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
