# Docker入门-学习笔记-阮一峰的网络日志

- [Docker 微服务教程](http://www.ruanyifeng.com/blog/2018/02/docker-wordpress-tutorial.html)

**查看docker镜像**

```cmd
docker info|grep Mirrors -A 1
```

**仓库网址改成国内的镜像站**
> ⚠️ 如果您使用“/etc/docker/daemon.json”修改镜像源，请忽略此步骤

打开/etc/default/docker文件（需要sudo权限），在文件的底部加上一行。

```cmd
DOCKER_OPTS="--registry-mirror=https://registry.docker-cn.com"
```

然后，重启 Docker 服务。

```cmd
$ sudo service docker restart
```

## 实例：hello world

原文：[Docker 入门教程](https://www.ruanyifeng.com/blog/2018/02/docker-tutorial.html)

将 image 文件从仓库抓取到本地

```cmd
$ docker image pull library/hello-world
```

- **docker image pull**是抓取 image 文件的命令。
- **library/hello-world**是 image 文件在仓库里面的位置
    - **library**是 image 文件所在的**组**
    - **hello-world**是 image **文件的名字**。

由于 Docker 官方提供的 image 文件，都放在**library**组里面，所以它的是默认组，可以省略。因此，上面的命令可以写成下面这样。

```cmd
$ docker image pull hello-world
```

抓取成功以后，就可以在本机看到这个 image 文件了

```cmd
$ docker image ls
```

运行这个 image 文件

```cmd
$ docker container run hello-world
```

- **docker container run** 命令会从 image 文件，生成一个正在运行的容器实例
- **docker container run** 命令具有自动抓取 image 文件的功能。如果发现本地没有指定的 image 文件，就会从仓库自动抓取。因此，前面的**docker image pull**命令并不是必需的步骤。

输出这段提示以后，**hello world**就会停止运行，容器自动终止。

有些容器不会自动终止，因为提供的是服务。比如，安装运行 Ubuntu 的 image，就可以在命令行体验 Ubuntu 系统。参数为以命令行模式进入该容器

```cmd
$ docker container run -it ubuntu bash
```

- -i: 交互式操作。
- -t: 终端。
- ubuntu: ubuntu 镜像。
- bash：放在镜像名后的是命令，这里我们希望有个交互式 Shell，因此用的是 bash。

对于那些不会自动终止的容器，必须使用 **docker container kill** 命令手动终止。

```cmd
# 查看容器id docker ps -a | grep ubuntu
$ docker container kill [containID]
```

## 容器文件

**image 文件生成的容器实例，本身也是一个文件，称为容器文件**。也就是说，一旦容器生成，就会同时存在两个文件： image 文件和容器文件。而且关闭容器并不会删除容器文件，只是容器停止运行而已。

```cmd
# 列出本机正在运行的容器
$ docker container ls

# 列出本机所有容器，包括终止运行的容器
$ docker container ls --all
```

终止运行的容器文件，依然会占据硬盘空间，可以使用**docker container rm**命令删除

```cmd
$ docker container rm [containerID]
```

## Dockerfile 文件
它是一个文本文件，用来配置 image。Docker 根据 该文件生成二进制的 image 文件。



[一键部署 Spring Boot 到远程 Docker 容器，就是这么秀！](https://juejin.im/post/6844903927964499975)

[Java程序制作Docker Image推荐方案](https://segmentfault.com/a/1190000016449865)

[菜鸟教程 Docker Dockerfile](https://www.runoob.com/docker/docker-dockerfile.html)

[使用 Dockerfile 定制镜像](https://yeasy.gitbook.io/docker_practice/image/build)

[Dockerfile最佳实践](https://zhuanlan.zhihu.com/p/75013836)

