# Docker 入门

## Dockerfile

```Dockerfile
FROM openjdk:11-jdk-slim
RUN addgroup -S spring && useradd -S spring -G spring
USER spring:spring
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

- 从尺寸上来讲，alpine最小、slim稍大、默认的最大

Container内的程序以非root用户启动
在Docker Image内部，我们应该使用非root用户启动程序，这需要新建用户。

如果你用的是 **openjdk:<version>-alpine** 新建用户命令是这样的：

```cmd
RUN set -eux; \
    addgroup --gid 1000 java-app; \
    adduser -S -u 1000 -g java-app -h /home/java-app/ -s /bin/sh -D java-app;
```

如果你用的是 **openjdk:<version>-slim** 或者 **openjdk:<version>** 新建用户命令是这样的：

```cmd
RUN set -eux; \
    addgroup --gid 1000 java-app; \
    adduser --system --uid 1000 --gid 1000 --home=/home/java-app/ --shell=/bin/sh --disabled-password java-app;
```

然后使用Dockerfile USER指令

```cmd
USER java-app
```
后面的指令就都是以java-app用户身份执行了

## 构建 启动

```cmd
$ docker build -t aoeai/spring-boot-docker:0.0.1-test .

# docker run -p 本地主机端口:容器端口 aoeai/spring-boot-docker:0.0.1-test
$ docker run -p 8080:8080 aoeai/spring-boot-docker:0.0.1-test

# 查看正在运行的容器
$ docker ps
```

--tag, -t: 镜像的名字及标签，通常 name:tag 或者 name 格式；可以在一次构建中为一个镜像设置多个标签。

## 上传镜像到hub仓库

```cmd
$ docker push aoeai/spring-boot-docker:0.0.1-test
```

## 删除

先删除容器

```cmd
$ docker ps -a
$ docker rm <CONTAINER ID>
```

删除镜像

```cmd
$ docker images
$ docker rmi <REPOSITORY>
```

## 参考资料
- [Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/)
- [Java程序制作Docker Image推荐方案](https://segmentfault.com/a/1190000016449865)
- [Docker build 命令](https://www.runoob.com/docker/docker-build-command.html)

