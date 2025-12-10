package com.lubayi.fight.apply;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lubayi
 * @date 2025/11/6
 */
@SpringBootApplication
@MapperScan({
        "com.lubayi.fight.apply.security.repository.mapper",
        "com.lubayi.fight.apply.transaction.mapper"
})
public class ApplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplyApplication.class, args);
    }

}
