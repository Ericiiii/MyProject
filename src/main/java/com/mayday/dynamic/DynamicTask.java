package com.mayday.dynamic;

import com.mayday.entity.TimingTask;
import com.mayday.serivice.TaskListService;
import com.sun.jmx.snmp.tasks.TaskServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

@Component
public class DynamicTask {


    @Autowired
    private TaskConfigurer TaskConfigurer;

    @Autowired
    private TaskListService taskListService;

    @Scheduled(cron="0/10 * * * * ?")
    public void loadTasks(){
        System.out.println("任务正在执行.."+new Date());
        //从数据库查询
        //当任务的执行周期发生变化时，定时器自动 更改策略
        List<TimingTask> tasks =taskListService.getTaskList();

        TaskConfigurer.refreshTasks(tasks);
    }
}
