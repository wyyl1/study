# Kubernetes学习笔记

## 屹立不倒
- 只要宿主机不倒，Pod中的应用就能不倒！

> 在 Kubernetes 中，你可以为 Pod 里的容器定义一个健康检查“探针”（Probe）。这样，kubelet 就会根据这个 Probe 的返回值决定这个容器的状态，而不是直接以容器进行是否运行（来自 Docker 返回的信息）作为依据。这种机制，是生产环境中保证应用健康存活的重要手段。

> Kubernetes 里的 Pod 恢复机制，也叫 restartPolicy。它是 Pod 的 Spec 部分的一个标准字段（pod.spec.restartPolicy），默认值是 Always，即：任何时候这个容器发生了异常，它一定会被重新创建。

> —《极客时间》[深入剖析Kubernetes | 15 深入解析Pod对象（二）](https://time.geekbang.org/column/article/40466?utm_source=app&utm_medium=geektime&utm_campaign=308-presell&utm_content=pcchaping&utm_term=pc_interstitial_261)

