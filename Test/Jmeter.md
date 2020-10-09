# JMeter 集群入门

## 快速启动

#### 1. 下载最新版 JMeter
- 为了避免不必要的错误，客户端、服务端请使用相同版本

#### 2. 服务端启动

```cmd
bin/jmeter-server -Djava.rmi.server.hostname=192.168.0.161 -Dserver.rmi.localport=9900 -Dserver_port=9900 -Dserver.rmi.ssl.disable=true
```

- java.rmi.server.hostname：远程访问地址
- server.rmi.localport：远程访问端口
- server_port：本地服务端口
- server.rmi.localport 与 server_port 必须一致
- server.rmi.ssl.disable：禁用SLL验证，否则需要配置相关参数

#### 3. 客户端启动

- 先修改 bin/jmeter.properties

```properties
remote_hosts=192.168.0.161:9900
server.rmi.ssl.disable=true
```

- bin 目录下点击 jmeter 图标启动图形界面；
- 或者使用命令：jmeter -n -t [jmx file] -l [results file] -e -o [Path to web report folder]

- 启动本地测试 

```cmd
jmeter -n -t /Users/aoe/data/HTTP-Request.jmx -l /Users/aoe/data/jmeter-result.log -e -o /Users/aoe/data/report
```
  
  - 启动所有远程测试

```cmd
jmeter -n -t /Users/aoe/data/HTTP-Request.jmx -r -l /Users/aoe/data/jmeter-result.log -e -o /Users/aoe/data/report
```


#### 4. 通过图形界面在服务端运行测试脚本


### [更多参数说明（官方文档）](https://jmeter.apache.org/usermanual/get-started.html)

- 1.4.4 CLI Mode (Command Line mode was called NON GUI mode)
- 1.4.5 Server Mode
- 1.4.6 Overriding Properties Via The Command Line
- 1.4.8 Full list of command-line options
- 1.5 Configuring JMeter

- 服务端可以通过 bin/setenv.sh 设置JVM参数

```vim
# This is the file bin/setenv.sh,
# it will be sourced in by bin/jmeter

# Use a bigger heap, but a smaller metaspace, than the default
export HEAP="-Xms1G -Xmx1G"
```

### 最佳实践

> Don't use GUI mode for load testing !, only for Test creation and Test debugging.
> For load testing, use CLI Mode (was NON GUI):
>    jmeter -n -t [jmx file] -l [results file] -e -o [Path to web report folder]
> & increase Java Heap to meet your test requirements:
>    Modify current env variable HEAP="-Xms1g -Xmx1g -XX:MaxMetaspaceSize=256m" in the jmeter batch file
> Check : https://jmeter.apache.org/usermanual/best-practices.html


## 远程测试

- 官方文档[Remote Testing](https://jmeter.apache.org/usermanual/remote-test.html)
