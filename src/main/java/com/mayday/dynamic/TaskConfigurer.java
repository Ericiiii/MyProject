package com.mayday.dynamic;

import com.mayday.entity.TimingTask;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 动态定时器配置类
 * 这个类主要使用毫秒
 */

@Component
public class TaskConfigurer implements SchedulingConfigurer {

    private volatile ScheduledTaskRegistrar registrar;

    private Log log= LogFactory.getLog(TaskConfigurer.class);

    private final ConcurrentMap<Integer, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<Integer, ScheduledFuture<?>>();
    private final ConcurrentMap<Integer, IntervalTask> inTask = new ConcurrentHashMap<Integer, IntervalTask>();

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        this.registrar = registrar;
    }

    public void refreshTasks(List<TimingTask> tasks) {
        //取消已经删除的策略任务
        Set<Integer> sids = scheduledFutures.keySet();
        for (Integer sid : sids) {
            if (!exists(tasks, sid)) {
                scheduledFutures.get(sid).cancel(false);
            }
        }
        for (TimingTask tt : tasks) {

            long time = tt.getExpression();

            if (scheduledFutures.containsKey(tt.getTaskId()) && inTask.get(tt.getTaskId()).getInterval()==time) {
                continue;
            }
            if (scheduledFutures.containsKey(tt.getTaskId())) {

                log.info("任务"+tt.getTaskId()+"定时策略已经被更改..");
                scheduledFutures.get(tt.getTaskId()).cancel(false);
                scheduledFutures.remove(tt.getTaskId());
                inTask.remove(tt.getTaskId());
            }


            DynamicTaskRunable t = new DynamicTaskRunable(tt.getTaskId());
            IntervalTask task=new IntervalTask(t,time);

            ScheduledFuture<?> future = registrar.getScheduler().scheduleAtFixedRate(task.getRunnable(), time);
            inTask.put(tt.getTaskId(), task);
            scheduledFutures.put(tt.getTaskId(), future);

            //如果策略执行时间发生了变化，则取消当前策略的任务


        }
    }

    private boolean exists(List<TimingTask> tasks, Integer tid) {
        for (TimingTask task : tasks) {
            if (task.getTaskId().equals(tid)) {
                return true;
            }
        }
        return false;
    }

    @PreDestroy
    public void destroy() {
        this.registrar.destroy();
    }


}