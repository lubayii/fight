package com.lubayi.fight.spring.proxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: lubayi
 * Date: 2025/11/13
 * Time: 21:36
 */
public class MyInterfaceFactory  {

    private static final AtomicInteger count  = new AtomicInteger();

    private static File createJavaFile(String className, MyHandler handler) throws IOException {
        String func1Body = handler.functionBody("func1");
        String func2Body = handler.functionBody("func2");
        String func3Body = handler.functionBody("func3");
        String context = "package com.lubayi.fight.spring.proxy;\n" +
                "\n" +
                "public class " + className + " implements MyInterface {\n" +
                "    MyInterface myInterface;\n" +
                "\n" +
                "    @Override\n" +
                "    public void func1() {\n" +
                "        "+ func1Body +"\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void func2() {\n" +
                "        "+ func2Body +"\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void func3() {\n" +
                "        "+ func3Body +"\n" +
                "    }\n" +
                "}\n";
        File javaFile = new File(className + ".java");
        Files.writeString(javaFile.toPath(), context);
        return javaFile;
    }

    private static String getClassName() {
        return "MyInterface$Proxy" + count.incrementAndGet();
    }

    private static MyInterface newInstance(String className, MyHandler handler) throws Exception {
        Class<?> aClass = MyInterfaceFactory.class.getClassLoader().loadClass(className);
        Constructor<?> constructor = aClass.getConstructor();
        MyInterface proxy = (MyInterface) constructor.newInstance();
        handler.setProxy(proxy);
        return proxy;
    }

    public static MyInterface createProxyObject(MyHandler handler) throws Exception {
        String className = getClassName();
        File javaFile = createJavaFile(className, handler);
        Compiler.compile(javaFile);
        return newInstance("com.lubayi.fight.spring.proxy." + className, handler);
    }

}
