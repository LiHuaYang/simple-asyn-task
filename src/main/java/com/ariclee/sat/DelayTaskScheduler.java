package com.ariclee.sat;

import com.ariclee.sat.strategy.BaseSchedulerStrategy;
import com.ariclee.sat.strategy.WeChatReTryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author lihy
 * @version 1.0  2021/03/04
 */
public final class DelayTaskScheduler {

    private static Logger log = LoggerFactory.getLogger(DelayTaskScheduler.class);

    private DelayTaskScheduler(){}

    private static final Integer worker_num = 3;
    private static final Integer queue_size = 10000;

    private static ExecutorService defaultWorkerThreadPool = new ThreadPoolExecutor(worker_num, worker_num, 0L,
                    TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queue_size), new ThreadFactoryBuilder().setNameFormat("Scheduler 默认 worker 线程").build());

    private static final BlockingDeque<DelayTaskContext> defaultQueue = new LinkedBlockingDeque<>(queue_size);
    private static final BlockingDeque<DelayTaskContext> failRetryQueue = new LinkedBlockingDeque<>(queue_size);

    // 使用默认策略（微信）加入队列
    public static void putWithDefaultStrategy(DelayRunnable runnable, DelayTaskBaseMessage message,
                                              ExecutorService workerThreadPool) {
        WeChatReTryStrategy strategy = new WeChatReTryStrategy();
        put(runnable, message, strategy, workerThreadPool);
    }

    public static void put(DelayRunnable runnable, DelayTaskBaseMessage message,
                           BaseSchedulerStrategy schedulerStrategy, ExecutorService workerThreadPool) {
        DelayTaskContext context = new DelayTaskContext(runnable, message, schedulerStrategy, workerThreadPool);
        try {
            defaultQueue.put(context);
        }
        catch (Exception e) {
            log.info("投递异常：", e);
        }
    }

    public static void rePut(DelayTaskContext context){
        try {
            context.getStrategy().caclAndResetParam(context);
            failRetryQueue.put(context);
        }
        catch (Exception e) {
            log.info("投递异常：", e);
        }
    }

    public static void init() {
        Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("队列消费者线程").build()).execute(() -> {
            while (true) {
                takeAndDispatch(defaultQueue, defaultWorkerThreadPool);
            }
        });
        Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("重新执行队列消费者线程").build()).execute(() -> {
            while (true) {
                takeAndDispatch(failRetryQueue, defaultWorkerThreadPool);
            }
        });
    }


    private static void takeAndDispatch(BlockingDeque<DelayTaskContext> queue, ExecutorService workerThreadPool) {
        try {
            DelayTaskContext context = queue.take();
            if (context.getStrategy().getExecuteTime().compareTo(new Date()) > 0) {
                int sleepTime = 1000 - queue.size();
                if (sleepTime <= 0) {
                    sleepTime = 1;
                }
                // log.info("队列长度：{} 睡眠时间：{}", queue.size(), sleepTime);
                Thread.sleep(sleepTime);
                queue.put(context);
                return ;
            }
            //
            if (context.getStrategy().getCount() > 0) {
                log.info("取出固定次数队列中消息：{}，投递到线程池中执行", context.getMessage());

                if (context.getWorkerThreadPool() != null) {
                    context.getWorkerThreadPool().execute(() -> context.getRunnable().run(context));
                }
                else {
                    workerThreadPool.execute(() -> context.getRunnable().run(context));
                }
            }
        }
        catch (Exception e) {
            log.error("延时队列，消费轮训线程异常", e);
            try { Thread.sleep(30 * 1000);} catch (Exception e1) {e1.printStackTrace();}
        }
    }
}
