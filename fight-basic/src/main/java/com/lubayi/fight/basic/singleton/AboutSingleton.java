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

class HungrySingleton {
    private static HungrySingleton hungrySingleton = new HungrySingleton();

    private HungrySingleton() {}

    public static HungrySingleton getHungrySingleton() {
        return hungrySingleton;
    }

}
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

class LazySingleton {
    private static LazySingleton lazySingleton;

    private LazySingleton() {}

    public static LazySingleton getLazySingleton() {
        synchronized (LazySingleton.class) {
            if (lazySingleton == null) {
                lazySingleton = new LazySingleton();
            }
        }
        return lazySingleton;
    }

}

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

}