# JVM优化
- 在高并发场景下，当大量线程同时竞争同一个锁资源时，偏向锁会升级为重量级锁

```cmd
-XX:-UseBiasedLocking // 关闭偏向锁（默认打开）
或者
-XX:+UseHeavyMonitors  // 设置重量级锁
```

- 高负载、高并发下建议关闭自旋锁

```cmd
-XX:-UseSpinning // 参数关闭自旋锁优化 (默认打开) 
-XX:PreBlockSpin // 参数修改默认的自旋次数。JDK1.7 后，去掉此参数，由 jvm 控制
```

java -jar -server -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xms1024m -Xmx2048m -Xmn256m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC

java -jar -server -Xms1024m -Xmx2048m -XX:-UseBiasedLocking

## 诊断工具
- jmap
- jstack
- jconsole
- jhsdb
- jcmd
- JFR（[Java Flight Recorder](https://docs.oracle.com/javacomponents/jmc-5-4/jfr-runtime-guide/about.htm#JFRUH172)）

## 可视化工具
- JConsole
- VisualVM
- JMC（[Java Mission Control](https://www.oracle.com/technetwork/java/javaseproducts/mission-control/java-mission-control-1998576.html)）

JMC在被监控的程序启动时加入以下参数

```cmd
-Dcom.sun.management.jmxremote.rmi.port=8192 -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=8192 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=192.168.0.104
```

## 编译方式
- AOT（Ahead-of-Time Compilation），直接将字节码编译成机器代码，，这样就避免了 JIT 预热等各方面的开销。 JDK 9开始支持

## JVM参数
- PrintReferenceGC 打印引用GC日志，排查内存溢出

```cmd
-XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintReferenceGC
```

- 包装类缓存范围
```cmd
-XX:AutoBoxCacheMax=N
```

对象创建

| 参数 | 功能 | 备注 |
| --- | --- | --- |
| -XX:+/-UseTLAB | 对象创建时在堆中开启本地线程池缓冲 | 默认同步处理（利用CAS） |

# 《Java 性能权威指南》

- [源码](https://github.com/ScottOaks/JavaPerformanceTuning)
- [faban](http://faban.org/) 负载生成器，可测试简单URL性能

## JVM 调优标致
### 布尔类型
- **-:XX: FlagName**表示开启,**-XX:FlagName**表示关闭

### 参数类型
- **-:XX:FlagName=something**,表示将标志**flagName**的值设置为**something**。其中**something**通常可以为任意值。


| 参数 | 功能 | 备注 |
| --- | --- | --- |
| -XX:+Printflagsfinal | 获得具体运行环境中特定标志的默认值 | 默认为false，即关闭 |

## Java 性能调优工具箱
### CPU 使用率

```cmd
vmstat 1 # 每隔1秒显示一行
```

### 磁盘使用率

```cmd
iostat -xm 5
```

### 网络使用率

- 网络无法支持100%的使用率。
- 对本地以太局域网来说,承受的网络使用率超过40%就意味着接口饱和了。

```cmd
netstat

nicstat 5 # 受欢迎的，可以显示每个网络接口流量的概要，包括网络接口使用度
```

### Java 监控工具

```cmd
jcmd # 它用来打印Java进程所涉及的基本类、线程和VM信息
```

#### 本地分析器
- 本地性能分析器可以提供JVM和应用代码内部的信息。
- 如果本地性能分析器显示GC占用了主要的CPU使用时间,优化垃圾收集器就是正确的做法。然而,如果显示编译线程占用了明显的时间,则说明通常对应用性能没什么影响。

## JIT 编译器

- 如果堆小于3GB，32位的Java会更快一些，并且内存占用也更少。
- 32位JVM的不足之处是进程最多只能占用4GB内存。
- 在32位JVM上运行的程序,只要与32位寻址空间吻合,无论机器是32位还是64位,都要比在类似配置的64位JVM上运行时快5%到20%。

```cmd
+XX: TieredCompilation # 开启分层编译：Java 8 以默认开启
```

⏰ 代码缓存 可能影响速度

```cmd
# 如何增加代码缓存，基本上就是摸着石头过河，通常的做法是简单地增加1倍或3倍。
-XX:ReservedCodeCacheSize=N
```


⏰ 编译阈值

使用较低的设置，主要基于以下2个原因
1. 节约一点应用热身的时间。
2. 使得某些原本可能不会被server编译器编译的方法得以编译。

```cmd
-XX:CompileThreshold=N # server 编译器 N 默认为 10000

# OSR 编译阈值 
# server 编译器 N 默认为 140 
-XX:OnStackReplacePercentage=N
```

- 观察代码如何被编译的最好方法是开启PrintCompilation

### 选择 GC 算法
- 选择Concurrent收集器时,如果堆较小,推荐使用CMS收集器
- G1的设计使得它能够在不同的分区(Region)处理堆,因此它的扩展性更好,比CMS更易于处理超大堆的情况

### 调整堆的大小
- 堆大小的调节是JVM自适应调优的核心
 
```cmd
-Xms N # 初始值
-Xmx N # 最大值
```

| 操作系统 | 初始堆的大小 Xms | 最大堆的大小 Xmx |
| --- | --- | --- |
| Linux/Soaris,64位服务器 | 取512MB和物理内存大小1/64二者中的最小值 | 取32GB和物理内存大小1/4二者中的最小值 |

**更多查看 表5-4:默认堆的大小**

- 当使用-Xmx 调整最大堆后，一个经验法则是完成Full GC 后，应该释放出70%的空间（30%的空间仍然占用）。

1. JVM会根据其运行的机器,尝试估算合适的最大、最小堆的大小。
2. 除非应用程序需要比默认值更大的堆,否则在进行调优时,尽量考虑通过调整GC算法的性能目标,而非微调堆的大小来改善程序性。

### 代空间的调整

- 所有用于调整代空间的命令行标志调整的都是新生代空间；新生代空间剩下的所有空间都被老年代占用。

```cmd
-XX:NewRatio=N # 设置新生代与老年代的空间占用比率，默认值 = 2
# Initial Young Gen Size = Initial Heap Size / (1 + NewRatio)
# 默认情况下，新生代空间大小是初始堆大小的 33%

-XX:NewSize=N　# 设置新生代空间的初始大小
-XX:MaxNewSize=N　# 设置新生代空间的最大大小
-XmnN　# 将NewSize和MaxNewSize设定为同一个值的快捷方法
```

### 垃圾回收工具

```cmd
-XX:+PrintGCDetails # 推荐使用
-XX:+PrintGCTimeStamps 或者 -XX:+PrintGCDateStamps # 便于我们更精确地判断几次GC操作之间的时间
-Xloggc:filename # 修改输出到某个文件。

jstat -gcutil process_id 1000 # 当忘开启日志时，可以通过命令查看
```

- [GC Histogram](http://java.net/projects/gchisto) 分析日志

## 垃圾收集算法

### 理解 Throughput 收集器

```cmd
-XX:MaxGCPauseMillis=N # 设定应用可承受的最大停顿时间，同时影响MinorGC和FullGC
-XX:GCTimeRatio=N # 设置你希望应用程序在垃圾回收上花费多少时间(与应用线程的运行时间相比较)。默认值 99
```

- 如果应用程序在垃圾回收上消耗总时间的3%至6%,其效果会是相当不错的


### G1 垃圾收集器调优

```cmd
# G1 最主要的调优标识
# 设定应用可承受的最大停顿时间，同时影响MinorGC和FullGC
-XX:MaxGCPauseMillis=N 
-XX:ParallelGCThreads=N # 标志设置运行的线程数
-XX:ConcGCThreads=N # 对于并发运行阶段可以使用来设置运行线程数
# ConcGCThreads = (ParallelGCThreads + 2) / 4

# 调整G1垃圾收集器运行的频率，默认值 45；
# 如果设置过高会陷入 Full GC 的泥潭
-XX:InitiatingHeapOccupancyPercent=N

-XX:MaxGCPauseMillis=N # GC停顿可忍受的最大时长
```

## 堆内存最佳实践

1. 有节制地创建对象并尽快丢弃。使用更少的内存,这是提升垃圾收集器效率的最好方法。
2. 对频繁重建的对象合理重用。

### 常用命令

```cmd
# 堆直方图
jcmd 8998 GC.class_histogram

# 堆转储
jcmd process_id GC.heap_dump /path/to/heap_dump.hprof
# 或
jmap -dump:live,file=/path/to/heap_dump.hprof process_id

# 开启自动堆转储，JVM会在抛出OutOfMemoryError时创建堆转储
-XX:+HeapDumpOnOutOfMemoryError

# 该标志指定了堆转储将被写入的位置
-XX:HeapDumpPath=<path>

# 在运行一次FullGC后生成一个堆转储文件
-XX:+HeapDumpBeforeFullGC
```

### 减少对象大小
1. 减少实例变量的个数（效果明显）
2. 减少实例变量的大小（效果不明显）

### 弱引用、软引用与其他引用

```cmd
# 当运行一个使用了大量非确定引用的对象时，查看处理这些引用花了多少时间，默认 false
-XX:+PrintReferenceGC
```

## 原生内存最佳实践

### 大页
- 使用大页通常可以明显提升应用的速度。
- 在大多数操作系统上，必须显式开启大页支持。


## 线程与同步的性能
### 设置最大线程数
- 最优线程数还与每个任务阻塞的频率有关；
- 外部资源可能是瓶颈（对方服务器4核，不能轻松应对客户端访问；本地客户端4台服务器同时访问）；
- 减少当前瓶颈处的负载，性能可能会上升；
- 充足的测试非常关键；
- 当任务的积压是由额外的负载进入系统（比如有更多的客户端发起请求），增加线程才有意义；
- 尝试获得最好性能时，使用KISS原则，将核心线程数与最大线程数设置成相同；
- 在使用ThreadPoolExecutor时，选择更简单的选项通常会带来最好的、最能预见的性能。

#### 计算密集型
- 没有外部网络调用（比如访问数据库）；
- 也不会激烈的竞争内部锁。

### ThreadPoolExecutor 与 ForkJoinPool
- 如果任务是均衡的，使用分段的ThreadPoolExecutor性能更好；
- 而如果任务是不均衡的，则使用ForkJoinPool性能更好。（任务窃取）
- ForkJoinPool 需要仔细考虑在哪个点结束递归。

### 自动并行化 ForkJoinPool

1.  ForkJoinPool类应该用于递归、分治算法。
2. 应该花些心思来确定，算法中的递归任务何时结束最为合适。创建太多任务会降低性能，但如果任务太少，而任务所需的执行时间又长短不一，也会降低性能。
3. Java8中使用了自动并行化的特性会用到一个公共的ForkJoinPool实例。我们可能需要根据实际情况调整这个实例的默认大小。

设置 ForkJoinTask 池的大小和设置其他任何线程池同样重要。

```cmd
-Djava.util.concurrent.ForkJoinPool.common.parallelism=N
```

- 如果Servlet代码会执行某个并行任务，而我们想确保CPU可供其他任务使用，可以考虑减小公共池的线程数。
- 如果公共池中的任务会阻塞等待I/O或其他数据，也可以考虑增大线程数。

### 调节线程栈大小

- 64位系统，线程栈大小都是 1M
- 32位 Linux 线程栈大小 320 KB
- 32位 JVM 中使用较小的栈（128 KB）往往是不错的选择，因为可以在进程空间中释放部分内存，使得 JVM 的堆可以大一些

```cmd
-Xss=N # 例如 -Xss=256k
```

### 偏向锁

- 使用了线程池，在偏向锁生效的情况下性能会更糟。因为不同的线程有同等的机会访问争用的锁。关闭偏向锁，可以稍稍改进性能。偏向锁默认是开启的。

```cmd
-XX:UseBiasedLocking # 禁用偏向锁
```

### 监控线程与锁

1. 线程总数。
2. 线程花在等待锁或其他资源上的世界。

## Java EE 性能调优

### Web 容器

- 减少输出，可以加快返回速度
- 减少不必要的空格，节省网络传输开销
- 合并 CSS、JS 资源，将多个小文件合并为一个大文件
- 压缩输出
- 关闭 JSP 动态编译
- 注意公网与内网的速度差别

### HTTP 会话状态
- 在session中存放数据，会使堆中的存活数据增加，增加GC时长

grep awk sort uniq Linux常用命令


