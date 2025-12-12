package com.lubayi.fight.spring.proxy.dynamic.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lubayi
 * @date 2025/12/12
 */
public class DebugInvocationHandler implements InvocationHandler {

    // 代理类中的真实对象
    private final Object target;

    public DebugInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 调用方法之前，我们可以添加自己的操作
        System.out.println("before method send()");
        Object result = method.invoke(target, args);
        // 调用方法之后，我们同样可以添加自己的操作
        System.out.println("after method send()");
        return result;
    }

}
