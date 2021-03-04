package com.ariclee.sat;


import com.ariclee.sat.strategy.BaseSchedulerStrategy;

import java.util.concurrent.ExecutorService;

/**
 * @author lihy
 * @version 1.0  2021/03/04
 */
public class DelayTaskContext {

    private DelayRunnable runnable;

    private DelayTaskBaseMessage message;

    /**
     * 非必填，当不填时，使用默认的线程池工作
     */
    private ExecutorService workerThreadPool;

    /**
     * 非必填，计算 executeTime、count 的策略
     * 当不填时，默认使用 WeChatReTryStrategy
     */
    private BaseSchedulerStrategy strategy;


    public DelayTaskContext(DelayRunnable runnable, DelayTaskBaseMessage message,
                            BaseSchedulerStrategy strategy, ExecutorService workerThreadPool) {
        this.runnable = runnable;
        this.message = message;
        this.strategy = strategy;
        this.workerThreadPool = workerThreadPool;
    }

    public DelayRunnable getRunnable() {
        return runnable;
    }

    public void setRunnable(DelayRunnable runnable) {
        this.runnable = runnable;
    }

    public DelayTaskBaseMessage getMessage() {
        return message;
    }

    public void setMessage(DelayTaskBaseMessage message) {
        this.message = message;
    }

    public ExecutorService getWorkerThreadPool() {
        return workerThreadPool;
    }

    public void setWorkerThreadPool(ExecutorService workerThreadPool) {
        this.workerThreadPool = workerThreadPool;
    }

    public BaseSchedulerStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(BaseSchedulerStrategy strategy) {
        this.strategy = strategy;
    }
}
