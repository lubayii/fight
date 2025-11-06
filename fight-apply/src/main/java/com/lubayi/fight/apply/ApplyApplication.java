package com.lubayi.fight.apply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lubayi
 * @date 2025/11/6
 */
@EnableAsync
@SpringBootApplication
public class ApplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplyApplication.class, args);
    }

}
