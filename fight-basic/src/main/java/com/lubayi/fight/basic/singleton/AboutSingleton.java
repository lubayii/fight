package com.lubayi.fight.basic.singleton;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author lubayi
 * @date 2025/7/21
 */
public class AboutSingleton {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException,
            IOException, ClassNotFoundException {
        // ======== 利用 反射 破坏单例模式：即通过 setAccessible(true) 强制访问类的私有构造器，去创建对象 ========
        // 获取 HungrySingleton 类的字节码
        Class<HungrySingleton> hungrySingletonClass = HungrySingleton.class;
        // 获取 HungrySingleton 类的无参构造方法，用来创建对象
        Constructor<HungrySingleton> constructor = hungrySingletonClass.getDeclaredConstructor();
        // 虽然是使用 private 修饰，但可以使用 setAccessible(true) 暴力破解
        constructor.setAccessible(true);
        // 创建对象
        HungrySingleton hungrySingleton1 = constructor.newInstance();
        HungrySingleton hungrySingleton2 = constructor.newInstance();
        System.out.println(hungrySingleton1 == hungrySingleton2);   // 输出 false

        // ======== 利用 序列化与反序列化 破坏单例模式 ========
        // 创建输出流
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Singleton.file"));
        // 将单例对象写到文件中
        oos.writeObject(DoubleCheckLockSingleton.getSingleton());
        // 从文件中读取单例对象
        File file = new File("Singleton.file");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        DoubleCheckLockSingleton singleton = (DoubleCheckLockSingleton) ois.readObject();
        // 判断是否是同一个对象：两个对象地址不相等的原因是，readObject() 读入对象时，它必定会返回一个新的对象实例，必然指向新的内存地址
        System.out.println(singleton == DoubleCheckLockSingleton.getSingleton());   // 输出 false
    }
}

/**
 * <h3> <a name="name">单例模式</a> </h3>
 * <p>单例模式，保证一个类仅有一个实例，并且提供一个访问这个实例的全局访问点。
 *
 * <p>Singleton 类只有一个私有的静态成员变量 instance，用于保存唯一的实例；构造函数被声明为私有，防止其他类直接实例化 Singleton；
 * getInstance() 方法是一个公有的静态方法，通过该方法获取唯一的实例 instance。
 * <p>即单例模式：1、成员是 私有的静态的；2、构造方法是 私有的；3、对外暴露的获取访问是 公有的静态的
 *
 * <p>分类：
 * 饿汉式单例模式：类加载就会导致该实例对象被创建；
 * 懒汉式单例模式：类加载不会导致该实例对象被创建，而是首次使用该对象时被创建。
 *
 */
// 饿汉式实现方式1
class HungrySingleton {
    private static HungrySingleton hungrySingleton = new HungrySingleton();

    private HungrySingleton() {}

    public static HungrySingleton getHungrySingleton() {
        return hungrySingleton;
    }

}
// 饿汉式实现方式2
class HungrySingleton2 {
    private static HungrySingleton2 hungrySingleton = null;

    static {
        hungrySingleton = new HungrySingleton2();
    }

    private HungrySingleton2() {}

    public static HungrySingleton2 getHungrySingleton() {
        return hungrySingleton;
    }
}
// 懒汉式实现方式：线程不安全的
class LazySingleton {
    private static LazySingleton lazySingleton;

    private LazySingleton() {}

    public static LazySingleton getLazySingleton() {
        // 每次去获取对象都需要先获取锁，导致并发性能非常的差，所以要对加锁优化 ——> 即 双重检验加锁
        synchronized (LazySingleton.class) {    // 通过加锁，解决懒汉式线程不安全问题
            if (lazySingleton == null) {
                lazySingleton = new LazySingleton();
            }
        }
        return lazySingleton;
    }

}
// 双重检验加锁
class DoubleCheckLockSingleton implements Serializable {

    private static volatile DoubleCheckLockSingleton singleton;

    private DoubleCheckLockSingleton() {}

    public static DoubleCheckLockSingleton getSingleton() {
        if (singleton == null) {
            synchronized (DoubleCheckLockSingleton.class) {
                if (singleton == null) {
                    singleton = new DoubleCheckLockSingleton();
                }
            }
        }
        return singleton;
    }

    /**
     * 序列化与反序列化 破坏单例模式的解决方案
     * <p>会破坏的原因：readObject() 方法执行时，会通过反射调用无参数的构造方法创建一个新的对象，
     * 从而导致每次返回的对象都不一致。
     * <p>解决方法：在 readObject() 方法中调用的有这么两个方法：hasReadResolveMethod()、invokeReadResolve()
     * <p><tt>hasReadResolveMethod()</tt>: 表示如果实现了 Serializable 或者 Externalizable 接口的类（即实现序列化的类）中包含 readResolve() 方法，则返回true
     * <p><tt>invokeReadResolve()</tt>: 通过反射的方式调用要被反序列化的类的 readResolve() 方法。
     */
    private Object readResolve() {
        return getSingleton();
    }

}