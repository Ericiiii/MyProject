package com.mayday.quartz;

import org.quartz.*;

/**
 * Quartz调度管理器
 */
public class QuartzManager {
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";

    public static void addJob(Scheduler sched, String jobName, @SuppressWarnings("rawtypes") Class cls, String time) {
        try {
            JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, cls);// 任务名，任务组，任务执行类
            // 触发器
            CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
            trigger.setCronExpression(time);// 触发器时间设定


            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 添加一个定时任务
     */
    public static void addJob(Scheduler sched, String jobName, String jobGroupName, String triggerName, String triggerGroupName, @SuppressWarnings("rawtypes") Class jobClass, long time) {
        try {
            JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);// 任务名，任务组，任务执行类
            // 触发器

            SimpleTrigger trigger = new SimpleTrigger(triggerName, triggerGroupName);// 触发器名,触发器组

            trigger.setRepeatInterval(time);// 触发器时间设定
            sched.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)

   /* @SuppressWarnings("rawtypes")
    public static void modifyJobTime(Scheduler sched, String jobName, long time) {
        try {
            SimpleTrigger trigger = (SimpleTrigger) sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
            if (trigger == null) {
                return;
            }
            long oldTime = trigger.getRepeatInterval();
            if (!(oldTime==time)) {
                JobDetail jobDetail = sched.getJobDetail(jobName, JOB_GROUP_NAME);
                Class objJobClass = jobDetail.getJobClass();
                removeJob(sched, jobName);
                addJob(sched, jobName, objJobClass, time);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/
   @SuppressWarnings("rawtypes")
   public static void modifyJobTime(Scheduler sched, String jobName, String  time) {
       try {
           CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
           if (trigger == null) {
               return;
           }
           String oldTime = trigger.getCronExpression();
           if (!oldTime.equalsIgnoreCase(time)) {
               JobDetail jobDetail = sched.getJobDetail(jobName, JOB_GROUP_NAME);
               Class objJobClass = jobDetail.getJobClass();
               removeJob(sched, jobName);
               addJob(sched, jobName, objJobClass, time);
           }
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }




    //修改一个任务的触发时间

    public static void modifyJobTime(Scheduler sched, String triggerName, String triggerGroupName, long time) {
        try {
            SimpleTrigger trigger = (SimpleTrigger) sched.getTrigger(triggerName, triggerGroupName);
            if (trigger == null) {
                return;
            }
            long oldTime = trigger.getRepeatInterval();
            if (!(oldTime==time)) {
                SimpleTrigger ct = (SimpleTrigger) trigger;
                // 修改时间
                ct.setRepeatInterval(time);
                // 重启触发器
                sched.resumeTrigger(triggerName, triggerGroupName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 移除一个任务(使用默认的任务组名，触发器名，触发器组名)

    public static void removeJob(Scheduler sched, String jobName) {
        try {
            sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
            sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
            sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //移除一个任务

    public static  void removeJob(Scheduler sched, String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            sched.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
            sched.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
            sched.deleteJob(jobName, jobGroupName);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


     //启动所有定时任务

    public static void startJobs(Scheduler sched) {
        try {
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //关闭所有定时任务

    public static void shutdownJobs(Scheduler sched) {
        try {
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}