package com.lubayi.fight.spring.proxy;

/**
 * @author lubayi
 * @date 2025/12/12
 */
// 实现发送短信的接口
public class SmsServiceImpl implements SmsService {

    @Override
    public String send(String message) {
        System.out.println("send message: " + message);
        return message;
    }

}
