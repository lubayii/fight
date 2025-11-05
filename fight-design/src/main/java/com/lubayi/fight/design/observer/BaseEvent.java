package com.lubayi.fight.design.observer;

/**
 * @author lubayi
 * @date 2025/11/5
 */
public abstract class BaseEvent implements Event {

    private final long timestamp;

    protected BaseEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

}
