package com.lubayi.fight.design.observer;

/**
 * @author lubayi
 * @date 2025/11/5
 */
public class WeatherUpdateEvent extends BaseEvent  {

    private final String info;

    public WeatherUpdateEvent(String info) {
        this.info = info;
    }

    @Override
    public Object source() {
        return info;
    }

}
