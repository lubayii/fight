package com.lubayi.fight.design.observer;

/**
 * @author lubayi
 * @date 2025/11/5
 */
public class MainDemo {

    public static void main(String[] args) throws InterruptedException {
        TVStation tvStation = new TVStation();
        WeatherStation station = new WeatherStation(tvStation);
        User tom = new User("tom", (info) -> {
            if (info.equals("晴天")) {
                System.out.println("晴天啦，tom出去玩");
            } else {
                System.out.println("tom在家待着");
            }
        });
        User jerry = new User("jerry", (info) -> {
            if (info.equals("雨天")) {
                System.out.println("jerry 钻洞");
            }
        });
        tvStation.subscribe(tom, WeatherUpdateEvent.class);
        tvStation.subscribe(jerry, WeatherUpdateEvent.class);
        station.start();
    }

}
