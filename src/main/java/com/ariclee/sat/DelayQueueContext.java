package com.ariclee.sat;

import java.util.Date;

/**
 * @author lihy
 * @version 1.0  2020/3/30
 */
public class DelayQueueContext {

    private DelayQueueRunnable runnable;
    private Date executeTime;
    private DelayMessage message;

    public DelayQueueContext(DelayQueueRunnable runnable, Date executeTime, DelayMessage message) {
        this.runnable = runnable;
        this.executeTime = executeTime;
        this.message = message;
    }

    public DelayQueueRunnable getRunnable() {
        return runnable;
    }

    public void setRunnable(DelayQueueRunnable runnable) {
        this.runnable = runnable;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public DelayMessage getMessage() {
        return message;
    }

    public void setMessage(DelayMessage message) {
        this.message = message;
    }
}
