# Java 新特性学习工程

本工程用于学习和实践 Java 8 到 Java 24 的新特性，包含详细的文档说明和可运行的示例代码。

## 工程结构

- **文档**: [`doc/JavaNewFeature.md`](doc/JavaNewFeature.md) - Java 新特性详细说明文档
- **代码**: [`src/test/java/com/example/playground/JavaNewFeatureTest.java`](src/test/java/com/example/playground/JavaNewFeatureTest.java) - 新特性示例和测试代码

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


## 说明

- 项目使用 Java 24 并启用预览特性
- 详细的新特性介绍请查看文档：[`doc/JavaNewFeature.md`](doc/JavaNewFeature.md)