package com.lubayi.fight.design.observer;

/**
 * @author lubayi
 * @date 2025/11/5
 */
public interface Event {

    long timestamp();

    Object source();

}
