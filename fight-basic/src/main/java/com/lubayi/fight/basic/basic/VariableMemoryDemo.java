package com.lubayi.fight.basic.basic;

/**
 * @author lubayi
 * @date 2025/11/6
 */
public class VariableMemoryDemo {

    // 1. 静态变量（属于类，存放在方法区）
    public static String staticVar = "我是静态变量";

    // 2. 成员变量（属于对象，随对象存放在堆中）
    private int memberVar;

    // 构造方法：初始化成员变量
    public VariableMemoryDemo(int memberVar) {
        this.memberVar = memberVar; // 成员变量赋值
    }

    public static void main(String[] args) {
        // 3. 局部基本类型变量（存放在栈中）
        int localBasicVar = 100;

        // 4. 局部引用类型变量（引用存栈中，对象存堆中）
        VariableMemoryDemo obj = new VariableMemoryDemo(200);

        // 5. 引用变量赋值：复制的是地址（栈中地址相同，指向堆中同一对象）
        VariableMemoryDemo obj2 = obj;

        // 6. 字符串常量（存放在方法区的常量池）
        String str = "常量池字符串";
        String str2 = new String("堆中字符串"); // new创建的对象存堆中
    }

}
/*
1. 静态变量 staticVar
类型：引用类型（String）+ 静态变量（带static）
存储位置：方法区（类加载时分配，全局唯一）
存储内容：字符串 "我是静态变量" 在常量池中的地址（因为 String 是引用类型）
2. 成员变量 memberVar
类型：基本类型（int）+ 成员变量（类中定义，非 static）
存储位置：堆中（随new VariableMemoryDemo(200)创建的对象一起存储）
存储内容：直接存储值200（基本类型存值）
3. 局部基本类型变量 localBasicVar
类型：基本类型（int）+ 局部变量（方法内定义）
存储位置：栈中（main 方法的栈帧里）
存储内容：直接存储值100（方法执行完，栈帧释放，变量消失）
4. 局部引用变量 obj
类型：引用类型（VariableMemoryDemo）+ 局部变量
存储位置：
引用本身（obj变量名）：存放在栈中（main 方法的栈帧）
对象实际数据（new出来的内容）：存放在堆中（包含成员变量memberVar=200）
存储内容：栈中的obj存堆中对象的地址（类似 “指针” 指向堆中对象）
5. 引用变量赋值 obj2 = obj
存储逻辑：obj2是新的局部引用变量，栈中会为obj2分配空间，但存储的地址和obj相同
结果：obj和obj2在栈中存相同地址，共同指向堆中同一个对象（修改obj.memberVar会影响obj2）
6. 字符串变量 str 和 str2
str = "常量池字符串"：
字符串常量存放在方法区的常量池，str（局部引用变量）在栈中存常量池的地址
str2 = new String("堆中字符串")：
new创建的 String 对象存放在堆中，str2（局部引用变量）在栈中存堆中对象的地址
注意："堆中字符串" 本身作为常量，也会先存入常量池，但new会在堆中再创建一个对象（指向常量池的字符串）
 */

/*
Java 中变量的内存存储

完全由变量类型（基本数据类型 / 引用数据类型）和作用域决定，核心是 “基本类型存值，引用类型存地址”。

一、
变量的存储位置，本质是对应到 Java 的以下 3 个内存区域，它们的职责和特性完全不同：

栈（Stack）：线程私有，内存自动分配和释放（方法执行完就清空），速度快。主要存局部变量（方法内定义的变量）。
堆（Heap）：所有线程共享，内存需 JVM 垃圾回收（GC）清理，速度较慢。主要存对象的实际数据（包括对象的成员变量）。
方法区（Method Area）：所有线程共享，存静态变量、常量池、类的结构信息（如类名、方法）。

二、基本数据类型变量的内存
基本数据类型（byte、short、int、long、float、double、char、boolean）的变量，直接存储 “值” 本身，不涉及 “引用”。
其存储位置分两种情况：
局部变量（方法内定义）：直接存在栈中。方法执行时，变量入栈；方法执行完，栈帧弹出，变量内存自动释放。
成员变量（类中定义，非 static）：随对象一起存在堆中。创建对象（new）时，成员变量的内存在堆中分配；对象被 GC 回收时，成员变量内存才释放。
静态变量（类中定义，带 static）：存在方法区的静态区。类加载时分配内存，程序结束时才释放，全局唯一。

三、引用数据类型变量的内存
引用数据类型（类、接口、数组、枚举等）的变量，存储的是 “对象在堆中的内存地址”，而非对象的实际数据。

以Student stu = new Student();为例，内存分布分两步：

引用变量 “stu” 的存储：
若 “stu” 是局部变量：存在栈中，栈里存的是new Student()这个对象在堆中的地址。
若 “stu” 是成员变量：随 Student 对象一起存在堆中，堆里的这个 “stu” 字段存的是另一个对象的地址。
若 “stu” 是静态变量：存在方法区，存的是对象在堆中的地址。
对象实际数据的存储：new Student()创建的对象，其属性（如 name、age）和方法信息，全部存在堆中。

注意：当引用变量赋值为null时，意味着它不指向堆中的任何对象，栈（或堆 / 方法区）中的这个变量只存 “空地址”。

 */
