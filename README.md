# Java 新特性学习工程

本工程专注于学习和实践 Java 8 到 Java 24 的新特性。

## 项目结构

```
java-new-features/
├── doc/
│   └── JavaNewFeature.md          # Java 新特性详细文档
├── src/test/java/com/example/playground/
│   └── JavaNewFeatureTest.java    # Java 新特性测试代码
├── build.gradle                   # 构建配置
├── settings.gradle               # Gradle 设置
├── .gitignore                    # Git 忽略文件
└── README.md                     # 本文件
```

## 环境要求

- Java 24 或更高版本（需要开启预览特性）
- Gradle 8.0 或更高版本

## 快速开始

### 构建项目

```bash
./gradlew build
```

### 运行测试

```bash
./gradlew test
```

### 运行特定测试

```bash
./gradlew test --tests "com.example.playground.JavaNewFeatureTest.virtualThreadsTest"
```

## 包含的新特性

### 语言核心特性
- 局部变量类型推断 (var) - Java 10
- Switch 表达式 - Java 14
- 文本块 (Text Blocks) - Java 15
- 记录类 (Record) - Java 16
- 密封类和接口 (Sealed Classes) - Java 17
- 模式匹配 (Pattern Matching) - Java 16-23
- super() 之前的语句 - Java 22
- 原始类型模式 - Java 23

### 并发与性能
- 虚拟线程 (Virtual Threads) - Java 21
- 结构化并发 (Structured Concurrency) - Java 21
- 作用域值 (Scoped Values) - Java 22

### API 与标准库
- 有序集合 (Sequenced Collections) - Java 21
- HTTP 客户端 - Java 11
- 集合工厂方法 - Java 9
- Stream, Optional, String API 增强 - Java 9+
- Stream 聚合器 (Stream Gatherers) - Java 24

### 平台与架构
- 模块化系统 (Project Jigsaw) - Java 9

## 注意事项

1. 本项目使用 Java 24 预览特性，运行时需要 `--enable-preview` 参数
2. 虚拟线程测试可能需要较长时间执行
3. 部分特性在不同JDK实现中可能有细微差别

## 文档

详细的特性说明和实现原理请参考 [`doc/JavaNewFeature.md`](doc/JavaNewFeature.md) 文档。