package com.lubayi.fight.spring.proxy.dynamic.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author lubayi
 * @date 2025/12/12
 */
public class DebugMethodInterceptor implements MethodInterceptor {

    /**
     * @param obj       代理对象本身（注意不是原始对象，如果使用method.invoke(obj, args)会导致循环调用）
     * @param method    被拦截的方法（需要增强的方法）
     * @param args      方法入参
     * @param methodProxy   高性能的方法调用机制，避免反射开销
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 调用方法之前，我们可以添加自己的操作
        System.out.println("before method " + method.getName());
        Object result = methodProxy.invokeSuper(obj, args);
        // 调用方法之后，我们同样可以添加自己的操作
        System.out.println("after method " + method.getName());
        return result;
    }

}
