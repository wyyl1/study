# K8S 集群部署

## 前置条件
- 禁用虚拟内存

## 1. [Install Docker Engine on Ubuntu](https://docs.docker.com/engine/install/ubuntu/)

### 1.1 文档中的最后一步，选择版本

```cmd
$ apt-get install docker-ce=5:19.03.13~3-0~ubuntu-focal docker-ce-cli=5:19.03.13~3-0~ubuntu-focal containerd.io -y
```

### 1.2 验证安装成功

```cmd
$ docker run hello-world
```

### 1.3 [添加 Docker 阿里云镜像加速](https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors)

```cmd
$ mkdir -p /etc/docker

$ tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://rmpv334g.mirror.aliyuncs.com"]
}
EOF

$ systemctl daemon-reload

$ systemctl restart docker
```

### 1.4 仓库网址改成国内的镜像站
打开/etc/default/docker文件（需要sudo权限），在文件的底部加上一行。

```cmd
DOCKER_OPTS="--registry-mirror=https://registry.docker-cn.com"
```

然后，重启 Docker 服务。

```cmd
$ sudo service docker restart
```

## 2. [安装 kubeadm](https://kubernetes.io/zh/docs/setup/production-environment/tools/kubeadm/install-kubeadm/#before-you-begin)

设置国内镜像 ustc的mirror

```cmd
cat <<EOF > /etc/apt/sources.list.d/kubernetes.list
deb http://mirrors.ustc.edu.cn/kubernetes/apt kubernetes-xenial main
EOF
```

安装 Key

```cmd
$ git clone https://gitee.com/wyyl1/download.git
$ cd download/key/google/
$ apt-key add apt-key.gpg
```

```cmd
$ apt-get update && sudo apt-get install -y apt-transport-https curl
$ apt-get install -y kubelet kubeadm kubectl
$ apt-mark hold kubelet kubeadm kubectl
```

### Master 节点
```cmd
$ kubeadm init --image-repository=registry.cn-hangzhou.aliyuncs.com/google_containers

初始化……

Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 172.16.141.208:6443 --token oqodbp.fqgnp8yqm0guil2j \
    --discovery-token-ca-cert-hash sha256:d3788f5268fb8483cbb71babd252b40a14fb6c3c5b3fd9bfb660d20dfd9871de
```

- 成功后根据提示（To start using your cluster, you need to run the following as a regular user:）执行初始化命令

#### 部署网络插件 Weave

```cmd
$ kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"
```

#### 确认状态为 Ready

```cmd
$ kubectl get nodes
```

### Work 节点

```cmd
kubeadm join 172.16.141.208:6443 --token oqodbp.fqgnp8yqm0guil2j \
    --discovery-token-ca-cert-hash sha256:d3788f5268fb8483cbb71babd252b40a14fb6c3c5b3fd9bfb660d20dfd9871de
```

### ~~疏通网络(放弃)~~

- [Configuring ss5 (SOCKS5) proxy server under CentOS](https://www.programmersought.com/article/52851342363/)
- 中文版 https://www.cnblogs.com/you-men/p/13475227.html

```
$ service ss5 start
$ netstat -an | grep 10809
$ service ss5 stop
```

