package com.mayday.dynamic;

import com.mayday.entity.TimingTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class DynamicTaskConfigurer implements SchedulingConfigurer {

    private volatile ScheduledTaskRegistrar registrar;

    private final ConcurrentHashMap<Integer, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<Integer, ScheduledFuture<?>>();
    private final ConcurrentHashMap<Integer, CronTask> cronTasks = new ConcurrentHashMap<Integer, CronTask>();

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        this.registrar = registrar;
    }

    public void refreshTasks(List<TimingTask> tasks){
        //取消已经删除的策略任务
        Set<Integer> sids = scheduledFutures.keySet();
        for (Integer sid : sids) {
            if(!exists(tasks, sid)){
                scheduledFutures.get(sid).cancel(false);
            }
        }
        for (TimingTask tt : tasks) {
            DynamicTaskRunable t = new DynamicTaskRunable(tt.getTaskId());
            String expression = tt.getExpression();
            if(StringUtils.isEmpty(expression)){
                continue;
            }
            if(scheduledFutures.containsKey(tt.getTaskId()) && cronTasks.get(tt.getTaskId()).getExpression().equals(expression)){
                continue;
            }
            //如果策略执行时间发生了变化，则取消当前策略的任务
            if(scheduledFutures.containsKey(tt.getTaskId())){
                scheduledFutures.get(tt.getTaskId()).cancel(false);
                scheduledFutures.remove(tt.getTaskId());
                cronTasks.remove(tt.getTaskId());
            }
            CronTask task = new CronTask(t, expression);
            ScheduledFuture<?> future = registrar.getScheduler().schedule(task.getRunnable(), task.getTrigger());
            cronTasks.put(tt.getTaskId(), task);
            scheduledFutures.put(tt.getTaskId(), future);
        }
    }

    private boolean exists(List<TimingTask> tasks,Integer tid){
        for(TimingTask task:tasks){
            if(task.getTaskId().equals(tid)){
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
