package com.lubayi.fight.basic.basic;

import java.util.Arrays;

/**
 * @author lubayi
 * @date 2025/11/14
 */
public class ClassInfo {

    public static void main(String[] args) {
        // ========================= Class<?>[] getInterfaces() ========================
        // 输出：[interface com.lubayi.fight.basic.basic.A, interface com.lubayi.fight.basic.basic.B]
        System.out.println(Arrays.toString(ABDemo.class.getInterfaces()));
        // 输出：[]
        System.out.println(Arrays.toString(ClassInfo.class.getInterfaces()));

        System.out.println("===============================================");

        // ========================= boolean isAssignableFrom(Class<?> cls) ============
        System.out.println(A.class.isAssignableFrom(B.class));      // 输出：false
        System.out.println(A.class.isAssignableFrom(ABDemo.class));     // 输出：true
        System.out.println(ABDemo.class.isAssignableFrom(A.class));     // 输出：false
        ABDemo abDemo = new ABDemo();
        System.out.println(abDemo instanceof A);        // 输出: true
    }

}
interface A {}
interface B {}
class ABDemo implements A, B {}

/*
Class.getInterfaces()
返回值：Class<?>[]
说明：该方法返回一个代表该类实现的接口的Class对象的数组。如果没有实现任何接口，则返回长度为0的数组。


Class.isAssignableFrom(Class<?> cls)
返回值：boolean
说明：该方法用于判断 所传的参数类 是否是调用类的子类
instanceof: 检测左边【对象】是否是右边类或其子类的实例，返回布尔值。
 */
