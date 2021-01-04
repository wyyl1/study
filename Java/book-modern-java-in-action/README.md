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

- 方法引用主要有三类
    - 指向静态方法的方法引用
    - 指向任意类型示例方法的方法引用
    - 指向现存对象或表达式实例方法的方法引用

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

#### 3.6.2 构造函数引用

### 3.8 复合 Lambda 表达式的有用方法
#### 3.8.2 谓词复合

谓词接口包括三个方法

- negate
    - 返回 Predicate 的非，例如苹果不是红的 Predicate<Apple> notRedApple = redApple.negate();
- and
- or

#### 3.8.3 函数复合
##### andThen 方法会返回一个函数
- 先对输入，执行一个给定函数
- 在对输出，执行另一个函数

```java
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        // 数学上写作：g(f(x))
        Function<Integer, Integer> h = f.andThen(g);
        // 结果是 4
        int result = h.apply(1);
```

##### compose 方法
- 先对输入执行 compose 后的函数
- 再将之前的结果作为输入，执行 compose 前的函数

```java
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        // 数学上写作：f(g(x))
        Function<Integer, Integer> h = f.compose(g);
        // 结果是 3
        int result = h.apply(1);
```
图3-6说明了andThen和compose之间的区别

##### 作用：可以组成强大的流水线（对新手来说并不友好，因为无法推测出正确的结果）

```java
    public static String addHeader(String text) {
        return "Header: " + text;
    }

    public static String addFooter(String text) {
        return " Footer " + text;
    }

    public static String checkSpelling(String text) {
        return text.replaceAll("labda", "lambda");
    }

    public static void assemblyLine1(){
        String text = "流水线1";
        Function<String, String> addHeader = Chap03Letter::addHeader;
        Function<String, String> transformationPipeline = addHeader
                .andThen(Chap03Letter::checkSpelling)
                .andThen(Chap03Letter::addFooter);
        System.out.println(transformationPipeline.apply(text));
        // 输出：Footer Header: 流水线1
    }

    public static void assemblyLine2(){
        String text = "流水线2";
        Function<String, String> addHeader = Chap03Letter::addHeader;
        Function<String, String> transformationPipeline = addHeader
                .compose(Chap03Letter::checkSpelling)
                .compose(Chap03Letter::addFooter);
        System.out.println(transformationPipeline.apply(text));
        // 输出：Header:  Footer 流水线2
    }
```

### 3.9 数学中的类似思想

f(x) = x + 10

### 3.10 小结
- Lambda 表达式可以理解为一种匿名函数
    - 没有名称
    - 有参数列表、函数主体、返回类型
    - 可能还有一个可以抛出的异常列表
- Lambda 表达式让你可以简洁地传递代码
- **函数式接口**就是仅仅声明了一个抽象方法的接口
- 只有在接受函数式接口的地方才可以使用 Lambda 表达式

## 第 4 章 引入流

### 4.1 流是什么

- 允许以声明性方式处理数据集合（通过查询语句来表达，而不是临时编写一个实现）
- 可以透明的并行处理，无须写任何多线程代码

Stream API 优点
- 声明性：更简洁、易读
- 可复合：更灵活
- 可并行：性能更好

### 4.2 流简介
- map：接受一个 Lambda，将元素转换成其他形式或提取信息
- limit：截断流，使其元素不超过给定数量
- collect：将流转换为其他形式

### 4.3 流与集合
流只能消费一次
内部迭代时，项目可以透明地并行处理，或者以更优化的顺序进行处理

## 第 5 章 使用流
### 5.1 筛选
- filter
- distinct：筛选时各异的元素

### 5.2 流的切片
#### 5.2.1 使用谓词对流进行切片
Java 9 引入了两个新方法，可以高效地选择流中的元素
- filter的缺点是需要遍历整个流中的数据，这两个方法会在遭遇第一个不符合要求的元素时停止处理
- takeWhile
- dropWhile 

```java
        List<Integer> list = Arrays.asList(12, 2, 9, 3, 3, 5);

        System.out.println(list.stream()
                .takeWhile(i -> i > 2)
                .collect(toList()));
        // [12]

        System.out.println("------------------------------------");
        System.out.println(list.stream()
                .dropWhile(i -> i > 2)
                .collect(toList()));
        // [2, 9, 3, 3, 5]
```

#### 5.2.2 截短流
limit(n)
#### 5.2.3 跳过元素
skip(n)
- 返回一个扔掉了前n个元素的流
- 如果流中元素不足n个，则返回一个空流
- limit(n) 和 skip(n) 是互补的

### 5.3 映射
- map
- flatMap

#### 5.3.1 对流中每一个元素应用函数
map
- 接收一个函数作为参数
- 这个函数会被应用到每个元素上，并将其映射成一个新的元素（创建一个新版本，而不是去"修改"）

找出每道菜的名称有多长

```java
List<Integer> dishNameLengths = menu.stream()
            .map(Dish::getName)
            .map(String::length)
            .collect(toList());
```
#### 5.3.2 流的扁平化
flatMap
- 各个数组并不是分别映射成一个流，而是映射成流的内容
- 也就是说：flatMap 方法让你把一个流中的每个值都换成另一个流，然后把所有的流连接起来成为一个流

使用 flatMap 找出单词列表中各不相同的字符

```java
        System.out.println("------------------------------------");
        List<String> words = Arrays.asList("Hello", "World");
        List<String> uniqueCharacters = words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());
        System.out.println(uniqueCharacters);
        // [H, e, l, o, W, r, d]
```

给定两个数字列表，如何返回所有的数对呢？
例如，给定[1,2,3]和[3,4]，应返回 [(1,3), (1,4), (2,3), (2,4), (3,3), (3,4)]
```java
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> pairs = numbers1.stream()
                .flatMap(i -> numbers2.stream()
                        .map(j -> new int[]{i, j}))
                .collect(toList());
        pairs.forEach(pair -> {
            System.out.println(Arrays.toString(pair));
        });
```
### 5.4 查找和匹配
看看数据集中的某些元素是否匹配一个给定的属性
- allMatch：是否匹配所有元素
- anyMatch：是否至少匹配一个元素
- noneMatch：和 allMatch 相对，流中没有任何元素匹配
- findFirst：查找第一个元素
- findAny：返回当前流中的任意元素

### 5.5 规约
#### 5.5.1 元素求和
reduce
- 接受两个参数
    - 一个初始值
    - 一个 BinaryOperator<T> 来将两个元素结合起来产生一个新值

- 元素求和：
    - int sum = numbers.stream().reduce(0, (a, b) -> a + b);
    - int sum2 = numbers.stream().reduce(0, Integer::sum);
- 最大值：int max = numbers.stream().reduce(0, (a, b) -> Integer.max(a, b));
- 最小值：Optional<Integer> min = numbers.stream().reduce(Integer::min);

规约方法的优势与并行化
- 迭代被内部迭代抽象化，让内部实现得以选择并行执行 reduce 操作

#### 5.7.2 数值范围
第一个参数是起始值，第二个参数结束值
- range：不包含结束值
- rangeClosed：包含结束值
```java
    IntStream evenNumbers = IntStream.rangeClosed(1, 100)
        .filter(n -> n % 2 == 0);
    System.out.println(evenNumbers.count());
```
### 5.8 构建流
#### 5.8.4 由文件生成流
Files.lines
- 返回一个由指定文件中的各行构成的字符串流
- Stream 接口通过实现 AutoCloseable 接口，自动的安全的关闭流

统计不重复的单词数量

```java
long uniqueWords = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())
        .flatMap(line -> Arrays.stream(line.split(" ")))
        .distinct()
        .count();
```
#### 5.8.5 由函数生成流：创建无限流
一般来说，应该使用 limit(n) 来对这种流加以限制
- Stream.iterate：迭代
- Stream.generate：生成

##### 迭代
生成一个所有正偶数的流
```java
Stream.iterate(0, n -> n + 2)
        .limit(10)
        .forEach(System.out::println);
```
裴波纳契数列
```java
Stream.iterate(new int[] { 0, 1 }, t -> new int[] { t[1], t[0] + t[1] })
        .limit(10)
        .forEach(t -> System.out.printf("(%d, %d)", t[0], t[1]));
```

Java 9 对 iterate 方法进行了增强，支持谓词操作
从0开始生成一个数字序列，当数字大于100时停止
```java
Stream.iterate(0, n -> n < 100, n -> n +4)
    .forEach(System.out::println);
```
或者使用 takeWhile
```java
Stream.iterate(0, n -> n + 4)
            .takeWhile(n -> n < 100)
            .forEach(System.out::println);
```
##### 生成
- 接受一个 Supplier<T> 类型的 Lambda 提供新的值

生成一个随机数流
```java
Stream.generate(Math::random)
        .limit(10)
        .forEach(System.out::println);
```
### 5.10 小结
- 如果明确指定数据源是排序的，使用 takeWhile、dropWhile 方法通常比 filter 高效的多
- filter、map 等操作都是无状态的
- reduce 等操作要存储状态才能计算出一个值
- sorted、distinct 等操作也要存储状态，因为它们需要把流中的所有元素缓存起来才能返回一个新的流
- 流不仅可以从集合创建，也可以从值、数组、文件、iterate、generate 等特定方法创建
## 第 6 章 用流收集数据
### 6.1 收集器简介
#### 6.1.1 收集器用作高级规约
优秀的函数式 API 设计优点
- 更易复合
- 更易重用
#### 6.1.2 预定义收集器
Collectors 类提供的工厂方法创建的收集器，主要提供三大功能：
- 将流元素规约和汇总为一个值
- 元素分组
- 元素分区
#### 6.2.1 查找流中的最大值和最小值
- Collectors.maxBy
- Collectors.minBy
#### 6.2.2 汇总
- Collectors.summingInt：求和
- Collectors.averagingInt：计算平均数
```java
int total = menu.stream().collect(summingInt(Dish::getCalories));
```
#### 6.2.3 链接字符串
joining()
```java
menu.stream().map(Dish::getName).collect(joining());
// porkbeefchickenfrench friesriceseason fruitpizzaprawnssalmon

menu.stream().map(Dish::getName).collect(joining(", "));
// porkbeefchickenfrench, friesriceseason, fruitpizzaprawnssalmon
```
#### 6.2.4 广义的规约汇总
Collectors.reducing 工厂方法是所有这些特殊情况的一般化
计算菜单总热量
```java
  private static int calculateTotalCalories() {
    return menu.stream().collect(reducing(0, Dish::getCalories, (Integer i, Integer j) -> i + j));
  }
```
它需要三个参数：
- 第一个是规约操作的起始值，也是流中没有元素时的返回值
- 第二个是一个函数
- 第三个是一个 BinaryOperator，将两个项目累积成一个同类型的值

##### 收集与规约
- reduce：
    - 把两个值结合起来生成一个新值，它是一个不可变的规约
- collect：
    - 设计是要改变容器，从而累积要输出的结果
    - 适合并行操作
### 6.3 分组
按类别分组
```java
  private static Map<Dish.Type, List<Dish>> groupDishesByType() {
    return menu.stream().collect(groupingBy(Dish::getType));
  }
  // {OTHER=[french fries, rice, season fruit, pizza], MEAT=[pork, beef, chicken], FISH=[prawns, salmon]}
```
自定义分组规则
```java
  private static Map<CaloricLevel, List<Dish>> groupDishesByCaloricLevel() {
    return menu.stream().collect(
        groupingBy(dish -> {
          if (dish.getCalories() <= 400) {
            return CaloricLevel.DIET;
          }
          else if (dish.getCalories() <= 700) {
            return CaloricLevel.NORMAL;
          }
          else {
            return CaloricLevel.FAT;
          }
        })
    );
  }
```


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
    

