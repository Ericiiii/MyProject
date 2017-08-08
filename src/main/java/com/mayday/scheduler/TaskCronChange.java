package com.mayday.scheduler;

import com.mayday.entity.LotteryEntity;
import com.mayday.serivice.LotteryService;
import com.mayday.utils.ConfigLoadUtils;
import com.mayday.utils.DateUtils;
import com.mayday.utils.XMLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

/*@Component
@EnableScheduling*/
public class TaskCronChange  implements SchedulingConfigurer {

    @Autowired
    private LotteryService lotteryService;

  //  public static String cron;

    //通过计算，获取下一次开奖时间
    //默认为十秒
    long  cron =10000;

    public TaskCronChange() {


        new Thread(new Runnable() {

            // 开启新线程模拟外部更改了任务执行周期.
            @Override
            public void run() {
                try {
                    // 让线程睡眠 1秒.
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //修改为：每10分钟执行一次.
              //  cron = "0 0/10 * * * *";
                cron=600000;
                System.err.println("cron change to:"+cron);
            }
        }).start();;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                //任务逻辑代码部分.
                System.out.println("TaskCronChange task is running ... "+ new Date());

                System.out.println("开始执行定时器，当前时间为:"+new Date());

                String name = ConfigLoadUtils.getConfigValueByKey("cqssc.one.api.name");// 彩种 ,04为重庆时时彩

                String date= DateUtils.getNow("yyyy-MM-dd");   //
                System.out.println("now"+date);

                String url = ConfigLoadUtils.getConfigValueByKey("cqssc.one.api.url");

                execute(name,date,url,01);


            }
        };
        Trigger trigger = new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                //任务触发，可修改任务的执行周期.
             //   CronTrigger trigger = new CronTrigger(cron);

                PeriodicTrigger trigger=new PeriodicTrigger(cron);


                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        };
        scheduledTaskRegistrar.addTriggerTask(task, trigger);
    }

    //执行
    public  void execute(String name,String data,String url,int lotteryId){


        String urlAll =url+name+"/"+data+".xml";
        String charset = "UTF-8";
        String jsonResult = get(urlAll, charset);// 得到一个xml字符串

        List<LotteryEntity> list= XMLUtils.getLotteryList(jsonResult);

        //首先查询开奖结果是否在数据库中存在
        List<LotteryEntity> queryList= lotteryService.queryLottery(new LotteryEntity(list.get(0).getPid(),list.get(0).getAcode(),list.get(0).getAtime(),lotteryId));
        if(queryList.size()<1){
            lotteryService.insertLottery(new LotteryEntity(list.get(0).getPid(),list.get(0).getAcode(),list.get(0).getAtime(),lotteryId));
        }

    }

    /**
     *
     * @param urlAll:请求接口
     * @param charset:字符编码
     * @return 返回json结果
     */
    public static String get(String urlAll, String charset) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";// 模拟浏览器
        try {
            URL url = new URL(urlAll);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);
            connection.setRequestProperty("User-agent", userAgent);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, charset));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}
