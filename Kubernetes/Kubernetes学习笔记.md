# Kubernetes学习笔记

## 屹立不倒
- 只要宿主机不倒，Pod中的应用就能不倒！

> 在 Kubernetes 中，你可以为 Pod 里的容器定义一个健康检查“探针”（Probe）。这样，kubelet 就会根据这个 Probe 的返回值决定这个容器的状态，而不是直接以容器进行是否运行（来自 Docker 返回的信息）作为依据。这种机制，是生产环境中保证应用健康存活的重要手段。

> Kubernetes 里的 Pod 恢复机制，也叫 restartPolicy。它是 Pod 的 Spec 部分的一个标准字段（pod.spec.restartPolicy），默认值是 Always，即：任何时候这个容器发生了异常，它一定会被重新创建。

> —《极客时间》[深入剖析Kubernetes | 15 深入解析Pod对象（二）](https://time.geekbang.org/column/article/40466?utm_source=app&utm_medium=geektime&utm_campaign=308-presell&utm_content=pcchaping&utm_term=pc_interstitial_261)

## 安装失败怎么办

### [使用阿里云镜像](https://developer.aliyun.com/mirror/kubernetes)

### [kubeadm 重置](https://k8smeetup.github.io/docs/reference/setup-tools/kubeadm/kubeadm-reset/)

```cmd
$ kubeadm reset
```

### 删除 pod

```cmd
$ kubectl delete -f xx.yaml
```

## [Kubernetes 文档](https://kubernetes.io/zh/docs/home/) (官方）

- Ubuntu 建议在root用户下安装，避免很多权限问题

### 部署 Kubernetes 的 Master 节点

- k8s.gcr.io 无法访问
 - 使用第三方镜像
 
>    在使用kubeadm工具初始化集群时，可以使用使用--image-repository参数接上第三方镜像地址，比如阿里云镜像是registry.cn-hangzhou.aliyuncs.com/google_containers
> ，微软云镜像为gcr.azk8s.cn/google_containers

- kudeadm 默认配置文件 $HOME/.kube/config

```cmd
kubeadm init --image-repository=registry.cn-hangzhou.aliyuncs.com/google_containers
```

- 报错 kubeadm running with swap on is not supported. Please disable swap

```cmd
swapoff -a
```

- 部署成功后有类似输出

```cmd
Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 192.168.0.107:6443 --token jmhyhr.i1fmkmxxiqztmzmk \
    --discovery-token-ca-cert-hash sha256:e616e1498502182c46883fed139e6091b15af4a6b4b838d3269a07cd0aa08af1
```

- 按 kubeadm 提示我们第一次使用 Kubernetes 集群所需要的配置命令：

```cmd
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

- 查看节点状态

```cmd
kubectl get nodes
```

- 列出所有命名空间下的所有容器

```
kubectl get pods --all-namespaces
```

- 在调试 Kubernetes 集群时，最重要的手段就是用 kubectl describe 来查看这个节点（Node）对象的详细信息、状态和事件（Event）

```cmd
kubectl describe node master
```

> 而需要这些配置命令的原因是：Kubernetes 集群默认需要加密方式访问。所以，这几条命令，就是将刚刚部署生成的 Kubernetes 集群的安全配置文件，保存到当前用户的.kube 目录下，kubectl 默认会使用这个目录下的授权信息访问 Kubernetes 集群。如果不这么做的话，我们每次都需要通过 export KUBECONFIG 环境变量告诉 kubectl 这个安全配置文件的位置。
 
- 跟随 11 | 从0到1：搭建一个完整的Kubernetes集群 继续

- 通过 kubectl 检查这个节点上各个系统 Pod 的状态，其中，kube-system 是 Kubernetes 项目预留的系统 Pod 的工作空间（Namepsace，注意它并不是 Linux Namespace，它只是 Kubernetes 划分不同工作空间的单位）：

```cmd
kubectl get pods -n kube-system
```

### 部署网络插件 Weave
- [Integrating Kubernetes via the Addon](https://www.weave.works/docs/net/latest/kubernetes/kube-addon/)

```cmd
kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"
```

- 列出所有命名空间下的所有容器

```cmd
kubectl get pods --all-namespaces
```

### 通过 Taint/Toleration 调整 Master 执行 Pod 的策略
- 为节点打上“污点”（Taint）的命令是

```cmd
$ kubectl taint nodes node1 foo=bar:NoSchedule
```

- Pod 声明 Toleration，只要在 Pod 的.yaml 文件中的 spec 部分，加入 tolerations 字段即可：

```yaml
apiVersion: v1
kind: Pod
...
spec:
  tolerations:
  - key: "foo"
    operator: "Equal"
    value: "bar"
    effect: "NoSchedule"
```

- 如果你就是想要一个单节点的 Kubernetes，删除这个 Taint 才是正确的选择：

```cmd
$ kubectl taint nodes --all node-role.kubernetes.io/master-
```


### [安装 Minikube](https://kubernetes.io/zh/docs/tasks/tools/install-minikube/)

#### 确认安装
- 由于国内无法直接连接 k8s.gcr.io，推荐使用阿里云镜像仓库，在 minikube start 中添加 --image-repository 参数。

 ```cmd
 minikube start --vm-driver=docker --image-repository=registry.cn-hangzhou.aliyuncs.com/google_containers --memory=4096mb
 ```
 
- 查看Kubernetes pod状态出现ImagePullBackOff的原因
 
 ```cmd
 kubectl describe pod hello-minikube-5d9b964bfb-49v4s
 ```

- 查看 Dashboard

```cmd
kubectl get pods -n kubernetes-dashboard
```

- Rook 安装失败，检查

[配置文件地址](https://github.com/rook/rook/tree/release-1.4/cluster/examples/kubernetes/ceph)

```cmd
https://raw.githubusercontent.com/rook/rook/release-1.4/cluster/examples/kubernetes/ceph/common.yaml

https://raw.githubusercontent.com/rook/rook/release-1.4/cluster/examples/kubernetes/ceph/operator.yaml

https://raw.githubusercontent.com/rook/rook/release-1.4/cluster/examples/kubernetes/ceph/cluster.yaml
```

```yaml

apiVersion: v1
kind: Pod
...
spec:
  tolerations:
  - key: "foo"
    operator: "Equal"
    value: "bar"
    effect: "NoSchedule"
```

```cmd
$ kubectl get pods --all-namespaces
$ kubectl get pods -n rook-ceph
$ kubectl -n rook-ceph get events
$ kubectl describe pod -n rook-ceph rook-ceph-operator-785b8d45dc-d5c6m
```

- 错误信息
 
```cmd
LAST SEEN   TYPE      REASON              OBJECT                                     MESSAGE
42s         Warning   FailedScheduling    pod/rook-ceph-operator-5f5fd8bf77-gfjnc    0/1 nodes are available: 1 node(s) had taint {foo: bar}, that the pod didn't tolerate
```

- 解决方法

```cmd
# 删除刚才的pod
$ kubectl delete -f operator.yaml
```

- 安装 [CRI-O](https://kubernetes.io/docs/setup/production-environment/container-runtimes/) ：之前已经安装了一个，安装这个后会有冲突；安装过程非常慢

## 安装

- 阿里云维护了一个国内版的[Minikube](https://github.com/AliyunContainerService/minikube)



安装Docker后，将当前用户加入docker组，不然启动Minikube时会报没有权限

 ```cmd
 sudo gpasswd -a ${USER} docker
 ```
 退出后重新登录生效
 
 - [使用 Minikube 安装 Kubernetes](https://kubernetes.io/zh/docs/setup/learning-environment/minikube/)

- 停止本地 Minikube 集群

```cmd
minikube stop
```

```cmd
minikube start --driver=docker --image-mirror-country cn --memory=4096mb
```

### [安装 kubeadm](https://kubernetes.io/zh/docs/setup/production-environment/tools/kubeadm/install-kubeadm/)
> Kubernetes 一键部署利器
> [Kubeadm 中文介绍](https://kubernetes.io/zh/docs/reference/setup-tools/kubeadm/)

- 谷歌镜像

```cmd
$ sudo apt-get update && sudo apt-get install -y apt-transport-https curl
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
cat <<EOF | sudo tee /etc/apt/sources.list.d/kubernetes.list
deb https://apt.kubernetes.io/ kubernetes-xenial main
EOF
```


- 阿里云镜像

```cmd
http://mirrors.aliyun.com/kubernetes/apt
```

- 中科大镜像

```cmd
http://mirrors.ustc.edu.cn/kubernetes/apt
```

```cmd
$ sudo apt-get update && sudo apt-get install -y apt-transport-https curl
curl -s http://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | sudo apt-key add -
cat <<EOF | sudo tee /etc/apt/sources.list.d/kubernetes.list
deb http://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main
EOF

$ sudo apt-get update
$ sudo apt-get install -y kubelet kubeadm kubectl
$ sudo apt-mark hold kubelet kubeadm kubectl
```

- 检查镜像地址 /etc/apt/sources.list.d/kubernetes.list

```cmd
$ sudo apt-get update
$ sudo apt-get install -y docker.io kubeadm
```

### [启动引导一个 Kubernetes 主节点](https://kubernetes.io/docs/reference/setup-tools/kubeadm/kubeadm-init/)

- kubeadm.yaml (给 kubeadm 用的 YAML 文件)
- 可以使用 kubeadm config print 命令打印出默认配置 [链接](https://kubernetes.io/zh/docs/reference/setup-tools/kubeadm/kubeadm-init/)

```yaml
apiVersion: kubeadm.k8s.io/v1alpha1
kind: MasterConfiguration
controllerManagerExtraArgs:
  # 将来部署的 kube-controller-manager 能够使用自定义资源（Custom Metrics）进行自动水平扩展
  horizontal-pod-autoscaler-use-rest-clients: "true"
  horizontal-pod-autoscaler-sync-period: "10s"
  node-monitor-grace-period: "10s"
apiServerExtraArgs:
  runtime-config: "api/all=true"
# kubeadm 帮我们部署的 Kubernetes 版本号  
# 匹配相应版本 kubectl version  
# client对应kubectl，server对应Master上的k8s版本
kubernetesVersion: "stable-1.19.1"
```

- 查看kubeadm版本

```cmd
kubeadm version
```

- 查看可用的config中可用的属性

```cmd
kubectl get cm -o yaml -n kube-system kubeadm-config
```

```cmd
kubeadm init --config kubeadm.yaml
```

```cmd
sudo apt remove kubelet kubectl kubeadm
sudo apt install kubelet=1.19.1-00 -y
sudo apt install kubectl=1.19.1-00 -y
sudo apt install kubeadm=1.19.1-00 -y
```


- 查看配置模板 [kubeadm config](https://kubernetes.io/docs/reference/setup-tools/kubeadm/kubeadm-config/)

```cmd
kubeadm config print init-defaults [flags]
```


