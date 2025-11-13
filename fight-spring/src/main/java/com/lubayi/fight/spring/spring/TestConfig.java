package com.lubayi.fight.spring.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author lubayi
 * @date 2025/11/4
 */
@Configuration
@ComponentScan(basePackageClasses = {TestConfig.class})
@Import(TestService3.class)
public class TestConfig {

    @Bean
    public TestService1 testService1() {
        return new TestService1();
    }

}
