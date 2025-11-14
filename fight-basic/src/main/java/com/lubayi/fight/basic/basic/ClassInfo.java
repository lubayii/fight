package com.lubayi.fight.basic.basic;

import java.util.Arrays;

/**
 * @author lubayi
 * @date 2025/11/14
 */
public class ClassInfo {

    public static void main(String[] args) {
        // 输出：[interface com.lubayi.fight.basic.basic.A, interface com.lubayi.fight.basic.basic.B]
        System.out.println(Arrays.toString(ABDemo.class.getInterfaces()));
        // 输出：[]
        System.out.println(Arrays.toString(ClassInfo.class.getInterfaces()));
    }

}
interface A {}
interface B {}
class ABDemo implements A, B {}

/*
Class.getInterfaces()
返回值：Class<?>[]
说明：该方法返回一个代表该类实现的接口的Class对象的数组。如果没有实现任何接口，则返回长度为0的数组。
 */
