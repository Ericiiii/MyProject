package com.mayday.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * quartz示例定时器类
 *
 * @author Administrator
 *
 */
public class QuartzJobExample implements Job {
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println("开始执行任务"+new Date());
    }


}