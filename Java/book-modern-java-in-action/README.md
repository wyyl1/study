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
#### 6.3.1 操作分组的元素
groupingBy + filtering 操作有点复杂，需要时查阅原文
- 使用 filter，会少一个分类 FISH
- 使用 groupingBy + filtering，FISH 分类会被保存，即使是空数据

modernjavainaction.chap06.Grouping.java
```java
  private static Map<Dish.Type, List<Dish>> groupCaloricDishesByType() {
//    return menu.stream()
//            .filter(dish -> dish.getCalories() > 500)
//            .collect(groupingBy(Dish::getType));
      // {OTHER=[french fries, pizza], MEAT=[pork, beef]}
    return menu.stream().collect(
        groupingBy(Dish::getType,
            filtering(dish -> dish.getCalories() > 500, toList())));
    // {OTHER=[french fries, pizza], MEAT=[pork, beef], FISH=[]}
  }
```
#### 6.3.2 多级分组
groupingBy 嵌套 groupingBy
```java
  private static Map<Dish.Type, Map<CaloricLevel, List<Dish>>> groupDishedByTypeAndCaloricLevel() {
    return menu.stream().collect(
        groupingBy(Dish::getType,
            groupingBy((Dish dish) -> {
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
        )
    );
  }
```

#### 6.3.3 按子组收集数据
传递给第一个 gropingBy 的第二个收集器可以是任何类型

统计每个类型的数量
```java
  private static Map<Dish.Type, Long> countDishesInGroups() {
    return menu.stream().collect(groupingBy(Dish::getType, counting()));
  }
  // {OTHER=4, MEAT=3, FISH=2}
```
Collectors.collectingAndThen 😅详情见原文
### 6.4 分区
分区是分组的特殊情况，Collectors.partitioningBy
- 分区函数返回一个布尔值

按荤素分
```java
  private static Map<Boolean, List<Dish>> partitionByVegeterian() {
    return menu.stream().collect(partitioningBy(Dish::isVegetarian));
  }
  // {false=[pork, beef, chicken, prawns, salmon], true=[french fries, rice, season fruit, pizza]}
```
#### 6.4.1 分区的优势
- 保留了分区函数返回 true 或 false 的两套流元素列表

按荤素、食材类型分类
```java
  private static Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType() {
    return menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
  }
  // {false={MEAT=[pork, beef, chicken], FISH=[prawns, salmon]}, true={OTHER=[french fries, rice, season fruit, pizza]}}
```

查找荤素中热量最高的食物
```java
  private static Object mostCaloricPartitionedByVegetarian() {
    return menu.stream().collect(
        partitioningBy(Dish::isVegetarian,
            collectingAndThen(
                maxBy(comparingInt(Dish::getCalories)),
                Optional::get)));
  }
  // {false=pork, true=pizza}
```
### 6.5 收集器接口
```java
public interface Collector<T, A, R> {
    Supplier<A> supplier();
    BiConsumer<A, T> accumulator();
    BinaryOperator<A> combiner();
    Function<A, R> finisher();
    Set<Characteristics> characteristics();
}
```
- T：流中要收集的项目的泛型
- A：累加器的类型，累加器是在收集过程中用于累积部分结果的对象
- R：收集操作得到的对象（通常但并不一定是集合）的类型

#### 6.5.1 理解 Collector 接口声明的方法 😅详见原文
- 前四个方法都返回一个会被 collect 方法调用的函数
- 第五个方法 characteristics 则提供了一系列特征，也就是一个提示列表，告诉 collect 方法在执行归约操作的时候可以应用哪些优化（比如并行化）

##### 01 建立新的结果容器： supplier 方法
##### 02 将元素添加到结果容器： accumulator 方法
##### 05 characteristics 方法
- UNORDERED：归约结果不受流中项目的遍历和累积顺序的影响
- CONCURRENT：
    - accumulator 函数可以从多个线程同时调用，且该收集器可以并行归约流
    - 如果收集器没有标为 UNORDERED，那它仅在用于无序数据源时才可以并行归约
- IDENTITY_FINISH：
    - 表明完成器方法返回的函数是一个恒等函数，可以跳过
    - 这种情况下，累加器对象将会直接用作归约过程的最终结果
    - 这也意味着，将累加器 A 不加检查地转换为结果 R 是安全的   
#### 6.6 开发你自己的收集器以获得更好的性能 😅太高级了，看书吧

## 第 7 章 并行数据处理与性能
### 7.1 并行流
**并行流**就是把内容拆分成多个数据块，用不同线程分别处理每个数据块的流
#### 7.1.1 将顺序流转换为并行流
对顺序流调用 parallel 方法
```java
  public static long parallelSum(long n) {
    return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(Long::sum).get();
  }
```
配置并行流使用的线程池
- 并行流内部使用了默认的 ForkJoinPool
- 默认线程数量就是处理器数量
- 这个值由 Runtime.getRuntime().availableProcessors() 得到的
- 可以通过系统属性 System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");
    - Java 11 这个方法已经失效
    - 没有充足的理由，强烈建议不要修改
#### 7.1.2 测量流性能
JMH （Java 微基准套件 Java microbenchmark harness）

Stream.iterate 本质上是顺序的，使用它的并行流，性能提升不明显
```java
  public long sequentialSum() {
    return Stream.iterate(1L, i -> i + 1).limit(N).reduce(0L, Long::sum);
  }
```
选择适当的数据结构往往比并行化算法更重要
```java
  public long parallelRangedSum() {
    return LongStream.rangeClosed(1, N).parallel().reduce(0L, Long::sum);
  }
```
并行化的代价
- 并行化过程本身需要对流做递归划分，把每个子流的规约操作分配到不同的线程，然后把这些操作的结果合并成一个值
- 在多核之间移动数据的代价
    - 要保证：在核中并行执行工作的时间 > 在核之间传输数据的时长
#### 7.1.3 正确使用并行流
- 产生错误的首要原因：使用的算法改变了某些共享状态
    - 要避免共享可变状态，确保并行 Stream 得到正确的结果
#### 7.1.4 高效使用并行流
- 通过测量，判断是否得到性能提升
- 留意装箱，原始类型流：IntStream、LongStream、DoubleStream
- 有些操作本身在并行流上的性能就比顺序流差
    - limit、findFirst 等医疗元素顺序的操作
    - findAny 不需要按顺序操作，性能优于 findFirst
    - 调用 unordered 方法把有序流变成无序流
- 考虑流的操作流水线的总计算成本
- 对于较小的数据量，选择并行几乎是一个糟糕的决定
- 要考虑流背后的数据结构是否易于分解
    - ArrayList 的拆分效率比 LinkedList 高的多，因为前者不用遍历就可以平均拆分，后者则必须遍历
    - 用 range 工厂方法创建的原始类型流也可以快速分解
- 流自身的特点以及流水线中的中间操作修改流的方式，都可能会改变分解过程的性能
- 还要考虑终端操作中合并步骤的代价的大小
### 7.2 分支/合并框架
- 以递归方式将可以并行的任务拆分成更小的任务
- 然后将美国子任务的结果合并起来生成整体结果
- 它是 ExecutorService 接口的一个实现，把子任务分配给线程池（ForkJoinPool）中的工作线程
#### 7.2.1 使用 RecursiveTask
#### 7.2.2 使用分支/合并框架的最佳做法
- 对一个任务调用 join 方法会阻塞调用方，直到该任务返回结果
    - 有必要在两个子任务的计算都开始之后再调用
    - 否则，你的代码会比原始的顺序算法更慢且更复杂，因为每个子任务都必须等待另一个子任务完成后才能启动
- 不应该在 RecursiveTask 内部使用 ForkJoinPool 的 invoke 方法
- 对子任务调用 fork 方法可以把它排进 ForkJoinPool
    - 同时对左右两边的子任务调用它似乎很自然，但这样的效率比直接对期中一个调用 compute 低
    - 这样做可以为期中一个子任务重用同一线程，从而避免在线程池中多分配一个任务造成的开销
- Debug 时会很郁闷
- 和并行流一样，不一定比顺序执行速度快
    - 一个惯用的方法：把输入/输出放在一个子任务，计算放在另一个，这样计算就可以和输入/输出同时进行
#### 7.2.3 工作窃取
由于每个任务花费的时间不同（比如磁盘、网络访问慢），导致有的线程很闲、有的很忙，为了平衡工作量，有了工作窃取算法
- 首先，将任务差不多平均分配到 ForkJoinPool 中的所有线程上
- 其次，每个线程都将分配到的任务保存在一个双端队列中
    - 每完成一个任务，就从队列头取出下一个任务执行
- 当任自己的务队列为空时，其他线程还很忙
    - 随机选择一个其他线程
    - 从队列尾部"偷走"一个任务执行
- 划分成许多小任务而不是少数几个大任务，有助于更好的在工作线程之间平衡负载
### 7.3 Spliterator
- Java 8 中的一个新接口
- 可分迭代器（splitable iterator）
- 和 Iterator 一样，用于遍历数据源中的元素，但它是为了并行执行而设计
#### 7.3.1 拆分过程
#### 7.3.2 实现你自己的 Spliterator 😅太高级了，看书吧
## 第 8 章 Collection API 的增强功能
### 8.1 集合工厂
Arrays.asList
- 创建一个固定大小的列表
- 列表的元素可以更新
- 列表的元素不能增加、删除
```java
List<String> friends = Arrays.asList("Raphael", "Olivia");
```
#### 8.1.1 List 工厂
List.of 创建一个列表
- 创建的是一个只读列表
- 可以更新元素
- 不能添加、删除元素
```java
List<String> friends5 = List.of("Raphael", "Olivia", "Thibaut");
```
#### 8.1.2 Set 工厂
Set.of
```java
Set<String> friends = Set.of("Raphael", "Olivia", "Thibaut");
```
#### 8.1.3 Map 工厂
- Map.of
- Map.ofEntries
```java
 private static void creatingMaps() {
    System.out.println("--> Creating a Map with Map.of()");
    Map<String, Integer> ageOfFriends = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 26);
    System.out.println(ageOfFriends);

    System.out.println("--> Creating a Map with Map.ofEntries()");
    Map<String, Integer> ageOfFriends2 = Map.ofEntries(
        entry("Raphael", 30),
        entry("Olivia", 25),
        entry("Thibaut", 26));
    System.out.println(ageOfFriends2);
  }
```
### 8.2 使用 List 和 Set
因为集合的修改烦琐且容易出错，所以添加了两个方法解决这个问题：
- removeIf
- replaceAll

```java
List<Integer> list = new ArrayList<>();
list.add(1);
list.add(2);
list.add(30);
```
```java
list.removeIf(i -> i > 10);
// [1, 2]
```
```java
list.replaceAll(i -> i > 10 ? i / 10 : i);
// [1, 2, 3]
```
### 8.3 使用 Map
- forEach 遍历 Map
- 排序
    - [Entry.comparingByKey](#Map.Entry.comparingByKey)
    - Entry.comparingByValue
- getOrDefault 方法
    - 解决要查找的键在 Map 中不存在
    - 第一个参数作为键，第二个参数为默认值
- 计算模式
    - [computeIfAbsent](#computeIfAbsent)：给指定的键添加新的值（与原来的值进行合并操作）
    - [computeIfPresent](#computeIfPresent)：如果指定的键在 Map 中存在，使用新值替换旧值；如果不存在，Map 内容无变化
    - [compute](#compute)：添加键值对
        - 键已存在：更新值
        - 键不存在：新增值
    - [remove](#remove)：删除数据
        - remove(Object key, Object value) 可以防止误删数据
- 替换模式（8.3.6）
    - [replaceAll](#replaceAll)：
        - 改变 Map 中所有键的值
        - 不能改变 Map 中键
    - replace(K key, V value)
    - replace(K key, V oldValue, V newValue)
- merge 方法（8.3.7）
    - [处理键冲突时的情况](#merge)
    - [简化代码](#merge-简化代码)

#### Map.Entry.comparingByKey
```java
        Map<String, String> favouriteMovies = Map.ofEntries(
                entry("Raphael", "Star Wars"),
                entry("Cristina", "Matrix"),
                entry("Olivia", "James Bond"));
        favouriteMovies.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(System.out::println);
```
#### computeIfAbsent
```java
        Map<String, List<String>> friendsToMovies = new HashMap<>();

        String friend = "小刚";
        friendsToMovies.computeIfAbsent(friend, name -> new ArrayList<>())
                .add("星球大战");
        System.out.println(friendsToMovies);
        // {小刚=[星球大战]}
        friendsToMovies.computeIfAbsent(friend, name -> new ArrayList<>())
                .add("星际争霸");
        System.out.println(friendsToMovies);
        // {小刚=[星球大战, 星际争霸]}
        friendsToMovies.computeIfAbsent(friend, name -> new ArrayList<>())
                .add("星际争霸");
        System.out.println(friendsToMovies);
        // {小刚=[星球大战, 星际争霸, 星际争霸]}
```
#### computeIfPresent
```java
        Map<String, List<String>> gameMap = new HashMap<>();

        String friend = "小刚";
        gameMap.computeIfAbsent(friend, name -> new ArrayList<>())
                .add("星球大战");
        System.out.println(gameMap);
        //{小刚=[星球大战]}
        gameMap.computeIfPresent(friend, (k, v) -> List.of("星际争霸"));
        System.out.println(gameMap);
        // {小刚=[星际争霸]}
        gameMap.computeIfPresent(friend, (k, v) -> List.of("暗黑3", "地球时代"));
        System.out.println(gameMap);
        // {小刚=[暗黑3, 地球时代]}
        gameMap.computeIfPresent("没有这个键", (k, v) -> List.of("消消乐"));
        System.out.println(gameMap);
        // {小刚=[暗黑3, 地球时代]}
```
#### compute
```java
        Map<String, List<String>> gameMap = new HashMap<>();

        String friend = "小刚";
        gameMap.computeIfAbsent(friend, name -> new ArrayList<>())
                .add("星球大战");
        System.out.println(gameMap);
        //{小刚=[星球大战]}
        gameMap.compute(friend, (k, v) -> List.of("星际争霸"));
        System.out.println(gameMap);
        // {小刚=[星际争霸]}
        gameMap.compute(friend, (k, v) -> List.of("暗黑3", "地球时代"));
        System.out.println(gameMap);
        // {小刚=[暗黑3, 地球时代]}
        gameMap.compute("又来一个键", (k, v) -> List.of("红警"));
        System.out.println(gameMap);
        // {小刚=[暗黑3, 地球时代], 又来一个键=[红警]}
```
#### remove
```java
        Map<String, String> gameMap = new HashMap<>();
        gameMap.put("暴雪1", "魔兽争霸");
        gameMap.put("暴雪2", "暗黑3");
        gameMap.put("暴雪3", "守望先锋");

        // 防止误删数据
        gameMap.remove("暴雪1", "疯狂的麦克斯");
        System.out.println(gameMap);
        // {暴雪1=魔兽争霸, 暴雪3=守望先锋, 暴雪2=暗黑3}
        gameMap.remove("暴雪1", "魔兽争霸");
        System.out.println(gameMap);
        // {暴雪3=守望先锋, 暴雪2=暗黑3}
```
#### replaceAll
```java
        Map<String, String> gameMap = new HashMap<>();
        gameMap.put("blizzard1", "starcraft2");
        gameMap.put("blizzard2", "diablo4");
        gameMap.put("blizzard3", "warcraft3");

        gameMap.replaceAll((k, v) -> v.toUpperCase());
        System.out.println(gameMap);
        // {blizzard3=WARCRAFT3, blizzard2=DIABLO4, blizzard1=STARCRAFT2}
        // 不能改变 key 的值
        gameMap.replaceAll((k, v) -> k.toUpperCase());
        System.out.println(gameMap);
        // {blizzard3=BLIZZARD3, blizzard2=BLIZZARD2, blizzard1=BLIZZARD1}
```
#### merge
```java
        Map<String, String> family = Map.ofEntries(
                entry("Teo", "Star Wars"),
                entry("Cristina", "James Bond"));
        Map<String, String> friends = Map.ofEntries(entry("Raphael", "Star Wars"));

        System.out.println("--> Merging the old way, 当键重复时会被覆盖");
        Map<String, String> everyone = new HashMap<>(family);
        everyone.putAll(friends);
        System.out.println(everyone);
        // {Cristina=James Bond, Raphael=Star Wars, Teo=Star Wars}

        Map<String, String> friends2 = Map.ofEntries(
                entry("Raphael", "Star Wars"),
                entry("Cristina", "Matrix"));

        System.out.println("--> Merging maps using merge(), 可以处理键重复时的情况");
        Map<String, String> everyone2 = new HashMap<>(family);
        friends2.forEach((k, v) -> everyone2.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));
        System.out.println(everyone2);
        // {Raphael=Star Wars, Cristina=James Bond & Matrix, Teo=Star Wars}
```
#### merge 简化代码
```java
        Map<String, Integer> gameMap = new HashMap<>();
        String gameName = "巫师3";
        Integer count = gameMap.get(gameName);
        if (count == null) {
            gameMap.put(gameName, 1);
        } else {
            gameMap.put(gameName, count + 1);
        }
        System.out.println(gameMap);
        // {巫师3=1}

        // 简化代码
        gameMap.merge(gameName, 1, (k, v) -> v + 1);
        System.out.println(gameMap);
        // {巫师3=2}

        gameMap.merge("英雄无敌", 1, (k, v) -> v + 1);
        System.out.println(gameMap);
        // {英雄无敌=1, 巫师3=2}
```
### 8.4 改进的 ConcurrentHashMap
推荐使用 mappingCount 代替 size 方法
## 第 9 章 重构、测试和调试
### 9.1 为改善可读性和灵活性重构代码
#### 9.1.1 改善代码的可读性
三种简单的重构：
- 用 Lambda 表达式取代匿名类；
    - 匿名类的类型是在初始化时确定的
    - Lambda 的类型取决于它的上下文
- 用方法引用重构 Lambda 表达式；
    - 更清晰地表达问题陈述是什么
- 用 Stream API 重构命令式的数据处理
    - 更清晰地表达数据处理管道的意图
### 9.2 使用 Lambda 重构面向对象的设计模式
- 策略模式（9.2.1）
- 模板方法（9.2.2）
- 观察者模式（9.2.3）
    - 适合逻辑简单的情况下使用 Lambda 表达式
- 责任链模式（9.2.4）
- 工厂模式
    - 使用 Supplier 包装产品
```java
  final static private Map<String, Supplier<Product>> map = new HashMap<>();
  static {
    map.put("loan", Loan::new);
    map.put("stock", Stock::new);
    map.put("bond", Bond::new);
  }

  public static Product createProductLambda(String name) {
      Supplier<Product> p = map.get(name);
      if (p != null) {
        return p.get();
      }
      throw new RuntimeException("No such product " + name);
    }
```
多个参数时就没那么好看了
```java
interface TriFunction<T, U, V, R>{
  R apply(T t, U u, V v);
}

Map<String, TriFunction<Integer, Integer, String, Product>> map = new HashMap<>();
```
### 9.4.2 使用日志调试
- peek
```java
    List<Integer> result = Stream.of(2, 3, 4, 5)
        .peek(x -> System.out.println("taking from stream: " + x))
        .map(x -> x + 17)
        .peek(x -> System.out.println("after map: " + x))
        .filter(x -> x % 2 == 0)
        .peek(x -> System.out.println("after filter: " + x))
        .limit(3)
        .peek(x -> System.out.println("after limit: " + x))
        .collect(toList());
```
## 第 10 章 基于 Lambda 的领域特定语言

### 10.5 小结
引入 DSL 的主要目的是为了弥补程序员与领域专家之间对程序认知理解上的差异

## 第 11 章 用 Optional 取代 null
### 11.3 应用 Optional 的几种模式
#### 11.3.1 创建 Optional 对象

##### 01 声明一个空的 Optional

```java
Optional<Car> optCar = Optional.empty();
```

##### 02 依据一个非空值创建 Optional
- 如果 car 是一个 null， 代码会立即抛出 NullPointerException

```java
Optional<Car> optCar = Optional.of(car);
```
##### 03 可接受 null 的 Optional
- 如果 car 是 null，那么得到的 Optional 对象就是空对象
```java
Optional<Car> optCar = Optional.ofNullable(car);
```
#### 11.3.2 使用 map 从 Optional 对象中提取和转换值
```java
Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
Optional<String> name = optInsurance.map(Insurance::getName);
```
#### 11.3.3 使用 flatMap 链接 Optional 对象
##### 01 使用 Optional 获取 car 的保险公司名称
- 🧨 一样会抛出 NullPointerException
- 不需要大量的 if else 分支判断
- 通过类型系统让你的域模型中隐藏的知识显示地提现在代码中
  - 人不一定有车、有保险
  - 保险公司一定有名称（如果没有，是因为数据出错，而不是代码问题） 
```java
  public String getCarInsuranceName(Optional<Person> person) {
    return person.flatMap(Person::getCar)
        .flatMap(Car::getInsurance)
        .map(Insurance::getName)
        .orElse("Unknown");
  }
```
#### 11.3.4 操纵由 Optional 对象构成的 Stream
- 🧨 一样会抛出 NullPointerException
```java
  public Set<String> getCarInsuranceNames(List<Person> persons) {
    return persons.stream()
        .map(Person::getCar)
        .map(optCar -> optCar.flatMap(Car::getInsurance))
        .map(optInsurance -> optInsurance.map(Insurance::getName))
        .flatMap(Optional::stream)
        .collect(toSet());
  }
```
#### 11.3.5 默认行为及解引用 Optional 对象

- get()
  - 最简单但最不安全
  - 相对于嵌套式的 null 检查，并未体现出多大改进
- orElse(T other)
  - 允许在 Optional 对象不包含值时提供一个默认值
- orElseGet(Supplier<? extends T> supplier)
  - 是 orElse 方法的延迟调用版
  - Supplier 方法只有在 Optional 对象不含值时才执行调用
  - 如果创建默认值耗时费力，建议采用这种方式，提升程序性能
  - 或者自己非常确定某个方法仅在 Optional 为空时才调用，也可以采用这种方式
- or(Supplier<? extends Optional<? extends T>> supplier)
  -  与 orElseGet 方法很像
  -  不过它不会解包 Optional 对象中的值，即便该值是存在的
  -  实战中
     - Optional 对象含有值：不会执行任何额外操作，直接返回该 Optional 对象
     - Optional 对象为空：该方法会延迟的返回一个不同的 Optional 对象
- orElseThrow(Supplier<? extends X> exceptionSupplier)
  - 和 get 方法非常类似
  - 遭遇 Optional 对象为空时都会抛出一个异常
  - 但使用 orElseThrow 你可以定制希望抛出的异常类型  
- ifPresent(Consumer<? super T> action)
  - 变量值存在时，执行一个以参数形式传入的方法
  - 否则就不进行任何操作
- ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)
  - Java 9 引入的一个新的实例方法
  - 该方法不同于 ifPresent
  - 接受一个 Runnable 方法，如果 Optional 对象为空，就执行该方法所定义的动作
### 11.4 使用 Optional 的实战示例

## 第12章 新的日期和时间 API
- Joda-Time 第三方日期和时间库

### 12.1 LocalDate、LocalTime、LocalDateTime、Instant、Duration 以及 Period
#### 12.1.1 使用 LocalDate 和 LocalTime
##### LocalDate
  - 是一个不可变对象
  - 只提供了简单的日期
  - 不含当天的时间信息
  - 也不附带任何与时区相关的信息
  
可以通过静态工厂方法 of 创建一个 LocalDate 实例
```java
    LocalDate date = LocalDate.of(2014, 3, 18);
    int year = date.getYear(); // 2014
    Month month = date.getMonth(); // MARCH
    int day = date.getDayOfMonth(); // 18
    DayOfWeek dow = date.getDayOfWeek(); // TUESDAY
    int len = date.lengthOfMonth(); // 31 (days in March)
    boolean leap = date.isLeapYear(); // false (not a leap year)
    System.out.println(date);
```
还可以使用工厂方法 now 从系统时钟获取当前的日期
```java
LocalDate today = LocalDate.now();
```
##### LocalTime
- 可以表示一天中的时间，比如 13:45:20
```java
    LocalTime time = LocalTime.of(13, 45, 20); // 13:45:20
    int hour = time.getHour(); // 13
    int minute = time.getMinute(); // 45
    int second = time.getSecond(); // 20
    System.out.println(time);
```

使用静态方法 parse 也可以创建
```java
LocalDate date = LocalDate.parse("2017-09-21");
LocalTime time = LocalTime.parse("13:45:20");
```
#### 12.1.2 合并日期和时间
- LocalDateTime：LocalDate 和 LocalTime 的合体
- 同时表示日期和时间
- 但不带有时区信息
```java
    // 2014-03-18T13:45
    LocalDateTime dt1 = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45, 20);
    LocalDateTime dt2 = LocalDateTime.of(date, time);
    LocalDateTime dt3 = date.atTime(13, 45, 20);
    LocalDateTime dt4 = date.atTime(time);
    LocalDateTime dt5 = time.atDate(date);
    System.out.println(dt1);

    LocalDate date1 = dt1.toLocalDate();
    System.out.println(date1);
    LocalTime time1 = dt1.toLocalTime();
    System.out.println(time1);
```
#### 12.1.3 机器的日期和时间格式
##### java.time.Instant
- 建模方式：表示一个持续时间段上某个点的单一大整型数
- 以 Unix 元年时间（传统的设定为 UTC 时区 1970 年 1 月 1 日午夜时分）开始所经历的秒数进行计算
- 可以通过向静态工厂方法 ofEpochSecond 传递代表秒数的值创建一个该类的实例
- ofEpochSecond 增强的重载版本，它接受第二个以纳秒为单位的参数值，对传入作为秒数的参数进行调整
  - 确保保存的纳秒分片在 0  到 999 999 999 之间

```java
    Instant instant = Instant.ofEpochSecond(44 * 365 * 86400);
    Instant now = Instant.now();
    // 2 秒之后再加上 10 亿纳秒（1 秒）
    Instant instant = Instant.ofEpochSecond(2, 1_000_000_000);
    // 4 秒之前的 10 亿纳秒（1 秒）
    Instant instant = Instant.ofEpochSecond(4, -1_000_000_000);
```

- **Instant 的设计初衷是为了便于机器使用**
  - 它包含的是由秒及纳秒所构成的数字
  - 它无法处理那些非常容易理解的时间单位（例如，获得今天是这个月的第几天）
    - 可以通过 Duration 或 Period 类使用 Instant
#### 12.1.4 定义 Duration 或 Period
##### Duration 类主要用于以秒和纳秒衡量时间
- 不能仅向 between 方法传递一个 LocalDate 对象做参数
```java
        LocalTime time = LocalTime.of(13, 45, 20); // 13:45:20
        Instant instant = Instant.ofEpochSecond(44 * 365 * 86400);
        Instant now = Instant.now();

        Duration d1 = Duration.between(LocalTime.of(13, 45, 10), time);
        Duration d2 = Duration.between(instant, now);
        System.out.println(d1.getSeconds());
        System.out.println(d2.getSeconds());
```
##### Period 以年、月、日的方式对多个时间单位建模
工厂方法 between 可以得到两个 LocalDate 之间的时长
```java
        Period tenDays = Period.between(LocalDate.of(2017, 9, 11),
                                        LocalDate.of(2017, 9, 21));
        System.out.println(tenDays.getDays());
```
##### Duration 和 Period 便利的创建工厂
```java
        Duration threeMinutes = Duration.ofMinutes(3);
        Duration threeDays = Duration.of(3, ChronoUnit.DAYS);
        Period tenDays = Period.ofDays(10);
        Period threeWeeks = Period.ofWeeks(3);
        Period towYearsSixMonthsOneDay = Period.of(2, 6, 1);
```

表 12-1 Duration 和 Period 中表示时间间隔的通用方法

|方法名 | 是否静态方法 | 方法描述 |
| - | - | - |
| between | 是 | 创建两个时间点之间的间隔 |
| from | 是 | 由一个临时时间点创建时间间隔 |
| of | 是 | 由它的组成部分创建时间间隔的实例 |
| parse | 是 | 由字符串创建时间间隔的实例 |
| addTo | 否 | 创建该时间间隔的副本，并将其**叠加**到某个指定的 temporal 对象 |
| get | 否 | 读取该时间间隔的状态 |
| isNegative | 否 | 检查该时间间隔是否为负值，不包括零 |
| isZero | 否 | 检查该时间间隔的时长是否为零 |
| minus | 否 | 通过**减去**一定的时间创建该间隔时间的副本 |
| multipliedBy | 否 | 将时间间隔的值**乘以**某个标量创建该时间间隔的副本 |
| negated | 否 | 以**忽略**某个时长的方式创建该时间间隔的副本 |
| plus | 否 | 以**增加**某个指定时长的方式创建该时间间隔的副本 |
| subtractFrom | 否 | 从指定的 temporal 对象中**减去**该时间间隔 |

##### 截止目前，Duration 和 Period 对象都是不可修改的
- 更好的支持函数式编程
- 线程安全
#### 12.2 操纵、解析和格式化日期
以下代码中所有的方法都返回一个修改了属性的对象，**不会修改原来的对象**
```java
        LocalDate date1 = LocalDate.of(2017, 9, 21); // 2017-09-21
        LocalDate date2 = date1.withYear(2011); // 2011-09-21
        LocalDate date3 = date2.withDayOfMonth(25); // 2011-09-25
        LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 2); // 2011-02-25
```
- 使用 get 和 with 方法，可以将 Temporal 对象值的读取和修改区分开
  - with 方法并不会直接修改现有的 Temporal 对象
  - 它会创建现有对象的副本并更新对应的字段
  - 这一过程也被称作**函数式更新**

以相对方式修改 LocalDate 对象的属性
```java
        LocalDate date1 = LocalDate.of(2017, 9, 21); // 2017-09-21
        LocalDate date2 = date1.plusWeeks(1); // 2017-09-28
        LocalDate date3 = date2.minusYears(6); // 2011-09-28
        LocalDate date4 = date3.plus(6, ChronoUnit.MONTHS); // 2012-03-28
```
#### 12.2.1 使用 TemporalAdjuster
- 将日期调整到下个周日、下个工作日
- 本月的最后一天
```java
        import static java.time.temporal.TemporalAdjusters.*;

        LocalDate date1 = LocalDate.of(2014, 3, 18); // 2014-03-18
        LocalDate date2 = date1.with(nextOrSame(DayOfWeek.SUNDAY)); // 2014-03-23
        LocalDate date3 = date2.with(lastDayOfMonth()); // 2014-03-31
```
更多方法详见 表 12-3 TemporalAdjusters 类中的工厂方法
##### 实现自己的时间调节器
- 使用 Lambda 表达式定义 TemporalAdjuster 对象，推荐使用静态工厂方法 TemporalAdjusters.ofDateAdjuster

下一个工作日
```java
        TemporalAdjuster nextWorkingDay = TemporalAdjusters.ofDateAdjuster(temporal -> {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK)); // 读取当前日期
            int dayToAdd = 1; // 正常情况：增加一天
            if (dow == DayOfWeek.FRIDAY) {
                dayToAdd = 3; // 如果当天是周五，增加三天
            }
            if (dow == DayOfWeek.SATURDAY) {
                dayToAdd = 2; // 如果当天是周六，增加两天
            }
            return temporal.plus(dayToAdd, ChronoUnit.DAYS); // 增加恰当的天数后，返回修改的日期
        });

        LocalDate date1 = LocalDate.of(2021, 1, 22); // 2021-01-22
        LocalDate date2 = date1.with(nextWorkingDay); // 2021-01-25
```
#### 12.2.2 打印输出及解析日期 - 时间对象
##### 包 java.time.format
- 格式化、解析日期
- 最重要的类 **DateTimeFormatter**

```java
        LocalDate date = LocalDate.of(2014, 3, 18);
        String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE); // 20140318
        String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 2014-03-18
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
    

