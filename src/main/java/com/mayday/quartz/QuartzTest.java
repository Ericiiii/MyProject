package com.mayday.quartz;

import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTest {
    @Test
    public void quartz() {
        try {
           /* SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
            Scheduler sche = gSchedulerFactory.getScheduler();
            String job_name = "动态任务调度";
            System.out.println("【系统启动】开始(每1秒输出一次)...");
            QuartzManager.addJob(sche, job_name, QuartzJobExample.class, 1000);

            SchedulerFactory gSchedulerFactory2 = new StdSchedulerFactory();
            Scheduler sche2 = gSchedulerFactory2.getScheduler();
            String job_name2 = "动态任务调度2";
            System.out.println("【系统启动2】开始(每1秒输出一次)...");
            QuartzManager.addJob(sche2, job_name2, QuartzJobExample2.class, 1000);

            Thread.sleep(3000);
            System.out.println("【修改时间】开始(每2秒输出一次)...");
            QuartzManager.modifyJobTime(sche, job_name, 2000);*/

            SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
            Scheduler sche = gSchedulerFactory.getScheduler();
            String job_name = "动态任务调度";
            System.out.println("【系统启动】开始(每1秒输出一次)...");
            QuartzManager.addJob(sche, job_name, QuartzJobExample.class, "0/1 * * * * ?");

            SchedulerFactory gSchedulerFactory2 = new StdSchedulerFactory();
            Scheduler sche2 = gSchedulerFactory2.getScheduler();
            String job_name2 = "动态任务调度2";
            System.out.println("【系统启动2】开始(每1秒输出一次)...");
            QuartzManager.addJob(sche2, job_name2, QuartzJobExample2.class, "0/1 * * * * ?");

            Thread.sleep(3000);
            System.out.println("【修改时间】开始(每10秒输出一次)...");
            QuartzManager.modifyJobTime(sche, job_name, "0/10 * * * * ?");
            Thread.sleep(4000);
            System.out.println("【移除定时】开始...");
            QuartzManager.removeJob(sche, job_name);
            System.out.println("【移除定时】成功");

            System.out.println("【再次添加定时任务】开始(每10秒输出一次)...");
            QuartzManager.addJob(sche, job_name, QuartzJobExample.class, "0/10 * * * * ?");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}