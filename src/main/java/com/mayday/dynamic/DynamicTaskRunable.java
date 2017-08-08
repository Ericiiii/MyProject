package com.mayday.dynamic;

import java.util.Date;

public class DynamicTaskRunable implements Runnable{

   // private final static Logger LOGGER =Logger.getLogger(DynamicTaskRunable.class);

    private Integer taskId;

    public DynamicTaskRunable(Integer taskId) {
        this.taskId = taskId;
    }
    @Override
    public void run() {
       // LOGGER.info("DynamicTaskRunable is running, taskId："+taskId);
        System.out.println("DynamicTaskRunable is running, taskId："+taskId+"执行时间为:"+new Date());
    }

    public Integer getTaskId() {
        return taskId;
    }
}