package com.ariclee.sat;

/**
 * @author lihy
 * @version 1.0  2020/3/30
 */
public interface DelayQueueRunnable<K> {

    void run(K message);
}
