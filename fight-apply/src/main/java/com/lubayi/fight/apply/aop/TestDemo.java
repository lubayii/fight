package com.lubayi.fight.apply.aop;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;

/**
 * @author lubayi
 * @date 2025/11/17
 */
public class TestDemo {

    @Test
    public void getToken() {
        String token = JWT.create()
                .withClaim("client_id", "must")
                .sign(Algorithm.HMAC256("0okmnji98uhbvgy7"));
        System.out.println(token);  // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGllbnRfaWQiOiJtdXN0In0.lA1ZE3TPOh4xh4Oqx1jrlJVTo2yrcypu3cbQ7Z0Wpfk
    }

}
