package com.ariclee.sat;

import com.ariclee.sat.strategy.FixPeriodStrategy;
import com.ariclee.sat.strategy.WeChatReTryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lihy
 * @version 1.0  2021/3/4
 */
public class Test {

    private static Logger log = LoggerFactory.getLogger(Test.class);

    static {
        DelayTaskScheduler.init();
    }

    public static void main(String[] args) {
        DelayTaskScheduler.put(new TestTask(), new TestMessage("消息正文1"), new FixPeriodStrategy(0, 1, 3), null);
        DelayTaskScheduler.put(new TestTask(), new TestMessage("消息正文2"), new WeChatReTryStrategy(), null);
        sleep(10);
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
