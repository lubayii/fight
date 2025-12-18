package com.lubayi.fight.apply.sse;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author lubayi
 * @date 2025/12/18
 */
@Slf4j
public class SseEmitterUtils {

    /**
     * 当前连接数
     */
    private static final AtomicInteger count = new AtomicInteger(0);

    /**
     * 使用map对象，便于根据userId来获取对应的SseEmitter
     */
    private static final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 创建用户连接并返回 SseEmitter
     *
     * @param userId 用户ID
     * @return SseEmitter
     */
    public static SseEmitter connect(String userId) {

        if (sseEmitterMap.containsKey(userId)) {
            return sseEmitterMap.get(userId);
        }
        try {
            // 设置超时时间，0表示不过期。默认30秒
            SseEmitter sseEmitter = new SseEmitter(0L);
            // 注册回调
            // 连接完成（正常/异常关闭）时的回调
            sseEmitter.onCompletion(completionCallBack(userId));
            // 连接出错时的回调
            sseEmitter.onError(errorCallBack(userId));
            // 连接超时的回调
            sseEmitter.onTimeout(timeoutCallBack(userId));
            sseEmitterMap.put(userId, sseEmitter);
            // 数量+1
            count.getAndIncrement();

            return sseEmitter;
        } catch (Exception e) {
            log.info("创建新的sse连接异常，当前用户：{}", userId);
        }
        return null;
    }

    private static Runnable completionCallBack(String userId) {
        return () -> {
            log.info("结束连接：{}", userId);
            removeUser(userId);
        };
    }

    private static Runnable timeoutCallBack(String userId) {
        return () -> {
            log.info("连接超时：{}", userId);
            removeUser(userId);
        };
    }

    private static Consumer<Throwable> errorCallBack(String userId) {
        return throwable -> {
            log.info("连接异常：{}", userId);
            removeUser(userId);
        };
    }

    /**
     * 移除用户连接
     */
    public static void removeUser(String userId) {
        if (userId == null || userId.isBlank()) {
            log.warn("移除SSE用户失败：userId为空");
            return;
        }
        // 一次get操作，替代 containsKey + get，减少map访问
        SseEmitter emitter = sseEmitterMap.get(userId);
        if (emitter == null) {
            log.info("SSE用户不存在，无需移除：{}", userId);
            return;
        }

        try {
            // 向客户端发送一个 “连接关闭” 的信号（Content-Type: text/event-stream 的结束标识）；
            // 释放服务器端与该连接关联的所有资源（线程、Socket、缓冲区）。
            emitter.complete();
            log.debug("SSE连接已正常关闭：{}", userId);
        } catch (Exception e) {
            log.error("SSE连接关闭异常：userId={}", userId, e);
        } finally {
            // 从map中移除映射（确保并发安全：仅当map中仍为当前emitter时才移除）
            boolean removed = sseEmitterMap.remove(userId, emitter);
            if (removed) {
                // 仅移除成功后，count才减1（统计准确）
                count.getAndDecrement();
                log.info("SSE用户移除成功：userId={}，当前连接数：{}", userId, count.get());
            }
        }
    }

    /**
     * 给指定用户发送消息
     */
    public static void sendMessage(String userId, String message) {
        SseEmitter emitter = sseEmitterMap.get(userId);
        if (emitter != null) {
            try {
                emitter.send(message);
            } catch (IOException e) {
                log.error("用户[{}]推送异常:{}", userId, e.getMessage());
                removeUser(userId);
            }
        }
    }

    /**
     * 向同组人发布消息   （要求userId+groupId）
     */
    public static void groupSendMessage(String groupId, String message) {

        if (MapUtil.isNotEmpty(sseEmitterMap)) {
            sseEmitterMap.forEach((k, v) -> {
                try {
                    if (k.startsWith(groupId)) {
                        v.send(message, MediaType.APPLICATION_JSON);
                    }
                } catch (IOException e) {
                    log.error("用户[{}]推送异常:{}", k, e.getMessage());
                    removeUser(k);
                }
            });
        }
    }

    /**
     * 广播群发消息
     */
    public static void batchSendMessage(String message) {
        sseEmitterMap.forEach((k, v) -> {
            try {
                v.send(message, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                log.error("用户[{}]推送异常:{}", k, e.getMessage());
                removeUser(k);
            }
        });
    }

    /**
     * 群发消息
     */
    public static void batchSendMessage(String message, Set<String> ids) {
        ids.forEach(userId -> sendMessage(userId, message));
    }

    /**
     * 获取当前连接信息
     */
    public static List<String> getIds() {
        return new ArrayList<>(sseEmitterMap.keySet());
    }

    /**
     * 获取当前连接数量
     */
    public static int getUserCount() {
        return count.intValue();
    }

}
