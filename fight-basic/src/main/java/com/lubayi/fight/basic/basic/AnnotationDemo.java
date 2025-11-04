package com.lubayi.fight.basic.basic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lubayi
 * @date 2025/11/4
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationDemo {
}
/*
注解（Annotation）是 Java 5 引入的特性，它可以标记在类上、⽅法上、属性上等，提供某些信息供程序在编译或者运行时使用。

元注解：用于注解其他注解的注解。Java提供了几个重要的元注解：
@Retention：定义注解的保留策略
@Target：指定注解可应用的程序元素类型
@Documented：指示注解应该被包含在 JavaDoc 中
@Inherited：指示注解可以被子类继承。
@Repeatable（Java 8）：指示注解可以在同一元素上多次使用。

@Retention取值：
RetentionPolicy.SOURCE      // 仅在源代码中保留，编译时丢弃
RetentionPolicy.CLASS       // 保留在编译后的类文件中，但 JVM 运行时不保留（默认行为）
RetentionPolicy.RUNTIME    // 保留至运行时，可通过反射 API 访问

@Target取值：
ElementType.TYPE               // 类、接口、枚举
ElementType.FIELD              // 字段、枚举常量
ElementType.METHOD             // 方法
ElementType.PARAMETER          // 方法参数
ElementType.CONSTRUCTOR        // 构造函数
ElementType.LOCAL_VARIABLE     // 局部变量
ElementType.ANNOTATION_TYPE    // 注解类型
ElementType.PACKAGE            // 包
ElementType.TYPE_PARAMETER     // 类型参数（Java 8）
ElementType.TYPE_USE           // 类型使用（Java 8）
ElementType.MODULE             // 模块（Java 9）

注解的处理方式：
1、编译时处理：在编译时扫描对应的注解并处理，通常用于代码生成、编译时验证等。
2、运行时通过反射处理：使用 Java 反射 API 在运行时读取和处理注解。

 */