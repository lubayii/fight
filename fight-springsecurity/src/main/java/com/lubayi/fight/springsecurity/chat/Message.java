package com.lubayi.fight.springsecurity.chat;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;

/**
 * @author lubayi
 * @date 2025/11/25
 */
public class Message implements MessageKey {
    private String roomId;
    private String name;
    private String command;
    private String info;

    public Message(String json) {
        if (json != null && json.length() > 0) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            this.roomId = jsonObject.getString(KEY_ROOM_ID);
            this.command = jsonObject.getString(KEY_MESSAGE_COMMAND);
            this.info = jsonObject.getString(KEY_INFO);
            this.name = jsonObject.getString(KEY_WEBSOCKET_USERNAME);
        }
    }

    public JSONObject toJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(KEY_INFO, this.info);
            jsonObject.put(KEY_WEBSOCKET_USERNAME, this.name);
            jsonObject.put(KEY_MESSAGE_COMMAND, this.command);
            jsonObject.put(KEY_ROOM_ID, this.roomId);
            return jsonObject;
        } catch (JSONException e) {

        }
        return null;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    public String getInfo() {
        return info;
    }
}
