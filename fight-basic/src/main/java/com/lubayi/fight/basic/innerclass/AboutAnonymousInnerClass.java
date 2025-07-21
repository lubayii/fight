package com.lubayi.fight.basic.innerclass;

/**
 * @author lubayi
 * @date 2025/7/21
 */
public class AboutAnonymousInnerClass {
}
/**
 * <h3> <a name="name">匿名内部类</a> </h3>
 * <p>定义：没有名字的局部内部类，通常用于简化接口或抽象类的实现。
 * <p>语法
 * <blockquote><pre>
 *     new interface/abstractClass() {
 *         // 必须实现所有抽象方法
 *         // @Override
 *         void method() {
 *
 *         }
 *     }
 * </pre></blockquote>
 */
class OuterClass {

    public void createThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        });
        // 将上述方式转换为 lambda 方式
        Thread lambdaThread = new Thread(() -> System.out.println(Thread.currentThread().getName()));

    }

}