# 鸟哥的Linux私房菜-基础篇学习笔记

## 第零章、计算机概论

### 容量单位 （进制转换）

0 / 1 这个二进制的单位称为 bit 。**但 bit 太小，在存储数据时每份简单的数据都会使用到 8 个 bits 的大小来记录**，因此定义出 byte 这个单位，他们的关系为：

1 Byte = 8 bits
1 K = 1024 byte
1 M = 1024 K


| 进制位 | Kilo | Mega | Giga | Tera | Peta | Exa | Zetta |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 二进制 | 1024 | 1024 K | 1024 M | 1024 G | 1024 T | 1024 P | 1024 E |
| 十进制 | 1000 | 1000 K | 1000 M | 1000 G | 1000 T | 1000 P | 1000 E |

文件容量使用的是二进制 1 GBytes = 1024 ✕ 1024 ✕ 1024 Bytes
速度单位使用十进制 1GHz = 1000 ✕ 1000 ✕ 1000 Hz

### CPU
**频率是 CPU 每秒钟可以进行的工作次数。** 频率越高表示这颗 CPU 单位时间内可以做更多的事情。
⚠️ 不同的 CPU 之间不能单纯的以频率来判断运算效率。因为每颗 CPU 的微指令集不同，架构也可能不同，可以使用的第二层快取及其计算器制可能也不同，加上每次频率能够进行的工作指令数也不同！所以，频率目前仅能用来比较同款 CPU 的速度！

## 第一章、Linux 是什么与如何学习

- [GNU 计划](http://www.gnu.org/)
- GPL 授权
- [Linux 内核](https://www.kernel.org/)

### GPL 优点
- 软件安全性交佳
- 软件执行效能交佳
- 软件除错时间较短
- 贡献的原始代码永远都存在

## 第二章、首次登入与在线求助

显示目前支持的语系

```cmd
$ locale
```

显示日期

```cmd
$ date +%Y/%m/%d
```

显示日历

```cmd
$ cal
$ cal 2015
```

计算器

```
# quit 退出
$ bc
```

### 4.2.3 重要的几个热键

#### [Tab]
- 命令补全
- 文件补齐
- 若安装 bash-completion 软件，则在某些指令后面使用 **[Tab]** 按键时，可以进行 【选项/参数的补齐】功能

#### [Ctrl]-d 按键
- 通常代表：键盘输入结束（End of file, EOF, End of input)
- 也可以用来取代 exit ：直接离开文字接口

#### [shift] + [PageUp] | [PageDown]
- 翻页

## 第五章、Linux 的文件权限与目录配置

- 文件可存取的身份
    - owner 文件拥有者
    - group 群组（每个账号可以有多个群组）
    - others 其他人

- 每种身份的权限
    - read
    - write
    - execute

### Linux 用户身份与群组记录的文件
- /etc/passwd 记录所有的账户信息
- /etc/shadow 记录个人的密码
- /etc/group 记录所有的组

### 5.2 Linux 文件权限概念

```cmd
$ ls -al
drwxr-xr-x 16 aoe  aoe     4096 Nov 23 11:41 .
drwxr-xr-x  3 root root    4096 Sep 12 17:34 ..
drwxrwxr-x  2 aoe  aoe     4096 Sep 17 13:37 apps
-rw-------  1 aoe  aoe    38063 Nov 23 12:23 .bash_history
-rw-r--r--  1 aoe  aoe      220 Feb 25  2020 .bash_logout
-rw-r--r--  1 aoe  aoe     3771 Sep 14 08:39 .bashrc
[  权限 ][连接][拥有者][群组][文件容量][修改日期][文件名]
```

-rwxr-xr-x


| - | r | w | x | r | - | x | r | - | x |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 文件类型 | 可读 | 可写 | 可执行 |  | 无权限 |  |  |  |  |
|  | 文件拥有者 | 文件拥有者 | 文件拥有者 | 群组 | 群组 | 群组 | 其他人 | 其他人 |  其他人|

- rwx 位置不变
- 第一个字符代表这个文件属性
    - [d] directory 目录
    - [-] 文件
    - [l] link 连接档（link file）
    - [b] block 区块设备档，可存储接口设备；通常集中在 /dev 目录下
    - [c] character 字符设备文件，串行端口设备，例如鼠标、键盘；通常集中在 /dev 目录下
    - [s] sockets 资料接口文件，用于网络通信；通常在 /run 或 /tmp
    - [p] FIFO,pipe 主要解决多个程序同时存取一个文件造成的错误问题
- 文件容量默认单位 bytes

显示完整时间

```cmd
$ ls -l --full-time
```

### 5.2.2 如何改变文件属性与权限

- **chgrp** 改变文件所属群组
- **chown** 改变文件拥有者
- **chmod** 改变文件的权限，SUID，SGID，SBIT 等等

#### chgrp （change group 的缩写）
变更范围 **/etc/group** 中的数据

```cmd
# -R 进行递归的持续变更，常用于变更某一目录内所有文件
$ chgrp -R dirname/filename

$ chgrp users test.cfg
```
 
#### chown （change owner 的缩写）
变更范围 **/etc/passwd** 中的数据

```cmd
# -R 进行递归的持续变更，常用于变更某一目录内所有文件
$ chown -R 账号名称 文件或目录
$ chown -R 账号名称:组名 文件或目录
```

#### chmod 改变文件的权限
- 数字类型
    - r : 4
    - w : 2
    - x : 1

```cmd
# -R 进行递归的持续变更，常用于变更某一目录内所有文件
$ chmod -R 属性累加值 文件或目录

$ chmod 777 .bas
```

- 符号类型
    - u user
    - g group
    - o other
    - a all


| chmod | u<br>g<br>o<br>a | + 加入<br>- 除去<br>= 设定 | r<br>w<br>x | 文件或目录 |
| --- | --- | --- | --- | --- |
 
```cmd
# -rwxr-xr-x 
# u=rwx,go=rx 中间没有空格
$ chmod u=rwx,go=rx .bashrc

# 给所有人加写入权限
$ chmod a+w .bashrc

# 拿回所有人的可执行权限
$ chmod a-x .bashrc
```

**通常要开放的目录至少具备 rx 权限**

### Linux 文件扩展名

- *.sh 脚本或批处理文件，使用 shell 写成
- *Z, *.tar, *.tar.gz, *.zip, *.tgz 压缩文件

### 5.3.1 Linux 目录配置的依据 FHS

- / : root 根目录，与开机系统有关
- /usr : unix software resource 与软件安装/执行有关
- /var : variable 与系统运作过程有关
- /etc : 系统主要的配置文件几乎都放这里；FHS 建议不要放可执行文件在这里
- /etc/opt : （必要）放置第三方协力软件 /opt 的相关配置文件
- /lib : 放置在开机时会用到的函数库，以及 /bin 、/sbin 底下的指令会调用的函数库
- /lib/modules : 主要放置可抽换式的核心相关模块（驱动程序）
- /media : 放置可移除的装置，包括软盘、光盘、DVD 等
- /mnt : 用来暂时挂载额外的装置
- /opt : 放置第三方协力软件
- /run : 放置系统开机后产生的各项信息；可以使用内存来仿真，性能高
- /sbin : 开机过程所需要的，开机、修复、还原系统所需要的指令
- /srv : service 的缩写，网络服务
- /tmp : 存放临时信息，建议定时清理，甚至可以在开机时删除 /tmp 下的所有数据
- /proc : 本身是一个虚拟的文件系统，放置的数据都在内存中
- /sys : 和 /proc 类似，也是一个虚拟的文件系统

## 第六章 Linux 文件与目录管理

### 6.1.2 目录的相关操作


| 操作符 | 功能 |
| --- | --- |
| . | 当前目录 |
| .. | 上一层目录 |
| - | 前一个工作目录 |
| ~ | 当前用户的家目录 |
| ~account | account 用户的的家目录 |

#### pwd -P
- -P 显示出真实的路径，而非使用连接（link）路径

#### mkdir -mp 新建目录
- -m 配置文件的权限
- -p 递归建立所需所有目录

```cmd
$ mkdir -p test1/test2/test3
```

建立权限为 rwx--x--x 的目录

```cmd
$ mkdir -m 711 test
```

### 6.1.3 关于执行文件路径的变量：$PATH

查看 PATH

```cmd
$ echo $PATH
```
### 6.2.1 文件与目录的检视：ls
- -l 包含文件属性与权限等
- -h 将文件的容量以人类较易读的方式列出来
- -R 连同子目录内容一起列出来
- -S 以文件容量大小排序
- -t 依时间排序
- --full-time 以完整时间模式

### 6.2.2 复制、删除与移动 cp rm mv

### 6.2.3 取得路径的文件名与目录名称

```cmd
$ basename /home/aoe/k8s/test/
test

$ dirname /home/aoe/k8s/test/
/home/aoe/k8s
```

### 6.3 文件内容查阅

- cat Concatenate 由第一行开始显示文件内容
- tac 从最后一行开始显示文件内容
- nl 显示的时候，顺道输出行号
- more 一页一页的显示文件内容
- less 与 more 类似，优点是可以向前翻页
- head 只看头几行
- tail 只看尾巴几行
- od 以二进制的方式读取文件内容

#### more 
- 空格键： 向下翻一页
- Enter： 向下翻一行
- /字符串：在显示的文档中，向下搜索【字符串】这个关键词 
- :f ：立刻显示出文件名以及目前显示的行数
- q：立刻离开
- b 或 ctrl + b：往回翻页，只对文件有用，对管线无用

#### less


