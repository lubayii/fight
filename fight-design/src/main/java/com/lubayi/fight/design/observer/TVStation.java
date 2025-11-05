package com.lubayi.fight.design.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lubayi
 * @date 2025/11/5
 */
public class TVStation {

    // 观察者列表
    private final Map<Class<? extends Event>, List<EventListener>> listenerMap = new HashMap<>();

    // 订阅函数：将观察者添加到观察者列表
    public void subscribe(EventListener listener, Class<? extends Event> eventClass) {
        listenerMap.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(listener);
    }

    public void publishEvent(Event event) {
        Class<? extends Event> aClass = event.getClass();
        List<EventListener> eventListeners = listenerMap.get(aClass);
        if (eventListeners != null) {
            eventListeners.forEach(listener -> listener.onEvent(event));
        }
    }

}
