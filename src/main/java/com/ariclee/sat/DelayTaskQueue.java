package com.ariclee.sat;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author lihy
 * @version 1.0  2020/3/30
 */
public class DelayTaskQueue {

    private static Logger log = LoggerFactory.getLogger(DelayTaskQueue.class);

    private static final BlockingDeque<DelayQueueContext> queue = new LinkedBlockingDeque<>(2000);

    // 加入队列方法
    public static void put(DelayQueueRunnable runnable, DelayMessage message, int second) {
        Date time1 = DateUtils.addSeconds(new Date(), second);
        DelayQueueContext context = new DelayQueueContext(runnable, time1, message);
        try {
            queue.put(context);
        }
        catch (Exception e) {
            log.info("投递异常：", e);
        }
    }

    public void init() {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        Executors.newSingleThreadExecutor().execute(() -> {
            while (true) {
                try {
                    DelayQueueContext context = queue.take();
                    if (context.getExecuteTime().compareTo(new Date()) > 0) {
                        int sleepTime = 1000 - queue.size();
                        if (sleepTime <= 0) {
                            sleepTime = 1;
                        }
                        log.info("队列长度：{} 睡眠时间：{}", queue.size(), sleepTime);
                        Thread.sleep(sleepTime);
                        queue.put(context);
                        continue;
                    }
                    log.info("取出元素：{}，投递到线程池中执行", context.toString());
                    threadPool.execute(() -> {
                        context.getRunnable().run(context.getMessage());
                    });
                }
                catch (Exception e) {
                    log.error("延时队列，消费轮训线程异常", e);
                    try {
                        Thread.sleep(30 * 1000);} catch (Exception e1) {e1.printStackTrace();}
                }
            }
        });
    }
}
