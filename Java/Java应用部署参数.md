# Java应用部署参数

- 通常情况下，我们会为生产环境的 Java 应用设置 -XX:+HeapDumpOnOutOfMemoryError 和 -XX:HeapDumpPath=…这 2 个 JVM 参数，用于在出现 OOM 时保留堆快照。这个课程中，我们也多次使用 MAT 工具来分析堆快照。

 ```cmd
 -XX:+HeapDumpOnOutOfMemoryError 设置当首次遭遇内存溢出时导出此时堆中相关信息
 -XX:HeapDumpPath=/tmp/heapdump.hprof 指定导出堆信息时的路径或文件名
 ```

 - JVM 参数需要放在 -jar 之前，不然会被当成了 Java 程序的启动参数

```cmd
java -Xms1g -Xmx1g -jar app.jar 
```

VM Flags:
Non-default VM flags: -XX:CICompilerCount=2 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=null -XX:InitialHeapSize=4294967296 -XX:MaxHeapSize=4294967296 -XX:MaxNewSize=1431306240 -XX:MinHeapDeltaBytes=524288 -XX:NewSize=1431306240 -XX:OldSize=2863661056 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC 


System.out.println("VM options");
System.out.println(ManagementFactory.getRuntimeMXBean().getInputArguments().stream().collect(Collectors.joining(System.lineSeparator())));
System.out.println("Program arguments");
System.out.println(Arrays.stream(args).collect(Collectors.joining(System.lineSeparator())));

Error Code: 1175. You are using safe update mode and you tried to update a table without a WHERE that uses a KEY column.  To disable safe mode, toggle the option in Preferences -> SQL Editor and reconnect.


