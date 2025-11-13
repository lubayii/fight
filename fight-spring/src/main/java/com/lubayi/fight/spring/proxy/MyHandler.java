package com.lubayi.fight.spring.proxy;

/**
 * Author: lubayi
 * Date: 2025/11/13
 * Time: 22:23
 */
public interface MyHandler {

    String functionBody(String methodName);

    default void setProxy(MyInterface proxy) {

    }

}
