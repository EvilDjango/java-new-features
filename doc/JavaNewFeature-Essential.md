# Java 新特性精华版 (Java 8 -> 24)

本文档是完整版 Java 新特性手册的精华版本，精选了最实用和最重要的 13 个新特性，涵盖语法增强、并发性能、API 增强和开发工具等核心领域。

> **📖 精华版特色**
> - 精选 13 个最实用的新特性
> - 保持完整的代码示例和最佳实践
> - 专注于日常开发中最常用的功能
> - 建议配合完整版文档深入学习

---

## 🚀 快速导航

### 按 Java 版本快速查找
| Java 版本 | 主要特性 | 
|-----------|---------|
| Java 10 | `var` 类型推断 |
| Java 11 | HTTP 客户端 |
| Java 14 | Switch 表达式、详尽 NPE |
| Java 15 | 文本块、新 GC |
| Java 16 | 记录类 |
| Java 17 | 密封类 |
| Java 21 | 虚拟线程、有序集合 |
| Java 24 | Stream 聚合器 |

### 按功能分类快速查找
| 分类 | 相关特性 |
|------|---------|
| 🎯 语法增强 | var、Switch表达式、文本块、记录类、密封类、模式匹配、未命名模式 |
| ⚡ 并发性能 | 虚拟线程、新GC |
| 📚 API 增强 | HTTP客户端、有序集合、集合工厂、Stream增强、Stream聚合器 |
| 🛠️ 开发工具 | 详尽NPE |

---

## 📋 详细目录

*   [**一、 语言核心特性**](#一-语言核心与语法)
    *   [1. 局部变量类型推断 (`var`) (Java 10)](#1-局部变量类型推断-var-java-10)
    *   [2. Switch 表达式 (Java 14)](#2-switch-表达式-java-14)
    *   [3. 文本块 (Text Blocks) (Java 15)](#3-文本块-text-blocks-java-15)
    *   [4. 记录类 (`record`) (Java 16)](#4-记录类-record-java-16)
    *   [5. 密封类和接口 (Sealed Classes) (Java 17)](#5-密封类和接口-sealed-classes-java-17)
    *   [6. 模式匹配 (Pattern Matching) (Java 16-23)](#6-模式匹配-pattern-matching-java-16-23)
    *   [7. 未命名模式和变量 (Unnamed Patterns & Variables) (Java 22, 正式)](#7-未命名模式和变量-unnamed-patterns--variables-java-22-正式)
*   [**二、 并发与性能**](#二-并发与性能)
    *   [10. 虚拟线程 (Virtual Threads) (Java 21, 正式)](#10-虚拟线程-virtual-threads-java-21-正式)
    *   [19. 新的垃圾收集器 (ZGC & Shenandoah) (Java 15)](#19-新的垃圾收集器-zgc--shenandoah-java-15)
*   [**三、 API 与标准库**](#三-api-与库)
    *   [13. 有序集合 (Sequenced Collections) (Java 21)](#13-有序集合-sequenced-collections-java-21)
    *   [14. HTTP 客户端 (Standard HTTP Client) (Java 11)](#14-http-客户端-standard-http-client-java-11)
    *   [15. 集合工厂方法 (Collection Factory Methods) (Java 9)](#15-集合工厂方法-collection-factory-methods-java-9)
    *   [16. Stream, Optional, String 等 API 增强 (Java 9+)](#16-stream-optional-string-等-api-增强-java-9)
    *   [17. Stream 聚合器 (Stream Gatherers) (Java 24, 正式)](#17-stream-聚合器-stream-gatherers-java-24-正式)
*   [**四、 开发体验与工具**](#四-开发体验与工具)
    *   [25. 更详尽的 NullPointerException (Java 14)](#25-更详尽的-nullpointerexception-java-14)

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

[**🔙 返回目录**](#📋-详细目录)

---

### 19. 新的垃圾收集器 (ZGC & Shenandoah) (Java 15)

* **演进**: 两者均在 Java 15 成为生产可用。
* **核心理念**: 现代的、并发的、低延迟的垃圾收集器 (GC)。
* **核心优势**: 旨在将 GC 的"Stop-The-World" (STW) 停顿时间控制在**毫秒级甚至亚毫秒级**，并且该停顿时间  **不随堆内存大小的增加而显著增加**。这解决了 G1 等传统 GC 在处理大堆内存时可能出现的长时停顿问题。
* **适用场景**: 对延迟极其敏感、且使用大堆内存（通常 4GB 以上）的应用。例如：金融交易平台、实时数据分析、大型电商网站、需要稳定响应时间的微服务等。
* **如何启用**: 这是配置层面的特性，通过 JVM 启动参数启用。
  * **ZGC**: `java -XX:+UseZGC -jar my-app.jar`
  * **Shenandoah**: `java -XX:+UseShenandoahGC -jar my-app.jar`

## 四、 开发体验与工具

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




---

## 📚 结语

这份精华版文档涵盖了 Java 8 到 Java 24 中最实用的 13 个新特性。这些特性不仅能显著提升开发效率，还能让代码更加简洁、安全和高性能。

### 推荐学习路径
1. **语法基础**: 从 `var`、Switch 表达式、文本块开始
2. **面向对象**: 掌握记录类和密封类的使用
3. **函数式编程**: 深入 Stream API 的各种增强
4. **并发编程**: 重点学习虚拟线程，这是现代 Java 的核心优势
5. **生产实践**: 关注 HTTP 客户端、详尽 NPE 等提升开发体验的特性

### 持续学习建议
- 配合完整版文档深入学习每个特性的高级用法
- 在实际项目中逐步应用这些新特性
- 关注 Java 社区的最新发展和最佳实践

> **💡 提示**: 本文档基于完整版 Java 新特性手册精选而成。如需了解更多特性（如模块化系统、JShell、向量 API 等）的详细内容，请参考完整版文档。

---

*最后更新: 2024年*  
*完整版地址: JavaNewFeature.md*
