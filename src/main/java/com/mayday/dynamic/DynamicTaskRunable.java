package com.mayday.dynamic;

import com.mayday.entity.LotteryEntity;
import com.mayday.entity.TimingTask;
import com.mayday.serivice.LotteryService;
import com.mayday.serivice.TaskListService;
import com.mayday.utils.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 动态定时器配置类
 * 这个类主要使用Cron表达式
 */
public class DynamicTaskRunable implements Runnable{

    Log log= LogFactory.getLog(DynamicTaskRunable.class);
    private Integer taskId;

    LotteryService lotteryService=(LotteryService) ApplicationContextUtil.getBean("lotteryService");
    TaskListService taskListService=(TaskListService) ApplicationContextUtil.getBean("taskListService");


    public DynamicTaskRunable(Integer taskId) {
        this.taskId = taskId;
    }
    @Override
    public void run() {


        log.info("TaskRunable is running, taskId：【"+taskId+"】执行时间为:"+new Date());

        //得到任务编号 : 1 .重庆时时彩 2 .幸运农场 3 .广西十一选五 4 . 江苏快3
       if(taskId==1){  //执行重庆时时彩
           //接口1
           log.info("开始执行【重庆时时彩】当前时间为:"+new Date());
           String date= DateUtils.getNow("yyyy-MM-dd");   //传入的时间 ，例如2017-08-08
           String url = ConfigLoadUtils.getConfigValueByKey("cqssc.one.api.url");
           String name=ConfigLoadUtils.getConfigValueByKey("cqssc.one.api.name");
           String time=ConfigLoadUtils.getConfigValueByKey("cqssc.one.api.time");
           String charset="UTF-8";
           String urlAll=url+name+"/"+date+".xml";

           //接口2
           String urlTwo=ConfigLoadUtils.getConfigValueByKey("cqssc.two.api.url");
           String codeTwo=ConfigLoadUtils.getConfigValueByKey("cqssc.two.api.code");
           String configTimeTwo=ConfigLoadUtils.getConfigValueByKey("cqssc.two.api.time");
           String urlAllTwo=urlTwo+"?"+"lotCode="+ codeTwo;

           List<LotteryEntity> list= null;

           try {  //如果接口请求出现异常，那么请求第二个接口
               String xmlResult = get(urlAll, charset);// 得到一个xml字符串
               list = XMLUtils.getLotteryList(xmlResult,"row","pid","acode","atime",1);
               if(list.size()<1){  //如果没有抓取到数据 ，那么开始抓取第二个接口

                   String jsonResult=get(urlAllTwo,charset);
                   list=JSONUtils.getLotteryList(jsonResult,taskId);

               }
           } catch (Exception e) {
               e.printStackTrace();
               //如果出现异常 ，那么请求第二个接口
               String jsonResult=get(urlAllTwo,charset);
               list=JSONUtils.getLotteryList(jsonResult,taskId);

           }

           execute(taskId,list,time);




       }
       if(taskId==2){
           log.info("开始执行【幸运农场】当前时间为:"+new Date());
           String url=ConfigLoadUtils.getConfigValueByKey("xync.one.api.url");
           String code=ConfigLoadUtils.getConfigValueByKey("xync.one.api.code");
           String ConfigTime=ConfigLoadUtils.getConfigValueByKey("xync.one.api.time");

           String urlAll=url+"?"+"lotCode="+code;
           String chatset="UTF-8";
           String jsonResult=get(urlAll,chatset);


           List <LotteryEntity> list= JSONUtils.getLotteryList(jsonResult,taskId);
           execute(taskId,list,ConfigTime);

       }
       if(taskId==3){
           log.info("开始执行【广西十一选五】当前时间为:"+new Date());

           String url=ConfigLoadUtils.getConfigValueByKey("gxklsf.one.api.url");
           String code=ConfigLoadUtils.getConfigValueByKey("gxklsf.one.api.code");
           String ConfigTime=ConfigLoadUtils.getConfigValueByKey("gxklsf.one.api.time");

           String urlAll=url+"?"+"lotCode="+code;
           String chatset="UTF-8";
           String jsonResult=get(urlAll,chatset);


           List <LotteryEntity> list= JSONUtils.getLotteryList(jsonResult,taskId);
           execute(taskId,list,ConfigTime);

       }
       if(taskId==4){
           log.info("开始执行【江苏快3】当前时间为:"+new Date());
           //接口1
           String url=ConfigLoadUtils.getConfigValueByKey("jsk3.one.api.url");
           String code=ConfigLoadUtils.getConfigValueByKey("jsk3.one.api.code");
           String ConfigTime=ConfigLoadUtils.getConfigValueByKey("jsk3.one.api.time");
           String urlAll=url+"?"+"lotCode="+code;

           //接口2
           String urlTwo=ConfigLoadUtils.getConfigValueByKey("jsk3.two.api.url");
           String codeTwo=ConfigLoadUtils.getConfigValueByKey("jsk3.two.api.code");
           String configTimeTwo=ConfigLoadUtils.getConfigValueByKey("jsk3.two.api.time");
           String date= DateUtils.getNow("yyyy-MM-dd");   //传入的时间 ，例如2017-08-08

           String urlAllTwo=urlTwo+codeTwo+"/"+date+".xml";

           String chatset="UTF-8";

           List <LotteryEntity> list= new ArrayList<LotteryEntity>();

           try {  //如果接口请求出现异常 ，那么执行第二个接口
               String jsonStr=get(urlAll,chatset);
               list = JSONUtils.getLotteryList(jsonStr,taskId);

               if(list.size()<1){  //没有抓取到开奖结果 ，那么继续抓取第二个接口
                   Thread.sleep(10000);  //延时10秒
                   String xmlResult=get(urlAllTwo,chatset);
                   list=XMLUtils.getLotteryList(xmlResult,"row","pid","acode","atime",4);

               }
           } catch (Exception e) {   //如果出现异常 ，那么继续抓取第二个接口
               e.printStackTrace();
               String xmlResult=get(urlAllTwo,chatset);
               list=XMLUtils.getLotteryList(xmlResult,"row","pid","acode","atime",4);
           }


           execute(taskId,list,ConfigTime);

       }



    }

    public Integer getTaskId() {
        return taskId;
    }


    //执行
    public  void execute(int lotteryId,List <LotteryEntity> list,String configTime){


        //首先查询开奖结果是否在数据库中存在
        List<LotteryEntity> queryList= lotteryService.queryLottery(new LotteryEntity(list.get(0).getPid(),list.get(0).getAcode(),list.get(0).getAtime(),lotteryId));
        if(queryList.size()<1){
            lotteryService.insertLottery(new LotteryEntity(list.get(0).getPid(),list.get(0).getAcode(),list.get(0).getAtime(),lotteryId));
            //查询最近一次开奖时间 ，计算出下一次开奖时间
            List<LotteryEntity> last= lotteryService.getLotteryLastTime(new LotteryEntity("","","",taskId));

            //获取数据库中最近一次开奖时间
            if(last.size()>0){
                String atime=last.get(0).getAtime();
                long  time=(long)Integer.parseInt(configTime);
                //计算下一次开间时间
                long intervalTime=DynamicTaskRunable.getIntervalTime(atime,time);

                if(intervalTime>0){ //下一次开奖时间不能为负数

                    //修改数据库中的定时器执行时间
                    boolean flag= taskListService.updateTaskTime(new TimingTask(taskId,intervalTime));
                    if(flag){

                        log.info("数据库定时器时间修改成功！");
                    }
                }
                log.info("距离下一次开奖时间为:【"+intervalTime+"】毫秒");
            }

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
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

    /**
     * 计算下一次开奖时间 ，精确到毫秒数
     * time 为最后一次开奖时间
     * IntervalTime 为距离下一次开奖的间隔时间
     */
    public static long getIntervalTime(String time,long intervalTime){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        long  millisecond=0;

        String now=DateUtils.getNow("yyyy-MM-dd HH:mm:ss");
        try {
            d1 = format.parse(time);
            d2 = format.parse(now);

            millisecond = d1.getTime() - d2.getTime() + intervalTime;

            long diffSeconds = millisecond / 1000 % 60;            //间隔的秒数
            long diffMinutes = millisecond / (60 * 1000) % 60;     //间隔的分钟数
            long diffHours = millisecond / (60 * 60 * 1000) % 24;  //间隔的小时数
            long diffDays = millisecond / (24 * 60 * 60 * 1000);   //间隔的天数


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millisecond;
    }

    public static void main(String [] args){
        getIntervalTime("2017-08-08 16:14:05",600000);

    }

}