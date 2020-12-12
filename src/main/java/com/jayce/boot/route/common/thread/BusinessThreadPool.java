package com.jayce.boot.route.common.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 业务公用线程池
 *
 * @author 111223/sunjie
 * @date 2020/12/12 11:18
 */
public class BusinessThreadPool {
    /**
     * 线程池名
     */
    private String name;

    /**
     * 池容量
     */
    private int corePoolSize = 5;

    /**
     * 最大池容量
     */
    private int maximumPoolSize = 15;

    /**
     * 最大池保活时间
     */
    private long keepAliveTime = 60;

    /**
     * 保活单位
     */
    private TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 阻塞队列，减少阻塞，实时处理
     */
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(10000);

    /**
     * 线程池执行者
     */
    private ThreadPoolExecutor executor;

    /**
     * 池缓存
     */
    public static Map<String, BusinessThreadPool> map = new ConcurrentHashMap<>();

    public static BusinessThreadPool getInstance(String name) {
        BusinessThreadPool pool = map.get(name);
        if (null == pool) {
            pool = new BusinessThreadPool(name);
            map.put(name, pool);
        }
        return pool;
    }

    /**
     * 默认参数构造
     *
     * @param name
     */
    public BusinessThreadPool(String name) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(name + "-pool-%d").build();
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.name = name;
        map.put(name, this);
    }

    /**
     * 指定参数构造
     *
     * @param name
     */
    public BusinessThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, String name) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(name + "-pool-%d").build();
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.name = name;
        map.put(name, this);
    }

    /**
     * runable任务
     */
    public void execute(Runnable runnable) {
        if (!isShutdown()) {
            executor.execute(runnable);
        }
    }

    /**
     * callable任务
     */
    public <V> Future<V> execute(Callable<V> callable) {
        return executor.submit(callable);
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        executor.shutdown();
        map.remove(this.name);
    }

    /**
     * 线程池是否关闭
     */
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    /**
     * 队列剩余
     */
    public boolean isQueueFull() {
        return workQueue.remainingCapacity() <= 0;
    }

    /**
     * 线程运行中
     */
    public boolean isWork() {
        return executor.getActiveCount() != 0;
    }

    @Getter
    public enum PoolNameEnum {
        //线程池名枚举
        POOL_TEST(1, "Test"),
        ;

        private Integer code;
        private String value;

        PoolNameEnum(Integer code, String value) {
            this.code = code;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        BusinessThreadPool pool = BusinessThreadPool.getInstance(PoolNameEnum.POOL_TEST.getValue());
        pool.execute(() -> {
            System.out.println(1);
        });
    }
}
