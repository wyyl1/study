# K8S 自动化部署应用

## 上传 Docker 镜像至仓库

### Jenkins 插件 
- 下载速度太慢请切换到清华大学下载源：https://mirror.tuna.tsinghua.edu.cn/jenkins/updates/update-center.json
- 搜索插件关键字：docker build and
- 选择插件 CloudBees Docker Build and Publish


/root/shell/issue-docker.sh /data/k8s/docker/water-k8s-test target/k8s-test-1.0.jar src/main/resources/Dockerfile starbuds/k8s-test:1.0.0

CentOS 系统 [解决 root 无法 su 到 jenkins 用户](https://blog.csdn.net/weixin_43819222/article/details/91038426)

报错

```cmd
Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock
```

让 jenkins 用户可以执行 docker 命令

```cmd
$ usermod -aG docker jenkins
$ chown -R jenkins /var/run/docker.sock
```

### Docker 插件

[CloudBees Docker Build and Publish](https://plugins.jenkins.io/docker-build-publish/)


