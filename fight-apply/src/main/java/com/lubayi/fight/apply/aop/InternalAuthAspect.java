package com.lubayi.fight.apply.aop;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author lubayi
 * @date 2025/11/17
 */
@Aspect
@Component
@Order(1)
public class InternalAuthAspect {

    private static final String SECRET = "0okmnji98uhbvgy7";

    @Before("@annotation(internalAuth)")
    public void internalAuthBefore(InternalAuth internalAuth) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String value = internalAuth.value();

        String token = request.getHeader(value);
        if (StringUtils.isNotBlank(token)) {
            token = token.replace("Bearer ", "");
        }

        if (token == null || !tokenIsValid(token)) {
            throw new RuntimeException("token校验失败，token值:" + token);
        }
    }

    private boolean tokenIsValid(String token) {
        Boolean valid = false;
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT decode = null;
        try {
            decode = jwtVerifier.verify(token);
            String client_id = decode.getClaim("client_id").asString();
            if (StringUtils.isNotBlank(client_id)) {
                valid = true;
            }
        } catch (JWTVerificationException e) {
            throw new RuntimeException("身份认证失败，token校验失败", e);
        }
        return valid;
    }

}

/*
@annotation：AOP中，常用的拦截方法的切点表达式，用来匹配 所有带有指定注解的方法。

两种写法：
1、@annotation(注解类路径) —— 只拦截，不读参数
@Before("@annotation(com.lubayi.fight.apply.aop.InternalAuth)")
public void internalAuthBefore() {}

2、@annotation(变量名) + 方法参数 —— 拦截并获取注解对象
@InternalAuth("test")
public void testMethod() {}

@Before("@annotation(internalAuth)")
public void internalAuthBefore(InternalAuth internalAuth) {
    String value = internalAuth.value(); // value值为 test，而非默认的 X-Internal-Token
}

 */