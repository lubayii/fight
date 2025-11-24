package com.lubayi.fight.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Author: lubayi
 * Date: 2025/11/25
 * Time: 06:59
 */
@Configuration
public class WebSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user1 = User.withUsername("alice")
                .password(passwordEncoder().encode("123"))
                .roles("USER") // 角色为普通用户
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("USER", "ADMIN") // 角色同时包含用户和管理员
                .build();

        UserDetails user2 = User.withUsername("bob")
                .password(passwordEncoder().encode("456"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1, admin, user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
