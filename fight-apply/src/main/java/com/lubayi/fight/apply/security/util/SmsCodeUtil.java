package com.lubayi.fight.apply.security.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author lubayi
 * @date 2025/11/19
 */
@Component
public class SmsCodeUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    // 验证码有效时间（单位：s）
    private static final Long CODE_EXPIRE = 60 * 10L;

    private static final String SMS_CODE_PREFIX = "sms:code:";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成6位数字验证码（安全随机）
     * @return 6位数字字符串
     */
    public static String generateSecureSixDigitCode() {
        int code = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    /**
     * 缓存验证码信息
     */
    public void cacheSmsCode(String phone, String smsCode) {
        redisTemplate.opsForValue().set(SMS_CODE_PREFIX + phone, smsCode, CODE_EXPIRE, TimeUnit.SECONDS);
    }

}
