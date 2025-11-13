package com.lubayi.fight.spring.proxy;

import java.lang.reflect.Field;

/**
 * Author: lubayi
 * Date: 2025/11/13
 * Time: 22:18
 */
public class Main {

    public static void main(String[] args) throws Exception {
        MyInterface proxyObject = MyInterfaceFactory.createProxyObject(new PrintFunctionName());
        proxyObject.func1();
        proxyObject.func2();
        proxyObject.func3();
        System.out.println("================");
        proxyObject = MyInterfaceFactory.createProxyObject(new PrintFunctionName1());
        proxyObject.func1();
        proxyObject.func2();
        proxyObject.func3();
        System.out.println("================");
        proxyObject = MyInterfaceFactory.createProxyObject(new LogHandler(proxyObject));
        proxyObject.func1();
        proxyObject.func2();
        proxyObject.func3();
    }

    static class PrintFunctionName implements MyHandler {
        @Override
        public String functionBody(String methodName) {
            return "System.out.println(\"" + methodName + "\");";
        }
    }

    static class PrintFunctionName1 implements MyHandler {
        @Override
        public String functionBody(String methodName) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("System.out.println(1);")
                    .append("System.out.println(\"" + methodName + "\");");
            return stringBuilder.toString();
        }
    }

    static class LogHandler implements MyHandler {

        MyInterface myInterface;

        public LogHandler(MyInterface myInterface) {
            this.myInterface = myInterface;
        }

        @Override
        public String functionBody(String methodName) {
            String context = "System.out.println(\"before\");\n" +
                    "        myInterface." + methodName +"();\n" +
                    "        System.out.println(\"after\");";
            return context;
        }

        @Override
        public void setProxy(MyInterface proxy) {
            try {
                Field field = proxy.getClass().getDeclaredField("myInterface");
                field.setAccessible(true);
                field.set(proxy, myInterface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
