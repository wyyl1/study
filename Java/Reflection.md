# Java 反射

## 常用方法
| 方法 | 说明 |
| - | - |
|Field getField(name) | 根据字段名获取某个 **public** 的 field（包括父类）|
| Field[] getFields()| 获取所有 **public** 的 field（包括父类）|
| Field getDeclaredField(name)| 根据字段名获取当前类的某个 field（**不包括父类**）|
| Field[] getDeclaredFields()| 获取当前类的所有 field（**不包括父类**）|

## 新手翻车集锦
### 1. 泛型擦除
因反省擦除，获取不到对象的**泛型类**
- Dubbo 项目之间的调用就会发生

## 2. 遗漏父类的属性
使用这样的方法，可以获取父类属性
```java
        List<Field> fieldList = new ArrayList<>();
        Class clazz = obj.getClass();
        // 获取包括父类的所有属性
        while (!Object.class.equals(clazz)) {
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
            clazz = clazz.getSuperclass();
        }
```