package com.jayce.boot.route.common.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.util.concurrent.*;

@ApiModel(value = "DesignThreadPool", description = "自定义线程池")
public class DesignThreadPool {
    @ApiModelProperty(value = "池容量")
    private int corePoolSize = Runtime.getRuntime().availableProcessors() * 3;

    @ApiModelProperty(value = "最大池容量")
    private int maximumPoolSize = corePoolSize * 3;

    @ApiModelProperty(value = "保活时间")
    private long keepAliveTime = 60;

    @ApiModelProperty(value = "保活单位")
    private TimeUnit unit = TimeUnit.SECONDS;

    @ApiModelProperty(value = "阻塞队列")
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(100000);

    @ApiModelProperty(value = "线程池执行者")
    private ThreadPoolExecutor executor;

    private ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("tread-pool-%d").build();

    @ApiModelProperty(value = "自定义线程池")
    public static DesignThreadPool designThreadPool;

    private DesignThreadPool() {
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public DesignThreadPool(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    @ApiOperation(value = "获取懒汉单例")
    public static DesignThreadPool getInstance() {
        synchronized (DesignThreadPool.class) {
            if (null == designThreadPool) {
                designThreadPool = new DesignThreadPool();
                return designThreadPool;
            } else {
                return designThreadPool;
            }
        }
    }

    @ApiOperation(value = "runable任务")
    public void execute(Runnable runnable) {
        if (!isShutdown()) {
            executor.execute(runnable);
        }
    }

    @ApiOperation(value = "callable任务")
    public <V> Future<V> execute(Callable<V> callable) {
        return executor.submit(callable);
    }

    @ApiOperation(value = "关闭线程池")
    public void shutdown() {
        executor.shutdown();
    }

    @ApiOperation(value = "线程池是否关闭")
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    @ApiOperation(value = "队列剩余")
    public boolean isQueueFull() {
        return workQueue.remainingCapacity() <= 0;
    }

    @ApiOperation(value = "线程运行中")
    public boolean isWork() {
        return executor.getActiveCount() != 0;
    }
}
