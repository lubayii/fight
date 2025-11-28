package com.lubayi.fight.concurrent.completableFuture;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lubayi
 * @date 2025/11/28
 */
public class LogHelper {

    public static void printLog(String logContent) {
        System.out.println(getCurrentTime() + currentThreadId() + logContent);
    }

    private static String getCurrentTime() {
        LocalTime time = LocalTime.now();
        return time.format(DateTimeFormatter.ofPattern("[HH:mm:ss.SSS]"));
    }

    private static String currentThreadId() {
        return "[" + Thread.currentThread().getName() + "|" + Thread.currentThread().getId() + "]";
    }

}
