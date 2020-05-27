# Redis Sorted sets 为什么那么快？

## 1. 官方文档说很快

### [Data types - Sorted sets](https://redis.io/topics/data-types#sorted-sets) 中介绍大意如下

> - 在大型在线游戏中排行榜，每次提交新分数时，您都可以使用ZADD进行更新。您可以轻松地使用ZRANGE吸引顶级用户，也可以给定用户名，使用ZRANK返回其在列表中的排名。一起使用ZRANK和ZRANGE可以向用户显示得分与给定用户相似的得分。一切都很快。
- 排序集通常用于索引Redis中存储的数据。例如，如果您有许多代表用户的哈希，则可以使用已排序的集合，其中元素以用户的年龄为得分，而用户的ID为值。因此，使用ZRANGEBYSCORE检索具有给定年龄段的所有用户将非常简单快捷。

### An introduction to Redis data types and abstractions
#### [Redis-sorted-sets](https://redis.io/topics/data-types-intro#redis-sorted-sets) 中介绍大意如下

> 排序集是通过包含**跳表**和**哈希表**的双端口数据结构实现的，因此，每次添加元素时，Redis都会执行**O（log（N））**操作。很好，但是当我们要求排序的元素时，Redis根本不需要做任何工作，它已经全部排序了

### [Updating the score: leader boards](https://redis.io/topics/data-types-intro#updating-the-score-leader-boards)

> - 在切换到下一个主题之前，请只对已排序集做最后的说明。排序集的分数可以随时更新。只需对已包含在排序集中的元素调用ZADD，将以**O（log（N））**时间复杂度更新其得分（和位置）。这样，当有大量更新时，排序集是合适的。
- 由于这种特性，常见的用例是排行榜。典型的应用是Facebook游戏，您可以结合使用按照高分对用户进行排序的能力以及排名操作，以显示前N位用户和排行榜中的用户排名（例如，“您是这里的＃4932最佳成绩”）。

## 原理分析
### 设计思想：空间换时间
- 链表相对数组会占用更多空间

### 为什么快？
1. 使用**有序**链表存储 score，可使用二分法查找，时间复杂度**O（log（N））**
2. 使用跳表加速链表查找，链表层数可以随数据量增加而增加，维持相对稳定的查找速度，时间复杂度**O（log（N））**
3. 插入时只需查询到插入位置（时间复杂度**O（log（N））**），然后插入（时间复杂度**O（1）**），总时间复杂度**O（log（N））**

### 为什么不用平衡树？
1. 平衡树可能占用更多的内存
2. 插入时平衡树的左旋、右旋更耗时
3. 

## 扩展阅读
- e-Book - 《Redis in Action》 [3.5 Sorted sets](https://redislabs.com/ebook/part-2-core-concepts/chapter-3-commands-in-redis/3-5-sorted-sets/)


