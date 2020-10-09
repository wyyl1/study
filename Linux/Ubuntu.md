# Ubuntu

- [Ubuntu更换阿里云软件源](https://developer.aliyun.com/article/704603)
- 选择源的版本 https://developer.aliyun.com/mirror/ubuntu

```cmd
# 备份
sudo cp /etc/apt/sources.list /etc/apt/sources.list.bak

# 修改
sudo vim /etc/apt/sources.list

# ubuntu 20.04(focal) 配置如下

deb http://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse

deb http://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse

deb http://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse

deb http://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse

deb http://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse

# 更新
sudo apt-get update
```

sudo apt-get update
sudo apt-get install openjdk-11-jdk
sudo apt-get install glances

## 网络配置

```vim
sudo cp /etc/sysctl.conf /etc/sysctl.conf.bak

sudo vim /etc/sysctl.conf

# 检查客户端的连接状态 单位:秒
# 减少检查状态的时间，提高连接的回收效率
net.ipv4.tcp_keepalive_time = 1200

# SYN队列的长度
# 加大队列长度，可以容纳更多等待连接的网络连接数
net.ipv4.tcp_max_syn_backlog = 8192

# 当出现SYN等待队列溢出时，可以开启该配置项
# 启用cookies来处理，可防范少量SYN攻击
# 0 关闭；1 开启
net.ipv4.tcp_syncookies = 1

# 当一个连接关闭时，TCP会通过四次握手来完成一次关闭连接操作，在请求量大的情况下，消费端会有大量TIME_WAIT状态的连接
# 由于该参数可以限制TIME_WAIT状态的连接数量，所以我们可以用减小参数的方式来应对。
# 如果TIME_WAIT的数量超过参数值，TIME_WAIT将会立刻被清除并打印警告信息
net.ipv4.tcp_max_tw_buckets = 65536

# 客户端每次连接服务端时，都会获得一个新的源端口以实现连接的唯一性。在TIME_WAIT状态的连接数量过大的情况下，会增加端口号的占用时间。
# 由于处于TIME_WAIT状态的连接属于关闭连接，所以新创建的连接可以复用该端口号
# 1 开启；0 关闭
net.ipv4.tcp_tw_reuse = 1

# 用于向外连接的端口范围（客户端连接服务端时，需要动态分配源端口号）
# 可以扩大该范围，默认范围32768 60999
net.ipv4.ip_local_port_range = 1025 65535

# 系统级别能够打开的文件句柄的数量
fs.file-max=2000000
```

- 运行 sudo sysctl -p 生效

- sudo vim /etc/security/limits.conf

```vim
# *表示针对所有的用户
* soft nofile 999999 
* hard nofile 999999
```

- 修改以后，需要重新登录才能生效
- ulimit -n

___

查看CPU信息
cat /proc/cpuinfo | grep name | cut -f2 -d: | uniq -c
____

ReactorNetty

安装JMeter 插件
https://jmeter-plugins.org/install/Install/

修改Mac参数
[实现单台测试机6万websocket长连接](https://segmentfault.com/a/1190000017364433)


[Spring WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux)

百万 Go TCP 连接的思考: epoll方式减少资源占用
https://colobu.com/2019/02/23/1m-go-tcp-connection/

reactor-netty
https://github.com/reactor/reactor-netty

3.5.3. Using an Event Loop Group
ReactorNetty.java

https://projectreactor.io/docs/netty/release/reference/index.html#server-tcp-level-configurations-event-loop-group

/**
 * Default value whether the native transport (epoll, kqueue) will be preferred,
 * fallback it will be preferred when available
 */
public static final String NATIVE = "reactor.netty.native";

net.ipv4.tcp_keepalive_time = 1200
net.ipv4.tcp_max_syn_backlog = 8192
net.ipv4.tcp_syncookies = 1
net.ipv4.tcp_max_tw_buckets = 65536
net.ipv4.tcp_tw_reuse = 1
net.ipv4.ip_local_port_range = 1025 65535
fs.file-max = 2000000



