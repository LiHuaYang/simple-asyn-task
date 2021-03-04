package com.ariclee.sat.strategy;

import com.ariclee.sat.DelayTaskContext;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 目前只支持默认策略
 * @author lihy
 * @version 1.0  2021/03/04
 */
public class WeChatReTryStrategy extends BaseSchedulerStrategy {

    // 初始化通知时间间隔，以秒为单位
    private static List<String> intervals;
    static  {
//        String defaultNoticeStrategy = "15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h";
        String defaultNoticeStrategy = "3s/3s/3s";
        intervals = new ArrayList<>(Arrays.asList(defaultNoticeStrategy.split("/")));

        // 以秒为单位重新计算时间间隔
        intervals = intervals.stream().map(item -> {
            String temp = item.substring(item.length() - 1);
            Integer intv = Integer.valueOf(item.substring(0, item.length() - 1));
            if (temp.endsWith("s")) {
                return String.valueOf(intv);
            }
            else if (temp.endsWith("m")) {
                return String.valueOf(intv * 60);
            }
            else if (temp.endsWith("h")) {
                return String.valueOf(intv * 60 * 60);
            }
            else {
                return String.valueOf(intv);
            }
        }).collect(Collectors.toList());
    }

    public WeChatReTryStrategy(){
        super(intervals.size(), new Date());
    }

    @Override
    public void doCaclAndResetParam(DelayTaskContext context) {
        // 因为当通知失败就会立刻重新加入队列
        // 所以这里视当前时间就为上一次通知完成时间
        // 下标需要 -1
        int index = (intervals.size() - super.getCount()) - 1;
        Date nextExecuteTime = DateUtils.addSeconds(new Date(), Integer.parseInt(intervals.get(index)));
        super.setExecuteTime(nextExecuteTime);
    }
}
