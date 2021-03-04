package com.ariclee.sat;

/**
 * @author lihy
 * @version 1.0  2021/3/4
 */
public class TestTask implements DelayRunnable {

    @Override
    public void run(DelayTaskContext context) {
        TestMessage message = (TestMessage) context.getMessage();
        System.out.println(message.getContent());
        DelayTaskScheduler.rePut(context);
    }
}
