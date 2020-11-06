#!/bin/bash
# 发布 jar 包到 Docker 镜像
# 参数说明
# $1 准备打包 Docker 镜像的目录
# $2 jar 文件路径
# $3 Dockerfile 文件路径
# $4 K8S 配置文件

docker_dir=$1
jar_path=$2
dockerfile_path=$3
k8s_yaml_path=$4

echo $docker_dir
echo $jar_path
echo $dockerfile_path

rm -rf $docker_dir
mkdir $docker_dir
cp $jar_path $docker_dir
cp $dockerfile_path $docker_dir

/data/k8s/shell/push-file-to-master.expect \
    src/main/resources/k8s.yaml \
    /root/k8s/jenkins-cicd/water-k8s-test.yaml