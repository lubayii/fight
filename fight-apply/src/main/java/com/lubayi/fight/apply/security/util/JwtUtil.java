package com.lubayi.fight.apply.security.util;

import com.lubayi.fight.apply.security.repository.entity.LoginUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lubayi
 * @date 2025/11/20
 */
@Component
public class JwtUtil {

    private String secretKeyBase64; // 这里存的是 Base64 编码后的密钥

    private int expireTime;

    private String header;

    private static final long MILLIS_MINUTE = 60 * 1000;

    private static final long MILLIS_MINUTE_TWENTY = 20 * 60 * 1000;

    private static final String LOGIN_TOKEN_KEY = "login_tokens:";   // 登录用户 redis key

    private static final String TOKEN_PREFIX = "Bearer ";   // 令牌前缀

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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

    public String createToken(LoginUser loginUser) {
        SecretKey key = getSignKey();

        String tokenId = generateTokenId();
        loginUser.setTokenId(tokenId);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenId", tokenId); // 添加tokenId用于Redis缓存
        return Jwts.builder().claims(claims).signWith(key).compact();
    }

    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = request.getHeader(this.header);
        if (StringUtils.isNotBlank(token) && token.startsWith(TOKEN_PREFIX)) {
            token = token.replace(TOKEN_PREFIX, "");
        }
    }

    /**
     * 刷新令牌有效期
     * @param loginUser
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        String tokenKey = getTokenKey(loginUser.getTokenId());
        redisTemplate.opsForValue().set(tokenKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String tokenId) {
        return LOGIN_TOKEN_KEY + tokenId;
    }

}
