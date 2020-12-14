# Java实战（第2版）学习笔记
- 书名：Java实战（第2版）
- 书号：978-7-115-52148-4
- 原书名：Modern Java in Action: Lambda, streams, functional and reactive programming
- 原书号：9781617293566
- 源码下载地址：https://www.ituring.com.cn/book/2659 "随书下载"处下载
- 作者：拉乌尔·加布里埃尔·乌尔玛,马里奥·富斯科,艾伦·米克罗夫特. 

## 方法引用 ::

- 创建方法引用（File::isHidden）
```java
File[] hiddenFiles = new File(".").listFiles(File::isHidden);
```

## Predicate
- 谓词：在数学上常常用来代表类似于函数的东西，它接受一个参数值，并返回 **true** 或 **false**
- 优点：代码更干净、更清晰
- 复杂的条件可以使用策略模式
- 更好的实现 DRY(Don't Repeat Yourself)

## 行为参数化
- 通俗解释：让方法接受多种行为（策略）作为参数，并在内部使用，来完成不同的行为
- 优点：让代码更能适应需求的变化

## Lambda
- 可以理解为一种简洁的可传递匿名函数
    - 没有名称
    - 有参数列表、函数主体、返回类型
    - 允许有一个可以抛出的异常列表
    - Lambda 表达式可以作为参数传递给方法或存储在变量中
    - Lambda 没有 return 语句，因为已经隐含了 return
    - 可以返回 void ``` () -> {} ```
    - 起源于学术界开发出的一套用来描述计算的λ演算法
              
### 表 3-1 Lambda 示例

| 使用案例 | Lambda 示例 |
| --- | --- |
| 布尔表达式 | (List<String> list) -> list.isEmpty()|
| 创建对象 | () -> new Apple(10) |
| 消费一个对象 | (Apple a) -> { System.out.println(a.getWeight()); } |
| 从一个对象中选择/抽取 | (String s) -> s.length() |
| 组合两个值 | (int a, int b) -> a * b |
| 比较两个对象 | (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()) |
| 返回 String 作为表达式 | () -> "Hi" |
| 返回 String（利用显示返回语句） | () -> {return "Hi";} |

## 函数式接口
- 只定义一个抽象方法的接口
                         
### @FunctionalInterface

### 3.4.1 Predicate
- java.util.function.Predicate
- boolean test(T t);
    - t : the function argument
    - r : the function result

### 3.4.2 Consumer
- 消费者
- void accept(T t);

### 3.4.3 Function
- R apply(T t);
- 接受泛型 T 的参数，返回泛型 R 的对象

### 基本类型函数接口
- 装箱后的值本质上就是把基本类型包裹起来，并保存在堆里
- 因此，装箱后的值需要更多的内存
- 并且需要额外的内存搜索来获取被包裹的基本值

使用类似下列函数式接口可以避免自动装箱操作

- IntPredicate
- LongConsumer
- DoubleFunction

更多接口详见：表 3-2 Java 8 中的常用函数式接口
表 3-3 Lambda 及函数式接口的例子

| 使用案例 | Lambda 的例子 | 对应的函数式接口 |
| --- | --- | --- |
| 布尔表达式 | (List<String> list) -> list.isEmpty() | Predicate<List<String>> |
| 创建对象 | () -> new Apple(10) | Supplier<Apple> |
| 消费一个对象 | (Apple a) -> System.out.println(a.getWeight) | Consumer<Apple> |
| 从一个对象中提取 | (String s) -> s.length() | Function<String, Integer> <br> ToIntFunction<String> |
| 合并两个值 | (int a, int b) -> a * b | IntBinaryOperator |
| 比较两个对象 | (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()) | Comparator<Apple> <br> BiFunction<Apple, Apple, Integer> <br> ToIntBiFunction<Apple, Apple, Apple> |

### 3.6 方法引用

图 3-5 改编

| Lambda | 方法引用 | 备注 |
| --- | --- | --- |
| (args) -> ClassName.staticMethod(args) | ClassName::staticMethod |  |
| (arg0, rest) -> arg0.instanceMethod(rest) | ClassName::instanceMethod | arg0 是 ClassName 类型的 |
| (args) -> expr.instanceMethod(args) | expr::instanceMethod |  |

测验3.6：方法引用

| Lambda | 方法引用 |
| --- | --- |
| ToIntFunction<String> stringToInt = (String s) -> Integer.parseInt(s); | ToIntFunction<String> stringToInt = Integer:parseInt; |
| BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element); | BiPredicate<List<String>, String> contains = List::contains; |
| Predicate<String> startsWithNumber = (String string) -> this.startWithNumber(string); | Predicate<String> startsWithNumber = this::startsWithNumber |

---
Java8InAction
===============

This repository contains all the source code for the examples and quizzes in the book Java 8 in Action: Lambdas, Streams and functional-style programming.

You can purchase the book here: [http://manning.com/urma/](http://manning.com/urma/) or on Amazon

The source code for all examples can be found in the directory [src/main/java/lambdasinaction](https://github.com/java8/Java8InAction/tree/master/src/main/java/lambdasinaction)

* Chapter 1: Java 8: why should you care?
* Chapter 2: Passing code with behavior parameterization
* Chapter 3: Lambda expressions
* Chapter 4: Working with Streams
* Chapter 5: Processing data with streams
* Chapter 6: Collecting data with streams
* Chapter 7: Parallel data processing and performance
* Chapter 8: Refactoring, testing, debugging
* Chapter 9: Default methods
* Chapter 10: Using Optional as a better alternative to null
* Chapter 11: CompletableFuture: composable asynchronous programming
* Chapter 12: New Date and Time API
* Chapter 13: Thinking functionally
* Chapter 14: Functional programming techniques
* Chapter 15: Blending OOP and FP: comparing Java 8 and Scala
* Chapter 16: Conclusions and "where next" for Java
* Appendix A: Miscellaneous language updates
* Appendix B: Miscellaneous library updates
* Appendix C: Performing multiple operations in parallel on a Stream
* Appendix D: Lambdas and JVM bytecode
We will update the repository as we update the book. Stay tuned!

### Make sure to have JDK8 installed
The latest binary can be found here: http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html

$ java -version

java version "1.8.0_05"
Java(TM) SE Runtime Environment (build 1.8.0_05-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.5-b02, mixed mode)


You can download a preview version here: https://jdk8.java.net/

### Compile/Run the examples
Using maven:

$ mvn compile

$ cd target/classes

$ java lambdasinaction/chap1/FilteringApples


Alternatively you can compile the files manually inside the directory src/main/java

You can also import the project in your favorite IDE:
    * In IntelliJ use "File->Open" menu and navigate to the folder where the project resides
    * In Eclipse use "File->Import->Existing Maven Projects" (also modify "Reduntant super interfaces" to report as Warnings instead of Errors
    * In Netbeans use "File->Open Project" menu
    

