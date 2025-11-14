package com.lubayi.fight.basic.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author lubayi
 * @date 2025/11/14
 */
public class JustInvocationHandler implements InvocationHandler {

    private Object target;

    public JustInvocationHandler(Object target) {
        super();
        this.target = target;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getName() + "方法调用前");
        Object object = method.invoke(target, args);
        System.out.println(method.getName() + "方法调用后");
        return object;
    }

}
