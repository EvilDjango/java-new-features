package com.example.playground;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * java新特性测试类
 *
 * @author chenxuejun
 * @since 2025/7/11 16:31
 */
public class JavaNewFeatureTest {

    @Test
    void localVariableTypeInferenceVarTest() {
        System.out.println("--- Java 10: Local-Variable Type Inference (var) ---");
        var greeting = "Hello, var!";
        System.out.println("变量 greeting 的推断类型: " + greeting.getClass().getName());
        var numberMap = new HashMap<String, List<Integer>>();
        numberMap.put("primes", List.of(2, 3, 5, 7));
        System.out.println("变量 numberMap 的推断类型: " + numberMap.getClass().getName());
        System.out.print("在循环中使用 var: ");
        for (var entry : numberMap.entrySet()) {
            System.out.print(entry.getKey() + " ");
        }
        System.out.println("\n");
    }

    @Test
    void switchExpressionsTest() {
        System.out.println("--- Java 14: Switch Expressions ---");
        enum Season {
            SPRING,
            SUMMER,
            AUTUMN,
            WINTER
        }
        Season season = Season.SUMMER;
        String fruitExpression = switch (season) {
            case SPRING -> "草莓";
            case SUMMER -> "西瓜";
            case AUTUMN, WINTER -> "苹果或橘子";
        };
        System.out.println("Switch 表达式获取的水果: " + fruitExpression);
        String description = switch (season) {
            case SUMMER -> {
                System.out.println("夏天是炎热的季节。");
                yield "夏天最适合吃西瓜";
            }
            default -> "其他季节的水果也很好吃";
        };
        System.out.println("带有多行逻辑的 Switch 表达式: " + description + "\n");
    }

    @Test
    void textBlocksTest() {
        System.out.println("--- Java 15: Text Blocks ---");
        String newHtml = """
                <!DOCTYPE html>
                <html>
                    <body>
                        <h1>\"Hello, World!\"</h1>
                    </body>
                </html>
                """;
        System.out.println("使用文本块创建的 HTML:\n" + newHtml);
        String json = """
                {
                    "name": "姜哥",
                    "javaVersion": 21,
                    "feature": "Text Blocks"
                }
                """;
        System.out.println("使用文本块创建的 JSON:\n" + json + "\n");
    }

    @Test
    void advancedSwitchFeaturesTest() {
        System.out.println("--- Java 21: Advanced Switch Features ---");
        System.out.println("圆形面积: " + getArea(new Circle(10)));
        System.out.println("矩形面积: " + getArea(new Rectangle(5, 10)));
        System.out.println("正方形面积 (通过矩形case处理): " + getArea(new Rectangle(7, 7)));
        System.out.println("正方形面积 (通过Square case处理): " + getArea(new Square(8)));
        System.out.println("处理 null: " + getArea(null));
        System.out.println("");
    }

    double getArea(Shape shape) {
        return switch (shape) {
            case null -> 0;
            case Circle(double r) -> Math.PI * r * r;
            case Rectangle(double l, double w) when l == w -> {
                System.out.println("(这是一个边长为 " + l + " 的正方形)");
                yield l * w;
            }
            case Rectangle(double l, double w) -> l * w;
            case Square(double s) -> s * s;
        };
    }

    @Test
    void recordTest() {
        System.out.println("--- Java 16: Record Classes ---");
        Point p1 = new Point(10, 20);
        System.out.println("创建 Point p1: " + p1);
        System.out.println("p1 的 x 坐标: " + p1.x());
        System.out.println("p1 的 y 坐标: " + p1.y());
        Point p2 = new Point(10, 20);
        System.out.println("p1 和 p2 是否相等? " + p1.equals(p2));
        System.out.println("p1 到原点的距离: " + p1.distanceToOrigin());
        System.out.println("原点坐标: " + Point.ORIGIN);
        try {
            new Point(-1, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("成功捕获到构造函数的异常: " + e.getMessage() + "\n");
        }
    }

    @Test
    void switchNullHandlingTest() {
        System.out.println("--- Java 21: Switch Null Handling ---");
        System.out.println("处理 'Java': " + processString("Java"));
        System.out.print("测试传入 null: ");
        try {
            processString(null);
        } catch (NullPointerException e) {
            System.out.println("成功捕获到 NullPointerException!");
        }
        System.out.println("");
    }

    String processString(String s) {
        return switch (s) {
            case "Java" -> "一种编程语言";
            case "Gemini" -> "一个大型语言模型";
            default -> "未知输入";
        };
    }

    @Test
    void sealedClassesTest() {
        System.out.println("--- Java 17: Sealed Classes and Interfaces ---");
        Vehicle car = new Car("CAR-123");
        Vehicle bicycle = new Bicycle("BIKE-456");
        Vehicle mountainBike = new MountainBike("MB-789");
        System.out.println("车辆类型: " + getVehicleType(car));
        System.out.println("车辆类型: " + getVehicleType(bicycle));
        System.out.println("车辆类型: " + getVehicleType(mountainBike));
        System.out.println("");
    }

    String getVehicleType(Vehicle vehicle) {
        return switch (vehicle) {
            case Car c -> "这是一辆有 " + c.getNumberOfWheels() + " 个轮子的汽车。";
            case Bicycle b -> "这是一辆自行车。";
        };
    }

    @Test
    void patternMatchingForInstanceofTest() {
        System.out.println("--- Java 16: Pattern Matching for instanceof ---");
        Object obj = "Hello, Pattern Matching!";
        if (obj instanceof String) {
            String s = (String) obj;
            System.out.println("传统方式获取的字符串长度: " + s.length());
        }
        if (obj instanceof String s) {
            System.out.println("新方式获取的字符串长度: " + s.length());
        }
        if (obj instanceof String s && s.length() > 5) {
            System.out.println("字符串 \"" + s + "\" 的长度大于 5");
        }
        System.out.println("");
    }

    @Test
    void virtualThreadsTest() throws InterruptedException {
        System.out.println("--- Java 21: Virtual Threads ---");
        int taskCount = 100_000;
        System.out.println("开始执行任务 (使用平台线程池)...");
        long platformStartTime = System.currentTimeMillis();
        try (var executor = java.util.concurrent.Executors.newFixedThreadPool(200)) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(
                        () -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        });
            }
        }
        long platformEndTime = System.currentTimeMillis();
        System.out.println(
                "平台线程池执行 "
                        + taskCount
                        + " 个任务耗时: "
                        + (platformEndTime - platformStartTime)
                        + " ms");
        System.out.println("\n开始执行任务 (使用虚拟线程)...");
        long virtualStartTime = System.currentTimeMillis();
        try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(
                        () -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        });
            }
        }
        long virtualEndTime = System.currentTimeMillis();
        System.out.println(
                "虚拟线程执行 " + taskCount + " 个任务耗时: " + (virtualEndTime - virtualStartTime) + " ms");
        System.out.println("\n注意：虚拟线程的优势在于高并发和I/O密集型任务，而不是CPU密集型计算。");
        System.out.println("这个例子中，所有任务几乎是同时开始，大约1秒后同时结束。");
    }

    @Test
    void virtualThreadCreationAndPinningTest() throws Exception {
        System.out.println("--- Virtual Threads: Creation & Pinning ---");
        Thread.startVirtualThread(
                () -> System.out.println("1. 直接通过 Thread.startVirtualThread() 启动"));
        ThreadFactory virtualThreadFactory = Thread.ofVirtual().name("my-virtual-thread-", 0).factory();
        Thread vt = virtualThreadFactory.newThread(
                () -> System.out.println(
                        "2. 通过 Factory 创建, 线程名: "
                                + Thread.currentThread().getName()));
        vt.start();
        vt.join();
        System.out.println("3. 使用 newVirtualThreadPerTaskExecutor:");
        try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> System.out.println("   - 任务在虚拟线程中执行"));
        }

        System.out.println("\n测试 ReentrantLock (不会钉住线程)...");
        int unpinnedTaskCount = 100_000;
        long startTime = System.currentTimeMillis();
        try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < unpinnedTaskCount; i++) {
                var lock = new java.util.concurrent.locks.ReentrantLock();
                executor.submit(
                        () -> {
                            lock.lock();
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            } finally {
                                lock.unlock();
                            }
                        });
            }
        }
        System.out.printf(
                "ReentrantLock 执行 %d 个任务耗时: %d ms\n",
                unpinnedTaskCount, System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        System.out.println("测试 synchronized (可能导致钉住)...");
        int pinnedTaskCount = 1000;
        try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < pinnedTaskCount; i++) {
                var monitor = new Object();

                executor.submit(
                        () -> {
                            synchronized (monitor) {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        });
            }
        }
        System.out.println(
                "synchronized 执行 "
                        + pinnedTaskCount
                        + " 个任务耗时: "
                        + (System.currentTimeMillis() - startTime)
                        + " ms");
        System.out.println("\n注意: '钉住'本身不报错, 但在高负载下会导致性能下降。应优先使用 ReentrantLock。");
        System.out.println("");
    }

    @Test
    void structuredConcurrency_Success_Test() throws Exception {
        System.out.println("--- Java 21 (Preview): Structured Concurrency ---");
        // 使用 ShutdownOnFailure 策略，任何一个子任务失败，整个作用域都会关闭
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // 1. fork: 提交一个任务去获取用户，返回一个 Subtask (类似 Future)
            StructuredTaskScope.Subtask<String> userSubtask = scope.fork(
                    () -> {
                        System.out.println("开始获取用户...");
                        Thread.sleep(Duration.ofSeconds(1));
                        System.out.println("获取用户成功");
                        return "姜哥";
                    });

            // 2. fork: 同时提交另一个任务去获取订单
            StructuredTaskScope.Subtask<String> orderSubtask = scope.fork(
                    () -> {
                        System.out.println("开始获取订单...");
                        Thread.sleep(Duration.ofSeconds(2));
                        System.out.println("获取订单成功");
                        return "订单-88888";
                    });

            // 3. join: 等待所有子任务完成，或者等待 ShutdownOnFailure 策略被触发
            scope.join();

            // 4. throwIfFailed: 如果任何子任务抛出了异常，join()会捕获它，
            // 然后在这里统一抛出，这样我们就能在一个地方处理所有并发错误。
            scope.throwIfFailed();

            // 5. 组合结果: 如果代码能走到这里，说明所有子任务都成功了
            String result = String.format("%s 的 %s", userSubtask.get(), orderSubtask.get());
            System.out.println(String.format("最终结果: %s", result));

            // 在真实测试中，我们会用断言来验证结果
            assert result.contains("姜哥") && result.contains("订单-88888");
            System.out.println("");
        }
    }

    // Helper method to demonstrate primitive type patterns
    String checkPrimitiveType(Object obj) {
        return switch (obj) {
            // 1. Pattern matching for primitive type int
            case int i -> String.format("这是一个原始类型 int: %d", i);

            // 2. Pattern matching for primitive type double with a 'when' clause
            case double d when d > 100.0 -> String.format("这是一个大的 double 值: %.1f", d);
            case double d -> String.format("这是一个 double 值: %s", d);

            // 3. Pattern matching for a reference type (to show consistency)
            case String s -> String.format("这是一个字符串，长度为: %s", s.length());

            // Default case for other types
            default -> String.format("是其他类型: %s", obj.getClass().getName());
        };
    }

    @Test
    void primitiveTypePatternsTest() {
        System.out.println("--- Java 23 (Preview): Primitive Type Patterns ---");
        System.out.println(checkPrimitiveType(101)); // int
        System.out.println(checkPrimitiveType(250.5)); // large double
        System.out.println(checkPrimitiveType(42.0)); // double
        System.out.println(checkPrimitiveType("你好, 原始类型!")); // String
        System.out.println(checkPrimitiveType(123L)); // Other type: Long
        System.out.println("");
    }

    // --- Helper Definitions ---
    sealed interface Shape permits Circle, Rectangle, Square {
    }

    sealed interface Vehicle {
    }

    record Circle(double radius) implements Shape {
    }

    record Rectangle(double length, double width) implements Shape {
    }

    record Square(double side) implements Shape {
    }

    record Point(int x, int y) {
        public static final Point ORIGIN = new Point(0, 0);

        public Point {
            if (x < 0 || y < 0) {
                throw new IllegalArgumentException("坐标不能为负数");
            }
        }

        public double distanceToOrigin() {
            return Math.sqrt(x * x + y * y);
        }
    }

    final class Car implements Vehicle {
        private final String registrationNumber;

        Car(String reg) {
            this.registrationNumber = reg;
        }

        public String getRegistrationNumber() {
            return registrationNumber;
        }

        public int getNumberOfWheels() {
            return 4;
        }
    }

    non-sealed class Bicycle implements Vehicle {
        private final String registrationNumber;

        Bicycle(String reg) {
            this.registrationNumber = reg;
        }

        public String getRegistrationNumber() {
            return registrationNumber;
        }
    }

    class MountainBike extends Bicycle {
        MountainBike(String reg) {
            super(reg);
        }
    }

    // --- Helper classes for StatementsBeforeSuperTest ---
    class ParentWithInfo {
        private final String info;

        ParentWithInfo(String info) {
            System.out.println(String.format("父类构造器被调用，传入信息: '%s'", info));
            this.info = info;
        }

        String getInfo() {
            return info;
        }
    }

    class ChildWithPreSuperLogic extends ParentWithInfo {
        ChildWithPreSuperLogic(String part1, int part2) {
            // 1. 在 super() 调用前执行逻辑
            System.out.println("子类构造器开始执行...");
            String validatedPart1 = java.util.Objects.requireNonNull(part1, "Part1 cannot be null");
            String combinedInfo = String.format("%s-%d", validatedPart1, part2 * 2);
            System.out.println(String.format("在 super() 之前完成计算，准备传入: '%s'", combinedInfo));

            // 2. 调用 super()，不再需要是第一行
            super(combinedInfo);

            // 3. super() 之后的逻辑
            System.out.println("子类构造器完成。");
        }
    }

    @Test
    void statementsBeforeSuperTest() {
        System.out.println("--- Java 22 (Preview): Statements before super() ---");
        var child = new ChildWithPreSuperLogic("data", 21);
        System.out.println(String.format("测试完成，获取到的子类信息: %s", child.getInfo()));

        System.out.println("\n测试参数校验:");
        try {
            new ChildWithPreSuperLogic(null, 0);
        } catch (NullPointerException e) {
            System.out.println(String.format("成功捕获到预期的异常: %s", e.getMessage()));
        }
        System.out.println("");
    }

    @Test
    void scopedValuesTest() {
        System.out.println("--- Java 22: Scoped Values ---");
        
        System.out.println("1. 基本用法演示:");
        basicScopedValueUsage();
        
        System.out.println("\n2. 多个作用域值:");
        multipleScopedValues();
        
        System.out.println("\n3. 嵌套作用域:");
        nestedScopes();
        
        System.out.println("\n4. 与虚拟线程配合:");
        scopedValuesWithVirtualThreads();
        
        System.out.println("\n5. ThreadLocal 对比:");
        compareWithThreadLocal();
        
        System.out.println("");
    }
    
    // 作用域值声明
    private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();
    private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();
    private static final ScopedValue<String> TENANT_ID = ScopedValue.newInstance();
    
    private void basicScopedValueUsage() {
        System.out.println("   在作用域外尝试访问:");
        try {
            USER_ID.get();
        } catch (NoSuchElementException e) {
            System.out.println("   ✓ 正确抛出异常: " + e.getClass().getSimpleName());
        }
        
        System.out.println("   在作用域内访问:");
        ScopedValue.where(USER_ID, "user-123")
                  .run(() -> {
                      System.out.println("   当前用户ID: " + USER_ID.get());
                      processBusinessLogic();
                  });
        System.out.println("   作用域结束后自动清理");
    }
    
    private void processBusinessLogic() {
        System.out.println("   业务逻辑中的用户ID: " + USER_ID.get());
        callDeepMethod();
    }
    
    private void callDeepMethod() {
        System.out.println("   深层方法中的用户ID: " + USER_ID.get());
    }
    
    private void multipleScopedValues() {
        ScopedValue.where(USER_ID, "user-456")
                  .where(REQUEST_ID, "req-789")
                  .where(TENANT_ID, "tenant-abc")
                  .run(() -> {
                      System.out.println("   用户ID: " + USER_ID.get());
                      System.out.println("   请求ID: " + REQUEST_ID.get());
                      System.out.println("   租户ID: " + TENANT_ID.get());
                  });
    }
    
    private void nestedScopes() {
        ScopedValue.where(USER_ID, "outer-user")
                  .run(() -> {
                      System.out.println("   外层作用域: " + USER_ID.get());
                      
                      ScopedValue.where(USER_ID, "inner-user")
                                .run(() -> {
                                    System.out.println("   内层作用域: " + USER_ID.get());
                                });
                      
                      System.out.println("   回到外层: " + USER_ID.get());
                  });
    }
    
    private void scopedValuesWithVirtualThreads() {
        List<String> userIds = List.of("user1", "user2", "user3");
        
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (String userId : userIds) {
                executor.submit(() -> {
                    ScopedValue.where(USER_ID, userId)
                              .run(() -> {
                                  System.out.println("   处理用户: " + USER_ID.get() + 
                                                   " 在线程: " + Thread.currentThread().getName());
                                  simulateService();
                              });
                });
            }
        }
        
        // 等待一下确保所有任务完成
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void simulateService() {
        System.out.println("     服务调用用户: " + USER_ID.get());
    }
    
    private void compareWithThreadLocal() {
        System.out.println("   ThreadLocal 方式 (传统):");
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        
        threadLocal.set("thread-local-user");
        System.out.println("   ThreadLocal 值: " + threadLocal.get());
        threadLocal.remove(); // 需要手动清理
        System.out.println("   ThreadLocal 清理后: " + threadLocal.get()); // null
        
        System.out.println("   ScopedValue 方式 (现代):");
        ScopedValue.where(USER_ID, "scoped-value-user")
                  .run(() -> {
                      System.out.println("   ScopedValue 值: " + USER_ID.get());
                      // 自动清理，无需手动管理
                  });
        System.out.println("   ScopedValue 自动清理完成");
    }

    @Test
    void sequencedCollectionsTest() {
        System.out.println("--- Java 21: Sequenced Collections (有序集合) ---");
        
        System.out.println("1. 统一的 API 演示:");
        demonstrateUnifiedAPI();
        
        System.out.println("\n2. 反向视图功能:");
        demonstrateReversedView();
        
        System.out.println("\n3. 实际应用场景:");
        demonstratePracticalUsage();
        
        System.out.println("\n4. 与传统方式对比:");
        demonstrateTraditionalVsSequenced();
        
        System.out.println("");
    }
    
    private void demonstrateUnifiedAPI() {
        // 创建不同类型的有序集合
        List<String> list = new ArrayList<>(List.of("first", "middle", "last"));
        LinkedHashSet<String> set = new LinkedHashSet<>(list);
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("first", 1);
        map.put("middle", 2);
        map.put("last", 3);
        
        System.out.println("   List: " + list);
        System.out.println("   Set: " + set);
        System.out.println("   Map: " + map);
        
        // 统一的访问方式
        System.out.println("   统一访问第一个元素:");
        System.out.println("     List.getFirst(): " + list.getFirst());
        System.out.println("     Set.getFirst(): " + set.getFirst());
        System.out.println("     Map.firstEntry(): " + map.firstEntry());
        
        System.out.println("   统一访问最后一个元素:");
        System.out.println("     List.getLast(): " + list.getLast());
        System.out.println("     Set.getLast(): " + set.getLast());
        System.out.println("     Map.lastEntry(): " + map.lastEntry());
    }
    
    private void demonstrateReversedView() {
        List<String> originalList = new ArrayList<>(List.of("A", "B", "C", "D"));
        
        System.out.println("   原始列表: " + originalList);
        
        // 获取反向视图
        var reversedView = originalList.reversed();
        System.out.println("   反向视图: " + reversedView);
        
        // 反向遍历
        System.out.print("   反向遍历: ");
        for (String item : originalList.reversed()) {
            System.out.print(item + " ");
        }
        System.out.println();
        
        // 重要：反向视图是原集合的视图，不是副本
        System.out.println("   测试视图特性:");
        originalList.addLast("E");
        System.out.println("     原始列表添加元素后: " + originalList);
        System.out.println("     反向视图自动更新: " + reversedView);
        
        // 通过反向视图修改
        reversedView.addFirst("F"); // 等同于 originalList.addLast("F")
        System.out.println("     通过反向视图添加元素后: " + originalList);
    }
    
    private void demonstratePracticalUsage() {
        System.out.println("   LRU 缓存示例:");
        
        // 简单的 LRU 缓存实现
        LinkedHashMap<String, String> cache = new LinkedHashMap<>();
        
        // 添加一些数据
        cache.putLast("key1", "value1");
        cache.putLast("key2", "value2");
        cache.putLast("key3", "value3");
        
        System.out.println("     初始缓存: " + cache);
        
        // 访问 key1 (将其移到最后，表示最近使用)
        String value = cache.remove("key1");
        cache.putLast("key1", value);
        
        System.out.println("     访问 key1 后: " + cache);
        
        // 缓存满了，移除最旧的
        if (cache.size() > 2) {
            var removed = cache.pollFirstEntry();
            System.out.println("     移除最旧的: " + removed);
            System.out.println("     移除后缓存: " + cache);
        }
        
        System.out.println("   队列操作示例:");
        // 使用 ArrayList 作为队列
        List<String> queue = new ArrayList<>();
        
        // 入队
        queue.addLast("task1");
        queue.addLast("task2");
        queue.addLast("task3");
        System.out.println("     入队后: " + queue);
        
        // 出队
        while (!queue.isEmpty()) {
            String task = queue.removeFirst();
            System.out.println("     处理任务: " + task + ", 剩余: " + queue);
        }
    }
    
    private void demonstrateTraditionalVsSequenced() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));
        
        System.out.println("   传统方式 vs 有序集合:");
        
        // 传统方式获取第一个和最后一个元素
        System.out.println("   传统方式:");
        System.out.println("     第一个: list.get(0) = " + list.get(0));
        System.out.println("     最后一个: list.get(list.size()-1) = " + list.get(list.size()-1));
        
        // 有序集合方式
        System.out.println("   有序集合方式:");
        System.out.println("     第一个: list.getFirst() = " + list.getFirst());
        System.out.println("     最后一个: list.getLast() = " + list.getLast());
        
        // 传统反向遍历
        System.out.print("   传统反向遍历: ");
        for (int i = list.size() - 1; i >= 0; i--) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();
        
        // 有序集合反向遍历
        System.out.print("   有序集合反向遍历: ");
        for (String item : list.reversed()) {
            System.out.print(item + " ");
        }
        System.out.println();
        
        // 空集合处理
        System.out.println("   空集合处理:");
        List<String> emptyList = new ArrayList<>();
        
        try {
            emptyList.getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("     空集合 getFirst() 抛出: " + e.getClass().getSimpleName());
        }
        
        try {
            emptyList.getLast();
        } catch (NoSuchElementException e) {
            System.out.println("     空集合 getLast() 抛出: " + e.getClass().getSimpleName());
        }
    }

    @Test
    void httpClientTest() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("--- Java 11: HTTP Client ---");
        
        System.out.println("1. 基本同步请求:");
        basicSyncRequest();
        
        System.out.println("\n2. 异步请求:");
        asyncRequest();
        
        System.out.println("\n3. POST 请求:");
        postRequest();
        
        System.out.println("\n4. 自定义配置:");
        customConfiguration();
        
        System.out.println("\n5. 错误处理:");
        errorHandling();
        
        System.out.println("");
    }
    
    private void basicSyncRequest() throws URISyntaxException, IOException, InterruptedException {
        // 创建 HttpClient
        HttpClient client = HttpClient.newHttpClient();
        
        // 构建请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://httpbin.org/get"))
                .GET()
                .build();
        
        // 发送同步请求
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("   状态码: " + response.statusCode());
        System.out.println("   响应头: " + response.headers().map());
        System.out.println("   响应体长度: " + response.body().length() + " 字符");
    }
    
    private void asyncRequest() {
        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/delay/1"))
                .GET()
                .build();
        
        // 异步请求
        CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("   异步请求已发送，等待响应...");
        
        future.thenAccept(response -> {
            System.out.println("   异步响应状态码: " + response.statusCode());
            System.out.println("   异步响应体长度: " + response.body().length() + " 字符");
        }).join(); // 等待完成
    }
    
    private void postRequest() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        
        String jsonData = """
                {
                    "name": "Java HttpClient",
                    "version": "Java 11+",
                    "description": "标准 HTTP 客户端测试"
                }
                """;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://httpbin.org/post"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("   POST 响应状态码: " + response.statusCode());
        System.out.println("   POST 响应体长度: " + response.body().length() + " 字符");
    }
    
    private void customConfiguration() throws URISyntaxException, IOException, InterruptedException {
        // 自定义配置的 HttpClient
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)  // 使用 HTTP/2
                .connectTimeout(Duration.ofSeconds(10))  // 连接超时
                .build();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://httpbin.org/get"))
                .timeout(Duration.ofSeconds(30))  // 请求超时
                .header("User-Agent", "Java-HttpClient/11+")
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("   自定义配置响应状态码: " + response.statusCode());
        System.out.println("   HTTP 版本: " + response.version());
        System.out.println("   User-Agent 头: " + response.request().headers().firstValue("User-Agent").orElse("N/A"));
    }
    
    private void errorHandling() {
        HttpClient client = HttpClient.newHttpClient();
        
        // 测试 404 错误
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/status/404"))
                .GET()
                .build();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("   404 错误状态码: " + response.statusCode());
            System.out.println("   HttpClient 不会为 4xx/5xx 抛出异常，需要手动检查状态码");
        } catch (IOException | InterruptedException e) {
            System.out.println("   网络错误: " + e.getMessage());
        }
        
        // 测试超时
        HttpClient timeoutClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(1))  // 极短超时
                .build();
        
        HttpRequest timeoutRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/delay/5"))
                .timeout(Duration.ofMillis(100))
                .GET()
                .build();
        
        try {
            timeoutClient.send(timeoutRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.out.println("   超时错误类型: " + e.getClass().getSimpleName());
        } catch (InterruptedException e) {
            System.out.println("   请求被中断");
        }
    }

    @Test
    void collectionFactoryMethodsTest() {
        System.out.println("--- Java 9: Collection Factory Methods ---");
        
        System.out.println("1. List.of() 方法:");
        demonstrateListOf();
        
        System.out.println("\n2. Set.of() 方法:");
        demonstrateSetOf();
        
        System.out.println("\n3. Map.of() 方法:");
        demonstrateMapOf();
        
        System.out.println("\n4. 不可变性测试:");
        demonstrateImmutability();
        
        System.out.println("\n5. 空值处理:");
        demonstrateNullHandling();
        
        System.out.println("\n6. 与传统方法对比:");
        demonstrateComparison();
        
        System.out.println("");
    }
    
    private void demonstrateListOf() {
        // 创建不可变列表
        List<String> fruits = List.of("apple", "banana", "orange");
        System.out.println("   不可变列表: " + fruits);
        
        // 空列表
        List<String> empty = List.of();
        System.out.println("   空列表: " + empty);
        
        // 单元素列表
        List<String> single = List.of("single");
        System.out.println("   单元素列表: " + single);
        
        // 多元素列表 - 可以有重复元素
        List<String> withDuplicates = List.of("a", "b", "a", "c");
        System.out.println("   含重复元素的列表: " + withDuplicates);
        
        // 列表访问
        System.out.println("   第一个元素: " + fruits.get(0));
        System.out.println("   列表大小: " + fruits.size());
        System.out.println("   是否包含 'banana': " + fruits.contains("banana"));
    }
    
    private void demonstrateSetOf() {
        // 创建不可变集合
        Set<String> colors = Set.of("red", "green", "blue");
        System.out.println("   不可变集合: " + colors);
        
        // 空集合
        Set<String> empty = Set.of();
        System.out.println("   空集合: " + empty);
        
        // 单元素集合
        Set<String> single = Set.of("unique");
        System.out.println("   单元素集合: " + single);
        
        // 测试重复元素检查
        System.out.println("   尝试创建含重复元素的集合:");
        try {
            Set<String> duplicate = Set.of("a", "b", "a"); // 会抛出异常
        } catch (IllegalArgumentException e) {
            System.out.println("   ✓ 正确抛出异常: " + e.getMessage());
        }
        
        // 集合操作
        System.out.println("   集合大小: " + colors.size());
        System.out.println("   是否包含 'red': " + colors.contains("red"));
        System.out.println("   遍历集合:");
        for (String color : colors) {
            System.out.println("     - " + color);
        }
    }
    
    private void demonstrateMapOf() {
        // 创建不可变映射
        Map<String, Integer> ages = Map.of("Alice", 25, "Bob", 30, "Charlie", 35);
        System.out.println("   不可变映射: " + ages);
        
        // 空映射
        Map<String, Integer> empty = Map.of();
        System.out.println("   空映射: " + empty);
        
        // 单键值对映射
        Map<String, String> single = Map.of("key", "value");
        System.out.println("   单键值对映射: " + single);
        
        // 使用 Map.ofEntries() 创建更大的映射
        Map<String, String> countries = Map.ofEntries(
            Map.entry("CN", "China"),
            Map.entry("US", "United States"),
            Map.entry("JP", "Japan"),
            Map.entry("DE", "Germany"),
            Map.entry("FR", "France")
        );
        System.out.println("   使用 ofEntries 创建的映射: " + countries);
        
        // 映射操作
        System.out.println("   Alice 的年龄: " + ages.get("Alice"));
        System.out.println("   映射大小: " + ages.size());
        System.out.println("   是否包含键 'Bob': " + ages.containsKey("Bob"));
        System.out.println("   所有键: " + ages.keySet());
        System.out.println("   所有值: " + ages.values());
    }
    
    private void demonstrateImmutability() {
        List<String> list = List.of("a", "b", "c");
        Set<String> set = Set.of("x", "y", "z");
        Map<String, Integer> map = Map.of("one", 1, "two", 2);
        
        System.out.println("   测试不可变性:");
        
        // 测试 List 不可变性
        try {
            list.add("d");
        } catch (UnsupportedOperationException e) {
            System.out.println("   ✓ List.add() 正确抛出异常: " + e.getClass().getSimpleName());
        }
        
        try {
            list.remove(0);
        } catch (UnsupportedOperationException e) {
            System.out.println("   ✓ List.remove() 正确抛出异常: " + e.getClass().getSimpleName());
        }
        
        // 测试 Set 不可变性
        try {
            set.add("w");
        } catch (UnsupportedOperationException e) {
            System.out.println("   ✓ Set.add() 正确抛出异常: " + e.getClass().getSimpleName());
        }
        
        // 测试 Map 不可变性
        try {
            map.put("three", 3);
        } catch (UnsupportedOperationException e) {
            System.out.println("   ✓ Map.put() 正确抛出异常: " + e.getClass().getSimpleName());
        }
        
        try {
            map.remove("one");
        } catch (UnsupportedOperationException e) {
            System.out.println("   ✓ Map.remove() 正确抛出异常: " + e.getClass().getSimpleName());
        }
    }
    
    private void demonstrateNullHandling() {
        System.out.println("   测试空值处理:");
        
        // List 不接受 null 值
        try {
            List<String> listWithNull = List.of("a", null, "c");
        } catch (NullPointerException e) {
            System.out.println("   ✓ List.of() 拒绝 null 值: " + e.getClass().getSimpleName());
        }
        
        // Set 不接受 null 值
        try {
            Set<String> setWithNull = Set.of("a", null, "c");
        } catch (NullPointerException e) {
            System.out.println("   ✓ Set.of() 拒绝 null 值: " + e.getClass().getSimpleName());
        }
        
        // Map 不接受 null 键或值
        try {
            Map<String, String> mapWithNullKey = Map.of(null, "value");
        } catch (NullPointerException e) {
            System.out.println("   ✓ Map.of() 拒绝 null 键: " + e.getClass().getSimpleName());
        }
        
        try {
            Map<String, String> mapWithNullValue = Map.of("key", null);
        } catch (NullPointerException e) {
            System.out.println("   ✓ Map.of() 拒绝 null 值: " + e.getClass().getSimpleName());
        }
    }
    
    private void demonstrateComparison() {
        System.out.println("   传统方法 vs 工厂方法:");
        
        // 传统方法创建不可变列表
        List<String> traditionalList = Collections.unmodifiableList(
            Arrays.asList("apple", "banana", "orange")
        );
        
        // 工厂方法创建不可变列表
        List<String> factoryList = List.of("apple", "banana", "orange");
        
        System.out.println("   传统方法创建: " + traditionalList);
        System.out.println("   工厂方法创建: " + factoryList);
        System.out.println("   内容相等: " + traditionalList.equals(factoryList));
        
        // 性能对比示例
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            List<String> traditional = Collections.unmodifiableList(
                Arrays.asList("a", "b", "c")
            );
        }
        long traditionalTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            List<String> factory = List.of("a", "b", "c");
        }
        long factoryTime = System.nanoTime() - startTime;
        
        System.out.println("   传统方法耗时: " + traditionalTime + " ns");
        System.out.println("   工厂方法耗时: " + factoryTime + " ns");
        System.out.println("   工厂方法快了: " + (traditionalTime - factoryTime) + " ns");
    }

    @Test
    void apiEnhancementsTest() {
        System.out.println("--- Java 9+: Stream, Optional, String API 增强 ---");
        
        System.out.println("1. Stream API 增强:");
        demonstrateStreamEnhancements();
        
        System.out.println("\n2. Optional API 增强:");
        demonstrateOptionalEnhancements();
        
        System.out.println("\n3. String API 增强:");
        demonstrateStringEnhancements();
        
        System.out.println("\n4. 实际应用场景:");
        demonstratePracticalUsageOfStringNewFeature();
        
        System.out.println("");
    }
    
    private void demonstrateStreamEnhancements() {
        System.out.println("   Stream.takeWhile() 和 dropWhile() (Java 9):");
        
        // takeWhile - 从头开始取元素，直到条件不满足为止
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> taken = numbers.stream()
                .takeWhile(n -> n <= 5)
                .collect(Collectors.toList());
        System.out.println("   takeWhile(n -> n <= 5): " + taken);
        
        // dropWhile - 从头开始丢弃元素，直到条件不满足为止
        List<Integer> dropped = numbers.stream()
                .dropWhile(n -> n <= 5)
                .collect(Collectors.toList());
        System.out.println("   dropWhile(n -> n <= 5): " + dropped);
        
        // 与 filter 的区别
        List<Integer> filtered = numbers.stream()
                .filter(n -> n > 5)
                .collect(Collectors.toList());
        System.out.println("   filter(n -> n > 5): " + filtered);
        
        System.out.println("\n   Stream.iterate() 重载 (Java 9):");
        
        // 传统的 iterate (无限流)
        List<Integer> traditional = Stream.iterate(1, n -> n + 1)
                .limit(5)
                .collect(Collectors.toList());
        System.out.println("   传统 iterate: " + traditional);
        
        // 新的 iterate (带条件的有限流)
        List<Integer> enhanced = Stream.iterate(1, n -> n <= 10, n -> n + 2)
                .collect(Collectors.toList());
        System.out.println("   增强 iterate: " + enhanced);
        
        System.out.println("\n   Stream.ofNullable() (Java 9):");
        
        // 安全处理可能为 null 的单个元素
        String nullableString = null;
        String validString = "hello";
        
        long nullCount = Stream.ofNullable(nullableString).count();
        long validCount = Stream.ofNullable(validString).count();
        
        System.out.println("   ofNullable(null) 元素数量: " + nullCount);
        System.out.println("   ofNullable(\"hello\") 元素数量: " + validCount);
        
        // 实际使用场景
        List<String> results = Stream.of("a", null, "b", "c")
                .flatMap(Stream::ofNullable)
                .collect(Collectors.toList());
        System.out.println("   过滤 null 后的结果: " + results);
    }
    
    private void demonstrateOptionalEnhancements() {
        System.out.println("   Optional.ifPresentOrElse() (Java 9):");
        
        Optional<String> present = Optional.of("Hello");
        Optional<String> empty = Optional.empty();
        
        present.ifPresentOrElse(
            value -> System.out.println("   有值时执行: " + value),
            () -> System.out.println("   无值时执行")
        );
        
        empty.ifPresentOrElse(
            value -> System.out.println("   有值时执行: " + value),
            () -> System.out.println("   无值时执行")
        );
        
        System.out.println("\n   Optional.or() (Java 9):");
        
        Optional<String> backup = Optional.of("backup");
        Optional<String> result = empty.or(() -> backup);
        System.out.println("   使用备选 Optional: " + result.get());
        
        System.out.println("\n   Optional.stream() (Java 9):");
        
        List<Optional<String>> optionals = List.of(
            Optional.of("a"),
            Optional.empty(),
            Optional.of("b"),
            Optional.of("c")
        );
        
        List<String> values = optionals.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
        System.out.println("   从 Optional 流中提取值: " + values);
        
        System.out.println("\n   Optional.isEmpty() (Java 11):");
        
        System.out.println("   present.isEmpty(): " + present.isEmpty());
        System.out.println("   empty.isEmpty(): " + empty.isEmpty());
        
        // 与 isPresent 的对比
        System.out.println("   present.isPresent(): " + present.isPresent());
        System.out.println("   empty.isPresent(): " + empty.isPresent());
    }
    
    private void demonstrateStringEnhancements() {
        System.out.println("   String.isBlank() (Java 11):");
        
        String empty = "";
        String whitespace = "   ";
        String content = "hello";
        
        System.out.println("   \"\".isBlank(): " + empty.isBlank());
        System.out.println("   \"   \".isBlank(): " + whitespace.isBlank());
        System.out.println("   \"hello\".isBlank(): " + content.isBlank());
        
        System.out.println("\n   String.lines() (Java 11):");
        
        String multiline = "line1\nline2\r\nline3\rline4";
        List<String> lines = multiline.lines().collect(Collectors.toList());
        System.out.println("   多行字符串分割: " + lines);
        
        System.out.println("\n   String.strip() 系列方法 (Java 11):");
        
        String padded = "   hello world   ";
        System.out.println("   原始: '" + padded + "'");
        System.out.println("   strip(): '" + padded.strip() + "'");
        System.out.println("   stripLeading(): '" + padded.stripLeading() + "'");
        System.out.println("   stripTrailing(): '" + padded.stripTrailing() + "'");
        
        // 与 trim() 的区别（Unicode 空白字符处理）
        String unicode = "\u2000hello\u2000";  // Unicode 空格
        System.out.println("   Unicode 空格 trim(): '" + unicode.trim() + "'");
        System.out.println("   Unicode 空格 strip(): '" + unicode.strip() + "'");
        
        System.out.println("\n   String.repeat() (Java 11):");
        
        String pattern = "=";
        String separator = pattern.repeat(20);
        System.out.println("   重复字符: " + separator);
        
        String greeting = "Hello! ";
        System.out.println("   重复问候: " + greeting.repeat(3));
        
        System.out.println("\n   String.transform() (Java 12):");
        
        String text = "  Hello World  ";
        String transformed = text.transform(String::strip)
                                 .transform(String::toLowerCase)
                                 .transform(s -> s.replace(" ", "_"));
        System.out.println("   链式变换: " + transformed);
        
        System.out.println("\n   String.formatted() (Java 15):");
        
        String name = "Alice";
        int age = 30;
        String formatted = "Name: %s, Age: %d".formatted(name, age);
        System.out.println("   格式化字符串: " + formatted);
        
        // 等价于 String.format()
        String traditional = String.format("Name: %s, Age: %d", name, age);
        System.out.println("   传统格式化: " + traditional);
    }
    
    private void demonstratePracticalUsageOfStringNewFeature() {
        System.out.println("   实际应用场景:");
        
        // 1. 日志处理
        System.out.println("\n   日志处理示例:");
        String logData = """
            2024-01-01 10:00:00 INFO Application started
            2024-01-01 10:00:01 DEBUG Loading configuration
            2024-01-01 10:00:02 ERROR Failed to connect to database
            2024-01-01 10:00:03 INFO Retrying connection
            2024-01-01 10:00:04 INFO Database connected successfully
            """;
        
        List<String> errorLogs = logData.lines()
                .map(String::strip)
                .filter(line -> !line.isBlank())
                .takeWhile(line -> !line.contains("Database connected"))
                .filter(line -> line.contains("ERROR"))
                .collect(Collectors.toList());
        
        System.out.println("   错误日志: " + errorLogs);
        
        // 2. 配置处理
        System.out.println("\n   配置处理示例:");
        Map<String, String> config = Map.of(
            "database.url", "jdbc:mysql://localhost:3306/mydb",
            "database.username", "admin",
            "database.password", "",
            "app.name", "MyApplication"
        );
        
        Optional<String> dbPassword = Optional.ofNullable(config.get("database.password"))
                .filter(pwd -> !pwd.isBlank());
        
        dbPassword.ifPresentOrElse(
            pwd -> System.out.println("   数据库密码已配置"),
            () -> System.out.println("   警告: 数据库密码未配置")
        );
        
        // 3. 数据验证
        System.out.println("\n   数据验证示例:");
        List<String> userInputs = List.of(
            "alice@example.com",
            "",
            "   ",
            "bob@test.com",
            "invalid-email",
            "charlie@domain.org"
        );
        
        List<String> validEmails = userInputs.stream()
                .filter(input -> !input.isBlank())
                .map(String::strip)
                .filter(email -> email.contains("@") && email.contains("."))
                .collect(Collectors.toList());
        
        System.out.println("   有效邮箱: " + validEmails);
        
        // 4. 报告生成
        System.out.println("\n   报告生成示例:");
        List<String> items = List.of("项目A", "项目B", "项目C");
        
        String report = "报告标题".transform(title -> 
            "=".repeat(20) + "\n" + 
            title + "\n" + 
            "=".repeat(20) + "\n"
        ) + items.stream()
            .map(item -> "- " + item)
            .collect(Collectors.joining("\n")) + "\n" +
            "=".repeat(20);
        
        System.out.println("   生成的报告:\n" + report);
    }

    @Test
    void streamGatherersTest() {
        System.out.println("--- Java 24: Stream Gatherers ---");
        
        System.out.println("注意：Stream Gatherers 是 Java 24 的正式特性");
        System.out.println("由于当前环境可能不支持，以下是概念性演示\n");
        
        System.out.println("1. 基本概念:");
        demonstrateGatherersBasics();
        
        System.out.println("\n2. 内置 Gatherers:");
        demonstrateBuiltinGatherers();
        
        System.out.println("\n3. 自定义 Gatherers:");
        demonstrateCustomGatherers();
        
        System.out.println("\n4. 实际应用场景:");
        demonstrateGatherersUseCases();
        
        System.out.println("");
    }
    
    private void demonstrateGatherersBasics() {
        System.out.println("   Stream Gatherers 的基本概念:");
        System.out.println("   - Gatherers 是可以插入到 Stream 管道中的自定义操作");
        System.out.println("   - 类似于 Collector，但作用于中间操作而不是终结操作");
        System.out.println("   - 支持有状态的流处理");
        System.out.println("   - 可以处理无限流");
        
        // 基本使用语法（伪代码）
        System.out.println("\n   基本语法:");
        System.out.println("   stream.gather(gatherer).collect(...)");
        
        // 示例：窗口处理
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("   输入数据: " + numbers);
        
        // 模拟滑动窗口处理（概念性代码）
        System.out.println("   滑动窗口示例（概念）:");
        System.out.println("   - 窗口大小: 3");
        System.out.println("   - 结果: [[1,2,3], [2,3,4], [3,4,5], ...]");
    }
    
    private void demonstrateBuiltinGatherers() {
        System.out.println("   内置 Gatherers 示例:");
        
        // 1. 滑动窗口 (sliding window)
        System.out.println("   1. 滑动窗口 (sliding):");
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        System.out.println("   输入: " + numbers);
        
        // 模拟滑动窗口结果
        System.out.println("   滑动窗口(3): [[1,2,3], [2,3,4], [3,4,5], [4,5,6], [5,6,7], [6,7,8]]");
        
        // 2. 固定窗口 (fixed window)
        System.out.println("\n   2. 固定窗口 (windowed):");
        System.out.println("   固定窗口(3): [[1,2,3], [4,5,6], [7,8]]");
        
        // 3. 去重 (distinct)
        System.out.println("\n   3. 去重 (distinct):");
        List<Integer> duplicates = List.of(1, 2, 2, 3, 3, 3, 4, 5, 5);
        System.out.println("   输入: " + duplicates);
        System.out.println("   去重后: [1, 2, 3, 4, 5]");
        
        // 4. 扫描 (scan)
        System.out.println("\n   4. 扫描 (scan):");
        List<Integer> nums = List.of(1, 2, 3, 4, 5);
        System.out.println("   输入: " + nums);
        System.out.println("   累积和: [1, 3, 6, 10, 15]");
    }
    
    private void demonstrateCustomGatherers() {
        System.out.println("   自定义 Gatherers 概念:");
        
        System.out.println("   1. 批处理 Gatherer:");
        System.out.println("   - 将流元素分组为指定大小的批次");
        System.out.println("   - 例如：[1,2,3,4,5,6,7,8] -> [[1,2,3], [4,5,6], [7,8]]");
        
        System.out.println("\n   2. 状态跟踪 Gatherer:");
        System.out.println("   - 跟踪处理过程中的状态变化");
        System.out.println("   - 例如：计数器、累加器、最大值跟踪等");
        
        System.out.println("\n   3. 条件过滤 Gatherer:");
        System.out.println("   - 基于复杂条件进行过滤");
        System.out.println("   - 例如：连续重复元素过滤、峰值检测等");
        
        // 自定义 Gatherer 的结构（概念性代码）
        System.out.println("\n   自定义 Gatherer 结构:");
        System.out.println("   Gatherer.of(");
        System.out.println("     supplier,     // 状态初始化");
        System.out.println("     integrator,   // 元素处理逻辑");
        System.out.println("     finisher      // 最终处理");
        System.out.println("   )");
        
        // 示例：简单的批处理实现概念
        System.out.println("\n   批处理示例实现概念:");
        List<Integer> input = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        System.out.println("   输入: " + input);
        
        // 手动实现批处理逻辑来演示概念
        List<List<Integer>> batches = new ArrayList<>();
        List<Integer> currentBatch = new ArrayList<>();
        int batchSize = 3;
        
        for (Integer num : input) {
            currentBatch.add(num);
            if (currentBatch.size() == batchSize) {
                batches.add(new ArrayList<>(currentBatch));
                currentBatch.clear();
            }
        }
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }
        
        System.out.println("   批处理结果: " + batches);
    }
    
    private void demonstrateGatherersUseCases() {
        System.out.println("   Stream Gatherers 的实际应用场景:");
        
        System.out.println("\n   1. 数据分析:");
        System.out.println("   - 移动平均计算");
        System.out.println("   - 趋势分析");
        System.out.println("   - 异常检测");
        
        // 移动平均示例（概念性实现）
        System.out.println("\n   移动平均示例:");
        List<Double> prices = List.of(100.0, 102.0, 101.0, 103.0, 105.0, 104.0, 106.0);
        System.out.println("   股价数据: " + prices);
        
        // 手动计算3日移动平均
        List<Double> movingAverage = new ArrayList<>();
        for (int i = 0; i <= prices.size() - 3; i++) {
            double avg = (prices.get(i) + prices.get(i + 1) + prices.get(i + 2)) / 3.0;
            movingAverage.add(Math.round(avg * 100.0) / 100.0);
        }
        System.out.println("   3日移动平均: " + movingAverage);
        
        System.out.println("\n   2. 实时数据处理:");
        System.out.println("   - 流式数据聚合");
        System.out.println("   - 实时统计");
        System.out.println("   - 事件窗口处理");
        
        System.out.println("\n   3. 批处理优化:");
        System.out.println("   - 数据库批量插入");
        System.out.println("   - 网络请求批处理");
        System.out.println("   - 文件批量处理");
        
        System.out.println("\n   4. 状态机处理:");
        System.out.println("   - 解析器状态跟踪");
        System.out.println("   - 工作流状态管理");
        System.out.println("   - 游戏状态处理");
        
        // 状态机示例（概念性实现）
        System.out.println("\n   简单状态机示例:");
        List<String> events = List.of("START", "PROCESS", "ERROR", "RETRY", "SUCCESS", "END");
        System.out.println("   事件序列: " + events);
        
        // 手动实现状态跟踪
        List<String> stateTransitions = new ArrayList<>();
        String currentState = "IDLE";
        
        for (String event : events) {
            String previousState = currentState;
            switch (event) {
                case "START" -> currentState = "RUNNING";
                case "PROCESS" -> currentState = "PROCESSING";
                case "ERROR" -> currentState = "ERROR";
                case "RETRY" -> currentState = "RETRYING";
                case "SUCCESS" -> currentState = "SUCCESS";
                case "END" -> currentState = "FINISHED";
            }
            stateTransitions.add(previousState + " -> " + currentState + " (事件: " + event + ")");
        }
        
        System.out.println("   状态转换:");
        stateTransitions.forEach(transition -> System.out.println("     " + transition));
        
        System.out.println("\n   5. 数据转换管道:");
        System.out.println("   - ETL 处理");
        System.out.println("   - 数据清洗");
        System.out.println("   - 格式转换");
        
        System.out.println("\n   Stream Gatherers 的优势:");
        System.out.println("   - 高度可定制的流处理");
        System.out.println("   - 内存效率高");
        System.out.println("   - 可组合性强");
        System.out.println("   - 支持无限流");
        System.out.println("   - 类型安全");
    }

    @Test
    void moduleSystemTest() {
        System.out.println("--- Java 9: 模块化系统 (Project Jigsaw) ---");
        
        // 实际的模块API调用演示
        System.out.println("检查当前运行时模块信息:");
        try {
            // 获取当前类的模块
            Module currentModule = this.getClass().getModule();
            System.out.println("当前模块名称: " + currentModule.getName());
            System.out.println("是否命名模块: " + currentModule.isNamed());
            System.out.println("模块描述符: " + currentModule.getDescriptor());
            
            // 列出一些系统模块
            System.out.println("\n系统模块示例:");
            ModuleLayer.boot().modules().stream()
                    .filter(m -> m.getName() != null)
                    .filter(m -> m.getName().startsWith("java."))
                    .sorted((m1, m2) -> m1.getName().compareTo(m2.getName()))
                    .limit(10)
                    .forEach(m -> System.out.println("  " + m.getName()));
                    
        } catch (Exception e) {
            System.out.println("获取模块信息时出错: " + e.getMessage());
        }
        
        System.out.println("");
    }

    @Test
    void detailedNullPointerExceptionTest() {
        System.out.println("--- Java 14: 更详尽的 NullPointerException ---");
        
        System.out.println("1. 方法调用链中的 NPE:");
        testChainedMethodCall();
        
        System.out.println("\n2. 数组访问中的 NPE:");
        testArrayAccess();
        
        System.out.println("\n3. 字段访问中的 NPE:");
        testFieldAccess();
        
        System.out.println("");
    }
    
    private void testChainedMethodCall() {
        try {
            TestUser user = new TestUser();
            // 这会产生详细的 NPE 信息
            String country = user.getProfile().getAddress().getCountry().toUpperCase();
            System.out.println("国家: " + country);
        } catch (NullPointerException e) {
            System.out.println("捕获到 NPE: " + e.getMessage());
        }
    }
    
    private void testArrayAccess() {
        try {
            String[][] matrix = new String[3][];
            // 这会产生详细的 NPE 信息
            int length = matrix[1].length;
            System.out.println("长度: " + length);
        } catch (NullPointerException e) {
            System.out.println("数组访问 NPE: " + e.getMessage());
        }
    }
    
    private void testFieldAccess() {
        try {
            TestContainer container = null;
            // 这会产生详细的 NPE 信息
            System.out.println("值: " + container.value);
        } catch (NullPointerException e) {
            System.out.println("字段访问 NPE: " + e.getMessage());
        }
    }

    @Test
    void jfrBasicUsageTest() throws Exception {
        System.out.println("--- Java 11: Java Flight Recorder (JFR) ---");
        
        System.out.println("1. 程序化控制 JFR:");
        demonstrateJfrProgrammatic();
        
        System.out.println("\n2. 自定义事件演示:");
        demonstrateCustomJfrEvent();
        
        System.out.println("");
    }
    
    private void demonstrateJfrProgrammatic() {
        try {
            // 注意：实际环境中需要添加 jdk.jfr 模块
            System.out.println("JFR 功能演示 - 由于模块限制，这里仅展示概念");
            System.out.println("实际使用需要:");
            System.out.println("  1. 添加 --add-modules jdk.jfr JVM 参数");
            System.out.println("  2. 或在 module-info.java 中添加 requires jdk.jfr;");
            System.out.println("  3. 使用 Recording API 进行编程控制");
            
            // 模拟 JFR 记录过程
            simulateBusinessOperation();
            
        } catch (Exception e) {
            System.out.println("JFR 演示过程中出现异常: " + e.getMessage());
        }
    }
    
    private void demonstrateCustomJfrEvent() {
        System.out.println("自定义 JFR 事件概念演示:");
        System.out.println("  - 定义自定义事件类继承 Event");
        System.out.println("  - 使用 @Name, @Label, @Description 注解");
        System.out.println("  - 在业务逻辑中调用 begin() 和 commit()");
        
        // 模拟业务操作并记录
        long startTime = System.nanoTime();
        performSampleBusinessLogic();
        long duration = System.nanoTime() - startTime;
        
        System.out.println("  模拟事件记录: 业务操作耗时 " + (duration / 1_000_000) + " ms");
    }
    
    private void simulateBusinessOperation() throws InterruptedException {
        System.out.println("  执行模拟业务操作...");
        
        // CPU 密集操作
        for (int i = 0; i < 10000; i++) {
            Math.sqrt(Math.random() * 1000);
        }
        
        // 内存分配
        List<String> tempData = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            tempData.add("测试数据-" + i);
        }
        
        // I/O 模拟
        Thread.sleep(50);
        
        System.out.println("  业务操作完成");
    }
    
    private void performSampleBusinessLogic() {
        // 模拟一些计算密集的操作
        double result = 0;
        for (int i = 0; i < 100000; i++) {
            result += Math.random();
        }
        
        // 模拟数据处理
        Map<String, Integer> dataMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            dataMap.put("key" + i, i * 2);
        }
    }

    @Test
    void singleFileSourceLaunchDemoTest() {
        System.out.println("--- Java 11: 单文件源码程序启动 ---");
        
        System.out.println("单文件启动功能演示:");
        System.out.println("1. 传统方式:");
        System.out.println("   javac HelloWorld.java");
        System.out.println("   java HelloWorld");
        System.out.println("");
        
        System.out.println("2. 单文件启动方式 (Java 11+):");
        System.out.println("   java HelloWorld.java");
        System.out.println("");
        
        System.out.println("3. 带参数的单文件启动:");
        System.out.println("   java HelloWorld.java arg1 arg2");
        System.out.println("");
        
        System.out.println("4. 使用类路径的单文件启动:");
        System.out.println("   java --class-path /path/to/libs MyScript.java");
        System.out.println("");
        
        System.out.println("5. Shebang 支持 (Unix/Linux):");
        System.out.println("   在 .java 文件第一行添加: #!/usr/bin/java --source 11");
        System.out.println("   chmod +x script.java");
        System.out.println("   ./script.java");
        System.out.println("");
        
        // 演示一个简单的工具类功能
        demonstrateUtilityFunction();
    }
    
    private void demonstrateUtilityFunction() {
        System.out.println("模拟单文件工具类功能:");
        
        // 模拟文件处理工具
        String[] sampleData = {"apple", "banana", "cherry", "date"};
        System.out.println("  原始数据: " + Arrays.toString(sampleData));
        
        // 数据处理
        String processed = Arrays.stream(sampleData)
            .map(String::toUpperCase)
            .collect(Collectors.joining(", "));
        System.out.println("  处理后: " + processed);
        
        // 统计信息
        int totalLength = Arrays.stream(sampleData)
            .mapToInt(String::length)
            .sum();
        System.out.println("  总字符数: " + totalLength);
        
        System.out.println("  这种功能非常适合单文件启动模式");
    }

    // 辅助测试类
    static class TestUser {
        private TestProfile profile;
        
        public TestProfile getProfile() {
            return profile; // 返回 null，会导致 NPE
        }
    }
    
    static class TestProfile {
        private TestAddress address;
        
        public TestAddress getAddress() {
            return address;
        }
    }
    
    static class TestAddress {
        private String country;
        
        public String getCountry() {
            return country;
        }
    }
    
    static class TestContainer {
        String value;
    }

}
