package com.lubayi.fight.basic.jdkproxy;

import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author lubayi
 * @date 2025/11/14
 */
public class Main {

    public static void main(String[] args) throws Exception {
        JustInterface justInterface = new JustImpl();
        JustInvocationHandler invocationHandler = new JustInvocationHandler(justInterface);
        JustInterface proxy = (JustInterface) invocationHandler.getProxy();
        proxy.just();
        System.out.println("===============================");
        System.out.print("proxy instanceof Proxy: ");
        System.out.println(proxy instanceof Proxy);

        System.out.println("proxy实现的接口：");
        System.out.println(Arrays.toString(proxy.getClass().getInterfaces()));

        System.out.println("proxy中的属性有：");
        Arrays.stream(proxy.getClass().getDeclaredFields()).map(Field::getName).forEach(System.out::println);

        System.out.println("proxy中的方法有：");
        Arrays.stream(proxy.getClass().getDeclaredMethods()).map(Method::getName).forEach(System.out::println);
    }

    private static void saveProxyFile() throws Exception {
        byte[] classFile = ProxyGenerator.generateProxyClass("$Proxy0", JustImpl.class.getInterfaces());
        File file = new File("$Proxy0.class");
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(classFile);
        outputStream.close();
    }

}

/*
proxy.just();
方法说明：根据 $Proxy0.class 文件可知proxy.just() 方法如下：

public final void just() throws  {
    try {
        super.h.invoke(this, m3, (Object[])null);
    } catch (RuntimeException | Error var2) {
        throw var2;
    } catch (Throwable var3) {
        throw new UndeclaredThrowableException(var3);
    }
}
当proxy.just() 方法调用时，会在 just() 方法中调用 InvocationHandler.invoke() 方法
在这个示例的 InvocationHandler.invoke() 中代码为：method.invoke(target, args); ，这个 target 为我们 传进去的 JustImpl 实例，
所以 JustImpl 中的 just() 方法会被执行，输出【JustImpl类中的just()被调用】
 */