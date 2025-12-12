package com.lubayi.fight.spring.proxy.dynamic.cglib;

/**
 * @author lubayi
 * @date 2025/12/12
 */
public class AliSmsService  {

    public String send(String message) {
        System.out.println("send message: " + message);
        return message;
    }

}
