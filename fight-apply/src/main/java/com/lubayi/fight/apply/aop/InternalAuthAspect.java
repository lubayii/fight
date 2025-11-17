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
