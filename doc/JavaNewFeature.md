# Java 新特性实操手册 (Java 8 -> 24)

本文档是一份详尽的实操指南，旨在深入、透彻地讲解从 Java 8 到 Java 24 的核心新特性。它不仅包含特性的基本用法，更沉淀了我们关于实现原理、最佳实践、常见陷阱和性能影响的深入探讨，可作为您在日常开发和架构设计中的参考手册。

---

## 目录

*   [**一、 语言核心特性**](#一-语言核心特性)
    *   [1. 局部变量类型推断 (`var`) (Java 10)](#1-局部变量类型推断-var-java-10)
    *   [2. Switch 表达式 (Java 14)](#2-switch-表达式-java-14)
    *   [3. 文本块 (Text Blocks) (Java 15)](#3-文本块-text-blocks-java-15)
    *   [4. 记录类 (`record`) (Java 16)](#4-记录类-record-java-16)
    *   [5. 密封类和接口 (Sealed Classes) (Java 17)](#5-密封类和接口-sealed-classes-java-17)
    *   [6. 模式匹配 (Pattern Matching) (Java 16-23)](#6-模式匹配-pattern-matching-java-16-23)
    *   [7. 未命名模式和变量 (Unnamed Patterns & Variables) (Java 22, 正式)](#7-未命名模式和变量-unnamed-patterns--variables-java-22-正式)
    *   [8. 未命名类和实例 main 方法 (Unnamed Classes and Instance Main Methods) (Java 21, 预览)](#8-未命名类和实例-main-方法-unnamed-classes-and-instance-main-methods-java-21-预览)
    *   [9. `super(...)` 之前的语句 (Statements before `super(...)`) (Java 22, 预览)](#9-super-之前的语句-statements-before-super-java-22-预览)
*   [**二、 并发与性能**](#二-并发与性能)
    *   [10. 虚拟线程 (Virtual Threads) (Java 21, 正式)](#10-虚拟线程-virtual-threads-java-21-正式)
    *   [11. 结构化并发 (Structured Concurrency) (Java 22, 预览)](#11-结构化并发-structured-concurrency-java-22-预览)
    *   [12. 作用域值 (Scoped Values) (Java 22, 预览)](#12-作用域值-scoped-values-java-22-预览)
    *   [19. 新的垃圾收集器 (ZGC & Shenandoah) (Java 15)](#19-新的垃圾收集器-zgc--shenandoah-java-15)
*   [**三、 API 与标准库**](#三-api-与标准库)
    *   [13. 有序集合 (Sequenced Collections) (Java 21)](#13-有序集合-sequenced-collections-java-21)
    *   [14. HTTP 客户端 (Standard HTTP Client) (Java 11)](#14-http-客户端-standard-http-client-java-11)
    *   [15. 集合工厂方法 (Collection Factory Methods) (Java 9)](#15-集合工厂方法-collection-factory-methods-java-9)
    *   [16. Stream, Optional, String 等 API 增强 (Java 9+)](#16-stream-optional-string-等-api-增强-java-9)
    *   [17. Stream 聚合器 (Stream Gatherers) (Java 24, 正式)](#17-stream-聚合器-stream-gatherers-java-24-正式)
*   [**四、 平台与架构**](#四-平台与架构)
    *   [18. 模块化系统 (Project Jigsaw) (Java 9)](#18-模块化系统-project-jigsaw-java-9)
    *   [20. 外部函数与内存 API (Foreign Function & Memory API) (Java 22, 正式)](#20-外部函数与内存-api-foreign-function--memory-api-java-22-正式)
    *   [21. 向量 API (Vector API) (Java 24, 第九轮孵化)](#21-向量-api-vector-api-java-24-第九轮孵化)
    *   [22. 类文件 API (Class-File API) (Java 24, 正式)](#22-类文件-api-class-file-api-java-24-正式)
    *   [23. JShell: Java REPL 工具 (Java 9)](#23-jshell-java-repl-工具-java-9)
    *   [24. 单文件源码程序启动 (Java 11)](#24-单文件源码程序启动-java-11)
    *   [25. 更详尽的 NullPointerException (Java 14)](#25-更详尽的-nullpointerexception-java-14)
    *   [26. 飞行记录器 (JFR) (Java 11)](#26-飞行记录器-jfr-java-11)

---

## 一、 语言核心与语法

### 1. 局部变量类型推断 (`var`) (Java 10)

*   **演进**: Java 10 正式引入。
*   **核心理念**: 在声明局部变量时，使用 `var` 替代具体类型，编译器会根据右侧的初始化表达式自动推断出变量的准确类型。

    > **注意**: 这不是动态类型！`var` 声明的变量类型在编译时就已经确定，后续不能再赋不同类型的值。

*   **核心优势**:
    *   **简洁性**: 极大减少了冗长的类型声明，尤其在面对复杂的泛型类型时效果显著。
    *   **可读性**: 当变量名已清晰表达意图时，省略类型声明能让代码更聚焦于业务逻辑。

*   **代码实战对比**:
    ```java
    // Java 10 之前，类型声明非常冗长
    Map<String, List<Integer>> transactionsByMerchant = new HashMap<String, List<Integer>>();
    
    // 使用 var，代码更整洁，意图同样清晰
    var transactionsByMerchant = new HashMap<String, List<Integer>>();
    ```

*   **实操限制与规则**:
    *   **必须初始化**: 声明时必须立即初始化，否则编译器无法推断类型。
    *   **仅限局部使用**: 只能用于方法内的局部变量、`for` 循环、`try-with-resources` 语句等。
    *   **禁止用于**: 类成员变量（字段）、方法参数、方法返回类型。

### 2. Switch 表达式 (Java 14)

*   **演进**: Java 14 正式发布。
*   **核心理念**: 对传统 `switch` 语句的增强，使其可以作为表达式使用，能够返回值。这让代码更简洁、更安全。
*   **核心优势**:
    *   **表达式返回值**: 可将 `switch` 的结果直接赋值给变量。
    *   **简洁安全**: 使用 `->` 箭头语法替代 `case...break`，从根本上避免了因忘记 `break` 导致的“贯穿”错误。
    *   **编译期详尽性**: 作为表达式使用时，编译器强制要求覆盖所有可能情况（或提供 `default`），将潜在的运行时错误提前到编译期发现。

*   **代码实战对比**:
    ```java
    // 传统 Switch 语句，需要临时变量，且有 break 遗漏风险
    DayOfWeek day = DayOfWeek.TUESDAY;
    int numLetters;
    switch (day) {
        case MONDAY:
        case FRIDAY:
        case SUNDAY:
            numLetters = 6;
            break;
        case TUESDAY:
            numLetters = 7;
            break;
        case THURSDAY:
        case SATURDAY:
            numLetters = 8;
            break;
        case WEDNESDAY:
            numLetters = 9;
            break;
        default:
            throw new IllegalStateException("Invalid day: " + day);
    }
    
    // 使用 Switch 表达式，代码更紧凑、更安全
    int numLetters2 = switch (day) {
        case MONDAY, FRIDAY, SUNDAY -> 6;
        case TUESDAY                -> 7;
        case THURSDAY, SATURDAY     -> 8;
        case WEDNESDAY              -> 9;
        default -> throw new IllegalStateException("Invalid day: " + day);
    };
    ```
    > **注意**: `switch` 的模式匹配能力是其更强大的扩展，我们在 [第6节](#6-模式匹配-pattern-matching-java-16-23) 中统一详细讲解。

### 3. 文本块 (Text Blocks) (Java 15)

*   **演进**: Java 15 正式发布。
*   **核心理念**: 使用三个双引号 `"""` 包裹，轻松定义多行字符串字面量。
*   **核心优势**:
    *   **可读性**: 编写 SQL、JSON、HTML 等多行文本时，格式所见即所得，无需 `\n` 和 `+` 拼接。
    *   **智能缩进**: 自动移除所有行共有的“附带”前导空格，只保留开发者真正想要的“必要”相对缩进，保持代码美观和内容纯净。

*   **代码实战对比**:
    ```java
    // 传统方式拼接 JSON，可读性差且容易出错
    String json = "{\n" +
                  "  \"name\": \"姜哥\",\n" +
                  "  \"role\": \"Developer\"\n" +
                  "}";
    
    // 使用文本块，结构清晰，所见即所得
    String textBlockJson = """
                           {
                             "name": "姜哥",
                             "role": "Developer"
                           }
                           """;
    ```

### 4. 记录类 (`record`) (Java 16)

* **演进**: Java 16 正式发布。
* **核心理念**: 一种用于声明**不可变数据聚合体**的简洁语法。它是一种特殊的、`final` 的类。
* **核心优势**: 彻底消除创建 DTO、POJO 等数据载体类时所需的大量样板代码，让代码更简洁、更安全、更专注于数据本身。

* **代码实战对比**:
    ```java
    // Java 16 之前，需要手动编写大量样板代码的 POJO
    public final class PointPojo {
        private final int x;
        private final int y;
    
        public PointPojo(int x, int y) {
            this.x = x;
            this.y = y;
        }
    
        public int x() { return x; }
        public int y() { return y; }
    
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PointPojo pointPojo = (PointPojo) o;
            return x == pointPojo.x && y == pointPojo.y;
        }
    
        @Override
        public int hashCode() {
            return java.util.Objects.hash(x, y);
        }
    
        @Override
        public String toString() {
            return "PointPojo[" +
                   "x=" + x + ", " +
                   "y=" + y + "]";
        }
    }
    
    // 使用 record，一行代码即可获得功能完备的不可变类
    record Point(int x, int y) {}
    ```

* **自动生成与自定义**: 编译器会为你生成所有组件的 `private final` 字段、一个全参数公共构造器、每个组件的公共访问器方法（如 `point.x()`）、以及基于所有组件实现的 `equals()`, `hashCode()`, `toString()` 方法。同时，你仍然可以添加静态字段/方法、实例方法，或通过“紧凑构造函数”来添加校验逻辑。

    ```java
    // 示例：定义一个功能丰富的 Point record
    record PointWithValidation(int x, int y) {
        // 紧凑构造函数，用于校验
        public PointWithValidation {
            if (x < 0 || y < 0) {
                throw new IllegalArgumentException("坐标不能为负数");
            }
            // 无需 this.x = x; 赋值是自动的
        }
    
        // 自定义实例方法
        public double distanceToOrigin() {
            return Math.sqrt(x * x + y * y);
        }
    }
    ```

### 5. 密封类和接口 (Sealed Classes) (Java 17)

*   **演进**: Java 17 正式发布。
*   **核心理念**: 通过 `sealed` 和 `permits` 关键字，精确地控制一个类或接口的继承体系，有且只有 `permits` 列表中指定的子类可以继承或实现它。
*   **核心优势**:
    *   **架构清晰**: 作者可以明确表达继承的设计意图，防止被意外或恶意扩展。
    *   **编译期安全**: 与 `switch` 模式匹配完美结合。当对一个 `sealed` 类型进行 `switch` 时，如果覆盖了所有 `permits` 的子类，则**无需 `default` 分支**，编译器会进行详尽性检查，保证代码完备。

*   **设计哲学与动机**:
    *   **建模精确性**: 允许库作者定义“代数数据类型”(ADT)，即一个类型的可能形态是有限且已知的集合。这在建模领域特定语言或状态机时非常强大。
    *   **API 演化控制**: 防止外部客户端随意扩展核心类，确保了库的封装性和可维护性。作者可以安全地添加新的 `permits` 子类，而不用担心破坏未知实现者的代码。
    *   **赋能模式匹配**: 这是 `sealed` 最直接的优势。它向编译器提供了完整的继承信息，使得 `switch` 模式匹配可以进行详尽性检查。如果所有允许的子类型都被覆盖，就不再需要 `default` 分支，从而写出更安全、更具表现力的代码。

*   **子类规则与代码示例**: `permits` 列表中的每个子类，必须是以下三者之一：
    1.  `final`: 继承链到此终止。
    2.  `sealed`: 继续密封，必须指定自己的 `permits` 列表。
    3.  `non-sealed`: 打破密封，回归传统，任何类都可以继承它。

    ```java
    // 示例：定义一个密封的 Vehicle 接口
    sealed interface Vehicle permits Car, Bicycle {}
    
    final class Car implements Vehicle { /* ... */ }
    
    non-sealed class Bicycle implements Vehicle { /* ... */ }
    
    // MountainBike 可以自由继承 non-sealed 的 Bicycle
    class MountainBike extends Bicycle { /* ... */ }
    ```
### 6. 模式匹配 (Pattern Matching) (Java 16-23)

模式匹配是 Java 近年来最重要的一组语言增强，它旨在以更安全、更简洁、更具表现力的方式检查对象的“模式”（即结构或类型），并在匹配成功时自动提取其中的数据。

#### 6.1 `instanceof` 的模式匹配 (Java 16)
* **演进**: Java 16 正式发布。
* **核心理念**: 极大地简化了“检查类型-强制转换-使用”这一经典繁琐流程。
* **实操**: 在 `instanceof` 检查为 `true` 时，自动完成类型转换，并将结果赋给你声明的模式变量。该变量的作用域被智能地限制在它能被确定赋值的地方。

```java
// 示例：instanceof 的模式匹配
Object obj = "Hello";
if (obj instanceof String s) {
    // 无需强转，变量 s 直接可用
    System.out.println(s.toUpperCase());
}
```

#### 6.2 `switch` 的模式匹配 (Java 21)
* **演进**: Java 21 正式发布。
* **核心理念**: 将模式匹配的能力全面引入 `switch`，使其能根据对象的类型和结构进行分支选择。
* **关键能力**:
    1. **`case null`**: `switch` 可以直接处理 `null` 选择器。若选择器为 `null` 且无 `case null` 分支，依然抛出 `NullPointerException`。
    2. **类型模式 (Type Patterns)**: `case String s -> ...` 会自动检查对象是否为 `String`，若是则自动转换并绑定到变量 `s`。
    3. **守护模式 (Guarded Patterns)**: `case Type t when condition -> ...` 允许在模式匹配成功后，增加一个额外的布尔条件判断，实现更精细的控制。

#### 6.3 记录模式 (Record Patterns) (Java 21)
* **演进**: Java 21 正式发布。
* **核心理念**: 对记录模式的深度支持，允许在匹配 `record` 类型时直接“解构”它，提取其组件。
* **实操**: `case Point(int x, int y) -> ...`，当 `switch` 的对象匹配 `Point` 类型时，它的组件 `x` 和 `y` 会被自动提取到同名的局部变量中，可以直接在 `->` 右侧使用。

```java
// 实战示例：统一展示 Switch 的多种模式匹配
sealed interface Shape permits Circle, Rectangle {}
record Circle(double radius) implements Shape {}
record Rectangle(double length, double width) implements Shape {}

double getArea(Shape shape) {
    return switch (shape) {
        // 1. null 模式
        case null -> 0;
        // 2. 记录模式 (解构)
        case Circle(double r) -> Math.PI * r * r;
        // 3. 守护模式
        case Rectangle(double l, double w) when l == w -> l * w; // 正方形
        // 4. 类型模式
        case Rectangle r -> r.length() * r.width(); // 普通矩形
    };
}
```

#### 6.4 原始类型模式 (Primitive Types in Patterns) (Java 23, 预览)

*   **演进**: Java 23 作为预览特性引入。
*   **核心理念**: 将模式匹配的能力无缝扩展到所有 8 种原始类型（`boolean`, `byte`, `short`, `int`, `long`, `float`, `double`, `char`）。
*   **解决的痛点**: 在此之前，`switch` 模式匹配主要针对引用类型，导致处理原始类型时语法不统一，无法使用 `when` 等高级子句。
*   **核心优势**:
    *   **语法统一**: `switch` 语句现在可以一致地处理任何类型（原始类型或引用类型），成为更通用的控制流工具。
    *   **表现力增强**: 允许对原始类型使用 `when` 守护子句，可以编写出更精炼、更清晰的判断逻辑。

*   **启用方式**: 作为一个预览功能，你必须在构建工具中添加 `--enable-preview` 编译和运行参数。

*   **代码实战：统一处理不同类型**

    下面的代码演示了如何使用一个 `switch` 表达式来统一处理包含原始类型和引用类型的 `Object`，并对 `double` 类型使用了 `when` 子句进行条件分支。

    ```java
    // 演示代码来自 JavaNewFeatureTest.java
    String checkPrimitiveType(Object obj) {
        return switch (obj) {
            // 1. 对原始类型 int 进行模式匹配
            case int i -> STR."这是一个原始类型 int: \{i}";
    
            // 2. 对原始类型 double 使用 'when' 子句进行模式匹配
            case double d when d > 100.0 -> STR."这是一个大的 double 值: \{d}";
            case double d -> STR."这是一个 double 值: \{d}";
    
            // 3. 对引用类型进行模式匹配 (以示对比)
            case String s -> STR."这是一个字符串，长度为: \{s.length()}";
    
            // 其他类型的默认情况
            default -> STR."是其他类型: \{obj.getClass().getName()}";
        };
    }
    
    @Test
    void primitiveTypePatternsTest() {
        System.out.println(checkPrimitiveType(101));        // -> 这是一个原始类型 int: 101
        System.out.println(checkPrimitiveType(250.5));      // -> 这是一个大的 double 值: 250.5
        System.out.println(checkPrimitiveType(42.0));       // -> 这是一个 double 值: 42.0
        System.out.println(checkPrimitiveType("你好!"));    // -> 这是一个字符串，长度为: 3
    }
    ```

### 7. 未命名模式和变量 (Unnamed Patterns & Variables) (Java 22, 正式)

*   **演进**: Java 22 正式发布。
*   **核心理念**: 引入下划线 `_` 作为一个特殊的标记，用于表示你**刻意忽略**的模式变量或普通变量，以提升代码清晰度并消除编译器警告。
*   **解决的痛点**: 在编程中，我们经常会遇到一些必须声明但又从未使用过的变量（如 `catch` 块的异常变量、lambda 的未使用参数等），它们会产生编译器警告，也让代码显得杂乱。

#### 未命名变量

当一个变量必须被声明，但其值在后续逻辑中永远不会被使用时，可以用 `_` 代替其名称。

```java
// 示例：在 for-each 和 catch 块中使用
// 1. 在 for-each 循环中忽略元素
var numbers = java.util.List.of(10, 20, 30);
int count = 0;
for (var _ : numbers) {
    count++;
}

// 2. 在 lambda 表达式中忽略参数
var map = new java.util.HashMap<String, String>();
map.forEach((key, _) -> System.out.println("Key: " + key));

// 3. 在 try-catch 中忽略异常变量
try {
    throw new RuntimeException("一个我们不关心的异常");
} catch (RuntimeException _) {
    System.out.println("成功捕获并忽略了异常。");
}
```

#### 未命名模式

在模式匹配（`instanceof` 或 `switch`）的上下文中，当下划线 `_` 用于模式变量时，它表示我们只关心类型或结构的匹配，而不关心匹配到的值本身。

```java
// 示例：在 switch 中解构 record 时忽略组件
record Point(int x, int y) {}
var point = new Point(10, 20);
switch (point) {
    // 我们只关心 x 坐标，忽略 y
    case Point(int x, int _) -> System.out.println("x-coordinate: " + x);
}
```

### 8. 未命名类和实例 main 方法 (Unnamed Classes and Instance Main Methods) (Java 21, 预览)

*   **演进**: Java 21 作为预览特性引入。
*   **核心理念**: 旨在大幅简化 Java 的入门体验，让初学者可以编写出最简单的程序，而无需理解 `class`, `public`, `static` 等复杂的修饰符。
*   **核心优势**:
    *   **降低学习曲线**: 使得第一个 "Hello, World!" 程序极其简洁，更接近脚本语言的体验。
    *   **平滑过渡**: 学生可以从最简单的 `main` 方法开始，随着学习的深入，再逐步、自然地引入 `class`、`static` 等概念。
*   **启用方式**: 作为一个预览功能，必须使用 `java` 启动器直接运行源文件，并添加 `--enable-preview` 和 `--source` 参数。
    ```shell
    # 假设文件名为 HelloWorld.java
    java --source 21 --enable-preview HelloWorld.java
    ```

*   **代码实战对比**:

    ```java
    // Java 21 之前，经典的 "Hello, World!"
    public class HelloWorld {
        public static void main(String[] args) {
            System.out.println("Hello, World!");
        }
    }
    ```

    ```java
    // 使用未命名类和实例 main 方法
    // 整个文件就是这样，没有 class 声明
    void main() {
        System.out.println("Hello, World!");
    }
    ```

*   **工作原理**:
    *   当你使用 `java` 命令运行这样的源文件时，Java 编译器在后台会为你自动合成一个未命名的类，并将你的 `main` 方法作为它的一个实例方法。然后，JVM 会创建这个类的实例并调用该 `main` 方法。

*   **限制与规则**:
    *   一个源文件最多只能有一个未命名类。
    *   未命名类中的方法不能是 `static` 的（除了将来可能的静态辅助方法）。
    *   它是一个预览特性，不建议在生产代码中使用。

### 9. `super(...)` 之前的语句 (Statements before `super(...)`) (Java 22, 预览)

*   **演进**: Java 22 作为预览特性引入。
*   **核心理念**: 适度放宽了 Java 语言中“构造函数的第一条语句必须是 `this(...)` 或 `super(...)`”的严格限制。
*   **解决的痛点**: 在此之前，任何需要在调用父类构造器前执行的参数校验或转换逻辑，都必须被封装在 `static` 辅助方法中，导致代码繁琐且可读性下降。
*   **核心优势**:
    *   **代码更自然**: 允许在调用 `super(...)` 之前，直接在构造函数中编写不依赖于当前实例 (`this`) 的准备代码，如参数校验、中间变量计算、日志记录等。
    *   **消除样板代码**: 不再需要为了简单的预处理逻辑而创建不必要的 `static` 辅助方法，使构造函数逻辑更内聚、更清晰。
*   **安全限制**: 编译器会严格确保在 `super(...)` 调用之前的代码**不能访问 `this`**，即不能引用当前正在创建的实例的任何字段、方法或内部类。这保证了对象在自身状态可用之前，其父类状态总是先被完全初始化。

*   **启用方式**: 作为一个预览功能，你必须在构建工具中添加 `--enable-preview` 编译和运行参数。

*   **代码实战：在构造函数中进行预处理**

    下面的代码演示了如何在子类的构造函数中，先对参数进行校验和计算，然后再将计算结果传递给父类的构造函数。

    ```java
    // 演示代码来自 JavaNewFeatureTest.java
    class ParentWithInfo {
        private final String info;
        ParentWithInfo(String info) {
            System.out.println(STR."父类构造器被调用，传入信息: '\{info}'");
            this.info = info;
        }
        String getInfo() { return info; }
    }
    
    class ChildWithPreSuperLogic extends ParentWithInfo {
        ChildWithPreSuperLogic(String part1, int part2) {
            // 1. 在 super() 调用前执行逻辑
            System.out.println("子类构造器开始执行...");
            String validatedPart1 = java.util.Objects.requireNonNull(part1, "Part1 cannot be null");
            String combinedInfo = STR."\{validatedPart1}-\{part2 * 2}";
            System.out.println(STR."在 super() 之前完成计算，准备传入: '\{combinedInfo}'");
    
            // 2. 调用 super()，不再需要是第一行
            super(combinedInfo);
    
            // 3. super() 之后的逻辑
            System.out.println("子类构造器完成。");
        }
    }
    
    @Test
    void statementsBeforeSuperTest() {
        var child = new ChildWithPreSuperLogic("data", 21);
        // 输出展示了清晰的执行顺序
    }
    ```

## 二、 并发与性能

### 10. 虚拟线程 (Virtual Threads) (Java 21, 正式)

*   **演进**: Java 21 正式发布，Project Loom 的核心成果。
*   **核心价值**: 允许开发者继续使用简单、直观、易于调试的**同步阻塞式代码风格**，来编写具有极高并发吞吐量的 I/O 密集型应用，而无需被迫转向复杂且易错的回调式或响应式编程模型。

#### 实现原理与核心理念

虚拟线程是由 JVM 调度和管理的、极其轻量级的用户态线程。其核心在于，当虚拟线程遇到受支持的阻塞操作（如网络 I/O）时，它会自动**“让出” (unmount/yield)** 其底层的平台线程（OS 线程），而不会像传统线程那样阻塞它。

*   **M:N 调度**: 大量（M）的虚拟线程，运行在少量（N）的平台线程上。默认的调度器是 `ForkJoinPool`。
*   **Continuation**: 这是实现虚拟线程魔法的底层机制。每个虚拟线程都有一个关联的 `Continuation` 对象，用于保存其当前的执行状态（调用栈等）。当虚拟线程让出时，它的状态被保存在 `Continuation` 中；当它可以继续执行时，JVM 会恢复该 `Continuation`，让它在某个可用的平台线程上继续运行。

#### 创建与使用方式

有多种方式可以创建和启动虚拟线程，但官方强烈推荐使用 `ExecutorService`。

```java
// 方式一：直接启动 (适合简单、一次性的任务)
Thread.startVirtualThread(() -> {
    System.out.println("Hello from a virtual thread!");
});

// 方式二：使用 Thread.Builder (需要更精细的控制，如命名)
Thread virtualThread = Thread.ofVirtual().name("MyVirtualThread").unstarted(() -> {
    // ...
});
virtualThread.start();

// 方式三：最佳实践 - 使用 ExecutorService
// 这是管理大量任务生命周期的首选方式
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10_000; i++) {
        executor.submit(() -> {
            // 模拟执行一个耗时1秒的网络请求
            Thread.sleep(Duration.ofSeconds(1));
            return "Task Complete";
        });
    }
} // try-with-resources 会自动关闭 executor
```

#### 核心适用场景

虚拟线程的“超能力”专门用于解决特定问题，并非万能灵药。

*   **非常适合**: **I/O 密集型任务**。
    *   **场景**: 大量并发的网络请求（微服务调用、数据库连接、消息队列等待）、访问慢速外部系统等。
    *   **原因**: 在这些场景中，线程绝大部分时间都在等待数据返回，而不是在做计算。虚拟线程可以将这些“等待时间”高效利用起来，让平台线程去服务其他就绪的任务。

*   **不适合**: **CPU 密集型任务**。
    *   **场景**: 复杂的数学计算、视频编码、大数据处理等需要持续占用 CPU 的任务。
    *   **原因**: 虚拟线程并不会带来额外的计算能力。对于 CPU 密集型任务，最佳的线程数通常等于或接近 CPU 核心数。使用超过核心数的虚拟线程（或平台线程）不仅没有好处，反而会因为频繁的上下文切换而降低性能。对于此类任务，应继续使用传统的平台线程池。

#### 实战示例：高并发 I/O 模拟

下面的代码直观地展示了虚拟线程的威力。它启动了 10,000 个并发任务，每个任务模拟一次耗时 1 秒的 I/O 操作。如果使用平台线程，这会立刻耗尽系统资源；而使用虚拟线程，则可以轻松应对。

```java
@Test
void highConcurrencyIoSimulationTest() throws InterruptedException {
    // 使用虚拟线程执行器
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        // 启动 10,000 个并发任务
        for (int i = 0; i < 10_000; i++) {
            final int taskNumber = i;
            executor.submit(() -> {
                System.out.println("开始执行任务: " + taskNumber + " on thread: " + Thread.currentThread());
                try {
                    // 模拟耗时 1 秒的 I/O 操作
                    Thread.sleep(Duration.ofSeconds(1));
                } catch (InterruptedException e) {
                    // ...
                }
                System.out.println("任务完成: " + taskNumber);
            });
        }
    }
    // 执行器关闭后，所有任务都已提交
    // 在真实应用中，你可能需要等待任务完成
}
```
> **观察输出**: 你会发现，尽管有上万个任务在“同时”运行，但底层的平台线程（输出中的 `ForkJoinPool-1-worker-X`）数量非常少。这证明了虚拟线程在等待 I/O 时让出了平台线程。

#### 深入辨析 1：阻塞操作的分类与后果

在虚拟线程的世界里，“阻塞”不再是一个单一概念，必须将其细分为三类：
| 等待类型                  | 例子                                                        | 处理机制                 | 对平台线程的影响          | 结论                                 |
|:----------------------|:----------------------------------------------------------|:---------------------|:------------------|:-----------------------------------:|
| **I/O 或 JUC 阻塞**      | `Socket.read()`, `lock.lock()`, `queue.take()`, `sleep()` | 虚拟线程被**挂起/卸载**       | **无影响** (平台线程被释放) | **The Good Guys** - 这是虚拟线程的理想工作场景。 |
| **`synchronized` 阻塞** | `synchronized` { `sleep()` }                              | 虚拟线程被**钉住 (Pinned)** | **阻塞** (平台线程被浪费)  | **The Bad Guys** - 虚拟线程优势尽失，性能退化。  |
| **本地方法阻塞**            | 调用 JNI 阻塞方法                                               | 虚拟线程被**钉住 (Pinned)** | **阻塞** (平台线程被浪费)  | **The Bad Guys** - 必须警惕。           |

#### 深入辨析 2：线程“钉住”的完美演示

```java
// 关键演示代码
@Test
void pinningDemonstrationTest() {
    // 场景一：ReentrantLock - 不钉住，高并发
    // 100,000 个任务，每个任务 sleep(200)，总耗时极短（如 955ms）。
    // sleep() 让出平台线程，所有任务近似并发执行。
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 100_000; i++) {
            var lock = new ReentrantLock(); // 关键：无共享锁
            executor.submit(() -> {
                lock.lock();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                } finally {
                    lock.unlock();
                }
            });
        }
    }

    // 场景二：synchronized - 钉住，性能灾难
    // 1,000 个任务，每个任务 sleep(200)，总耗时极长（如 14666ms）。
    // synchronized 钉住了虚拟线程，sleep() 真实阻塞了平台线程。
    // 平台线程池被迅速耗尽，任务只能分批串行执行。
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 1000; i++) {
            var monitor = new Object(); // 关键：无共享监视器
            executor.submit(() -> {
                synchronized (monitor) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                }
            });
        }
    }
}
```

#### 深入辨析 3：第三方库与文件 I/O 的兼容性

*   **第三方库**: 绝大多数依赖 JDK 标准**网络 API**的库（JDBC, HTTP 客户端等）都能**自动受益**于虚拟线程。需警惕在关键路径使用 `synchronized` 的老旧库。
*   **文件 I/O 的重要例外**:
    *   标准的 `java.io.File*` 和 `java.nio.file.Files` API 在执行文件操作时，**会阻塞平台线程**！这是当前操作系统层面的限制。
    *   **最佳实践**: 在需要高并发文件 I/O 的场景，**必须使用 `java.nio.channels.AsynchronousFileChannel`**，并结合 `Future.get()` 来等待结果。这是目前唯一与虚拟线程完美配合的标准文件 I/O 方式。

#### 深入辨析 4：与 Go 协程的对比

| 对比维度     | Go 协程 (Goroutine)                  | Java 虚拟线程 (Virtual Thread)  |
|:---------|:-----------------------------------|:----------------------------|
| **调度模型** | **抢占式** (运行时可强制切换长时间运行的 Goroutine) | **协作式** (只在明确的“让出点”切换)      |
| **创建方式** | 语言级 `go` 关键字                       | 库级 `Thread`/`Executors` API |
| **通信范式** | 推崇 **Channels** (通过通信共享内存)         | 推崇 **共享内存与锁**               |
| **设计哲学** | **并发原生**，自上而下，为并发而生。               | **兼容生态**，自下而上，为赋能现有生态而生。    |

### 11. 结构化并发 (Structured Concurrency) (Java 22, 预览)

*   **演进**: Java 19 引入预览，在 Java 22 中继续预览，至 Java 24 仍为预览特性。
*   **核心理念**: 将不同线程中运行的相关任务组视为一个单一的、原子性的工作单元。如果一组任务是为了同一个业务目标而协同工作的，那么它们的生命周期就应该被绑定在一起。
*   **解决的痛点**:
    *   **线程泄漏**: 在传统并发模型中，如果启动任务的线程提前退出，已提交的子任务会继续运行，造成资源浪费。
    *   **错误处理复杂**: 当一个子任务失败时，需要手动编写复杂的逻辑去取消其他所有相关的子任务。
    *   **任务取消困难**: 传播取消信号非常繁琐，容易出错。
*   **核心优势**:
    *   **可靠性**: 通过 `StructuredTaskScope` API，将并发任务的生命周期与一个清晰的词法作用域（`try-with-resources` 块）绑定。当代码流离开该作用域时，所有子任务都会被自动终结，从根本上杜绝了线程泄漏。
    *   **可观测性**: 代码的结构（父任务与子任务）与运行时的线程结构相匹配，使得通过线程转储（Thread Dump）来诊断并发问题变得极其容易。
    *   **简化的错误处理**: 提供了开箱即用的策略，如 `ShutdownOnFailure`，只要有一个子任务失败，整个作用域就会立即关闭，并自动取消所有其他正在运行的兄弟任务。

*   **代码实战：并发获取用户信息和订单**

    下面的代码演示了一个经典场景：为了响应一个请求，我们需要同时去获取用户信息和订单信息，然后将它们组合起来。`StructuredTaskScope` 确保了这两个操作要么都成功，要么在任何一个失败时另一个会被立即取消。

    ```java
    // 演示代码来自 JavaNewFeatureTest.java
    @Test
    void structuredConcurrency_Success_Test() throws Exception {
        // 使用 ShutdownOnFailure 策略，任何一个子任务失败，整个作用域都会关闭
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // 1. fork: 提交一个任务去获取用户，返回一个 Subtask (类似 Future)
            Subtask<String> userSubtask = scope.fork(() -> {
                System.out.println("开始获取用户...");
                Thread.sleep(Duration.ofSeconds(1));
                System.out.println("获取用户成功");
                return "姜哥";
            });
    
            // 2. fork: 同时提交另一个任务去获取订单
            Subtask<String> orderSubtask = scope.fork(() -> {
                System.out.println("开始获取订单...");
                Thread.sleep(Duration.ofSeconds(2));
                System.out.println("获取订单成功");
                return "订单-88888";
            });
    
            // 3. join: 等待所有子任务完成，或者等待 ShutdownOnFailure 策略被触发
            scope.join();
    
            // 4. throwIfFailed: 如果任何子任务抛出了异常，join()会捕获它，
            //    然后在这里统一抛出，这样我们就能在一个地方处理所有并发错误。
            scope.throwIfFailed();
    
            // 5. 组合结果: 如果代码能走到这里，说明所有子任务都成功了
            String result = STR."\{userSubtask.get()} 的 \{orderSubtask.get()}";
            System.out.println(STR."最终结果: \{result}");
        }
    }
    ```

### 12. 作用域值 (Scoped Values) (Java 22, 预览)

*   **演进**: Java 20 引入预览，在 Java 22 中继续预览，至 Java 24 仍为预览特性，是 Project Loom 并发改进计划的重要组成部分。
*   **核心理念**: 提供一种在调用栈中安全、高效地共享不可变数据的机制，是对 `ThreadLocal` 的现代化替代，专门为虚拟线程和结构化并发而设计。

#### 解决 ThreadLocal 的关键问题

**1. 虚拟线程环境下的性能问题**
```java
// ThreadLocal 在大量虚拟线程下的问题
ThreadLocal<String> userId = new ThreadLocal<>();

// 创建百万虚拟线程时，每个线程都有自己的 ThreadLocal 副本
// 内存开销巨大，垃圾回收压力很大
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 1_000_000; i++) {
        executor.submit(() -> {
            userId.set("user-" + Thread.currentThread().getId());
            // 业务逻辑...
            userId.remove();  // 容易忘记清理，导致内存泄漏
        });
    }
}
```

**2. 内存泄漏风险**
```java
// 常见的 ThreadLocal 内存泄漏场景
public class UserService {
    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();
    
    public void processRequest(User user) {
        CURRENT_USER.set(user);
        // 处理业务逻辑...
        // 忘记调用 CURRENT_USER.remove(); → 内存泄漏！
    }
}
```

#### ScopedValue 的解决方案

**1. 基本语法与用法**
```java
// 声明作用域值
private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();

// 使用作用域值
public void handleRequest(String userId) {
    // 在特定作用域内绑定值
    ScopedValue.where(USER_ID, userId)
              .run(() -> {
                  // 在这个作用域内，所有方法都可以访问 USER_ID
                  processBusinessLogic();
              });
    // 作用域结束后，值自动清理，无需手动管理
}

private void processBusinessLogic() {
    String currentUserId = USER_ID.get();  // 安全访问
    System.out.println("当前用户ID: " + currentUserId);
    callDeepMethod();  // 调用栈的任何深度都可以访问
}
```

**2. 多个作用域值的组合**
```java
private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();
private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();
private static final ScopedValue<String> TENANT_ID = ScopedValue.newInstance();

public void handleRequest(String userId, String requestId, String tenantId) {
    ScopedValue.where(USER_ID, userId)
              .where(REQUEST_ID, requestId)
              .where(TENANT_ID, tenantId)
              .run(() -> {
                  // 所有三个值都在作用域内可用
                  processRequest();
              });
}
```

**3. 嵌套作用域的行为**
```java
public void demonstrateNesting() {
    ScopedValue.where(USER_ID, "outer-user")
              .run(() -> {
                  System.out.println("外层: " + USER_ID.get()); // "outer-user"
                  
                  ScopedValue.where(USER_ID, "inner-user")
                            .run(() -> {
                                System.out.println("内层: " + USER_ID.get()); // "inner-user"
                            });
                  
                  System.out.println("回到外层: " + USER_ID.get()); // "outer-user"
              });
}
```

#### 与虚拟线程的完美结合

```java
private static final ScopedValue<String> USER_CONTEXT = ScopedValue.newInstance();

public void processUserRequests() {
    List<String> userIds = List.of("user1", "user2", "user3");
    
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (String userId : userIds) {
            executor.submit(() -> {
                // 每个虚拟线程都有自己的作用域
                ScopedValue.where(USER_CONTEXT, userId)
                          .run(() -> {
                              simulateBusinessLogic();
                          });
            });
        }
    }
}

private void simulateBusinessLogic() {
    String currentUser = USER_CONTEXT.get();
    System.out.println("处理用户: " + currentUser);
    
    // 调用其他服务，用户上下文自动传递
    callDatabaseService();
    callExternalAPI();
}
```

#### 核心优势

**1. 性能优异**
- 专为虚拟线程优化，内存占用极小
- 访问速度接近局部变量
- 无需手动清理，自动垃圾回收

**2. 类型安全**
```java
private static final ScopedValue<Integer> COUNT = ScopedValue.newInstance();
private static final ScopedValue<String> NAME = ScopedValue.newInstance();

// 编译时类型检查，避免类型错误
Integer count = COUNT.get();  // 返回 Integer
String name = NAME.get();     // 返回 String
```

**3. 不可变性保证**
```java
// 作用域值中的数据应该是不可变的
record UserContext(String userId, String tenantId, Instant timestamp) {}
private static final ScopedValue<UserContext> CONTEXT = ScopedValue.newInstance();

public void safeUsage() {
    UserContext context = new UserContext("user123", "tenant456", Instant.now());
    ScopedValue.where(CONTEXT, context)
              .run(() -> {
                  UserContext ctx = CONTEXT.get();
                  // ctx 是不可变的，线程安全
              });
}
```

**4. 安全的作用域控制**
```java
private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();

public void safeAccess() {
    try {
        String userId = USER_ID.get();
        // 使用 userId
    } catch (NoSuchElementException e) {
        // 在作用域外访问会抛出此异常，避免意外使用 null 值
        System.out.println("未在作用域内，无法获取用户ID");
    }
}
```

#### 实际应用场景

**1. Web 请求上下文管理**
```java
@RestController
public class UserController {
    private static final ScopedValue<RequestContext> REQUEST_CONTEXT = 
        ScopedValue.newInstance();
    
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable String id, HttpServletRequest request) {
        RequestContext context = new RequestContext(
            request.getHeader("X-Request-ID"),
            request.getHeader("X-User-ID"),
            System.currentTimeMillis()
        );
        
        return ScopedValue.where(REQUEST_CONTEXT, context)
                         .call(() -> userService.findUser(id));
    }
}
```

**2. 分布式链路追踪**
```java
public class TraceContext {
    private static final ScopedValue<String> TRACE_ID = ScopedValue.newInstance();
    private static final ScopedValue<String> SPAN_ID = ScopedValue.newInstance();
    
    public static void withTrace(String traceId, String spanId, Runnable task) {
        ScopedValue.where(TRACE_ID, traceId)
                  .where(SPAN_ID, spanId)
                  .run(task);
    }
    
    public static String currentTraceId() {
        return TRACE_ID.get();
    }
}
```

#### 最佳实践

**1. 声明原则**
```java
// ✅ 正确：声明为 static final
private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();

// ❌ 避免：非静态字段
private ScopedValue<String> userId = ScopedValue.newInstance();
```

**2. 不可变数据**
```java
// ✅ 推荐：使用不可变对象
record UserContext(String userId, String tenantId) {}
private static final ScopedValue<UserContext> CONTEXT = ScopedValue.newInstance();

// ⚠️ 谨慎：可变对象可能导致线程安全问题
private static final ScopedValue<Map<String, Object>> MUTABLE_CONTEXT = 
    ScopedValue.newInstance();
```

#### 与 ThreadLocal 的全面对比

| 特性 | ThreadLocal | ScopedValue |
|------|-------------|-------------|
| **内存管理** | 手动 `remove()` | 自动清理 |
| **虚拟线程性能** | 较差，内存开销大 | 优秀，专门优化 |
| **内存泄漏风险** | 高 | 无 |
| **作用域控制** | 整个线程生命周期 | 明确的代码块 |
| **访问安全性** | 可能返回 `null` | 抛出明确异常 |
| **可变性** | 支持可变值 | 推荐不可变值 |
| **嵌套支持** | 需要手动管理 | 天然支持 |

#### 迁移建议

**从 ThreadLocal 迁移到 ScopedValue：**
```java
// 旧代码 (ThreadLocal)
private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

public void oldWay(String userId) {
    USER_ID.set(userId);
    try {
        processRequest();
    } finally {
        USER_ID.remove();  // 容易忘记
    }
}

// 新代码 (ScopedValue)
private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();

public void newWay(String userId) {
    ScopedValue.where(USER_ID, userId)
              .run(() -> {
                  processRequest();
                  // 自动清理，无需 finally 块
              });
}
```

作用域值代表了 Java 并发编程的现代化方向，特别是在虚拟线程大量使用的场景下，它提供了比 ThreadLocal 更安全、更高效的解决方案。

---

## 三、 API 与库

### 13. 有序集合 (Sequenced Collections) (Java 21)

*   **演进**: Java 21 正式引入。
*   **核心理念**: 提供统一的 API 来处理具有**确定顺序**的集合，无论这个顺序是插入顺序、排序顺序还是其他类型的顺序。

#### 解决的核心问题

**1. 不一致的 API**
```java
// Java 21 之前，访问第一个和最后一个元素的方式不统一
List<String> list = List.of("first", "middle", "last");
String first = list.get(0);              // List 的方式
String last = list.get(list.size() - 1); // List 的方式

Deque<String> deque = new ArrayDeque<>(list);
String firstInDeque = deque.getFirst();  // Deque 的方式
String lastInDeque = deque.getLast();    // Deque 的方式

LinkedHashSet<String> set = new LinkedHashSet<>(list);
String firstInSet = set.iterator().next(); // Set 只能通过迭代器
// 没有直接获取最后一个元素的方法！需要遍历整个集合
```

**2. 缺乏反向访问能力**
```java
// 反向遍历集合的复杂性
List<String> list = List.of("a", "b", "c");

// 反向遍历 List - 需要索引操作
for (int i = list.size() - 1; i >= 0; i--) {
    System.out.println(list.get(i));
}

// 反向遍历 LinkedHashSet - 非常复杂
LinkedHashSet<String> set = new LinkedHashSet<>(list);
String[] array = set.toArray(new String[0]);
for (int i = array.length - 1; i >= 0; i--) {
    System.out.println(array[i]);
}
```

#### 新的接口层次结构

**SequencedCollection<E>**
```java
public interface SequencedCollection<E> extends Collection<E> {
    // 访问第一个和最后一个元素
    E getFirst();
    E getLast();
    
    // 在开头和结尾添加元素
    void addFirst(E e);
    void addLast(E e);
    
    // 移除第一个和最后一个元素
    E removeFirst();
    E removeLast();
    
    // 反向视图
    SequencedCollection<E> reversed();
}
```

**SequencedSet<E>**
```java
public interface SequencedSet<E> extends Set<E>, SequencedCollection<E> {
    SequencedSet<E> reversed();
}
```

**SequencedMap<K,V>**
```java
public interface SequencedMap<K,V> extends Map<K,V> {
    // 访问第一个和最后一个条目
    Map.Entry<K,V> firstEntry();
    Map.Entry<K,V> lastEntry();
    
    // 移除第一个和最后一个条目
    Map.Entry<K,V> pollFirstEntry();
    Map.Entry<K,V> pollLastEntry();
    
    // 在开头和结尾添加条目
    V putFirst(K key, V value);
    V putLast(K key, V value);
    
    // 序列化视图
    SequencedSet<K> sequencedKeySet();
    SequencedCollection<V> sequencedValues();
    SequencedSet<Map.Entry<K,V>> sequencedEntrySet();
    
    // 反向视图
    SequencedMap<K,V> reversed();
}
```

#### 统一的 API 使用

```java
// 现在所有有序集合都有统一的 API
List<String> list = List.of("first", "middle", "last");
LinkedHashSet<String> set = new LinkedHashSet<>(list);
LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
map.put("first", 1);
map.put("middle", 2);
map.put("last", 3);

// 统一的访问方式
String firstFromList = list.getFirst();    // "first"
String lastFromList = list.getLast();      // "last"

String firstFromSet = set.getFirst();      // "first"
String lastFromSet = set.getLast();        // "last"

var firstFromMap = map.firstEntry();       // first=1
var lastFromMap = map.lastEntry();         // last=3
```

#### 反向视图 - 核心特性

```java
List<String> list = List.of("a", "b", "c", "d");

// 获取反向视图
SequencedCollection<String> reversed = list.reversed();

System.out.println("原始: " + list);     // [a, b, c, d]
System.out.println("反向: " + reversed); // [d, c, b, a]

// 反向遍历
for (String item : list.reversed()) {
    System.out.println(item); // 输出: d, c, b, a
}

// 重要：反向视图是原集合的视图，不是副本
// 对反向视图的修改会影响原集合
```

#### 实际应用场景

**1. LRU 缓存实现**
```java
public class LRUCache<K, V> {
    private final LinkedHashMap<K, V> cache;
    private final int capacity;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>();
    }
    
    public V get(K key) {
        V value = cache.remove(key);
        if (value != null) {
            cache.putLast(key, value); // 移到最后（最近使用）
        }
        return value;
    }
    
    public void put(K key, V value) {
        cache.remove(key); // 先移除（如果存在）
        cache.putLast(key, value); // 添加到最后
        
        if (cache.size() > capacity) {
            cache.pollFirstEntry(); // 移除最老的条目
        }
    }
    
    public void printCacheOrder() {
        System.out.println("从旧到新: " + cache.sequencedKeySet());
        System.out.println("从新到旧: " + cache.sequencedKeySet().reversed());
    }
}
```

**2. 队列操作的统一**
```java
public class QueueOperations {
    public static <T> void demonstrateQueue(SequencedCollection<T> queue, T... items) {
        // 统一的队列操作，无论是 List、Deque 还是 LinkedHashSet
        
        // 入队操作
        for (T item : items) {
            queue.addLast(item);
        }
        
        System.out.println("队列状态: " + queue);
        
        // 出队操作
        while (!queue.isEmpty()) {
            T item = queue.removeFirst();
            System.out.println("出队: " + item);
        }
    }
    
    public static void main(String[] args) {
        // 可以用任何 SequencedCollection 实现
        demonstrateQueue(new ArrayList<>(), "A", "B", "C");
        demonstrateQueue(new LinkedHashSet<>(), "X", "Y", "Z");
        demonstrateQueue(new ArrayDeque<>(), 1, 2, 3);
    }
}
```

**3. 历史记录管理**
```java
public class BrowserHistory {
    private final List<String> history = new ArrayList<>();
    private int currentIndex = -1;
    
    public void visit(String url) {
        // 移除当前位置之后的所有历史
        while (history.size() > currentIndex + 1) {
            history.removeLast();
        }
        
        // 添加新页面
        history.addLast(url);
        currentIndex = history.size() - 1;
    }
    
    public String back() {
        if (currentIndex > 0) {
            currentIndex--;
            return history.get(currentIndex);
        }
        return null;
    }
    
    public String forward() {
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            return history.get(currentIndex);
        }
        return null;
    }
    
    public void printHistory() {
        System.out.println("历史记录（最新到最旧）:");
        for (String url : history.reversed()) {
            String marker = (history.indexOf(url) == currentIndex) ? " <- 当前" : "";
            System.out.println("  " + url + marker);
        }
    }
}
```

#### 实现类支持

**实现了 SequencedCollection 的类：**
- `ArrayList`, `LinkedList`, `Vector`
- `ArrayDeque`, `LinkedHashSet`
- `TreeSet` (基于排序顺序)

**实现了 SequencedMap 的类：**
- `LinkedHashMap`
- `TreeMap` (基于排序顺序)

#### 重要注意事项

**1. 反向视图的行为**
```java
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
SequencedCollection<String> reversed = list.reversed();

// 修改原集合影响反向视图
list.add("d");
System.out.println(reversed); // [d, c, b, a]

// 修改反向视图影响原集合
reversed.addFirst("e"); // 等同于 list.addLast("e")
System.out.println(list); // [a, b, c, d, e]
```

**2. 空集合的异常处理**
```java
List<String> emptyList = new ArrayList<>();

try {
    emptyList.getFirst(); // 抛出 NoSuchElementException
} catch (NoSuchElementException e) {
    System.out.println("空集合无法获取第一个元素");
}

try {
    emptyList.getLast(); // 抛出 NoSuchElementException
} catch (NoSuchElementException e) {
    System.out.println("空集合无法获取最后一个元素");
}
```

**3. 不可变集合的限制**
```java
List<String> immutableList = List.of("a", "b", "c");

// 可以获取元素
String first = immutableList.getFirst(); // "a"
String last = immutableList.getLast();   // "c"

// 但不能修改
try {
    immutableList.addFirst("x"); // 抛出 UnsupportedOperationException
} catch (UnsupportedOperationException e) {
    System.out.println("不可变集合不支持修改操作");
}
```

#### 核心优势总结

1. **API 统一性**: 所有有序集合都有一致的访问接口
2. **代码简洁性**: 无需复杂的索引操作或迭代器
3. **反向操作**: 天然支持反向遍历和操作
4. **类型安全**: 编译时类型检查
5. **性能优化**: 避免了不必要的数组转换和索引计算

有序集合是 Java 21 中最实用的新特性之一，它让集合 API 更加一致和直观，显著提升了开发效率。

### 14. HTTP 客户端 (Standard HTTP Client) (Java 11)

*   **演进**: Java 11 正式引入，替代了长期以来依赖第三方库（如 Apache HttpClient、OkHttp）进行 HTTP 通信的现状。
*   **核心理念**: 提供现代化、高性能的 HTTP 客户端，原生支持 HTTP/2 和异步编程模型，减少外部依赖。

#### 核心特性

**1. 同步和异步 API**
```java
// 同步请求
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.example.com/data"))
        .GET()
        .build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
System.out.println("状态码: " + response.statusCode());
System.out.println("响应体: " + response.body());

// 异步请求
CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
future.thenAccept(resp -> {
    System.out.println("异步响应: " + resp.body());
});
```

**2. HTTP/2 支持**
```java
// 明确指定 HTTP/2
HttpClient client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .build();

// 或者让客户端自动选择版本
HttpClient autoClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2) // 首选 HTTP/2，降级到 HTTP/1.1
        .build();
```

**3. 请求配置**
```java
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.example.com/users"))
        .timeout(Duration.ofSeconds(30))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + token)
        .GET()
        .build();
```

**4. 不同的请求方法**
```java
// GET 请求
HttpRequest getRequest = HttpRequest.newBuilder()
        .uri(URI.create("https://api.example.com/users"))
        .GET()
        .build();

// POST 请求
String jsonData = """
        {
            "name": "张三",
            "email": "zhangsan@example.com"
        }
        """;

HttpRequest postRequest = HttpRequest.newBuilder()
        .uri(URI.create("https://api.example.com/users"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonData))
        .build();

// PUT 请求
HttpRequest putRequest = HttpRequest.newBuilder()
        .uri(URI.create("https://api.example.com/users/123"))
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(jsonData))
        .build();

// DELETE 请求
HttpRequest deleteRequest = HttpRequest.newBuilder()
        .uri(URI.create("https://api.example.com/users/123"))
        .DELETE()
        .build();
```

**5. 不同的响应体处理**
```java
// 字符串响应
HttpResponse<String> stringResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

// 字节数组响应
HttpResponse<byte[]> bytesResponse = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

// 文件响应
HttpResponse<Path> fileResponse = client.send(request, 
    HttpResponse.BodyHandlers.ofFile(Paths.get("download.txt")));

// 流响应
HttpResponse<InputStream> streamResponse = client.send(request, 
    HttpResponse.BodyHandlers.ofInputStream());
```

#### 实际应用场景

**1. RESTful API 客户端**
```java
public class ApiClient {
    private final HttpClient client;
    private final String baseUrl;
    private final String authToken;
    
    public ApiClient(String baseUrl, String authToken) {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.baseUrl = baseUrl;
        this.authToken = authToken;
    }
    
    public CompletableFuture<User> getUser(String userId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/" + userId))
                .header("Authorization", "Bearer " + authToken)
                .GET()
                .build();
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        return parseUser(response.body());
                    } else {
                        throw new RuntimeException("API 错误: " + response.statusCode());
                    }
                });
    }
    
    public CompletableFuture<User> createUser(User user) {
        String jsonData = toJson(user);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> parseUser(response.body()));
    }
    
    private User parseUser(String json) {
        // JSON 解析逻辑
        return new User();
    }
    
    private String toJson(User user) {
        // JSON 序列化逻辑
        return "{}";
    }
}
```

**2. 文件下载**
```java
public class FileDownloader {
    private final HttpClient client;
    
    public FileDownloader() {
        this.client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }
    
    public CompletableFuture<Path> downloadFile(String url, Path destination) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofFile(destination))
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        System.out.println("文件下载成功: " + destination);
                        return response.body();
                    } else {
                        throw new RuntimeException("下载失败: " + response.statusCode());
                    }
                });
    }
    
    public void downloadWithProgress(String url, Path destination) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        client.sendAsync(request, HttpResponse.BodyHandlers.ofFile(destination))
                .thenAccept(response -> {
                    System.out.println("下载完成，文件大小: " + 
                        response.headers().firstValue("content-length").orElse("未知"));
                });
    }
}
```

**3. 批量异步请求**
```java
public class BatchApiClient {
    private final HttpClient client;
    
    public BatchApiClient() {
        this.client = HttpClient.newBuilder()
                .executor(Executors.newFixedThreadPool(10))
                .build();
    }
    
    public CompletableFuture<List<String>> fetchMultipleUrls(List<String> urls) {
        List<CompletableFuture<String>> futures = urls.stream()
                .map(url -> {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .GET()
                            .build();
                    
                    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                            .thenApply(HttpResponse::body);
                })
                .toList();
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }
}
```

#### 错误处理

**1. 状态码处理**
```java
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

switch (response.statusCode()) {
    case 200 -> System.out.println("请求成功");
    case 404 -> System.out.println("资源不存在");
    case 500 -> System.out.println("服务器内部错误");
    default -> System.out.println("其他错误: " + response.statusCode());
}
```

**2. 异常处理**
```java
try {
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    // 处理响应
} catch (IOException e) {
    // 网络错误、连接超时等
    System.err.println("网络错误: " + e.getMessage());
} catch (InterruptedException e) {
    // 请求被中断
    System.err.println("请求被中断: " + e.getMessage());
    Thread.currentThread().interrupt();
}
```

**3. 超时处理**
```java
HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.example.com/slow-endpoint"))
        .timeout(Duration.ofSeconds(30))
        .GET()
        .build();
```

#### 与其他 HTTP 客户端对比

| 特性 | Java HttpClient | Spring WebClient | OkHttp | Apache HttpClient |
|------|----------------|------------------|--------|-------------------|
| **依赖** | 无需外部依赖 | Spring 生态一部分 | 需要外部依赖 | 需要外部依赖 |
| **HTTP/2** | ✅ 原生支持 | ✅ 支持 | ✅ 支持 | ✅ 支持 |
| **异步** | ✅ CompletableFuture | ✅ 响应式 | ✅ 回调/异步 | ⚠️ 有限支持 |
| **JSON 处理** | ⚠️ 需要手动处理 | ✅ 自动序列化 | ⚠️ 需要适配器 | ⚠️ 需要手动处理 |
| **连接池** | ✅ 内置 | ✅ 内置 | ✅ 内置 | ✅ 内置 |
| **拦截器** | ❌ 不支持 | ✅ 支持 | ✅ 强大的拦截器 | ✅ 支持 |

#### 在 Spring 项目中的使用建议

**1. 选择 WebClient 的场景**
```java
// Spring Boot 项目推荐使用 WebClient
@Service
public class UserService {
    private final WebClient webClient;
    
    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.example.com")
                .build();
    }
    
    public Mono<User> getUser(String id) {
        return webClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(User.class);
    }
}
```

**2. 选择 HttpClient 的场景**
```java
// 独立工具类或简单 HTTP 调用
@Component
public class HttpUtils {
    private static final HttpClient client = HttpClient.newHttpClient();
    
    public static String get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
```

#### 核心优势

1. **标准化**: 无需外部依赖，减少依赖管理复杂度
2. **现代化**: 原生支持 HTTP/2、异步编程
3. **高性能**: JVM 层面优化，连接池管理
4. **易用性**: 简洁的 API 设计，链式调用
5. **维护性**: 由 JDK 团队维护，长期支持

#### 适用场景

**推荐使用 HttpClient**:
- 新项目且追求最小依赖
- 微服务间的简单 HTTP 调用
- 需要 HTTP/2 支持的高性能场景
- 独立工具或命令行应用

**考虑其他选择**:
- Spring 项目推荐 WebClient（响应式编程）
- 需要复杂拦截器功能时考虑 OkHttp
- 已有项目且 Apache HttpClient 工作良好

Java 11 的 HttpClient 为 Java 生态系统提供了一个现代化的、高性能的 HTTP 客户端解决方案，虽然不能完全替代所有第三方库，但在许多场景下都是很好的选择。

### 15. 集合工厂方法 (Collection Factory Methods) (Java 9)

*   **演进**: Java 9 正式引入。
*   **核心理念**: 提供简洁、直观的方式来创建不可变集合，避免传统方法的冗长代码。

#### 解决的核心问题

**1. 传统创建不可变集合的复杂性**
```java
// Java 9 之前 - 创建不可变列表
List<String> list = Arrays.asList("apple", "banana", "orange");
List<String> immutableList = Collections.unmodifiableList(list);

// Java 9 之前 - 创建不可变集合
Set<String> set = new HashSet<>();
set.add("red");
set.add("green");
set.add("blue");
Set<String> immutableSet = Collections.unmodifiableSet(set);

// Java 9 之前 - 创建不可变映射
Map<String, Integer> map = new HashMap<>();
map.put("apple", 1);
map.put("banana", 2);
map.put("orange", 3);
Map<String, Integer> immutableMap = Collections.unmodifiableMap(map);
```

**2. 代码冗长且容易出错**
```java
// 传统方法的问题
List<String> mutableList = Arrays.asList("a", "b", "c");
List<String> immutableList = Collections.unmodifiableList(mutableList);
// 问题：原始列表仍然可以被修改，影响"不可变"列表

// 更安全但更复杂的做法
List<String> safelist = Collections.unmodifiableList(
    new ArrayList<>(Arrays.asList("a", "b", "c"))
);
```

#### 新的工厂方法

**1. List.of() - 创建不可变列表**
```java
// 创建不可变列表
List<String> fruits = List.of("apple", "banana", "orange");
List<Integer> numbers = List.of(1, 2, 3, 4, 5);
List<String> empty = List.of();
List<String> single = List.of("single");

// 支持泛型推断
var colors = List.of("red", "green", "blue");

// 允许重复元素
List<String> withDuplicates = List.of("a", "b", "a", "c");
```

**2. Set.of() - 创建不可变集合**
```java
// 创建不可变集合
Set<String> colors = Set.of("red", "green", "blue");
Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
Set<String> empty = Set.of();
Set<String> single = Set.of("unique");

// 重复元素会抛出异常
try {
    Set<String> duplicate = Set.of("a", "b", "a"); // IllegalArgumentException
} catch (IllegalArgumentException e) {
    System.out.println("Set 不允许重复元素");
}
```

**3. Map.of() - 创建不可变映射**
```java
// 创建不可变映射（最多 10 个键值对）
Map<String, Integer> ages = Map.of("Alice", 25, "Bob", 30, "Charlie", 35);
Map<String, String> empty = Map.of();
Map<String, String> single = Map.of("key", "value");

// 创建更大的映射使用 Map.ofEntries()
Map<String, String> countries = Map.ofEntries(
    Map.entry("CN", "China"),
    Map.entry("US", "United States"),
    Map.entry("JP", "Japan"),
    Map.entry("DE", "Germany"),
    Map.entry("FR", "France"),
    Map.entry("UK", "United Kingdom"),
    Map.entry("IT", "Italy"),
    Map.entry("ES", "Spain"),
    Map.entry("RU", "Russia"),
    Map.entry("IN", "India"),
    Map.entry("BR", "Brazil")
);
```

#### 核心特性

**1. 真正的不可变性**
```java
List<String> list = List.of("a", "b", "c");
Set<String> set = Set.of("x", "y", "z");
Map<String, Integer> map = Map.of("one", 1, "two", 2);

// 所有修改操作都会抛出 UnsupportedOperationException
try {
    list.add("d");          // 抛出异常
    set.remove("x");        // 抛出异常
    map.put("three", 3);    // 抛出异常
} catch (UnsupportedOperationException e) {
    System.out.println("真正的不可变集合");
}
```

**2. 拒绝 null 值**
```java
// 以下代码都会抛出 NullPointerException
try {
    List.of("a", null, "c");        // 不允许 null 元素
    Set.of("a", null, "c");         // 不允许 null 元素
    Map.of("key", null);            // 不允许 null 值
    Map.of(null, "value");          // 不允许 null 键
} catch (NullPointerException e) {
    System.out.println("工厂方法拒绝 null 值");
}
```

**3. 性能优化**
```java
// 针对不同大小的集合，使用不同的内部实现
List<String> small = List.of("a");              // 单元素优化实现
List<String> medium = List.of("a", "b", "c");   // 小集合优化实现
List<String> large = List.of(/* 很多元素 */);      // 大集合实现

// 内存占用更少，访问速度更快
```

#### 实际应用场景

**1. 常量定义**
```java
public class Constants {
    public static final List<String> SUPPORTED_LANGUAGES = 
        List.of("Java", "Python", "JavaScript", "Go", "Rust");
    
    public static final Set<String> VALID_STATUSES = 
        Set.of("ACTIVE", "INACTIVE", "PENDING", "SUSPENDED");
    
    public static final Map<String, String> ERROR_MESSAGES = Map.of(
        "INVALID_USER", "用户不存在",
        "INVALID_PASSWORD", "密码错误",
        "ACCOUNT_LOCKED", "账户已锁定"
    );
}
```

**2. 方法返回值**
```java
public class UserService {
    public List<String> getDefaultPermissions() {
        return List.of("READ", "WRITE", "DELETE");
    }
    
    public Set<String> getSupportedFormats() {
        return Set.of("JSON", "XML", "CSV", "YAML");
    }
    
    public Map<String, Object> getDefaultSettings() {
        return Map.of(
            "theme", "dark",
            "language", "zh-CN",
            "notifications", true,
            "autoSave", true
        );
    }
}
```

**3. 测试数据**
```java
@Test
void testUserValidation() {
    // 测试数据创建更加简洁
    List<String> validEmails = List.of(
        "user@example.com",
        "admin@company.org",
        "test@domain.net"
    );
    
    Set<String> bannedDomains = Set.of(
        "spam.com",
        "fake.org",
        "invalid.net"
    );
    
    Map<String, String> testUsers = Map.of(
        "admin", "admin123",
        "user", "user456",
        "guest", "guest789"
    );
    
    // 测试逻辑...
}
```

**4. 配置和映射**
```java
public class ApiController {
    private static final Map<String, String> STATUS_CODES = Map.of(
        "SUCCESS", "200",
        "BAD_REQUEST", "400",
        "UNAUTHORIZED", "401",
        "FORBIDDEN", "403",
        "NOT_FOUND", "404",
        "INTERNAL_ERROR", "500"
    );
    
    private static final Set<String> ALLOWED_METHODS = 
        Set.of("GET", "POST", "PUT", "DELETE", "PATCH");
    
    public ResponseEntity<?> handleRequest(String method, String path) {
        if (!ALLOWED_METHODS.contains(method)) {
            return ResponseEntity.status(405).build();
        }
        // 处理请求...
    }
}
```

#### 限制和注意事项

**1. 元素数量限制**
```java
// Map.of() 最多支持 10 个键值对
Map<String, String> map = Map.of(
    "k1", "v1", "k2", "v2", "k3", "v3", "k4", "v4", "k5", "v5",
    "k6", "v6", "k7", "v7", "k8", "v8", "k9", "v9", "k10", "v10"
); // 这是最大限制

// 超过 10 个键值对需要使用 Map.ofEntries()
Map<String, String> largeMap = Map.ofEntries(
    Map.entry("k1", "v1"),
    Map.entry("k2", "v2"),
    // ... 可以有更多条目
    Map.entry("k15", "v15")
);
```

**2. 类型推断**
```java
// 正确的类型推断
var numbers = List.of(1, 2, 3);        // List<Integer>
var strings = Set.of("a", "b", "c");   // Set<String>

// 混合类型需要显式声明
List<Object> mixed = List.of("string", 123, true);
// 或者使用公共父类型
List<Serializable> serializable = List.of("string", 123);
```

**3. 与传统方法的互操作性**
```java
// 工厂方法创建的集合与传统方法创建的集合相等
List<String> factoryList = List.of("a", "b", "c");
List<String> traditionalList = Collections.unmodifiableList(
    Arrays.asList("a", "b", "c")
);

assert factoryList.equals(traditionalList); // true
assert factoryList.hashCode() == traditionalList.hashCode(); // true

// 但内部实现不同
assert factoryList.getClass() != traditionalList.getClass(); // true
```

#### 性能比较

**1. 创建性能**
```java
// 工厂方法更快
List<String> factory = List.of("a", "b", "c");

// 传统方法较慢
List<String> traditional = Collections.unmodifiableList(
    Arrays.asList("a", "b", "c")
);
```

**2. 内存使用**
```java
// 工厂方法创建的集合内存占用更少
// 因为使用了针对特定大小优化的内部实现

// 例如：单元素列表使用特殊的单元素实现
List<String> single = List.of("single");
// 而不是通用的 ArrayList 实现
```

#### 最佳实践

**1. 优先使用工厂方法**
```java
// 好的做法
public static final List<String> COLORS = List.of("red", "green", "blue");

// 避免的做法
public static final List<String> COLORS = Collections.unmodifiableList(
    Arrays.asList("red", "green", "blue")
);
```

**2. 明确表达不可变性**
```java
// 方法签名明确表达返回不可变集合
public List<String> getReadOnlyList() {
    return List.of("item1", "item2", "item3");
}

// 而不是返回可变集合
public List<String> getMutableList() {
    return new ArrayList<>(Arrays.asList("item1", "item2", "item3"));
}
```

**3. 在构造函数中使用**
```java
public class ImmutableConfig {
    private final List<String> allowedValues;
    private final Map<String, String> properties;
    
    public ImmutableConfig(List<String> values, Map<String, String> props) {
        // 创建防御性副本
        this.allowedValues = List.copyOf(values);
        this.properties = Map.copyOf(props);
    }
    
    // 或者直接使用工厂方法
    public static ImmutableConfig createDefault() {
        return new ImmutableConfig(
            List.of("default1", "default2"),
            Map.of("key1", "value1", "key2", "value2")
        );
    }
}
```

#### 相关的 copyOf 方法

**Java 10 引入的 copyOf 方法**
```java
// 从现有集合创建不可变副本
List<String> mutableList = new ArrayList<>();
mutableList.add("a");
mutableList.add("b");

List<String> immutableCopy = List.copyOf(mutableList);
Set<String> immutableSet = Set.copyOf(mutableList);
Map<String, String> sourceMap = new HashMap<>();
Map<String, String> immutableMap = Map.copyOf(sourceMap);

// 如果源集合已经是不可变的，copyOf 会返回原集合
List<String> original = List.of("a", "b", "c");
List<String> copy = List.copyOf(original);
assert original == copy; // true，同一个对象
```

#### 核心优势总结

1. **代码简洁**: 大幅减少创建不可变集合的样板代码
2. **性能优异**: 针对不同大小的集合进行优化
3. **类型安全**: 编译时类型检查，运行时类型一致
4. **真正不可变**: 无法通过任何方式修改集合内容
5. **Null 安全**: 拒绝 null 值，避免 NullPointerException

集合工厂方法是 Java 9 中最实用的特性之一，它让不可变集合的创建变得简单而优雅，是现代 Java 开发中的标准做法。

### 16. Stream, Optional, String 等 API 增强 (Java 9+)

*   **演进**: 从 Java 9 开始，对核心 API 进行了持续的增强和改进。
*   **核心理念**: 让常用的 API 更加强大、便利和直观，提升开发效率。

#### Stream API 增强

**1. takeWhile() 和 dropWhile() (Java 9)**
```java
// takeWhile - 从头开始取元素，直到条件不满足为止
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
List<Integer> taken = numbers.stream()
        .takeWhile(n -> n <= 5)
        .collect(Collectors.toList());
// 结果: [1, 2, 3, 4, 5]

// dropWhile - 从头开始丢弃元素，直到条件不满足为止
List<Integer> dropped = numbers.stream()
        .dropWhile(n -> n <= 5)
        .collect(Collectors.toList());
// 结果: [6, 7, 8, 9, 10]

// 与 filter 的区别
List<Integer> filtered = numbers.stream()
        .filter(n -> n > 5)
        .collect(Collectors.toList());
// 结果: [6, 7, 8, 9, 10] (功能相同，但语义不同)
```

**区别说明：**
- `takeWhile/dropWhile` 是基于顺序的，遇到第一个不满足条件的元素就停止
- `filter` 是基于条件的，会检查所有元素

**2. iterate() 方法重载 (Java 9)**
```java
// 传统的 iterate (无限流，需要 limit)
List<Integer> traditional = Stream.iterate(1, n -> n + 1)
        .limit(5)
        .collect(Collectors.toList());

// 新的 iterate (带条件的有限流)
List<Integer> enhanced = Stream.iterate(1, n -> n <= 10, n -> n + 2)
        .collect(Collectors.toList());
// 结果: [1, 3, 5, 7, 9]
```

**3. ofNullable() 方法 (Java 9)**
```java
// 安全处理可能为 null 的单个元素
String nullableString = null;
String validString = "hello";

long nullCount = Stream.ofNullable(nullableString).count();    // 0
long validCount = Stream.ofNullable(validString).count();      // 1

// 实际应用：过滤集合中的 null 值
List<String> results = Stream.of("a", null, "b", "c")
        .flatMap(Stream::ofNullable)
        .collect(Collectors.toList());
// 结果: [a, b, c]
```

#### Optional API 增强

**1. ifPresentOrElse() 方法 (Java 9)**
```java
Optional<String> optional = Optional.of("Hello");

// 同时处理有值和无值的情况
optional.ifPresentOrElse(
    value -> System.out.println("有值: " + value),
    () -> System.out.println("无值时的处理")
);
```

**2. or() 方法 (Java 9)**
```java
Optional<String> empty = Optional.empty();
Optional<String> backup = Optional.of("backup");

// 提供备选的 Optional
Optional<String> result = empty.or(() -> backup);
// 结果: Optional[backup]
```

**3. stream() 方法 (Java 9)**
```java
List<Optional<String>> optionals = List.of(
    Optional.of("a"),
    Optional.empty(),
    Optional.of("b"),
    Optional.of("c")
);

// 从 Optional 流中提取有值的元素
List<String> values = optionals.stream()
        .flatMap(Optional::stream)
        .collect(Collectors.toList());
// 结果: [a, b, c]
```

**4. isEmpty() 方法 (Java 11)**
```java
Optional<String> empty = Optional.empty();
Optional<String> present = Optional.of("Hello");

// 更直观的空值检查
boolean isEmpty = empty.isEmpty();     // true
boolean isPresent = present.isEmpty(); // false

// 与 isPresent() 相比更加直观
if (optional.isEmpty()) {
    // 处理空值情况
}
```

#### String API 增强

**1. isBlank() 方法 (Java 11)**
```java
String empty = "";
String whitespace = "   ";
String content = "hello";

// 检查字符串是否为空或只包含空白字符
empty.isBlank();      // true
whitespace.isBlank(); // true
content.isBlank();    // false

// 与 isEmpty() 的区别
whitespace.isEmpty(); // false
whitespace.isBlank(); // true
```

**2. lines() 方法 (Java 11)**
```java
String multiline = "line1\nline2\r\nline3\rline4";

// 按行分割，自动处理不同的换行符
List<String> lines = multiline.lines()
        .collect(Collectors.toList());
// 结果: [line1, line2, line3, line4]
```

**3. strip() 系列方法 (Java 11)**
```java
String padded = "   hello world   ";

// 比 trim() 更强大的空白字符处理
String stripped = padded.strip();          // "hello world"
String leading = padded.stripLeading();    // "hello world   "
String trailing = padded.stripTrailing();  // "   hello world"

// 与 trim() 的区别：支持 Unicode 空白字符
String unicode = "\u2000hello\u2000";  // Unicode 空格
String trimmed = unicode.trim();        // 可能无法正确处理
String stripped = unicode.strip();      // 正确处理 Unicode 空格
```

**4. repeat() 方法 (Java 11)**
```java
String pattern = "=";
String separator = pattern.repeat(20);    // "===================="

String greeting = "Hello! ";
String repeated = greeting.repeat(3);     // "Hello! Hello! Hello! "
```

**5. transform() 方法 (Java 12)**
```java
String text = "  Hello World  ";

// 链式变换，增强可读性
String transformed = text.transform(String::strip)
                         .transform(String::toLowerCase)
                         .transform(s -> s.replace(" ", "_"));
// 结果: "hello_world"
```

**6. formatted() 方法 (Java 15)**
```java
String name = "Alice";
int age = 30;

// 更流畅的字符串格式化
String formatted = "Name: %s, Age: %d".formatted(name, age);
// 等价于 String.format("Name: %s, Age: %d", name, age)
```

#### 实际应用场景

**1. 日志处理**
```java
String logData = """
    2024-01-01 10:00:00 INFO Application started
    2024-01-01 10:00:01 DEBUG Loading configuration
    2024-01-01 10:00:02 ERROR Failed to connect to database
    2024-01-01 10:00:03 INFO Retrying connection
    2024-01-01 10:00:04 INFO Database connected successfully
    """;

// 提取错误日志，直到数据库连接成功
List<String> errorLogs = logData.lines()
        .map(String::strip)
        .filter(line -> !line.isBlank())
        .takeWhile(line -> !line.contains("Database connected"))
        .filter(line -> line.contains("ERROR"))
        .collect(Collectors.toList());
```

**2. 配置处理**
```java
Map<String, String> config = Map.of(
    "database.url", "jdbc:mysql://localhost:3306/mydb",
    "database.username", "admin",
    "database.password", "",
    "app.name", "MyApplication"
);

// 安全地检查配置项
Optional<String> dbPassword = Optional.ofNullable(config.get("database.password"))
        .filter(pwd -> !pwd.isBlank());

dbPassword.ifPresentOrElse(
    pwd -> System.out.println("数据库密码已配置"),
    () -> System.out.println("警告: 数据库密码未配置")
);
```

**3. 数据验证**
```java
List<String> userInputs = List.of(
    "alice@example.com",
    "",
    "   ",
    "bob@test.com",
    "invalid-email",
    "charlie@domain.org"
);

// 过滤并验证邮箱地址
List<String> validEmails = userInputs.stream()
        .filter(input -> !input.isBlank())
        .map(String::strip)
        .filter(email -> email.contains("@") && email.contains("."))
        .collect(Collectors.toList());
```

**4. 报告生成**
```java
List<String> items = List.of("项目A", "项目B", "项目C");

// 使用 transform 和 repeat 生成格式化报告
String report = "报告标题".transform(title -> 
    "=".repeat(20) + "\n" + 
    title + "\n" + 
    "=".repeat(20) + "\n"
) + items.stream()
    .map(item -> "- " + item)
    .collect(Collectors.joining("\n")) + "\n" +
    "=".repeat(20);
```

**5. 流式数据处理**
```java
// 处理分页数据，直到遇到结束标记
List<String> processedData = dataStream
        .takeWhile(data -> !data.equals("END"))
        .map(String::strip)
        .filter(data -> !data.isBlank())
        .collect(Collectors.toList());
```

#### 其他重要的 API 增强

**1. CompletableFuture 增强 (Java 9+)**
```java
// 超时处理
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    // 长时间运行的任务
    return "result";
}).orTimeout(5, TimeUnit.SECONDS);

// 延迟执行
CompletableFuture<String> delayed = CompletableFuture
        .delayedExecutor(2, TimeUnit.SECONDS)
        .execute(() -> System.out.println("Delayed execution"));
```

**2. Process API 增强 (Java 9)**
```java
// 获取当前进程信息
ProcessHandle current = ProcessHandle.current();
ProcessHandle.Info info = current.info();

System.out.println("PID: " + current.pid());
System.out.println("命令: " + info.command().orElse("Unknown"));
System.out.println("用户: " + info.user().orElse("Unknown"));
```

#### 最佳实践

**1. 优先使用新的 API**
```java
// 好的做法
String result = input.strip();
if (!input.isBlank()) {
    // 处理逻辑
}

// 避免的做法
String result = input.trim();
if (!input.isEmpty()) {
    // 处理逻辑
}
```

**2. 合理使用 takeWhile/dropWhile**
```java
// 适合使用 takeWhile 的场景：有序数据的前缀处理
List<Integer> scores = List.of(95, 88, 92, 76, 84, 90);
List<Integer> excellentScores = scores.stream()
        .sorted(Comparator.reverseOrder())
        .takeWhile(score -> score >= 90)
        .collect(Collectors.toList());
```

**3. 链式操作的可读性**
```java
// 使用 transform 提高可读性
String result = input
        .transform(String::strip)
        .transform(String::toLowerCase)
        .transform(s -> s.replace(" ", "_"));

// 而不是嵌套调用
String result = input.strip().toLowerCase().replace(" ", "_");
```

#### 核心优势总结

1. **简洁性**: 减少样板代码，提高开发效率
2. **直观性**: API 设计更加直观，易于理解和使用
3. **功能性**: 提供了更多实用的功能，覆盖常见使用场景
4. **一致性**: 保持与现有 API 的一致性，学习成本低
5. **性能**: 很多新方法都经过性能优化

这些 API 增强虽然看起来是小的改进，但在日常开发中能显著提升代码的可读性和开发效率，是现代 Java 开发的重要组成部分。

### 17. Stream 聚合器 (Stream Gatherers) (Java 24, 正式)

*   **演进**: Java 22 引入预览版，Java 24 正式发布。
*   **核心理念**: 提供自定义 Stream 中间操作的能力，让开发者能够创建复杂的、有状态的流处理逻辑。

#### 核心概念

Stream Gatherers 是一种新的 Stream 操作类型，它允许开发者创建自定义的中间操作。与现有的 `map()`, `filter()`, `reduce()` 等操作相比，Gatherers 提供了更高的灵活性和自定义能力。

```java
// 基本语法
stream.gather(gatherer).collect(collector)

// 等价于传统的
stream.someCustomOperation().collect(collector)
```

#### 与 Collector 的区别

| 特性 | Collector | Gatherer |
|------|-----------|----------|
| **作用阶段** | 终结操作 | 中间操作 |
| **数据流向** | 多对一 | 一对多或多对多 |
| **状态管理** | 累积状态 | 流式状态 |
| **组合性** | 有限 | 高度可组合 |
| **无限流支持** | 不支持 | 支持 |

#### 内置 Gatherers

**1. 滑动窗口 (sliding)**
```java
// 滑动窗口处理
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);

List<List<Integer>> windows = numbers.stream()
        .gather(Gatherers.sliding(3))
        .collect(Collectors.toList());
// 结果: [[1,2,3], [2,3,4], [3,4,5], [4,5,6], [5,6,7], [6,7,8]]
```

**2. 固定窗口 (windowed)**
```java
// 固定大小窗口
List<List<Integer>> fixedWindows = numbers.stream()
        .gather(Gatherers.windowed(3))
        .collect(Collectors.toList());
// 结果: [[1,2,3], [4,5,6], [7,8]]
```

**3. 扫描 (scan)**
```java
// 累积计算
List<Integer> nums = List.of(1, 2, 3, 4, 5);

List<Integer> cumulativeSum = nums.stream()
        .gather(Gatherers.scan(0, Integer::sum))
        .collect(Collectors.toList());
// 结果: [0, 1, 3, 6, 10, 15]
```

**4. 去重 (distinct)**
```java
// 基于自定义键的去重
List<Person> people = List.of(
    new Person("Alice", 25),
    new Person("Bob", 30),
    new Person("Alice", 28)  // 重复的名字
);

List<Person> distinctByName = people.stream()
        .gather(Gatherers.distinct(Person::getName))
        .collect(Collectors.toList());
// 结果: [Person("Alice", 25), Person("Bob", 30)]
```

#### 自定义 Gatherers

**1. 基本结构**
```java
// 自定义 Gatherer 的基本结构
public static <T, R> Gatherer<T, ?, R> myGatherer() {
    return Gatherer.of(
        supplier,     // 状态初始化
        integrator,   // 元素处理逻辑
        finisher      // 最终处理（可选）
    );
}
```

**2. 批处理 Gatherer**
```java
// 自定义批处理 Gatherer
public static <T> Gatherer<T, ?, List<T>> batching(int batchSize) {
    return Gatherer.of(
        // 状态初始化：创建一个 ArrayList 来存储当前批次
        ArrayList::new,
        
        // 元素处理逻辑
        (batch, element, downstream) -> {
            batch.add(element);
            if (batch.size() == batchSize) {
                // 批次满了，发送到下游并清空
                downstream.push(new ArrayList<>(batch));
                batch.clear();
            }
            return true; // 继续处理
        },
        
        // 最终处理：发送剩余的元素
        (batch, downstream) -> {
            if (!batch.isEmpty()) {
                downstream.push(batch);
            }
        }
    );
}

// 使用示例
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
List<List<Integer>> batches = numbers.stream()
        .gather(batching(3))
        .collect(Collectors.toList());
// 结果: [[1,2,3], [4,5,6], [7,8,9]]
```

**3. 移动平均 Gatherer**
```java
// 移动平均计算
public static Gatherer<Double, ?, Double> movingAverage(int windowSize) {
    return Gatherer.of(
        // 状态：使用循环缓冲区存储窗口数据
        () -> new MovingAverageState(windowSize),
        
        // 处理逻辑
        (state, element, downstream) -> {
            state.add(element);
            if (state.isFull()) {
                downstream.push(state.getAverage());
            }
            return true;
        }
    );
}

// 使用示例
List<Double> prices = List.of(100.0, 102.0, 101.0, 103.0, 105.0, 104.0, 106.0);
List<Double> movingAvg = prices.stream()
        .gather(movingAverage(3))
        .collect(Collectors.toList());
// 结果: [101.0, 102.0, 103.0, 104.0, 105.0]
```

**4. 状态机 Gatherer**
```java
// 状态机处理
public static <T> Gatherer<T, ?, StateTransition<T>> stateMachine(
        StateMachine<T> machine) {
    return Gatherer.of(
        // 初始状态
        machine::getInitialState,
        
        // 状态转换
        (currentState, input, downstream) -> {
            StateTransition<T> transition = machine.process(currentState, input);
            downstream.push(transition);
            return true;
        }
    );
}

// 使用示例
List<String> events = List.of("START", "PROCESS", "ERROR", "RETRY", "SUCCESS");
List<StateTransition<String>> transitions = events.stream()
        .gather(stateMachine(new WorkflowStateMachine()))
        .collect(Collectors.toList());
```

#### 实际应用场景

**1. 数据分析**
```java
// 实时数据流分析
public class DataAnalyzer {
    // 异常检测 Gatherer
    public static Gatherer<Double, ?, Alert> anomalyDetector(double threshold) {
        return Gatherer.of(
            () -> new AnomalyDetectorState(threshold),
            (state, value, downstream) -> {
                if (state.isAnomaly(value)) {
                    downstream.push(new Alert("异常值检测", value));
                }
                state.update(value);
                return true;
            }
        );
    }
    
    // 使用示例
    public void analyzeData(Stream<Double> dataStream) {
        List<Alert> alerts = dataStream
                .gather(anomalyDetector(2.0))
                .collect(Collectors.toList());
    }
}
```

**2. 实时流处理**
```java
// 事件窗口处理
public class EventProcessor {
    public static <T> Gatherer<T, ?, List<T>> timeWindow(Duration duration) {
        return Gatherer.of(
            () -> new TimeWindowState<T>(duration),
            (state, event, downstream) -> {
                state.add(event);
                if (state.shouldFlush()) {
                    downstream.push(state.getAndClearEvents());
                }
                return true;
            }
        );
    }
    
    // 使用示例
    public void processEvents(Stream<Event> eventStream) {
        eventStream
                .gather(timeWindow(Duration.ofMinutes(5)))
                .forEach(this::processEventBatch);
    }
}
```

**3. 批量数据处理**
```java
// 数据库批量操作
public class DatabaseProcessor {
    public static <T> Gatherer<T, ?, Void> batchInsert(
            int batchSize, Consumer<List<T>> insertOperation) {
        return Gatherer.of(
            ArrayList::new,
            (batch, item, downstream) -> {
                batch.add(item);
                if (batch.size() >= batchSize) {
                    insertOperation.accept(new ArrayList<>(batch));
                    batch.clear();
                }
                return true;
            },
            (batch, downstream) -> {
                if (!batch.isEmpty()) {
                    insertOperation.accept(batch);
                }
            }
        );
    }
    
    // 使用示例
    public void saveUsers(Stream<User> userStream) {
        userStream
                .gather(batchInsert(1000, this::batchInsertUsers))
                .collect(Collectors.toList());
    }
}
```

**4. 数据转换管道**
```java
// ETL 处理管道
public class ETLProcessor {
    public static <T> Gatherer<T, ?, T> validate(Predicate<T> validator) {
        return Gatherer.of(
            () -> null,
            (state, item, downstream) -> {
                if (validator.test(item)) {
                    downstream.push(item);
                } else {
                    // 记录验证失败的项目
                    logValidationFailure(item);
                }
                return true;
            }
        );
    }
    
    public static <T, R> Gatherer<T, ?, R> transform(Function<T, R> transformer) {
        return Gatherer.of(
            () -> null,
            (state, item, downstream) -> {
                try {
                    R transformed = transformer.apply(item);
                    downstream.push(transformed);
                } catch (Exception e) {
                    logTransformationError(item, e);
                }
                return true;
            }
        );
    }
    
    // 使用示例
    public void processData(Stream<RawData> dataStream) {
        List<ProcessedData> results = dataStream
                .gather(validate(this::isValidData))
                .gather(transform(this::transformData))
                .gather(batching(100))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
```

#### 性能优化

**1. 内存效率**
```java
// 内存高效的滑动窗口
public static <T> Gatherer<T, ?, List<T>> efficientSlidingWindow(int size) {
    return Gatherer.of(
        () -> new CircularBuffer<T>(size),  // 使用循环缓冲区
        (buffer, element, downstream) -> {
            buffer.add(element);
            if (buffer.isFull()) {
                downstream.push(buffer.toList());
            }
            return true;
        }
    );
}
```

**2. 并行处理支持**
```java
// 支持并行处理的 Gatherer
public static <T> Gatherer<T, ?, T> parallelProcessor(
        Function<T, T> processor, int parallelism) {
    return Gatherer.of(
        () -> new ParallelProcessorState<T>(processor, parallelism),
        (state, item, downstream) -> {
            CompletableFuture<T> future = state.processAsync(item);
            future.thenAccept(downstream::push);
            return true;
        }
    );
}
```

#### 与现有 API 的互操作

**1. 与 Collector 组合**
```java
// Gatherer 与 Collector 的组合使用
Map<String, List<Integer>> groupedBatches = numbers.stream()
        .gather(batching(3))
        .collect(Collectors.groupingBy(
            batch -> "batch_" + batch.size()
        ));
```

**2. 与其他 Stream 操作组合**
```java
// 复杂的处理管道
List<Double> result = dataStream
        .filter(x -> x > 0)
        .gather(movingAverage(5))
        .map(Math::sqrt)
        .gather(anomalyDetector(2.0))
        .map(Alert::getValue)
        .collect(Collectors.toList());
```

#### 最佳实践

**1. 状态管理**
```java
// 正确的状态管理
public static <T> Gatherer<T, ?, T> statefulProcessor() {
    return Gatherer.of(
        // 每个线程都有自己的状态实例
        ProcessorState::new,
        
        // 状态应该是线程安全的或每个实例独立的
        (state, item, downstream) -> {
            state.process(item);
            downstream.push(state.getResult());
            return true;
        }
    );
}
```

**2. 错误处理**
```java
// 健壮的错误处理
public static <T> Gatherer<T, ?, T> resilientProcessor() {
    return Gatherer.of(
        () -> new ProcessorState(),
        (state, item, downstream) -> {
            try {
                T result = state.process(item);
                downstream.push(result);
                return true;
            } catch (Exception e) {
                // 记录错误但继续处理
                logError(item, e);
                return true;
            }
        }
    );
}
```

**3. 资源管理**
```java
// 正确的资源管理
public static <T> Gatherer<T, ?, T> resourceManagedProcessor() {
    return Gatherer.of(
        ResourceState::new,
        (state, item, downstream) -> {
            T result = state.process(item);
            downstream.push(result);
            return true;
        },
        // 确保资源被正确释放
        (state, downstream) -> {
            state.close();
        }
    );
}
```

#### 核心优势

1. **高度可定制**: 可以创建复杂的、有状态的流处理逻辑
2. **内存效率**: 支持流式处理，不需要将所有数据加载到内存
3. **可组合性**: 可以与其他 Stream 操作无缝组合
4. **无限流支持**: 可以处理无限数据流
5. **类型安全**: 编译时类型检查，减少运行时错误
6. **性能优化**: 专为高性能流处理设计

Stream Gatherers 为 Java 的流处理能力带来了革命性的提升，使得开发者可以构建更加复杂和高效的数据处理管道。它是现代 Java 流处理的重要组成部分，特别适合大数据处理、实时分析和复杂的数据转换场景。

---

## 四、 JVM 与底层

### 18. 模块化系统 (Project Jigsaw) (Java 9)

*   **演进**: Java 9 正式引入，是 Java 历史上最重要的架构改进之一。
*   **核心理念**: 提供模块化的代码组织方式，实现强封装、可靠配置和可扩展性。

#### 解决的核心问题

**1. 传统 Classpath 的问题**
 1. 类路径地狱：所有JAR文件平铺在classpath上
 2. 包冲突：多个JAR包含相同的类，使用"先找到的"规则
 3. 运行时依赖错误：缺少JAR包时运行时才发现
 4. 无封装性：所有public类都可以被访问
 5. 巨大的运行时：即使简单应用也需要完整的JRE

**2. 依赖管理复杂性**
 传统的依赖管理问题
 - 传递性依赖不明确
 - 版本冲突难以解决
 - 循环依赖检测困难
 - 未使用的依赖难以清理

#### 模块系统核心概念

**1. 模块声明文件 (module-info.java)**
```java
// 基本模块声明
module com.example.myapp {
    // 依赖声明
    requires java.base;          // 隐式依赖，可省略
    requires java.logging;       // 显式依赖
    requires transitive java.sql;  // 传递性依赖
    
    // 导出包
    exports com.example.myapp.api;
    exports com.example.myapp.util to com.example.client;
    
    // 开放包用于反射
    opens com.example.myapp.internal to com.fasterxml.jackson.databind;
    opens com.example.myapp.entity;
    
    // 服务提供和使用
    provides com.example.myapp.spi.Service 
        with com.example.myapp.impl.ServiceImpl;
    uses com.example.myapp.spi.Service;
}
```

**2. 模块指令详解**

| 指令 | 作用 | 示例 |
|------|------|------|
| `requires` | 声明对其他模块的依赖 | `requires java.logging;` |
| `requires transitive` | 传递性依赖 | `requires transitive java.sql;` |
| `exports` | 导出包供其他模块使用 | `exports com.example.api;` |
| `exports...to` | 限定导出给特定模块 | `exports com.example.internal to com.example.test;` |
| `opens` | 开放包用于反射访问 | `opens com.example.entity;` |
| `opens...to` | 限定开放给特定模块 | `opens com.example.config to spring.core;` |
| `uses` | 声明使用某个服务 | `uses com.example.spi.Service;` |
| `provides...with` | 提供服务实现 | `provides Service with ServiceImpl;` |

#### 模块类型

**1. 系统模块**
```java
// JDK 内置模块
java.base          // 基础模块，所有模块隐式依赖
java.logging       // 日志模块
java.sql           // 数据库模块
java.xml           // XML 处理模块
java.net.http      // HTTP 客户端模块
java.desktop       // 桌面应用模块
```

**2. 应用模块**
```java
// 用户定义的模块
module com.example.service {
    requires java.base;
    requires java.logging;
    
    exports com.example.service.api;
}
```

**3. 自动模块**
```java
// 将传统JAR作为模块使用
// JAR文件名：mysql-connector-java-8.0.28.jar
// 自动模块名：mysql.connector.java
module com.example.app {
    requires mysql.connector.java;  // 自动模块
}
```

**4. 未命名模块**
```java
// 传统classpath上的所有类
// 当前项目就是未命名模块
// 可以访问所有导出的包和classpath上的类
```

#### 强封装机制

**1. 包级别的封装**
```java
// 模块A
module com.example.modulea {
    exports com.example.modulea.api;        // 公开API
    // com.example.modulea.internal 不导出，外部无法访问
}

// 模块B
module com.example.moduleb {
    requires com.example.modulea;
    
    // 可以访问
    import com.example.modulea.api.PublicClass;
    
    // 编译错误：无法访问
    // import com.example.modulea.internal.InternalClass;
}
```

**2. 反射访问控制**
```java
module com.example.entity {
    // 开放给框架使用
    opens com.example.entity.model to 
        com.fasterxml.jackson.databind,
        org.hibernate.orm.core;
    
    // 完全开放
    opens com.example.entity.dto;
}
```

#### 服务加载机制

**1. 服务接口定义**
```java
// 服务接口模块
module com.example.service.api {
    exports com.example.service.api;
}

// 服务接口
package com.example.service.api;
public interface DataProcessor {
    void process(String data);
}
```

**2. 服务实现**
```java
// 服务实现模块
module com.example.service.impl {
    requires com.example.service.api;
    
    provides com.example.service.api.DataProcessor
        with com.example.service.impl.JsonProcessor,
             com.example.service.impl.XmlProcessor;
}
```

**3. 服务消费**
```java
// 服务消费模块
module com.example.client {
    requires com.example.service.api;
    uses com.example.service.api.DataProcessor;
}

// 服务使用
ServiceLoader<DataProcessor> loader = ServiceLoader.load(DataProcessor.class);
for (DataProcessor processor : loader) {
    processor.process(data);
}
```

#### 编译和运行

**1. 模块化编译**
```bash
# 项目结构
src/
├── com.example.service/
│   ├── module-info.java
│   └── com/example/service/
├── com.example.client/
│   ├── module-info.java
│   └── com/example/client/

# 编译
javac -d out \
  --module-source-path src \
  src/com.example.service/module-info.java \
  src/com.example.service/com/example/service/*.java \
  src/com.example.client/module-info.java \
  src/com.example.client/com/example/client/*.java
```

**2. 模块化运行**
```bash
# 运行模块
java --module-path out \
  --module com.example.client/com.example.client.Main

# 添加模块到启动时
java --module-path out \
  --add-modules com.example.service \
  --module com.example.client/com.example.client.Main
```

#### 实际应用场景

**1. 微服务架构**
```java
// 订单服务模块
module com.company.order.service {
    requires com.company.common.api;
    requires java.net.http;
    
    exports com.company.order.api;
    
    provides com.company.common.api.Service
        with com.company.order.service.OrderService;
}

// 用户服务模块
module com.company.user.service {
    requires com.company.common.api;
    requires java.sql;
    
    exports com.company.user.api;
    
    provides com.company.common.api.Service
        with com.company.user.service.UserService;
}
```

**2. 库的模块化**
```java
// 工具库核心模块
module com.company.utils.core {
    exports com.company.utils.core.string;
    exports com.company.utils.core.collection;
}

// 工具库扩展模块
module com.company.utils.json {
    requires com.company.utils.core;
    requires com.fasterxml.jackson.databind;
    
    exports com.company.utils.json;
}
```

**3. 插件系统**
```java
// 插件接口
module com.app.plugin.api {
    exports com.app.plugin.api;
}

// 插件实现
module com.app.plugin.image {
    requires com.app.plugin.api;
    
    provides com.app.plugin.api.Plugin
        with com.app.plugin.image.ImagePlugin;
}

// 主应用
module com.app.main {
    requires com.app.plugin.api;
    uses com.app.plugin.api.Plugin;
}
```

#### 与 Spring Boot 的集成

**1. Spring Boot 模块化支持**
```java
// Spring Boot 应用模块
module com.example.springapp {
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires spring.data.jpa;
    
    // 开放给 Spring 框架
    opens com.example.springapp.controller to spring.core;
    opens com.example.springapp.service to spring.core;
    opens com.example.springapp.entity to 
        spring.core,
        org.hibernate.orm.core;
    
    exports com.example.springapp.api;
}
```

**2. 自动配置处理**
```java
// Spring Boot 需要特殊处理
// 1. 开放包给 Spring 框架
// 2. 处理自动配置的反射访问
// 3. 配置类路径扫描
```

#### 迁移策略

**1. 自下而上迁移**
```java
// 第一步：模块化依赖库
module com.company.common {
    exports com.company.common.api;
    exports com.company.common.util;
}

// 第二步：模块化应用层
module com.company.app {
    requires com.company.common;
    requires java.net.http;
}
```

**2. 混合模式**
```java
// 部分模块化：新代码使用模块，旧代码保持 classpath
java --module-path modules \
     --class-path legacy-libs/*.jar \
     --add-modules com.company.newmodule \
     --module com.company.app/com.company.app.Main
```

**3. 自动模块过渡**
```java
// 将传统JAR作为自动模块使用
module com.company.app {
    requires mysql.connector.java;  // 自动模块
    requires spring.boot;           // 自动模块
}
```

#### 工具支持

**1. jdeps - 依赖分析**
```bash
# 分析JAR依赖
jdeps --class-path libs/*.jar myapp.jar

# 生成模块信息
jdeps --generate-module-info out myapp.jar

# 分析模块依赖
jdeps --module-path modules --module com.example.app
```

**2. jlink - 自定义运行时**
```bash
# 创建自定义JRE
jlink --module-path modules:$JAVA_HOME/jmods \
      --add-modules com.example.app \
      --output custom-jre

# 运行自定义JRE
./custom-jre/bin/java --module com.example.app/com.example.Main
```

**3. IDE 支持**
- IntelliJ IDEA: 完整的模块化支持
- Eclipse: 通过插件支持
- VS Code: 通过扩展支持

#### 最佳实践

**1. 模块设计原则**
```java
// 单一职责：每个模块有明确的职责
module com.company.user.service {
    // 只处理用户相关功能
}

// 稳定抽象：API模块应该稳定
module com.company.user.api {
    // 只包含接口和数据传输对象
}

// 最小依赖：只依赖必要的模块
module com.company.user.impl {
    requires com.company.user.api;
    // 避免不必要的依赖
}
```

**2. 包导出策略**
```java
module com.company.service {
    // 导出 API 包
    exports com.company.service.api;
    
    // 有选择地导出工具包
    exports com.company.service.util to 
        com.company.client,
        com.company.test;
    
    // 不导出实现包
    // com.company.service.impl 保持内部
}
```

**3. 版本管理**
```java
// 使用语义化版本
module com.company.service {
    requires com.company.api;  // 使用稳定版本
    
    // 避免快照版本的传递依赖
    requires transitive com.company.stable.api;
}
```

#### 性能影响

**1. 启动时间**
- 模块化可以减少启动时间
- 更少的类加载和初始化
- 更精确的依赖解析

**2. 内存使用**
- 减少不必要的类加载
- 更好的内存布局
- 自定义运行时减少内存占用

**3. 安全性**
- 强封装提高安全性
- 减少攻击面
- 更好的权限控制

#### 核心优势

1. **强封装**: 明确的模块边界和访问控制
2. **可靠配置**: 编译时依赖检查
3. **可扩展性**: 服务加载机制支持插件
4. **性能优化**: 减少运行时大小和启动时间
5. **安全性**: 更好的封装性和权限控制

#### 模块导入声明 (Java 23 预览)

**传统包导入的痛点：**
```java
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
// ... 当模块包含几十个包时，导入变得冗长
```

**模块导入声明的解决方案：**
```java
import module java.base;  // 一次性导入整个模块的所有导出包

public class Example {
    public void method() {
        List<String> list = List.of("a", "b");  // 直接使用，无需逐个导入
        Map<String, Integer> map = Map.of("key", 1);
    }
}
```

**模块导入的优势：**
- **简洁性**：避免冗长的包导入列表
- **一致性**：确保使用模块中的相关类时保持一致
- **维护性**：模块升级时减少导入声明的维护工作

**使用限制：**
- 仅在 Java 23+ 作为预览特性提供
- 需要启用预览特性：`--enable-preview`
- 不能与通配符导入（`import java.util.*`）混用
- 只能导入模块明确导出的包

#### 第三方库的模块化现状

**✅ 已支持模块的主流库：**

- Spring Framework (5.0+)
- Jackson (2.9+)
- JUnit 5
- SLF4J & Logback

**⚠️ 兼容性解决方案：**
```java
module my.app {
    requires spring.boot;           // 已模块化的库
    requires java.sql;              // 自动模块（未模块化的 JDBC 驱动）
    requires some.legacy.library;   // 通过自动模块机制使用遗留库
}
```

#### 实际应用建议

**何时使用模块？**
- ✅ 新的大型项目，特别是需要清晰架构边界的项目
- ✅ 构建可复用的库或框架
- ✅ 对安全性和封装性要求高的企业应用

**何时可以不用模块？**
- ✅ 现有项目（迁移成本高）
- ✅ 小型项目或原型开发
- ✅ 团队对模块系统不熟悉的情况

**渐进式采用策略：**
```java
// 第一步：在现有项目中添加 module-info.java（可选）
module com.example.demo {
    requires spring.boot;
    requires java.sql;
    exports com.example.api;
}

// 第二步：保持传统导入方式
import org.springframework.boot.SpringApplication;

// 第三步：根据需要尝试模块导入（Java 23+）
import module spring.boot.autoconfigure;
```

模块化系统是 Java 平台的重要进步，Java 23 的模块导入声明使其更加易用。虽然学习曲线较陡峭，但为大型应用提供了更好的架构支持和运行时优化。

### 19. 新的垃圾收集器 (ZGC & Shenandoah) (Java 15)

* **演进**: 两者均在 Java 15 成为生产可用。
* **核心理念**: 现代的、并发的、低延迟的垃圾收集器 (GC)。
* **核心优势**: 旨在将 GC 的“Stop-The-World” (STW) 停顿时间控制在**毫秒级甚至亚毫秒级**，并且该停顿时间  **不随堆内存大小的增加而显著增加**。这解决了 G1 等传统 GC 在处理大堆内存时可能出现的长时停顿问题。
* **适用场景**: 对延迟极其敏感、且使用大堆内存（通常 4GB 以上）的应用。例如：金融交易平台、实时数据分析、大型电商网站、需要稳定响应时间的微服务等。
* **如何启用**: 这是配置层面的特性，通过 JVM 启动参数启用。
  * **ZGC**: `java -XX:+UseZGC -jar my-app.jar`
  * **Shenandoah**: `java -XX:+UseShenandoahGC -jar my-app.jar`

### 20. 外部函数与内存 API (Foreign Function & Memory API) (Java 22, 正式)

*   **演进**: 经历多个预览版本后，在 Java 22 成为正式特性。
*   **核心理念**: 为 Java 程序提供与非 Java 代码（如 C/C++）交互的标准化、高效、安全的方式。替代传统的 JNI（Java Native Interface）。
*   **核心优势**:
    *   **性能更优**: 避免了 JNI 的大部分开销，调用本地代码的性能接近直接调用。
    *   **类型安全**: 在编译时就能发现类型不匹配的错误。
    *   **内存管理**: 提供精确的非堆内存控制，避免内存泄漏。
    *   **跨平台**: 统一的 API 可在不同平台上使用。

#### 主要组件

**1. Foreign Function Interface (FFI)**
*   允许 Java 调用本地库中的函数
*   无需编写 JNI 代码，直接映射本地函数

**2. Memory API**
*   管理非堆内存（off-heap memory）
*   提供内存段（MemorySegment）的概念
*   自动内存管理和安全边界检查

#### 核心 API 类型

```java
// 主要接口和类
import java.lang.foreign.*;

// 1. MemorySegment - 内存段
MemorySegment segment = MemorySegment.allocateNative(1024);

// 2. MemoryLayout - 内存布局
MemoryLayout layout = MemoryLayout.structLayout(
    ValueLayout.JAVA_INT.withName("x"),
    ValueLayout.JAVA_INT.withName("y")
);

// 3. Linker - 链接器
Linker linker = Linker.nativeLinker();

// 4. FunctionDescriptor - 函数描述符
FunctionDescriptor descriptor = FunctionDescriptor.of(
    ValueLayout.JAVA_INT,  // 返回类型
    ValueLayout.JAVA_INT,  // 参数类型
    ValueLayout.JAVA_INT
);

// 5. SymbolLookup - 符号查找
SymbolLookup lookup = SymbolLookup.loaderLookup();
```

#### 实际应用场景

**1. 调用 C 标准库函数**
```java
// 调用 C 标准库的 strlen 函数
import java.lang.foreign.*;

public class StringLengthExample {
    public static void main(String[] args) throws Throwable {
        // 1. 获取链接器
        Linker linker = Linker.nativeLinker();
        
        // 2. 查找 strlen 函数
        SymbolLookup stdlib = linker.defaultLookup();
        MemorySegment strlen = stdlib.find("strlen").orElseThrow();
        
        // 3. 描述函数签名：size_t strlen(const char*)
        FunctionDescriptor descriptor = FunctionDescriptor.of(
            ValueLayout.JAVA_LONG,      // size_t 返回值
            ValueLayout.ADDRESS         // const char* 参数
        );
        
        // 4. 创建方法句柄
        MethodHandle strlenHandle = linker.downcallHandle(strlen, descriptor);
        
        // 5. 创建 C 字符串
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment cString = arena.allocateUtf8String("Hello, FFI!");
            
            // 6. 调用 C 函数
            long length = (long) strlenHandle.invoke(cString);
            System.out.println("字符串长度: " + length); // 输出: 12
        }
    }
}
```

**2. 结构体操作**
```java
// 处理 C 结构体
public class StructExample {
    // 定义 C 结构体布局：struct Point { int x; int y; };
    private static final MemoryLayout POINT_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("x"),
        ValueLayout.JAVA_INT.withName("y")
    );
    
    public static void main(String[] args) {
        try (Arena arena = Arena.ofConfined()) {
            // 分配结构体内存
            MemorySegment point = arena.allocate(POINT_LAYOUT);
            
            // 设置字段值
            point.set(ValueLayout.JAVA_INT, 0, 10);  // x = 10
            point.set(ValueLayout.JAVA_INT, 4, 20);  // y = 20
            
            // 读取字段值
            int x = point.get(ValueLayout.JAVA_INT, 0);
            int y = point.get(ValueLayout.JAVA_INT, 4);
            
            System.out.println("Point: (" + x + ", " + y + ")");
        }
    }
}
```

**3. 数组和指针操作**
```java
// 处理数组和指针
public class ArrayExample {
    public static void main(String[] args) {
        try (Arena arena = Arena.ofConfined()) {
            // 分配 int 数组
            int[] javaArray = {1, 2, 3, 4, 5};
            MemorySegment nativeArray = arena.allocateArray(
                ValueLayout.JAVA_INT, 
                javaArray.length
            );
            
            // 复制数据到本地内存
            MemorySegment.copy(javaArray, 0, nativeArray, 
                ValueLayout.JAVA_INT, 0, javaArray.length);
            
            // 读取本地内存数据
            for (int i = 0; i < javaArray.length; i++) {
                int value = nativeArray.getAtIndex(ValueLayout.JAVA_INT, i);
                System.out.println("arr[" + i + "] = " + value);
            }
        }
    }
}
```

#### 内存管理

**Arena 模式**
```java
// 使用 Arena 进行资源管理
public class MemoryManagementExample {
    public static void managedMemory() {
        // 自动资源管理
        try (Arena arena = Arena.ofConfined()) {
            // 在 arena 中分配的所有内存会自动释放
            MemorySegment buffer1 = arena.allocate(1024);
            MemorySegment buffer2 = arena.allocate(2048);
            MemorySegment string = arena.allocateUtf8String("Hello");
            
            // 使用内存...
            
        } // arena 关闭时，所有分配的内存自动释放
    }
    
    public static void sharedMemory() {
        // 共享 arena，需要手动管理生命周期
        Arena shared = Arena.ofShared();
        
        try {
            MemorySegment buffer = shared.allocate(1024);
            // 使用内存...
        } finally {
            shared.close(); // 手动关闭
        }
    }
}
```

#### 安全性和限制

**1. 内存安全**
*   内存段有明确的边界，越界访问会抛出异常
*   Arena 关闭后，相关内存段变为无效
*   提供了安全的内存访问检查

**2. 访问限制**
```java
// 需要特殊的模块声明或 JVM 参数
// --enable-native-access=ALL-UNNAMED
// 或在 module-info.java 中：
module myapp {
    requires java.base;
    // 启用本地访问
}
```

**3. 平台兼容性**
*   不同平台上的本地库需要分别处理
*   Windows(.dll)、Linux(.so)、macOS(.dylib)

#### 与 JNI 对比

| 特性 | JNI | Foreign Function API |
|------|-----|----------------------|
| 性能 | 中等（有调用开销） | 高（接近本地调用） |
| 代码复杂度 | 高（需要 C 代码） | 低（纯 Java） |
| 类型安全 | 运行时检查 | 编译时检查 |
| 内存管理 | 手动管理 | 自动管理 |
| 调试难度 | 困难 | 相对容易 |

#### 实际应用领域

**1. 高性能计算**
*   调用优化的数学库（如 BLAS、LAPACK）
*   图像和信号处理库

**2. 系统编程**
*   操作系统 API 调用
*   硬件驱动接口

**3. 遗留系统集成**
*   与现有 C/C++ 代码库集成
*   数据库原生驱动

**4. 跨语言互操作**
*   调用其他语言编写的库
*   嵌入式脚本引擎集成

#### 最佳实践

1. **优先使用 try-with-resources**：确保内存资源正确释放
2. **使用 Arena 管理内存**：避免内存泄漏
3. **仔细设计内存布局**：匹配本地数据结构
4. **处理平台差异**：考虑跨平台兼容性
5. **充分测试**：本地调用可能引入新的错误类型
6. **评估安全性**：本地代码可能绕过 Java 安全机制

Foreign Function & Memory API 代表了 Java 平台的重大进步，为高性能本地代码集成提供了现代化的解决方案。

### 21. 向量 API (Vector API) (Java 24, 第九轮孵化)

*   **演进**: 从 Java 16 开始孵化，到 Java 24 已经是第九轮孵化。
*   **核心理念**: 为 Java 提供底层向量化计算能力，充分利用现代 CPU 的 SIMD（Single Instruction, Multiple Data）指令集。
*   **核心优势**:
    *   **高性能**: 单条指令处理多个数据，显著提升数值计算性能。
    *   **跨平台**: 抽象了不同 CPU 架构的 SIMD 指令差异。
    *   **类型安全**: 编译期类型检查，避免底层汇编的错误风险。
    *   **JIT 优化**: JVM 可以进一步优化向量操作。

#### SIMD 基础概念

**传统标量运算 vs 向量运算**
```java
// 标量运算 - 逐个处理
int[] a = {1, 2, 3, 4};
int[] b = {5, 6, 7, 8};
int[] result = new int[4];
for (int i = 0; i < 4; i++) {
    result[i] = a[i] + b[i]; // 4 次独立的加法操作
}

// 向量运算 - 并行处理
// 一条指令同时计算 4 个加法：[1,2,3,4] + [5,6,7,8] = [6,8,10,12]
```

#### 核心 API 概述

```java
// 主要包和类
import jdk.incubator.vector.*;

// 1. VectorSpecies - 向量物种（定义向量的类型和长度）
VectorSpecies<Integer> SPECIES = IntVector.SPECIES_256; // 256位向量，可容纳8个int

// 2. Vector - 向量实例
IntVector vector1 = IntVector.fromArray(SPECIES, array1, 0);
IntVector vector2 = IntVector.fromArray(SPECIES, array2, 0);

// 3. VectorMask - 向量掩码（用于条件操作）
VectorMask<Integer> mask = vector1.compare(VectorOperators.GT, threshold);

// 4. 向量操作
IntVector result = vector1.add(vector2);
```

#### 实际代码示例

**1. 基础向量加法**
```java
import jdk.incubator.vector.*;

public class VectorAdditionExample {
    // 选择合适的向量物种
    private static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;
    
    public static void vectorAdd(int[] a, int[] b, int[] result) {
        int i = 0;
        int upperBound = SPECIES.loopBound(a.length);
        
        // 向量化循环
        for (; i < upperBound; i += SPECIES.length()) {
            // 加载向量
            IntVector va = IntVector.fromArray(SPECIES, a, i);
            IntVector vb = IntVector.fromArray(SPECIES, b, i);
            
            // 向量加法
            IntVector vr = va.add(vb);
            
            // 存储结果
            vr.intoArray(result, i);
        }
        
        // 处理剩余元素
        for (; i < a.length; i++) {
            result[i] = a[i] + b[i];
        }
    }
    
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] b = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        int[] result = new int[a.length];
        
        vectorAdd(a, b, result);
        System.out.println("结果: " + Arrays.toString(result));
    }
}
```

**2. 浮点数向量操作**
```java
public class FloatVectorExample {
    private static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_256;
    
    // 向量化的点积计算
    public static float dotProduct(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("数组长度不匹配");
        }
        
        FloatVector sum = FloatVector.zero(SPECIES);
        int i = 0;
        int upperBound = SPECIES.loopBound(a.length);
        
        // 向量化计算
        for (; i < upperBound; i += SPECIES.length()) {
            FloatVector va = FloatVector.fromArray(SPECIES, a, i);
            FloatVector vb = FloatVector.fromArray(SPECIES, b, i);
            sum = va.fma(vb, sum); // fused multiply-add: va * vb + sum
        }
        
        // 归约求和
        float result = sum.reduceLanes(VectorOperators.ADD);
        
        // 处理剩余元素
        for (; i < a.length; i++) {
            result += a[i] * b[i];
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        float[] vector1 = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f};
        float[] vector2 = {2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
        
        float result = dotProduct(vector1, vector2);
        System.out.println("点积结果: " + result); // 1*2 + 2*3 + 3*4 + 4*5 + 5*6 = 70.0
    }
}
```

**3. 条件操作和掩码**
```java
public class VectorMaskExample {
    private static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_256;
    
    // 条件性向量操作：将大于阈值的元素设为0，其他保持不变
    public static void conditionalZero(int[] array, int threshold) {
        int i = 0;
        int upperBound = SPECIES.loopBound(array.length);
        
        for (; i < upperBound; i += SPECIES.length()) {
            IntVector vector = IntVector.fromArray(SPECIES, array, i);
            
            // 创建掩码：标记大于阈值的元素
            VectorMask<Integer> mask = vector.compare(VectorOperators.GT, threshold);
            
            // 条件性设置为0
            IntVector result = vector.blend(IntVector.zero(SPECIES), mask);
            
            result.intoArray(array, i);
        }
        
        // 处理剩余元素
        for (; i < array.length; i++) {
            if (array[i] > threshold) {
                array[i] = 0;
            }
        }
    }
    
    public static void main(String[] args) {
        int[] data = {1, 5, 10, 15, 3, 8, 12, 6};
        System.out.println("原始数据: " + Arrays.toString(data));
        
        conditionalZero(data, 7);
        System.out.println("处理后: " + Arrays.toString(data)); // [1, 5, 0, 0, 3, 0, 0, 6]
    }
}
```

**4. 复杂数学运算**
```java
public class MathVectorExample {
    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_512;
    
    // 向量化的平方根计算
    public static void vectorSqrt(double[] input, double[] output) {
        int i = 0;
        int upperBound = SPECIES.loopBound(input.length);
        
        for (; i < upperBound; i += SPECIES.length()) {
            DoubleVector vector = DoubleVector.fromArray(SPECIES, input, i);
            DoubleVector result = vector.lanewise(VectorOperators.SQRT);
            result.intoArray(output, i);
        }
        
        // 处理剩余元素
        for (; i < input.length; i++) {
            output[i] = Math.sqrt(input[i]);
        }
    }
    
    // 向量化的指数运算
    public static void vectorExp(double[] input, double[] output) {
        int i = 0;
        int upperBound = SPECIES.loopBound(input.length);
        
        for (; i < upperBound; i += SPECIES.length()) {
            DoubleVector vector = DoubleVector.fromArray(SPECIES, input, i);
            DoubleVector result = vector.lanewise(VectorOperators.EXP);
            result.intoArray(output, i);
        }
        
        // 处理剩余元素
        for (; i < input.length; i++) {
            output[i] = Math.exp(input[i]);
        }
    }
    
    public static void main(String[] args) {
        double[] input = {1.0, 4.0, 9.0, 16.0, 25.0};
        double[] sqrtResult = new double[input.length];
        double[] expResult = new double[input.length];
        
        vectorSqrt(input, sqrtResult);
        vectorExp(input, expResult);
        
        System.out.println("原始数据: " + Arrays.toString(input));
        System.out.println("平方根: " + Arrays.toString(sqrtResult));
        System.out.println("指数: " + Arrays.toString(expResult));
    }
}
```

#### 性能基准测试

**标量 vs 向量性能对比**
```java
public class VectorBenchmark {
    private static final int SIZE = 1_000_000;
    private static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_256;
    
    // 标量版本
    public static void scalarMultiply(float[] a, float[] b, float[] result) {
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * b[i];
        }
    }
    
    // 向量版本
    public static void vectorMultiply(float[] a, float[] b, float[] result) {
        int i = 0;
        int upperBound = SPECIES.loopBound(a.length);
        
        for (; i < upperBound; i += SPECIES.length()) {
            FloatVector va = FloatVector.fromArray(SPECIES, a, i);
            FloatVector vb = FloatVector.fromArray(SPECIES, b, i);
            FloatVector vr = va.mul(vb);
            vr.intoArray(result, i);
        }
        
        for (; i < a.length; i++) {
            result[i] = a[i] * b[i];
        }
    }
    
    public static void benchmark() {
        float[] a = new float[SIZE];
        float[] b = new float[SIZE];
        float[] result = new float[SIZE];
        
        // 初始化数据
        for (int i = 0; i < SIZE; i++) {
            a[i] = (float) i;
            b[i] = (float) (i + 1);
        }
        
        // 预热
        for (int i = 0; i < 1000; i++) {
            scalarMultiply(a, b, result);
            vectorMultiply(a, b, result);
        }
        
        // 标量版本基准测试
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            scalarMultiply(a, b, result);
        }
        long scalarTime = System.nanoTime() - startTime;
        
        // 向量版本基准测试
        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            vectorMultiply(a, b, result);
        }
        long vectorTime = System.nanoTime() - startTime;
        
        System.out.println("标量版本耗时: " + scalarTime / 1_000_000 + " ms");
        System.out.println("向量版本耗时: " + vectorTime / 1_000_000 + " ms");
        System.out.println("性能提升: " + (double)scalarTime / vectorTime + "x");
    }
}
```

#### 实际应用场景

**1. 机器学习**
*   矩阵乘法优化
*   神经网络前向传播
*   梯度计算加速

**2. 图像处理**
*   像素级并行处理
*   滤波器卷积运算
*   颜色空间转换

**3. 科学计算**
*   数值积分
*   统计分析
*   信号处理

**4. 金融计算**
*   蒙特卡洛模拟
*   期权定价模型
*   风险计算

#### 使用注意事项

**1. 硬件支持**
```java
// 检查硬件支持情况
VectorSpecies<Float> species = FloatVector.SPECIES_PREFERRED;
System.out.println("首选向量长度: " + species.length());
System.out.println("向量位宽: " + species.bitSize());
```

**2. 内存对齐**
*   确保数据在内存中正确对齐
*   避免跨越缓存行的访问模式

**3. 数据布局**
*   优先使用连续的内存布局
*   考虑数据的访问模式

**4. 性能测量**
*   使用 JMH 进行准确的基准测试
*   考虑 JIT 编译的预热时间

#### 限制和挑战

**1. 孵化状态**
*   API 可能在未来版本中变化
*   需要使用 `--add-modules=jdk.incubator.vector`

**2. 学习曲线**
*   需要理解 SIMD 概念
*   调试复杂度较高

**3. 适用性**
*   不是所有算法都能有效向量化
*   需要足够的并行度才能获得收益

#### 最佳实践

1. **选择合适的向量长度**：使用 `SPECIES_PREFERRED` 获得最佳性能
2. **处理边界情况**：正确处理不能整除向量长度的数组
3. **内存访问优化**：保持数据的连续访问模式
4. **性能验证**：通过基准测试验证性能提升
5. **可维护性**：保持代码可读性，必要时提供标量版本作为备选

Vector API 代表了 Java 在高性能数值计算领域的重要进步，为 CPU 密集型应用提供了接近原生代码的性能。

### 22. 类文件 API (Class-File API) (Java 24, 正式)

*   **演进**: 在 Java 22 作为预览特性，Java 24 成为正式特性。
*   **核心理念**: 为解析、生成和转换 Java 类文件提供标准化的 API，替代第三方库如 ASM、Javassist 等。
*   **核心优势**:
    *   **官方支持**: JDK 内置，不需要额外依赖。
    *   **类型安全**: 强类型 API，编译期错误检测。
    *   **性能优化**: 与 JVM 内部实现紧密集成。
    *   **版本兼容**: 自动处理不同版本的类文件格式。

#### 背景与动机

**传统方式的问题**
*   第三方库（ASM、Javassist）增加依赖复杂性
*   版本兼容性问题，新 Java 版本需要库更新
*   性能开销和学习成本
*   缺乏官方标准化

**Class-File API 的解决方案**
*   JDK 内置，零外部依赖
*   与 JVM 演进同步更新
*   统一的 API 设计范式
*   性能优化的内部实现

#### 核心 API 概述

```java
// 主要包和接口
import java.lang.classfile.*;
import java.lang.classfile.attribute.*;
import java.lang.classfile.constantpool.*;
import java.lang.classfile.instruction.*;

// 主要组件：
// 1. ClassFile - 类文件的主要入口点
// 2. ClassModel - 表示一个类文件的模型
// 3. ClassBuilder - 用于构建类文件
// 4. CodeModel/CodeBuilder - 处理方法字节码
// 5. ConstantPool - 常量池操作
```

#### 基础用法示例

**1. 读取和分析类文件**
```java
import java.lang.classfile.*;
import java.io.*;

public class ClassFileReaderExample {
    public static void analyzeClass(String className) throws IOException {
        // 从类路径加载类文件
        try (InputStream is = ClassFileReaderExample.class
                .getResourceAsStream("/" + className.replace('.', '/') + ".class")) {
            
            // 解析类文件
            ClassModel classModel = ClassFile.of().parse(is.readAllBytes());
            
            // 基本信息
            System.out.println("类名: " + classModel.thisClass().asInternalName());
            System.out.println("访问标志: " + classModel.flags());
            System.out.println("超类: " + classModel.superclass()
                    .map(ce -> ce.asInternalName()).orElse("无"));
            
            // 接口信息
            System.out.println("实现的接口:");
            classModel.interfaces().forEach(intf -> 
                System.out.println("  - " + intf.asInternalName()));
            
            // 字段信息
            System.out.println("字段:");
            classModel.fields().forEach(field -> 
                System.out.println("  - " + field.fieldName().stringValue() + 
                                 " : " + field.fieldType().stringValue()));
            
            // 方法信息
            System.out.println("方法:");
            classModel.methods().forEach(method -> {
                System.out.println("  - " + method.methodName().stringValue() + 
                                 " : " + method.methodType().stringValue());
                
                // 分析方法字节码
                method.code().ifPresent(codeAttr -> {
                    System.out.println("    指令数量: " + codeAttr.elementList().size());
                    System.out.println("    最大栈深度: " + codeAttr.maxStack());
                    System.out.println("    本地变量槽数: " + codeAttr.maxLocals());
                });
            });
        }
    }
    
    public static void main(String[] args) throws IOException {
        analyzeClass("java.lang.String");
    }
}
```

**2. 生成简单的类**
```java
import java.lang.classfile.*;
import java.lang.constant.*;
import java.lang.classfile.constantpool.*;
import static java.lang.classfile.ClassFile.*;

public class ClassGeneratorExample {
    public static byte[] generateHelloWorldClass() {
        return ClassFile.of().build(
            // 类名
            ClassDesc.of("com.example.HelloWorld"),
            
            // 类构建器
            classBuilder -> {
                // 类的基本信息
                classBuilder
                    .withFlags(AccessFlag.PUBLIC)
                    .withSuperclass(ClassDesc.of("java.lang.Object"));
                
                // 添加默认构造函数
                classBuilder.withMethod("<init>", MethodTypeDesc.of(
                    CD_void), AccessFlag.PUBLIC, methodBuilder ->
                    methodBuilder.withCode(codeBuilder ->
                        codeBuilder
                            .aload(0) // 加载 this
                            .invokespecial(ClassDesc.of("java.lang.Object"), 
                                         "<init>", MethodTypeDesc.of(CD_void))
                            .return_()
                    )
                );
                
                // 添加 main 方法
                classBuilder.withMethod("main", 
                    MethodTypeDesc.of(CD_void, CD_String.arrayType()),
                    AccessFlag.PUBLIC, AccessFlag.STATIC,
                    methodBuilder ->
                        methodBuilder.withCode(codeBuilder ->
                            codeBuilder
                                .getstatic(ClassDesc.of("java.lang.System"),
                                         "out", ClassDesc.of("java.io.PrintStream"))
                                .ldc("Hello, World!")
                                .invokevirtual(ClassDesc.of("java.io.PrintStream"),
                                             "println", MethodTypeDesc.of(CD_void, CD_String))
                                .return_()
                        )
                );
            }
        );
    }
    
    public static void main(String[] args) throws Exception {
        // 生成类字节码
        byte[] classBytes = generateHelloWorldClass();
        
        // 写入文件
        try (FileOutputStream fos = new FileOutputStream("HelloWorld.class")) {
            fos.write(classBytes);
        }
        
        // 动态加载并执行
        ClassLoader loader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                if ("com.example.HelloWorld".equals(name)) {
                    return defineClass(name, classBytes, 0, classBytes.length);
                }
                throw new ClassNotFoundException(name);
            }
        };
        
        Class<?> clazz = loader.loadClass("com.example.HelloWorld");
        clazz.getMethod("main", String[].class).invoke(null, (Object) new String[0]);
    }
}
```

**3. 字节码转换和增强**
```java
import java.lang.classfile.*;
import java.lang.classfile.instruction.*;

public class BytecodeTransformExample {
    
    // 为类的所有方法添加日志
    public static byte[] addLoggingToClass(byte[] originalClassBytes) {
        ClassModel originalClass = ClassFile.of().parse(originalClassBytes);
        
        return ClassFile.of().transform(originalClass, (classBuilder, classElement) -> {
            if (classElement instanceof MethodModel method) {
                // 转换方法，添加进入和退出日志
                classBuilder.withMethod(method.methodName().stringValue(),
                    method.methodType(), method.flags().flagsMask(),
                    methodBuilder -> transformMethod(methodBuilder, method));
            } else {
                // 其他元素直接复制
                classBuilder.with(classElement);
            }
        });
    }
    
    private static void transformMethod(MethodBuilder methodBuilder, MethodModel originalMethod) {
        originalMethod.code().ifPresentOrElse(
            originalCode -> {
                methodBuilder.withCode(codeBuilder -> {
                    // 方法开始时的日志
                    addLogStatement(codeBuilder, "进入方法: " + 
                        originalMethod.methodName().stringValue());
                    
                    // 复制原始字节码
                    originalCode.elementStream().forEach(codeElement -> {
                        if (codeElement instanceof ReturnInstruction) {
                            // 在返回前添加日志
                            addLogStatement(codeBuilder, "退出方法: " + 
                                originalMethod.methodName().stringValue());
                        }
                        codeBuilder.with(codeElement);
                    });
                });
            },
            () -> {
                // 抽象方法或本地方法，直接复制
                originalMethod.elementStream().forEach(methodBuilder::with);
            }
        );
    }
    
    private static void addLogStatement(CodeBuilder codeBuilder, String message) {
        codeBuilder
            .getstatic(ClassDesc.of("java.lang.System"), "out", 
                      ClassDesc.of("java.io.PrintStream"))
            .ldc(message)
            .invokevirtual(ClassDesc.of("java.io.PrintStream"), "println",
                          MethodTypeDesc.of(CD_void, CD_String));
    }
    
    public static void main(String[] args) throws Exception {
        // 读取原始类文件
        byte[] originalBytes = ClassGeneratorExample.generateHelloWorldClass();
        
        // 添加日志功能
        byte[] enhancedBytes = addLoggingToClass(originalBytes);
        
        // 保存增强后的类
        try (FileOutputStream fos = new FileOutputStream("HelloWorldWithLogging.class")) {
            fos.write(enhancedBytes);
        }
        
        System.out.println("增强的类已生成到 HelloWorldWithLogging.class");
    }
}
```

#### 与现有工具的对比

| 特性 | ASM | Javassist | Class-File API |
|------|-----|-----------|----------------|
| JDK 内置 | ❌ | ❌ | ✅ |
| 学习曲线 | 陡峭 | 中等 | 中等 |
| 性能 | 高 | 中等 | 很高 |
| 类型安全 | 低 | 中等 | 高 |
| 版本兼容 | 需更新 | 需更新 | 自动 |
| API 风格 | 访问者模式 | 反射式 | 构建者模式 |

#### 实际应用领域

**1. 框架开发**
*   Spring 的代理生成
*   Hibernate 的实体增强
*   依赖注入容器

**2. 开发工具**
*   IDE 的代码分析
*   静态分析工具
*   代码覆盖率工具

**3. 性能优化**
*   JIT 编译器
*   热点代码优化
*   内存使用分析

**4. 安全工具**
*   代码混淆
*   恶意代码检测
*   访问控制增强

#### 最佳实践

1. **合理使用转换模式**：优先使用 transform 而不是完全重建
2. **注意性能影响**：字节码操作可能影响启动时间
3. **保持兼容性**：确保生成的字节码符合 JVM 规范
4. **调试支持**：保留调试信息和行号映射
5. **测试覆盖**：充分测试各种边界情况和错误路径

Class-File API 为 Java 字节码操作提供了官方标准化的解决方案，预期将成为字节码工具的新标准。

---

## 五、 开发体验与工具

### 23. JShell: Java REPL 工具 (Java 9)

*   **演进**: Java 9 正式引入。
*   **核心理念**: 为 Java 提供交互式编程环境（REPL - Read-Eval-Print Loop），允许逐行执行 Java 代码片段。
*   **核心优势**:
    *   **快速原型**: 无需完整的类结构即可测试代码片段。
    *   **学习工具**: 非常适合 Java 初学者和实验性编程。
    *   **调试辅助**: 快速验证表达式和方法行为。
    *   **API 探索**: 交互式探索 Java API 和第三方库。

#### 基本使用

**启动 JShell**
```bash
# 命令行启动
jshell

# 带详细输出启动
jshell -v

# 加载外部 classpath
jshell --class-path /path/to/libs/*
```

**基本操作示例**
```java
// 1. 变量声明和计算
jshell> int x = 10
x ==> 10

jshell> int y = 20
y ==> 20

jshell> int sum = x + y
sum ==> 30

// 2. 方法定义
jshell> int add(int a, int b) {
   ...>     return a + b;
   ...> }
|  created method add(int,int)

jshell> add(5, 7)
$5 ==> 12

// 3. 导入包
jshell> import java.util.*

jshell> List<String> names = Arrays.asList("Alice", "Bob", "Charlie")
names ==> [Alice, Bob, Charlie]

// 4. 流式操作
jshell> names.stream()
   ...>      .filter(name -> name.startsWith("A"))
   ...>      .collect(Collectors.toList())
$8 ==> [Alice]
```

#### 高级特性

**1. 多行编辑和代码块**
```java
// 类定义
jshell> class Person {
   ...>     private String name;
   ...>     private int age;
   ...>     
   ...>     public Person(String name, int age) {
   ...>         this.name = name;
   ...>         this.age = age;
   ...>     }
   ...>     
   ...>     public String toString() {
   ...>         return name + " (" + age + ")";
   ...>     }
   ...> }
|  created class Person

jshell> Person p = new Person("Alice", 25)
p ==> Alice (25)
```

**2. 代码片段管理**
```java
// 查看已定义的变量
jshell> /vars
|    int x = 10
|    int y = 20
|    int sum = 30
|    List<String> names = [Alice, Bob, Charlie]
|    Person p = Alice (25)

// 查看已定义的方法
jshell> /methods
|    int add(int,int)

// 查看历史记录
jshell> /history

// 保存会话到文件
jshell> /save mycode.jsh

// 加载文件
jshell> /open mycode.jsh
```

**3. 外部编辑器集成**
```java
// 设置外部编辑器
jshell> /set editor vim

// 编辑代码片段
jshell> /edit add
// 打开外部编辑器编辑 add 方法
```

#### 实用命令大全

```java
// === 基础命令 ===
/help                 // 显示帮助
/exit                 // 退出 JShell
/quit                 // 退出 JShell

// === 代码管理 ===
/list                 // 显示当前会话中的代码
/vars                 // 显示变量
/methods              // 显示方法
/types                // 显示类型
/imports              // 显示导入

// === 文件操作 ===
/save filename        // 保存会话
/open filename        // 加载文件
/drop name            // 删除变量/方法/类

// === 环境设置 ===
/set editor cmd       // 设置编辑器
/set start filename   // 设置启动脚本
/set mode verbose     // 设置详细模式
/set feedback verbose // 设置反馈级别

// === 调试信息 ===
/history              // 命令历史
/reload               // 重新加载会话
/reset                // 重置 JShell 环境
```

#### 实际应用场景

**1. 算法验证**
```java
jshell> // 快速验证排序算法
jshell> int[] arr = {64, 34, 25, 12, 22, 11, 90}
arr ==> int[7] { 64, 34, 25, 12, 22, 11, 90 }

jshell> // 冒泡排序实现
jshell> void bubbleSort(int[] arr) {
   ...>     int n = arr.length;
   ...>     for (int i = 0; i < n-1; i++) {
   ...>         for (int j = 0; j < n-i-1; j++) {
   ...>             if (arr[j] > arr[j+1]) {
   ...>                 int temp = arr[j];
   ...>                 arr[j] = arr[j+1];
   ...>                 arr[j+1] = temp;
   ...>             }
   ...>         }
   ...>     }
   ...> }

jshell> bubbleSort(arr)
jshell> Arrays.toString(arr)
$12 ==> "[11, 12, 22, 25, 34, 64, 90]"
```

**2. API 探索**
```java
jshell> // 探索 Stream API
jshell> List<Integer> numbers = IntStream.range(1, 10)
   ...>                                   .boxed()
   ...>                                   .collect(Collectors.toList())

jshell> numbers.stream()
   ...>        .filter(n -> n % 2 == 0)
   ...>        .map(n -> n * n)
   ...>        .collect(Collectors.toList())
$15 ==> [4, 16, 36, 64]
```

**3. 正则表达式测试**
```java
jshell> import java.util.regex.*

jshell> String text = "Java 21 and Spring Boot 3.0"
text ==> "Java 21 and Spring Boot 3.0"

jshell> Pattern pattern = Pattern.compile("\\d+(\\.\\d+)*")
pattern ==> \d+(\.\d+)*

jshell> Matcher matcher = pattern.matcher(text)
matcher ==> java.util.regex.Matcher[pattern=\d+(\.\d+)* region=0,26 lastmatch=]

jshell> while (matcher.find()) {
   ...>     System.out.println("Found: " + matcher.group());
   ...> }
Found: 21
Found: 3.0
```

**4. 数据分析**
```java
jshell> // 简单的数据分析
jshell> double[] scores = {85.5, 92.0, 78.5, 95.0, 88.0, 91.5}
scores ==> double[6] { 85.5, 92.0, 78.5, 95.0, 88.0, 91.5 }

jshell> // 计算平均分
jshell> double average = Arrays.stream(scores).average().orElse(0.0)
average ==> 88.41666666666667

jshell> // 找出最高分
jshell> double max = Arrays.stream(scores).max().orElse(0.0)
max ==> 95.0

jshell> // 统计及格人数（假设60分及格）
jshell> long passCount = Arrays.stream(scores)
   ...>                           .filter(score -> score >= 60)
   ...>                           .count()
passCount ==> 6
```

#### 教学和学习应用

**1. 概念演示**
```java
// 演示面向对象概念
jshell> abstract class Animal {
   ...>     protected String name;
   ...>     public Animal(String name) { this.name = name; }
   ...>     public abstract void makeSound();
   ...>     public void sleep() { System.out.println(name + " is sleeping"); }
   ...> }

jshell> class Dog extends Animal {
   ...>     public Dog(String name) { super(name); }
   ...>     public void makeSound() { System.out.println(name + " says Woof!"); }
   ...> }

jshell> Animal dog = new Dog("Buddy")
dog ==> Dog@...

jshell> dog.makeSound()
Buddy says Woof!
```

**2. 设计模式演示**
```java
// 快速演示单例模式
jshell> class Singleton {
   ...>     private static Singleton instance = null;
   ...>     private Singleton() {}
   ...>     public static Singleton getInstance() {
   ...>         if (instance == null) {
   ...>             instance = new Singleton();
   ...>         }
   ...>         return instance;
   ...>     }
   ...> }

jshell> Singleton s1 = Singleton.getInstance()
jshell> Singleton s2 = Singleton.getInstance()
jshell> s1 == s2
$25 ==> true
```

#### 集成开发

**启动脚本示例**
```java
// 创建 startup.jsh 文件
import java.util.*;
import java.util.stream.*;
import java.time.*;
import java.math.*;

// 常用工具方法
void println(Object obj) {
    System.out.println(obj);
}

String now() {
    return LocalDateTime.now().toString();
}

// 使用启动脚本
// jshell startup.jsh
```

**与 IDE 集成**
*   IntelliJ IDEA: 内置 JShell 控制台
*   Eclipse: 通过插件支持
*   VS Code: 通过 Java 扩展支持

JShell 极大地提升了 Java 的交互性和学习体验，是现代 Java 开发的重要工具。

### 24. 单文件源码程序启动 (Java 11)

*   **演进**: Java 11 正式引入。
*   **核心理念**: 允许直接运行单个 `.java` 文件，无需先编译成 `.class` 文件，简化小型程序和脚本的开发流程。
*   **核心优势**:
    *   **零配置**: 无需构建工具或复杂的项目结构。
    *   **快速原型**: 适合编写工具脚本和一次性程序。
    *   **学习友好**: 降低 Java 学习门槛。
    *   **脚本化**: Java 程序可以像脚本语言一样使用。

#### 基本使用方法

**传统方式 vs 单文件启动**
```java
// === 传统方式 ===
// 1. 编写 HelloWorld.java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}

// 2. 编译
// javac HelloWorld.java

// 3. 运行
// java HelloWorld

// === 单文件启动方式 ===
// 直接运行（Java 11+）
// java HelloWorld.java
```

**命令语法**
```bash
# 基本语法
java [OPTIONS] <source-file> [args...]

# 示例
java HelloWorld.java
java --class-path /path/to/libs MyScript.java arg1 arg2
java -Dproperty=value Script.java
```

#### 实际应用示例

**1. 系统工具脚本**
```java
// FileStats.java - 文件统计工具
import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Stream;

public class FileStats {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("用法: java FileStats.java <目录路径>");
            System.exit(1);
        }
        
        Path directory = Paths.get(args[0]);
        if (!Files.isDirectory(directory)) {
            System.err.println("错误: " + args[0] + " 不是一个目录");
            System.exit(1);
        }
        
        try (Stream<Path> files = Files.walk(directory)) {
            var stats = files
                .filter(Files::isRegularFile)
                .collect(java.util.stream.Collectors.groupingBy(
                    FileStats::getFileExtension,
                    java.util.stream.Collectors.counting()
                ));
            
            System.out.println("文件统计 - " + directory + ":");
            stats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .forEach(entry -> 
                    System.out.printf("  %s: %d 个文件\n", 
                        entry.getKey(), entry.getValue()));
                        
            long totalFiles = stats.values().stream()
                .mapToLong(Long::longValue).sum();
            System.out.println("总计: " + totalFiles + " 个文件");
        }
    }
    
    private static String getFileExtension(Path file) {
        String fileName = file.getFileName().toString();
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "[无扩展名]";
    }
}

// 使用: java FileStats.java /path/to/directory
```

**2. 网络工具**
```java
// HttpChecker.java - HTTP 状态检查工具
import java.net.http.*;
import java.net.*;
import java.time.Duration;
import java.io.IOException;

public class HttpChecker {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("用法: java HttpChecker.java <URL1> <URL2> ...");
            System.exit(1);
        }
        
        HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
            
        for (String url : args) {
            checkUrl(client, url);
        }
    }
    
    private static void checkUrl(HttpClient client, String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();
                
            long startTime = System.currentTimeMillis();
            HttpResponse<String> response = client.send(request, 
                HttpResponse.BodyHandlers.ofString());
            long responseTime = System.currentTimeMillis() - startTime;
            
            System.out.printf("%s - 状态: %d, 响应时间: %d ms\n", 
                url, response.statusCode(), responseTime);
                
        } catch (IOException | InterruptedException e) {
            System.err.printf("%s - 错误: %s\n", url, e.getMessage());
        }
    }
}

// 使用: java HttpChecker.java https://google.com https://github.com
```

**3. 数据处理脚本**
```java
// CsvAnalyzer.java - CSV 文件分析器
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.io.IOException;

public class CsvAnalyzer {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("用法: java CsvAnalyzer.java <CSV文件> <列号>");
            System.exit(1);
        }
        
        String filename = args[0];
        int columnIndex = Integer.parseInt(args[1]) - 1; // 转为0基索引
        
        List<String> lines = Files.readAllLines(Paths.get(filename));
        if (lines.isEmpty()) {
            System.out.println("文件为空");
            return;
        }
        
        // 跳过标题行，提取指定列的数据
        List<Double> values = lines.stream()
            .skip(1) // 跳过标题
            .map(line -> line.split(","))
            .filter(parts -> parts.length > columnIndex)
            .map(parts -> parts[columnIndex].trim())
            .filter(value -> !value.isEmpty())
            .mapToDouble(Double::parseDouble)
            .boxed()
            .collect(Collectors.toList());
            
        if (values.isEmpty()) {
            System.out.println("没有找到有效的数值数据");
            return;
        }
        
        // 统计分析
        DoubleSummaryStatistics stats = values.stream()
            .mapToDouble(Double::doubleValue)
            .summaryStatistics();
            
        System.out.printf("第 %d 列数据分析:\n", Integer.parseInt(args[1]));
        System.out.printf("  数据个数: %d\n", stats.getCount());
        System.out.printf("  最小值: %.2f\n", stats.getMin());
        System.out.printf("  最大值: %.2f\n", stats.getMax());
        System.out.printf("  平均值: %.2f\n", stats.getAverage());
        System.out.printf("  总和: %.2f\n", stats.getSum());
        
        // 中位数
        Collections.sort(values);
        double median = values.size() % 2 == 0 ?
            (values.get(values.size()/2 - 1) + values.get(values.size()/2)) / 2.0 :
            values.get(values.size()/2);
        System.out.printf("  中位数: %.2f\n", median);
    }
}

// 使用: java CsvAnalyzer.java data.csv 3
```

**4. 文本处理工具**
```java
// TextProcessor.java - 文本处理工具
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.io.IOException;

public class TextProcessor {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }
        
        String command = args[0];
        String filename = args[1];
        
        String content = Files.readString(Paths.get(filename));
        
        switch (command.toLowerCase()) {
            case "count" -> countWords(content);
            case "lines" -> countLines(content);
            case "find" -> findPattern(content, args[2]);
            case "replace" -> replacePattern(content, args[2], args[3], filename);
            default -> {
                System.err.println("未知命令: " + command);
                printUsage();
            }
        }
    }
    
    private static void printUsage() {
        System.out.println("用法:");
        System.out.println("  java TextProcessor.java count <文件>     - 统计单词");
        System.out.println("  java TextProcessor.java lines <文件>     - 统计行数");
        System.out.println("  java TextProcessor.java find <文件> <正则>  - 查找模式");
        System.out.println("  java TextProcessor.java replace <文件> <查找> <替换> - 替换文本");
    }
    
    private static void countWords(String content) {
        String[] words = content.trim().split("\\s+");
        System.out.println("单词数: " + (content.trim().isEmpty() ? 0 : words.length));
        
        Map<String, Integer> wordFreq = new HashMap<>();
        for (String word : words) {
            word = word.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
            if (!word.isEmpty()) {
                wordFreq.merge(word, 1, Integer::sum);
            }
        }
        
        System.out.println("\n高频词汇 (前10个):");
        wordFreq.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(10)
            .forEach(entry -> System.out.printf("  %s: %d 次\n", 
                entry.getKey(), entry.getValue()));
    }
    
    private static void countLines(String content) {
        long lineCount = content.lines().count();
        long nonEmptyLines = content.lines()
            .filter(line -> !line.trim().isEmpty())
            .count();
            
        System.out.println("总行数: " + lineCount);
        System.out.println("非空行数: " + nonEmptyLines);
    }
    
    private static void findPattern(String content, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(content);
        
        int count = 0;
        while (matcher.find()) {
            count++;
            System.out.printf("匹配 %d: %s (位置: %d-%d)\n", 
                count, matcher.group(), matcher.start(), matcher.end());
        }
        
        if (count == 0) {
            System.out.println("未找到匹配项");
        } else {
            System.out.println("\n总计找到 " + count + " 个匹配项");
        }
    }
    
    private static void replacePattern(String content, String find, 
                                     String replace, String filename) throws IOException {
        String newContent = content.replaceAll(find, replace);
        String outputFile = filename + ".new";
        Files.writeString(Paths.get(outputFile), newContent);
        System.out.println("替换完成，结果保存到: " + outputFile);
    }
}

// 使用示例:
// java TextProcessor.java count document.txt
// java TextProcessor.java find document.txt "\\b\\w+@\\w+\\.\\w+\\b"
```

#### 高级特性和限制

**支持的特性**
*   完整的 Java 语言特性
*   导入外部库（通过 --class-path）
*   JVM 参数传递
*   程序参数传递
*   系统属性设置

**限制条件**
*   只能包含一个公共类
*   类名必须与文件名匹配
*   不支持包声明
*   编译是临时的，不生成 .class 文件

**性能考虑**
*   首次运行需要编译时间
*   适合小型程序和脚本
*   不适合大型应用程序

#### 与脚本语言的对比

| 特性 | Java 单文件 | Python | Node.js |
|------|-------------|--------|---------|
| 性能 | 高（JVM） | 中等 | 高（V8） |
| 启动速度 | 慢（JVM 启动） | 快 | 快 |
| 类型安全 | 强类型 | 动态类型 | 动态类型 |
| 生态系统 | Java 生态 | 丰富 | 丰富 |
| 学习曲线 | 中等 | 简单 | 简单 |

#### 实际部署建议

**1. Shebang 支持（Unix/Linux）**
```java
#!/usr/bin/java --source 11
// MyScript.java
public class MyScript {
    public static void main(String[] args) {
        System.out.println("这是一个可执行的 Java 脚本!");
    }
}
```

```bash
# 使脚本可执行
chmod +x MyScript.java

# 直接运行
./MyScript.java
```

**2. 容器化部署**
```dockerfile
# Dockerfile
FROM openjdk:11-jre-slim
COPY script.java /app/
WORKDIR /app
ENTRYPOINT ["java", "script.java"]
```

**最佳实践**
1. **适合场景**: 工具脚本、原型开发、教学演示
2. **避免场景**: 大型应用、生产环境的核心服务
3. **性能优化**: 使用 GraalVM 原生编译提升启动速度
4. **代码组织**: 保持单文件的简洁性，复杂逻辑考虑传统项目结构

单文件源码程序启动为 Java 开发带来了脚本化的便利性，是现代 Java 生态系统的重要补充。

### 25. 更详尽的 NullPointerException (Java 14)

*   **演进**: Java 14 正式引入。
*   **核心理念**: 提供更精确、更有用的 NullPointerException 错误信息，帮助开发者快速定位和解决问题。
*   **核心优势**:
    *   **精确定位**: 明确指出哪个具体的引用为 null。
    *   **调试助手**: 显著减少调试时间和心智负担。
    *   **学习友好**: 帮助初学者理解错误原因。
    *   **生产可用**: 提升生产环境的问题诊断效率。

#### 传统 vs 增强版对比

**传统的 NullPointerException**
```java
// 代码示例
public class Person {
    private String name;
    private Address address;
    
    // getters and setters...
    
    public void printStreetName() {
        // 这行代码可能抛出 NPE
        System.out.println(address.getStreet().getName().toUpperCase());
    }
}

class Address {
    private Street street;
    // ...
}

class Street {
    private String name;
    // ...
}

// 传统错误信息（Java 13 及以前）
// Exception in thread "main" java.lang.NullPointerException
//     at Person.printStreetName(Person.java:10)
```

**增强版的 NullPointerException**
```java
// 同样的代码，Java 14+ 的错误信息
// Exception in thread "main" java.lang.NullPointerException: 
//     Cannot invoke "Street.getName()" because the return value of 
//     "Address.getStreet()" is null
//     at Person.printStreetName(Person.java:10)
```

#### 详细示例对比

**1. 方法调用链**
```java
public class ChainedCallExample {
    public static void main(String[] args) {
        User user = new User();
        
        // 这行代码会抛出 NPE
        String country = user.getProfile().getAddress().getCountry().toUpperCase();
        System.out.println(country);
    }
}

class User {
    private Profile profile;
    public Profile getProfile() { return profile; } // 返回 null
}

class Profile {
    private Address address;
    public Address getAddress() { return address; }
}

class Address {
    private String country;
    public String getCountry() { return country; }
}

// Java 13 及以前的错误信息
// java.lang.NullPointerException
//     at ChainedCallExample.main(ChainedCallExample.java:6)

// Java 14+ 的增强错误信息
// java.lang.NullPointerException: Cannot invoke "Profile.getAddress()" 
//     because the return value of "User.getProfile()" is null
//     at ChainedCallExample.main(ChainedCallExample.java:6)
```

**2. 数组访问**
```java
public class ArrayAccessExample {
    public static void main(String[] args) {
        String[][] matrix = new String[3][];
        
        // 这行会抛出 NPE
        int length = matrix[1].length;
        System.out.println(length);
    }
}

// Java 13 及以前
// java.lang.NullPointerException
//     at ArrayAccessExample.main(ArrayAccessExample.java:6)

// Java 14+
// java.lang.NullPointerException: Cannot read the array length 
//     because "matrix[1]" is null
//     at ArrayAccessExample.main(ArrayAccessExample.java:6)
```

**3. 字段访问**
```java
public class FieldAccessExample {
    private static class Container {
        String value;
    }
    
    public static void main(String[] args) {
        Container container = null;
        
        // 这行会抛出 NPE
        System.out.println(container.value);
    }
}

// Java 13 及以前
// java.lang.NullPointerException
//     at FieldAccessExample.main(FieldAccessExample.java:9)

// Java 14+
// java.lang.NullPointerException: Cannot read field "value" 
//     because "container" is null
//     at FieldAccessExample.main(FieldAccessExample.java:9)
```

**4. 数组元素赋值**
```java
public class ArrayAssignmentExample {
    public static void main(String[] args) {
        int[][] matrix = new int[3][];
        
        // 这行会抛出 NPE
        matrix[0][1] = 42;
    }
}

// Java 14+ 增强错误信息
// java.lang.NullPointerException: Cannot store to int array 
//     because "matrix[0]" is null
//     at ArrayAssignmentExample.main(ArrayAssignmentExample.java:6)
```

**5. synchronized 语句**
```java
public class SynchronizedExample {
    public static void main(String[] args) {
        Object lock = null;
        
        // 这行会抛出 NPE
        synchronized (lock) {
            System.out.println("不会执行到这里");
        }
    }
}

// Java 14+ 增强错误信息
// java.lang.NullPointerException: Cannot enter synchronized block 
//     because "lock" is null
//     at SynchronizedExample.main(SynchronizedExample.java:6)
```

#### 复杂场景示例

**涉及泛型和集合**
```java
import java.util.*;

public class ComplexExample {
    public static void main(String[] args) {
        Map<String, List<Person>> groups = new HashMap<>();
        groups.put("team1", null);
        
        // 这行会抛出详细的 NPE
        int teamSize = groups.get("team1").size();
        System.out.println(teamSize);
    }
    
    static class Person {
        String name;
        Person(String name) { this.name = name; }
    }
}

// Java 14+ 增强错误信息
// java.lang.NullPointerException: Cannot invoke "java.util.List.size()" 
//     because the return value of "java.util.Map.get(Object)" is null
//     at ComplexExample.main(ComplexExample.java:9)
```

**嵌套方法调用**
```java
public class NestedCallExample {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        
        // 复杂的嵌套调用
        double result = calc.getOperations().getAdvanced().sqrt(
            calc.getMemory().recall().getValue()
        );
        System.out.println(result);
    }
    
    static class Calculator {
        public Operations getOperations() { return new Operations(); }
        public Memory getMemory() { return null; } // 返回 null
    }
    
    static class Operations {
        public Advanced getAdvanced() { return new Advanced(); }
    }
    
    static class Advanced {
        public double sqrt(double value) { return Math.sqrt(value); }
    }
    
    static class Memory {
        public StoredValue recall() { return new StoredValue(42.0); }
    }
    
    static class StoredValue {
        private double value;
        StoredValue(double value) { this.value = value; }
        public double getValue() { return value; }
    }
}

// Java 14+ 增强错误信息
// java.lang.NullPointerException: Cannot invoke "NestedCallExample$Memory.recall()" 
//     because the return value of "NestedCallExample$Calculator.getMemory()" is null
//     at NestedCallExample.main(NestedCallExample.java:7)
```

#### 如何启用/禁用

**JVM 参数控制**
```bash
# 启用详细 NPE 信息（Java 14+ 默认启用）
java -XX:+ShowCodeDetailsInExceptionMessages MyClass

# 禁用详细 NPE 信息
java -XX:-ShowCodeDetailsInExceptionMessages MyClass

# 查看当前设置
java -XX:+PrintFlagsFinal | grep ShowCodeDetailsInExceptionMessages
```

#### 实际开发中的价值

**1. 快速问题定位**
```java
// 在复杂的业务逻辑中快速定位问题
public class BusinessLogicExample {
    public void processOrder(Order order) {
        // 这里有很多链式调用
        String customerEmail = order.getCustomer()
                                   .getContactInfo()
                                   .getEmailAddress()
                                   .toLowerCase();
        
        // 有了详细的 NPE 信息，能立刻知道是哪一步出了问题
        sendConfirmationEmail(customerEmail);
    }
}
```

**2. 单元测试调试**
```java
@Test
public void testComplexDataProcessing() {
    DataProcessor processor = new DataProcessor();
    
    // 在测试中如果出现 NPE，现在能更容易定位问题
    Result result = processor.process(
        mockData.getDataSet()
                .getRecords()
                .stream()
                .filter(record -> record.isValid())
                .collect(Collectors.toList())
    );
    
    assertNotNull(result);
}
```

**3. 生产问题诊断**
```java
// 在生产环境的日志中，现在能看到更有用的信息
public class ProductionExample {
    public void handleRequest(HttpServletRequest request) {
        try {
            User user = sessionManager.getUser(request.getSession())
                                     .orElseThrow(() -> new UnauthorizedException());
            
            String preference = user.getSettings()
                                   .getDisplaySettings()
                                   .getTheme();
            
            renderPage(preference);
        } catch (NullPointerException e) {
            // 现在日志中会显示具体是哪个对象为 null
            logger.error("处理请求时出错: {}", e.getMessage(), e);
            response.sendError(500, "内部错误");
        }
    }
}
```

#### 性能影响和考虑

**性能测试结果**
*   无明显的运行时性能影响
*   错误信息生成只在抛出异常时发生
*   对正常代码执行无影响

**适用场景**
*   开发和测试环境（强烈推荐启用）
*   生产环境的问题诊断
*   初学者学习 Java
*   代码审查和调试

**不适用场景**
*   对安全极其敏感的环境（可能泄露代码结构信息）
*   超高性能要求的场景（可考虑禁用）

#### 最佳实践

1. **开发环境始终启用**: 可显著提升开发效率
2. **日志记录**: 将详细错误信息记录在日志中
3. **防御式编程**: 仍然需要主动进行 null 检查
4. **单元测试**: 利用详细信息优化测试用例
5. **团队分享**: 向团队成员科普这一特性的价值

更详尽的 NullPointerException 是 Java 平台的一个重要改进，显著提升了开发者的调试体验。

### 26. 飞行记录器 (JFR) (Java 11)

*   **演进**: 在 Java 11 成为正式特性，之前是 Oracle JDK 的商业特性。
*   **核心理念**: 提供低开销、生产可用的应用程序性能监控和问题诊断工具。
*   **核心优势**:
    *   **低开销**: 通常仅增加 1-3% 的性能开销。
    *   **生产可用**: 可在生产环境中安全使用。
    *   **全面监控**: 涵盖内存、CPU、I/O、锁等各个方面。
    *   **实时分析**: 提供实时的性能数据和问题警告。

#### JFR 的核心概念

**事件（Events）**
*   JFR 基于事件的数据收集模型
*   每个事件包含时间戳、线程信息和具体数据
*   内置数百种预定义事件类型

**记录（Recording）**
*   一次数据收集的会话
*   可以设置收集的时间、事件类型和等级
*   支持持续记录和定时记录

**配置文件（Profiles）**
*   预定义的事件集合和设置
*   内置 `default` 和 `profile` 两种配置

#### 基本使用方法

**1. 命令行启动 JFR**
```bash
# 基本记录命令
java -XX:+FlightRecorder \
     -XX:StartFlightRecording=duration=60s,filename=myapp.jfr \
     MyApplication

# 使用特定配置
java -XX:+FlightRecorder \
     -XX:StartFlightRecording=duration=5m,filename=profiling.jfr,settings=profile \
     MyApplication

# 持续记录（手动停止）
java -XX:+FlightRecorder \
     -XX:StartFlightRecording=filename=continuous.jfr \
     MyApplication
```

**2. 程序化控制**
```java
import jdk.jfr.*;

public class JfrProgrammaticExample {
    public static void main(String[] args) throws Exception {
        // 创建记录
        Recording recording = new Recording();
        
        // 配置记录
        recording.setDuration(Duration.ofMinutes(1));
        recording.setDestination(Paths.get("app-recording.jfr"));
        
        // 启用特定事件
        recording.enable("jdk.GarbageCollection");
        recording.enable("jdk.JavaMonitorEnter");
        recording.enable("jdk.FileRead");
        recording.enable("jdk.FileWrite");
        
        // 开始记录
        recording.start();
        
        // 模拟一些工作负载
        performBusinessLogic();
        
        // 停止记录
        recording.stop();
        
        System.out.println("记录已保存到: " + recording.getDestination());
    }
    
    private static void performBusinessLogic() throws InterruptedException {
        // 模拟一些 CPU 密集和 I/O 密集的操作
        for (int i = 0; i < 1000; i++) {
            // CPU 密集操作
            double result = Math.sqrt(Math.random() * 1000000);
            
            // 内存分配
            List<String> tempList = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                tempList.add("测试数据-" + j);
            }
            
            Thread.sleep(10);
        }
    }
}
```

**3. 自定义事件**
```java
import jdk.jfr.*;

// 定义自定义事件
@Name("com.example.UserLogin")
@Label("用户登录事件")
@Description("记录用户登录相关信息")
@Category("Application")
public class UserLoginEvent extends Event {
    @Label("用户ID")
    public String userId;
    
    @Label("登录方式")
    public String loginMethod;
    
    @Label("成功标志")
    public boolean success;
    
    @Label("响应时间(ms)")
    public long responseTime;
}

// 使用自定义事件
public class UserService {
    public boolean authenticateUser(String userId, String password, String method) {
        // 创建事件实例
        UserLoginEvent event = new UserLoginEvent();
        event.userId = userId;
        event.loginMethod = method;
        event.begin(); // 开始记录时间
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 模拟认证逻辑
            boolean authenticated = performAuthentication(userId, password);
            
            event.success = authenticated;
            event.responseTime = System.currentTimeMillis() - startTime;
            
            return authenticated;
            
        } finally {
            // 提交事件
            event.commit();
        }
    }
    
    private boolean performAuthentication(String userId, String password) {
        // 模拟认证逻辑
        try {
            Thread.sleep(50); // 模拟数据库查询时间
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return Math.random() > 0.1; // 90% 成功率
    }
}
```

#### JFR 数据分析工具

**1. Java Mission Control (JMC)**
```bash
# 下载并启动 JMC
# https://jdk.java.net/jmc/
jmc

# 或者使用 Java 8 自带的版本
jmc &
```

**JMC 主要功能**:
*   性能监控仪表板
*   CPU 使用率分析
*   内存分配和垃圾回收分析
*   线程和锁分析
*   I/O 操作分析
*   自定义事件视图

**2. 命令行分析工具**
```bash
# 使用 jfr 命令行工具
# 打印记录摘要
jfr print --events CPULoad,GarbageCollection myapp.jfr

# 按事件类型统计
jfr summary myapp.jfr

# 转换为 JSON 格式
jfr print --json --events jdk.GarbageCollection myapp.jfr > gc_events.json

# 查看所有可用事件类型
jfr metadata myapp.jfr
```

#### 常用监控场景

**1. 性能问题诊断**
```java
// 模拟性能问题的代码
public class PerformanceIssueExample {
    private static final Random random = new Random();
    
    public static void main(String[] args) {
        // 启动 JFR 记录
        startRecording();
        
        // 模拟不同类型的性能问题
        for (int i = 0; i < 1000; i++) {
            if (i % 100 == 0) {
                System.out.println("处理进度: " + (i * 100 / 1000) + "%");
            }
            
            // CPU 密集操作
            performCpuIntensiveTask();
            
            // 内存分配
            createTemporaryObjects();
            
            // I/O 操作
            performIoOperation();
            
            // 线程同步
            performSynchronizedOperation();
        }
    }
    
    private static void startRecording() {
        try {
            Recording recording = new Recording();
            recording.enable("jdk.CPULoad").withPeriod(Duration.ofSeconds(1));
            recording.enable("jdk.GarbageCollection");
            recording.enable("jdk.JavaMonitorEnter");
            recording.enable("jdk.FileRead");
            recording.enable("jdk.FileWrite");
            recording.setDestination(Paths.get("performance-analysis.jfr"));
            recording.start();
            
            // 注册关闭钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                recording.stop();
                System.out.println("性能记录已保存到: performance-analysis.jfr");
            }));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void performCpuIntensiveTask() {
        // 模拟 CPU 密集计算
        double result = 0;
        for (int i = 0; i < 10000; i++) {
            result += Math.sqrt(Math.random() * 1000);
        }
    }
    
    private static void createTemporaryObjects() {
        // 模拟大量对象创建
        List<String> tempData = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            tempData.add("临时数据-" + i + "-" + System.nanoTime());
        }
        // tempData 将被 GC 回收
    }
    
    private static void performIoOperation() {
        try {
            // 模拟文件 I/O
            Path tempFile = Files.createTempFile("jfr-test", ".tmp");
            Files.write(tempFile, "test data".getBytes());
            Files.readAllLines(tempFile);
            Files.delete(tempFile);
        } catch (Exception e) {
            // 忽略错误
        }
    }
    
    private static final Object lock = new Object();
    
    private static void performSynchronizedOperation() {
        synchronized (lock) {
            try {
                Thread.sleep(1); // 模拟同步操作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
```

**2. 内存泄露检测**
```java
@Name("com.example.MemoryLeak")
@Label("内存泄露检测")
@Category("Memory")
public class MemoryLeakEvent extends Event {
    @Label("对象类型")
    public String objectType;
    
    @Label("分配大小")
    public long allocationSize;
    
    @Label("堆使用率")
    public double heapUsagePercent;
}

public class MemoryLeakDetector {
    private static final List<byte[]> leakyList = new ArrayList<>();
    
    public static void simulateMemoryLeak() {
        MemoryLeakEvent event = new MemoryLeakEvent();
        event.begin();
        
        try {
            // 模拟内存泄露
            for (int i = 0; i < 100; i++) {
                byte[] data = new byte[1024 * 1024]; // 1MB
                leakyList.add(data); // 持续添加，不释放
            }
            
            // 记录事件信息
            event.objectType = "byte[]";
            event.allocationSize = leakyList.size() * 1024 * 1024;
            
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            event.heapUsagePercent = (double) heapUsage.getUsed() / heapUsage.getMax() * 100;
            
        } finally {
            event.commit();
        }
    }
}
```

#### 生产环境最佳实践

**1. JFR 配置策略**
```bash
# 低开销配置（生产环境推荐）
java -XX:+FlightRecorder \
     -XX:StartFlightRecording=duration=30m,filename=prod-monitoring.jfr,settings=default \
     -XX:FlightRecorderOptions=disk=true,maxchunksize=10M \
     MyProductionApp

# 详细分析配置（问题诊断时使用）
java -XX:+FlightRecorder \
     -XX:StartFlightRecording=duration=5m,filename=detailed-analysis.jfr,settings=profile \
     MyProductionApp
```

**2. 监控脚本示例**
```bash
#!/bin/bash
# jfr-monitoring.sh

APP_PID=$(pgrep -f "MyProductionApp")
RECORDING_DIR="/var/log/jfr"
DATE_SUFFIX=$(date +"%Y%m%d_%H%M%S")

if [ -z "$APP_PID" ]; then
    echo "找不到应用进程"
    exit 1
fi

mkdir -p $RECORDING_DIR

# 启动 30 分钟的记录
jcmd $APP_PID JFR.start name=monitoring_$DATE_SUFFIX \
    duration=30m filename=$RECORDING_DIR/monitoring_$DATE_SUFFIX.jfr \
    settings=default

echo "JFR 记录已启动，记录文件: $RECORDING_DIR/monitoring_$DATE_SUFFIX.jfr"

# 等待记录完成
sleep 1800  # 30 分钟

# 生成简单的分析报告
jfr summary $RECORDING_DIR/monitoring_$DATE_SUFFIX.jfr > $RECORDING_DIR/summary_$DATE_SUFFIX.txt

echo "监控完成，报告文件: $RECORDING_DIR/summary_$DATE_SUFFIX.txt"
```

**3. 自动化监控和警告**
```java
public class JfrAlertingSystem {
    public static void main(String[] args) throws Exception {
        // 启动监控记录
        Recording alertRecording = new Recording();
        alertRecording.enable("jdk.GarbageCollection");
        alertRecording.enable("jdk.CPULoad").withPeriod(Duration.ofSeconds(1));
        alertRecording.enable("jdk.JavaMonitorEnter");
        
        // 设置持续记录
        alertRecording.setMaxAge(Duration.ofHours(1)); // 保持 1 小时的数据
        alertRecording.start();
        
        // 定期检查和警告
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkPerformanceMetrics(alertRecording);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 30, TimeUnit.SECONDS);
        
        // 保持运行
        Thread.currentThread().join();
    }
    
    private static void checkPerformanceMetrics(Recording recording) {
        // 这里可以实现实时数据分析和警告逻辑
        // 例如：GC 频繁、CPU 过高、锁竞争等
        System.out.println("执行性能检查 - " + new Date());
        
        // 实际实现中，可以通过 JFR API 读取实时数据
        // 并根据阈值发送警告
    }
}
```

#### 与其他工具的对比

| 特性 | JFR | JProfiler | YourKit | VisualVM |
|------|-----|-----------|---------|----------|
| 开销 | 极低(1-3%) | 中等(5-15%) | 中等(5-15%) | 低(2-5%) |
| 生产环境 | ✓ | ✗ | ✗ | ✓ |
| 详细程度 | 高 | 非常高 | 非常高 | 中等 |
| 易用性 | 中等 | 高 | 高 | 高 |
| 成本 | 免费 | 商业 | 商业 | 免费 |

Java Flight Recorder 是现代 Java 应用性能监控和问题诊断的强大工具，特别适合生产环境使用。