package com.lubayi.fight.apply.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * @author lubayi
 * @date 2025/11/20
 */
@Component
public class JwtUtil {

    private String secretKeyBase64; // 这里存的是 Base64 编码后的密钥

    private static final long EXPIRATION_TIME = 3600000;    // 1h

    /**
     * 解析 Base64 密钥，并返回 SecretKey
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyBase64);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成唯一的tokenId
     */
    private String generateTokenId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String generateToken() {
        SecretKey key = getSignKey();

        String tokenId = generateTokenId();
        long expireTime = System.currentTimeMillis() + EXPIRATION_TIME;

        // 创建token内容
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenId", tokenId); // 添加tokenId用于Redis缓存

        String token = Jwts.builder()
                .claims(claims)
                .expiration(new Date(expireTime))
                .signWith(key)
                .compact();

        return token;
    }

}
