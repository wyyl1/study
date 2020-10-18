# JMeter Docker 镜像

## JMeter 基础镜像

Dockerfile

```cmd
FROM openjdk:11-jdk-slim

# JMeter version
ARG JMETER_VERSION=5.3

# Install few utilities
RUN apt-get clean && \
    apt-get update && \
    apt-get -qy install \
                wget \
                telnet \
                iputils-ping \
                unzip && \
      mkdir /jmeter && \
      cd /jmeter/ && \
      wget https://apachemirror.sg.wuchna.com/jmeter/binaries/apache-jmeter-$JMETER_VERSION.tgz && \
      tar -xzf apache-jmeter-$JMETER_VERSION.tgz && \
      rm apache-jmeter-$JMETER_VERSION.tgz

# Set JMeter Home
ENV JMETER_HOME /jmeter/apache-jmeter-$JMETER_VERSION/
# Add JMeter to the Path
ENV PATH $JMETER_HOME/bin:$PATH
```

```cmd
$ docker build -t aoeai/jmeter-5.3-base .
```

## JMeter Client 镜像
Dockerfile

```cmd
FROM aoeai/jmeter-5.3-base

EXPOSE 6000
```

```cmd
$ docker build -t aoeai/jmeter-5.3-client .
```

## JMeter Server 镜像
Dockerfile

```cmd
FROM aoeai/jmeter-5.3-base

# Ports to be exposed from the container for JMeter Slaves/Server
# 从JMeter Slaves/Server的容器中要公开的端口
EXPOSE 1099 50000

# Application to run on starting the container
# 启动容器 运行应用程序
ENTRYPOINT $JMETER_HOME/bin/jmeter-server \
                        -Dserver.rmi.localport=5000 \
                        -Dserver_port=1099
```

```cmd
$ docker build -t aoeai/jmeter-5.3-server .

$ docker run --name jmeter-server-0 -p 8000:1099 aoeai/jmeter-5.3-server /bin/bash

$ docker run --name jmeter-server-1 aoeai/jmeter-5.3-server /bin/bash
```

- -p参数：容器的 1099 端口映射到本机的 8000 端口。
- -it参数：容器的 Shell 映射到当前的 Shell，然后你在本机窗口输入的命令，就会传入容器。
- aoeai/jmeter-5.3-server：image 文件的名字（如果有标签，还需要提供标签，默认是 latest 标签）。
- /bin/bash：容器启动以后，内部第一个执行的命令。这里是启动 Bash，保证用户可以使用 Shell。

```cmd
docker inspect --format '{{ .Name }} => {{ .NetworkSettings.IPAddress }}' $(docker ps -a -q)
```

## 参考资料

- [Make Use of Docker with JMeter - Learn How](https://www.blazemeter.com/blog/make-use-of-docker-with-jmeter-learn-how)
- [JMeter：使用Docker进行分布式负载测试](https://blog.csdn.net/zbj18314469395/article/details/104566755)

