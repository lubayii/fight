package com.lubayi.fight.design.observer;

import java.util.Random;

/**
 * @author lubayi
 * @date 2025/11/5
 */
public class WeatherStation {

    private final TVStation tvStation;

    public WeatherStation(TVStation tvStation) {
        this.tvStation = tvStation;
    }

    public String getInfo() {
        if (new Random().nextBoolean()) {
            return "晴天";
        }
        return "雨天";
    }

    public void start() throws InterruptedException {
        while (true) {
            String info = getInfo();
            tvStation.publishEvent(new WeatherUpdateEvent(info));
            Thread.sleep(5000);
        }
    }

}
