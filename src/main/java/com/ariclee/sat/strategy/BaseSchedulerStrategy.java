package com.ariclee.sat.strategy;


import com.ariclee.sat.DelayTaskContext;

import java.util.Date;

/**
 * @author lihy
 * @version 1.0  2021/03/04
 */
public abstract class BaseSchedulerStrategy {

    // 剩余可执行次数
    private Integer count;
    // 上次执行时间
    private Date executeTime;

    public BaseSchedulerStrategy(Integer count, Date executeTime) {
        this.count = count;
        this.executeTime = executeTime;
    }

    public void caclAndResetParam(DelayTaskContext context) {
        // 剩余可通知次数 -1
        this.setCount(this.getCount() - 1);
        this.doCaclAndResetParam(context);
    }

    abstract void doCaclAndResetParam(DelayTaskContext context);

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }
}
