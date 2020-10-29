# Kubernetes学习笔记

## 屹立不倒
- 只要宿主机不倒，Pod中的应用就能不倒！

> 在 Kubernetes 中，你可以为 Pod 里的容器定义一个健康检查“探针”（Probe）。这样，kubelet 就会根据这个 Probe 的返回值决定这个容器的状态，而不是直接以容器进行是否运行（来自 Docker 返回的信息）作为依据。这种机制，是生产环境中保证应用健康存活的重要手段。

> Kubernetes 里的 Pod 恢复机制，也叫 restartPolicy。它是 Pod 的 Spec 部分的一个标准字段（pod.spec.restartPolicy），默认值是 Always，即：任何时候这个容器发生了异常，它一定会被重新创建。

> —《极客时间》[深入剖析Kubernetes | 15 深入解析Pod对象（二）](https://time.geekbang.org/column/article/40466?utm_source=app&utm_medium=geektime&utm_campaign=308-presell&utm_content=pcchaping&utm_term=pc_interstitial_261)

## 安装失败怎么办

### 安装 kubeadm ([使用阿里云镜像](https://developer.aliyun.com/mirror/kubernetes)) 和 Docker

### [kubeadm 重置](https://k8smeetup.github.io/docs/reference/setup-tools/kubeadm/kubeadm-reset/)

```cmd
$ kubeadm reset
```

### 删除 pod

```cmd
$ kubectl delete -f xx.yaml
```

## 安装 Docker、Kubernetes

```cmd
$ sudo apt-get update
$ sudo apt-get install -y docker.io kubeadm
```

## 运行 Kubernetes

- Ubuntu 建议在root用户下安装，避免很多权限问题

- k8s.gcr.io 无法访问
 - 使用第三方镜像
 
>    在使用kubeadm工具初始化集群时，可以使用使用--image-repository参数接上第三方镜像地址，比如阿里云镜像是registry.cn-hangzhou.aliyuncs.com/google_containers
> ，微软云镜像为gcr.azk8s.cn/google_containers

- kudeadm 默认配置文件 $HOME/.kube/config

- kubeadm 初始化

```cmd
kubeadm init --image-repository=registry.cn-hangzhou.aliyuncs.com/google_containers
```

- 报错 kubeadm running with swap on is not supported. Please disable swap

```cmd
# 重启电脑后需要重新执行
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
- **Weave 安装成功后 coredns 才能启动成功**
- [Integrating Kubernetes via the Addon](https://www.weave.works/docs/net/latest/kubernetes/kube-addon/)

```cmd
kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"
```

- 列出所有命名空间下的所有容器

```cmd
kubectl get pods --all-namespaces
```

### wwww安装 [container-runtimes](https://kubernetes.io/docs/setup/production-environment/container-runtimes/)

### Ceph

```cmd
$ kubectl apply -f https://raw.githubusercontent.com/rook/rook/master/cluster/examples/kubernetes/ceph/common.yaml

$ kubectl apply -f https://raw.githubusercontent.com/rook/rook/master/cluster/examples/kubernetes/ceph/operator.yaml

$ kubectl apply -f https://raw.githubusercontent.com/rook/rook/master/cluster/examples/kubernetes/ceph/cluster.yaml
```

### 通过 Taint/Toleration 调整 Master 执行 Pod 的策略（建议用2台机器，省去很多麻烦）
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
# 标记 master 节点允许调度 pod
$ kubectl taint nodes --all node-role.kubernetes.io/master-
```


### [安装 Minikube](https://github.com/AliyunContainerService/minikube)

```cmd
$ curl -Lo minikube https://kubernetes.oss-cn-hangzhou.aliyuncs.com/minikube/releases/v1.13.0/minikube-linux-amd64 && chmod +x minikube && sudo mv minikube /usr/local/bin/
```

[阿里云镜像加速](https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors)

```cmd
# 启动
$ minikube start

$ minikube start --cpus=4 --memory=4096mb
$ minikube start --cpus=4 --memory=6100mb --registry-mirror=https://rmpv334g.mirror.aliyuncs.com

$ minikube stop
$ minikube delete
```

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

```cmd
$ kubeadm init --config kubeadm.yaml
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

## 常用命令

- kubectl get 指令的作用，就是从 Kubernetes 里面获取（GET）指定的 API 对象。可以看到，在这里我还加上了一个 -l 参数，即获取所有匹配 app: nginx 标签的 Pod。需要注意的是，在命令行中，所有 key-value 格式的参数，都使用“=”而非“:”表示。

```cmd
kubectl get pods -l app=nginx
```

- 使用 kubectl exec 指令，进入到这个 Pod 当中（即容器的 Namespace 中）查看这个 Volume 目录：

```cmd
$ kubectl exec -it nginx-deployment-5c678cfb6d-lg9lw -- /bin/bash
```

## Deployment 配置文件
nginx-deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  selector:
    matchLabels:
      app: nginx
  replicas: 2
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.19.2
        ports:
        - containerPort: 80
```

- **kind** 指定了这个 API 对象的类型（Type），是一个 Deployment
    - Deployment，是一个定义多副本应用（即多个副本 Pod）的对象
    - Deployment 还负责在 Pod 定义发生变化时，对每个副本进行滚动更新（Rolling Update）
- **spec.replicas** Pod 副本个数
- **spec.template** 定义了一个 Pod 模版，这个模版描述了想要创建的 Pod 的细节
- **spec.containers.image** 容器的镜像
- **containerPort** 容器监听端口
- **metadata** API 对象的“标识”，即元数据，它也是我们从 Kubernetes 里找到这个对象的主要依据。这其中最主要使用到的字段是 **Labels**。
- **Labels** 就是一组 key-value 格式的标签。而像 Deployment 这样的控制器对象，就可以通过这个 Labels 字段从 Kubernetes 中过滤出它所关心的被控制对象。
    - 比如，在上面这个 YAML 文件中，Deployment 会把所有正在运行的、携带“app: nginx”标签的 Pod 识别为被管理的对象，并确保这些 Pod 的总数严格等于两个。
- **spec.selector.matchLabels** 过滤规则的定义，我们一般称之为：Label Selector

```cmd
# 启动
$ kubectl apply -f nginx-deployment.yaml

# 查看运行状态
$ kubectl get pods -l app=nginx

# 查看一个 API 对象的细节
kubectl describe pod nginx-deployment-67594d6bf6-9gdvr

# 查看 Pod 的 ip 地址
$ kubectl get pod -o wide
```

spring-boot-app.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-boot-app-deployment
spec:
  selector:
    matchLabels:
      app: aoe-spring-boot-app
  replicas: 1
  template:
    metadata:
      labels:
        app: aoe-spring-boot-app
    spec:
      tolerations:
      - key: "foo"
        operator: "Equal"
        value: "bar"
        effect: "NoSchedule"
      containers:
      - name: aoe-spring-boot-app
        image: registry.cn-shenzhen.aliyuncs.com/aoeai/test:0.0.2
        ports:
        - containerPort: 8080
```

### 一个 Kubernetes 的 API 对象的定义，大多可以分为 Metadata 和 Spec 两个部分。

- **Metadata** 存放的是这个对象的元数据，对所有 API 对象来说，这一部分的字段和格式基本上是一样的；
- **Spec** 存放的，则是属于这个对象独有的定义，用来描述它所要表达的功能。

## Deployments

- [Deployments](https://kubernetes.io/zh/docs/concepts/workloads/controllers/deployment/) (官方文档)

## Service
- [服务](https://kubernetes.io/zh/docs/concepts/services-networking/service/) (官方文档)
- [使用 Service 连接到应用](https://kubernetes.io/zh/docs/concepts/services-networking/connect-applications-service/) (官方文档)
- [阿里云视频教程-Service](https://www.bilibili.com/video/BV1Kz411i788?p=14)

### 动机

Kubernetes Pod 是有生命周期的。 它们可以被创建，而且销毁之后不会再启动。 如果您使用 Deployment 来运行您的应用程序，则它可以动态创建和销毁 Pod。

每个 Pod 都有自己的 IP 地址，但是在 [Deployment](https://kubernetes.io/zh/docs/concepts/workloads/controllers/deployment/) 中，在同一时刻运行的 Pod 集合可能与稍后运行该应用程序的 Pod 集合不同。

这导致了一个问题： 如果一组 Pod（称为“后端”）为群集内的其他 Pod（称为“前端”）提供功能， 那么前端如何找出并跟踪要连接的 IP 地址，以便前端可以使用工作量的后端部分？

Kubernetes 之所以需要 Service，一方面是因为 Pod 的 IP 不是固定的，另一方面则是因为一组 Pod 实例之间总会有负载均衡的需求。

当你的宿主机上有大量 Pod 的时候，成百上千条 iptables 规则不断地被刷新，会大量占用该宿主机的 CPU 资源，甚至会让宿主机“卡”在这个过程中。所以说，**一直以来，基于 iptables 的 Service 实现，是制约 Kubernetes 项目承载更多量级的 Pod 的主要障碍。**

在大规模集群里，建议为 kube-proxy 设置 **–proxy-mode=ipvs** 来开启 [IPVS 代理模式](https://kubernetes.io/zh/docs/concepts/services-networking/service/#proxy-mode-ipvs)。它为 Kubernetes 集群规模带来的提升，还是非常巨大的。

> 我们不应该期望 Kubernetes Pod 是健壮的，而是要假设 Pod 中的容器很可能因为各种原因发生故障而死掉。Deployment 等 controller 会通过动态创建和销毁 Pod 来保证应用整体的健壮性。换句话说，Pod 是脆弱的，但应用是健壮的。

> — [k8s如何访问pod](https://www.cnblogs.com/ajunyu/p/11283772.html)

### 通过 Service 访问服务 (只能使用 K8S 中的 IP 访问)

service-app.yaml 使用集群内的 ip 访问 Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: domain-aoeai-test-app
  labels:
    app: aoeai-test-app
spec:
  selector:
    app: aoeai-test-app
  ports:
  - name: foo
    port: 80
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: aoeai-test-app
spec:
  selector:
    matchLabels:
      app: aoeai-test-app
  replicas: 1
  template:
    metadata:
      labels:
        app: aoeai-test-app
    spec:
      tolerations:
      - key: "foo"
        operator: "Equal"
        value: "bar"
        effect: "NoSchedule"
      containers:
      - name: aoeai-test-app
        image: registry.cn-shenzhen.aliyuncs.com/aoeai/test:0.0.2
        ports:
        - containerPort: 8080
```

```cmd
$ kubectl get services
```

### 通过 [NodePort](https://kubernetes.io/zh/docs/concepts/services-networking/service/#nodeport) 将 Service 暴露给外界

```yaml
apiVersion: v1
kind: Service
metadata:
  name: domain-aoeai-test-app
  labels:
    app: aoeai-test-app
spec:
  # 暴露到本机 IP
  type: NodePort
  selector:
    app: aoeai-test-app
  ports:
  - name: foo
    port: 80
    targetPort: 8080
    # 暴露到本机 IP
    # 可选字段
    # 默认情况下，为了方便起见，Kubernetes 控制平面会从某个范围内分配一个端口号（默认：30000-32767）
    nodePort: 30007
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: aoeai-test-app
spec:
  selector:
    matchLabels:
      app: aoeai-test-app
  replicas: 1
  template:
    metadata:
      labels:
        app: aoeai-test-app
    spec:
      tolerations:
      - key: "foo"
        operator: "Equal"
        value: "bar"
        effect: "NoSchedule"
      containers:
      - name: aoeai-test-app
        image: registry.cn-shenzhen.aliyuncs.com/aoeai/test:0.0.2
        ports:
        - containerPort: 8080
```

```cmd
$ curl localhost:30007
```

### Headless Service
- Headless Service 不需要分配一个 VIP，而是可以直接以 DNS 记录的方式解析出被代理 Pod 的 IP 地址。
    - clusterIP: None
- 它所代理的所有 Pod 的 IP 地址，都会被绑定一个这样格式的 DNS 记录，如下所示：

```cmd
<pod-name>.<svc-name>.<namespace>.svc.cluster.local
```

## [PV、PVC、StorageClass，这些到底在说啥](https://time.geekbang.org/column/article/42698)

用户创建的 PVC 要真正被容器使用起来，就必须先和某个符合条件的 PV 进行绑定。这里要检查的条件，包括两部分：

- 第一个条件，当然是 PV 和 PVC 的 spec 字段。比如，PV 的存储（storage）大小，就必须满足 PVC 的要求。

- 而第二个条件，则是 PV 和 PVC 的 **storageClassName 字段必须一样**。

### PersistentVolumeController
扮演撮合 PV 和 PVC 的“红娘”的角色

### StorageClass
- Kubernetes 为我们提供了一套可以自动创建 PV 的机制，即：Dynamic Provisioning。

- Dynamic Provisioning 机制工作的核心，在于一个名叫 StorageClass 的 API 对象
- **StorageClass 对象的作用，其实就是创建 PV 的模板**
- **StorageClass 并不是专门为了 Dynamic Provisioning 而设计的**
- 需要类似 ceph 的支持

## Ingress

- 所谓 Ingress 对象，其实就是 Kubernetes 项目对“反向代理”的一种抽象。
- 目前，业界常用的各种反向代理项目，比如 Nginx、HAProxy、Envoy、Traefik 等，都已经为 Kubernetes 专门维护了对应的 Ingress Controller。

## 常用命令

### [查看和查找资源](https://kubernetes.io/zh/docs/reference/kubectl/cheatsheet/#%E6%9F%A5%E7%9C%8B%E5%92%8C%E6%9F%A5%E6%89%BE%E8%B5%84%E6%BA%90)

```cmd
# get 命令的基本输出
kubectl get services                          # 列出当前命名空间下的所有 services
kubectl get pods --all-namespaces             # 列出所有命名空间下的全部的 Pods
kubectl get pods -o wide                      # 列出当前命名空间下的全部 Pods，并显示更详细的信息
kubectl get deployment my-dep                 # 列出某个特定的 Deployment
kubectl get pods                              # 列出当前命名空间下的全部 Pods
kubectl get pod my-pod -o yaml                # 获取一个 pod 的 YAML

# describe 命令的详细输出
kubectl describe nodes my-node
kubectl describe pods my-pod

# 列出当前名字空间下所有 Services，按名称排序
kubectl get services --sort-by=.metadata.name

# 列出 Pods，按重启次数排序
kubectl get pods --sort-by='.status.containerStatuses[0].restartCount'

# 列举所有 PV 持久卷，按容量排序
kubectl get pv --sort-by=.spec.capacity.storage

# 获取包含 app=cassandra 标签的所有 Pods 的 version 标签
kubectl get pods --selector=app=cassandra -o \
  jsonpath='{.items[*].metadata.labels.version}'

# 获取所有工作节点（使用选择器以排除标签名称为 'node-role.kubernetes.io/master' 的结果）
kubectl get node --selector='!node-role.kubernetes.io/master'

# 获取当前命名空间中正在运行的 Pods
kubectl get pods --field-selector=status.phase=Running

# 获取全部节点的 ExternalIP 地址
kubectl get nodes -o jsonpath='{.items[*].status.addresses[?(@.type=="ExternalIP")].address}'

# 列出属于某个特定 RC 的 Pods 的名称
# 在转换对于 jsonpath 过于复杂的场合，"jq" 命令很有用；可以在 https://stedolan.github.io/jq/ 找到它。
sel=${$(kubectl get rc my-rc --output=json | jq -j '.spec.selector | to_entries | .[] | "\(.key)=\(.value),"')%?}
echo $(kubectl get pods --selector=$sel --output=jsonpath={.items..metadata.name})

# 显示所有 Pods 的标签（或任何其他支持标签的 Kubernetes 对象）
kubectl get pods --show-labels

# 检查哪些节点处于就绪状态
JSONPATH='{range .items[*]}{@.metadata.name}:{range @.status.conditions[*]}{@.type}={@.status};{end}{end}' \
 && kubectl get nodes -o jsonpath="$JSONPATH" | grep "Ready=True"

# 列出被一个 Pod 使用的全部 Secret
kubectl get pods -o json | jq '.items[].spec.containers[].env[]?.valueFrom.secretKeyRef.name' | grep -v null | sort | uniq

# 列举所有 Pods 中初始化容器的容器 ID（containerID）
# Helpful when cleaning up stopped containers, while avoiding removal of initContainers.
kubectl get pods --all-namespaces -o jsonpath='{range .items[*].status.initContainerStatuses[*]}{.containerID}{"\n"}{end}' | cut -d/ -f3

# 列出事件（Events），按时间戳排序
kubectl get events --sort-by=.metadata.creationTimestamp

# 比较当前的集群状态和假定某清单被应用之后的集群状态
kubectl diff -f ./my-manifest.yaml
```

### [为容器设置启动时要执行的命令和参数](https://kubernetes.io/zh/docs/tasks/inject-data-application/define-command-argument-container/)

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: command-demo
  labels:
    purpose: demonstrate-command
spec:
  containers:
  - name: command-demo-container
    image: debian
    command: ["printenv"]
    args: ["HOSTNAME", "KUBERNETES_PORT"]
  restartPolicy: OnFailure
```

```yaml
command: ["/bin/sh"]
args: ["-c", "while true; do echo hello; sleep 10;done"]
```

## 问题

- pod 如何访问 Service
 - flannel
- [Kubernetes之路 3 - 解决服务依赖](https://developer.aliyun.com/article/573791)
- [Init 容器](https://kubernetes.io/zh/docs/concepts/workloads/pods/init-containers/) (官方文档)


```cmd
$ kubectl get svc

$ kubectl describe svc

$ kubectl describe service domain-aoeai-test-app
```

[获取正在运行容器的 Shell](https://kubernetes.io/zh/docs/tasks/debug-application-cluster/get-shell-running-container/)

```cmd
$ kubectl exec -it shell-demo -- /bin/bash
```

