package com.lubayi.fight.basic.innerclass;

/**
 * @author lubayi
 * @date 2025/7/21
 */
public class AboutMemberInnerClass {

    public static void main(String[] args) {
        // ====== 内部类对象创建方式：成员内部类（非静态内部类）必须通过外部类实例创建 ======
        // 方式一：new OuterClass().new MemberInnerClass();
        MemberOuterClass.MemberInnerClass inner = new MemberOuterClass().new MemberInnerClass();
        // 方式二：
        // OuterClass outer = new OuterClass();
        // outer.new MemberInnerClass();
        MemberOuterClass outer = new MemberOuterClass();
        MemberOuterClass.MemberInnerClass inner2 = outer.new MemberInnerClass();
        inner.test();
    }

}

/**
 * <h3> <a name="name">成员内部类</a> </h3>
 * <p>定义：定义在 类的成员位置（与字段、方法同级），也可以说是 定义在类中，方法外的非静态类。
 * <p>语法
 * <blockquote><pre>
 *     class OuterClass {
 *
 *         class MemberInnerClass {
 *
 *         }
 *     }
 * </pre></blockquote>
 */
class MemberOuterClass {

    public String data1 = "public field";

    private String data2 = "private field";

    public static String data3 = "public static field";

    private static String data4 = "private static field";

    class MemberInnerClass {

        /*
        我们知道static修饰的成员是最早被执行的，不依赖于任何对象的，也就是说不依赖于InnerClass2的，
        但是我们的内部类InnerClass2是依赖于外部类对象的，两者之间就矛盾了，存在了问题。
        换句话说就是我们将一个不依赖于任何对象的变量定义在了一个需要依赖外部类对象的一个类当中，当然是矛盾的啦~
         */

        // public static String dataStatic = "inner static field";
        // 上面这句代码编译报错。成员内部类（非静态内部类）不能声明非 final 的静态成员。

        // 为什么加上 final 就可以了呢？因为 final 修饰的是常量，不需要被加载，编译的时候就能被通过
        public static final String dataStaticFinal = "inner static final field";

        public void test() {
            System.out.println("内部类访问外部类不同类型的成员变量：");
            System.out.println(data1);
            System.out.println(data2);
            System.out.println(data3);
            System.out.println(data4);
        }

    }

}
