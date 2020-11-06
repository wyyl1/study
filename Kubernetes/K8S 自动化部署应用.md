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

### [从私有仓库拉取镜像](https://kubernetes.io/zh/docs/tasks/configure-pod-container/pull-image-private-registry/)

- [Secret](https://kubernetes.io/zh/docs/concepts/configuration/secret/) 配置
- [kubectl create secret docker-registry](https://jamesdefabia.github.io/docs/user-guide/kubectl/kubectl_create_secret_docker-registry/)

创建一个名字为 aliyun-secret 的 Secret

```cmd
kubectl create secret docker-registry aliyun-secret \
  --docker-server=仓库地址 \
  --docker-username=用户名 \
  --docker-password=密码 \
  --docker-email=邮箱 \
  --namespace=命名空间
```

查看 Secret 的描述

```cmd
$ kubectl delete secret aliyun-secret

$ kubectl get secret aliyun-secret --output=yaml

$ kubectl describe secrets/aliyun-secret

$ kubectl get secret aliyun-secret --output="jsonpath={.data.\.dockerconfigjson}" | base64 --decode
```

### K8S [配置最佳实践](https://kubernetes.io/zh/docs/concepts/configuration/overview/)（官方）

