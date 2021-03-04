package com.ariclee.sat.strategy;

import com.ariclee.sat.DelayTaskContext;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * @author lihy
 * @version 1.0  2021/03/04
 */
public class FixPeriodStrategy extends BaseSchedulerStrategy {

    private int periodSecond;

    public FixPeriodStrategy(int initialDelay, int periodSecond, int maxExecuteCount) {
        super(maxExecuteCount, DateUtils.addSeconds(new Date(), initialDelay));
        this.periodSecond = periodSecond;
    }

    @Override
    public void doCaclAndResetParam(DelayTaskContext context) {
        Date time1 = DateUtils.addSeconds(super.getExecuteTime(), periodSecond);
        super.setExecuteTime(time1);
    }
}
