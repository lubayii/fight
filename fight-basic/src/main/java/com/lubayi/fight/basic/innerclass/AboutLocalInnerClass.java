package com.lubayi.fight.basic.innerclass;

/**
 * @author lubayi
 * @date 2025/7/21
 */
public class AboutLocalInnerClass {
}
/**
 * <h3> <a name="name">局部内部类</a> </h3>
 * <p>定义：定义在 方法体内部 或 代码块内部 的类。
 * <p>仅在定义它的方法/代码块内有效（类似局部变量）。
 * 不能被访问限定修饰符（public/private/protected）、static修饰
 * <p>语法
 * <blockquote><pre>
 *     class OuterClass {
 *         public void func() {
 *             class LocalInnerClass {
 *
 *             }
 *         }
 *     }
 * </pre></blockquote>
 */
class LocalOuterClass {

    public void func() {
        class LocalInnerClass {
            public int data = 666;
        }
        LocalInnerClass innerClass = new LocalInnerClass();
        System.out.println(innerClass.data);
    }

}