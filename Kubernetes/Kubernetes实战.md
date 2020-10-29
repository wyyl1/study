
# Kubernetes 实战

## 安装 Minikube

- [阿里云 Minikube](https://github.com/AliyunContainerService/minikube)
- [Hello Minikube](https://kubernetes.io/docs/tutorials/hello-minikube/)

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

## App

### aoeai-3w-tps-test.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: aoeai-3w-tps-test
spec:
  selector:
    matchLabels:
      app: aoeai-3w-tps-test
  replicas: 1
  template:
    metadata:
      labels:
        app: aoeai-3w-tps-test
    spec:
      tolerations:
      - key: "foo"
        operator: "Equal"
        value: "bar"
        effect: "NoSchedule"
      containers:
      - name: aoeai-3w-tps-test
        image: registry.cn-shenzhen.aliyuncs.com/aoeai/test:0.0.2
        ports:
        - containerPort: 8080
```

### service.yaml

```yaml
apiVersion: v1
kind: Service
metadata:
  name: aoeai-3w-tps-test-nodeport
  labels:
    app: aoeai-3w-tps-test
spec:
  # 暴露到本机 IP
  type: NodePort
  selector:
    app: aoeai-3w-tps-test
  ports:
  - name: foo
    port: 80
    targetPort: 8080
    # 暴露到本机 IP
    # 可选字段
    # 默认情况下，为了方便起见，Kubernetes 控制平面会从某个范围内分配一个端口号（默认：30000-32767）
    nodePort: 30100
```

```cmd
$ kubectl apply -f aoeai-3w-tps-test.yaml -f service.yaml

$ minikube service aoeai-3w-tps-test-nodeport
```

### InfluxDB
- 使用 v1.8 （2.0 太超前，目前大部分解决方案都是 1.X 的）
- [v1.8 文档](https://docs.influxdata.com/influxdb/v1.8/)
- 参考 v2.0 安装 [Install InfluxDB in a Kubernetes cluster](https://docs.influxdata.com/influxdb/v2.0/get-started/#install-influxdb-in-a-kubernetes-cluster)
- [influxdb Docker 镜像](https://hub.docker.com/_/influxdb)

influxdb.yaml

```yaml
---
apiVersion: v1
kind: Namespace
metadata:
    name: influxdb
---
apiVersion: apps/v1
kind: StatefulSet
metadata:
    labels:
        app: influxdb
    name: influxdb
    namespace: influxdb
spec:
    replicas: 1
    selector:
        matchLabels:
            app: influxdb
    serviceName: influxdb
    template:
        metadata:
            labels:
                app: influxdb
        spec:
            containers:
              - image: influxdb:1.8.3-alpine
                name: influxdb
                ports:
                  - containerPort: 8086
                    name: influxdb
                volumeMounts:
                  - mountPath: /root/.influxdbv2
                    name: data
    volumeClaimTemplates:
      - metadata:
            name: data
            namespace: influxdb
        spec:
            accessModes:
              - ReadWriteOnce
            resources:
                requests:
                    storage: 10G
---
apiVersion: v1
kind: Service
metadata:
    name: influxdb
    namespace: influxdb
spec:
    ports:
      - name: influxdb
        port: 8086
        targetPort: 8086
    selector:
        app: influxdb
    type: ClusterIP
```

```cmd
$ kubectl get pods -n influxdb
```

#### [Get started with InfluxDB](https://docs.influxdata.com/influxdb/v2.0/get-started/#start-with-influxdb-oss) 选 Kubernetes 的方法

```cmd
# 进入
$ kubectl exec -it influxdb-0 -n influxdb -- /bin/bash
# 设置数据库 root/root123456
$ influx setup

# 查看信息
$ influx config list
```

```cmd
$ minikube service influxdb -n influxdb

# 将端口8086从群集内部转发到localhost
$ kubectl port-forward svc/influxdb -n influxdb 8086:8086 &
```

#### K8S 中服务地址

http://influxdb.influxdb:8086

### Grafana

- [Deploying Grafana to Kubernetes](https://www.metricfire.com/blog/deploying-grafana-to-kubernetes/)

#### grafana.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: grafana
  name: grafana
spec:
  replicas: 1
  selector:
    matchLabels:
      app: grafana
  template:
    metadata:
      labels:
        app: grafana
    spec:
      containers:
      - image: grafana/grafana:5.4.3
        name: grafana
        ports:
        - containerPort: 3000
          name: http
        volumeMounts:
          - name: grafana-storage
            mountPath: /var/lib/grafana
      volumes:
        - name: grafana-storage
          persistentVolumeClaim:
            claimName: grafana-storage
      securityContext:
        runAsNonRoot: true
        runAsUser: 65534
        fsGroup: 472
```

#### pvc.yaml

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: grafana-storage
spec:
  accessModes:
    - ReadWriteOnce
  volumeMode: Filesystem
  resources:
    requests:
      storage: 1Gi
```

#### service.yaml

```yaml
apiVersion: v1
kind: Service
metadata:
  name: grafana
  labels:
    app: grafana
spec:
  type: NodePort
  ports:
  - port: 3000
    targetPort: 3000
    protocol: TCP
    targetPort: http
    nodePort: 30200
  selector:
    app: grafana
```

```cmd
$ kubectl apply -f grafana.yaml -f pvc.yaml -f service.yaml

$ kubectl exec -it grafana-96fd979c-kjfhr grafana-cli plugins install grafana-kubernetes-app

$ minikube service grafana

# 将 127.0.0.1:3000 映射到3000 端口上 minikube ip 上的 Grafana 服务
$ kubectl port-forward svc/grafana 3000:3000 &
```

#### 插件

- [Apache JMeter Dashboard using Core InfluxdbBackendListenerClient](https://grafana.com/grafana/dashboards/5496)
- JMeter dashboard，我们常用的 dashboard 是 Grafana 官方 ID 为 5496 的模板。数据来源：JMeter 中 Backend Listener

### [Prometheus.io](https://prometheus.io/)

- [官方文档](https://prometheus.io/docs/introduction/overview/)
- [官方安装说明](https://prometheus.io/docs/prometheus/latest/installation/)
- [Docker 镜像](https://hub.docker.com/r/prom/prometheus/tags)

#### prometheus.yaml 最简单版本

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: prometheus
  name: prometheus
spec:
  replicas: 1
  selector:
    matchLabels:
      app: prometheus
  template:
    metadata:
      labels:
        app: prometheus
    spec:
      containers:
      - image: prom/prometheus:v2.22.0
        name: prometheus
        ports:
        - containerPort: 9090
          name: http
        volumeMounts:
          - name: prometheus-storage
            mountPath: /var/lib/prometheus
      volumes:
        - name: prometheus-storage
          persistentVolumeClaim:
            claimName: prometheus-storage
      securityContext:
        runAsNonRoot: true
        runAsUser: 65534
        fsGroup: 472
```

#### prometheus.yaml node_exporter 版本

##### 1. 利用 prometheus.yml 生成 ConfigMap

prometheus.yml

```yaml
global:
  scrape_interval: 15s
  scrape_timeout: 10s
  evaluation_interval: 15s
alerting:
  alertmanagers:
  - scheme: http
    timeout: 10s
    api_version: v1
    static_configs:
    - targets: []
scrape_configs:
- job_name: prometheus
  honor_timestamps: true
  scrape_interval: 15s
  scrape_timeout: 10s
  metrics_path: /metrics
  scheme: http
  static_configs:
  - targets:
    - localhost:9090
- job_name: i7
  honor_timestamps: true
  scrape_interval: 15s
  scrape_timeout: 10s
  metrics_path: /metrics
  scheme: http
  static_configs:
  - targets:
    - 192.168.0.109:9200
```

```cmd
# 从 prometheus.yml 文件创建 ConfigMap
$ kubectl create configmap prometheus-config --from-file=prometheus.yml

# 查看这个ConfigMap里保存的信息(data)
# kubectl get -o yaml 这样的参数，会将指定的 Pod API 对象以 YAML 的方式展示出来
$ kubectl get configmaps prometheus-config -o yaml
```

prometheus.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: prometheus
  name: prometheus
spec:
  replicas: 1
  selector:
    matchLabels:
      app: prometheus
  template:
    metadata:
      labels:
        app: prometheus
    spec:
      containers:
      - image: prom/prometheus:v2.22.0
        name: prometheus
        args:
        - "--config.file=/etc/prometheus/prometheus.yml"
        - "--storage.tsdb.path=/prometheus"
        ports:
        - containerPort: 9090
          name: http
        volumeMounts:
          - name: prometheus-storage
            mountPath: /prometheus
          - name: config-volume
            mountPath: /etc/prometheus
      volumes:
        - name: prometheus-storage
          persistentVolumeClaim:
            claimName: prometheus-storage
        - configMap:
            name: prometheus-config
          name: config-volume
      securityContext:
        runAsNonRoot: true
        runAsUser: 65534
        fsGroup: 472
```

#### pvc.yaml

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: prometheus-storage
spec:
  accessModes:
    - ReadWriteOnce
  volumeMode: Filesystem
  resources:
    requests:
      storage: 1Gi
```

#### service.yaml

```yaml
apiVersion: v1
kind: Service
metadata:
  name: prometheus
  labels:
    app: prometheus
spec:
  type: NodePort
  ports:
  - port: 9090
    targetPort: 9090
    protocol: TCP
    targetPort: http
    nodePort: 30201
  selector:
    app: prometheus
```

```cmd
$ kubectl apply -f prometheus.yaml -f pvc.yaml -f service.yaml

$ minikube service prometheus

# 将端口8086从群集内部转发到localhost
$ kubectl port-forward svc/prometheus 9090:9090 &
```

- [参考使用 ConfigMap 动态加载配置文件](https://www.qikqiak.com/k8strain/monitor/prometheus/)
- [k8s 监控（一）安装 Prometheus](https://juejin.im/post/6844903908251451406)

### Telegraf
- [Telegraf 1.16 documentation](https://docs.influxdata.com/telegraf/v1.16/)
- [Docker 镜像](https://hub.docker.com/_/telegraf/?tab=description)
- [源码](https://github.com/influxdata/telegraf)

## Ubuntu 20.04 

### [node_exporter](https://github.com/prometheus/node_exporter)
- [node_exporter-1.0.1.linux-amd64.tar.gz](https://github.com/prometheus/node_exporter/releases/download/v1.0.1/node_exporter-1.0.1.linux-amd64.tar.gz)

```cmd
$ wget https://github.com/prometheus/node_exporter/releases/download/v1.0.1/node_exporter-1.0.1.linux-amd64.tar.gz

$ tar -xvzf node_exporter-1.0.1.linux-amd64.tar.gz

$ ./node_exporter --web.listen-address=:9200 &
```



