package com.mayday.dynamic;

import com.mayday.entity.LotteryEntity;
import com.mayday.entity.TimingTask;
import com.mayday.serivice.LotteryService;
import com.mayday.serivice.TaskListService;
import com.mayday.utils.ApplicationContextUtil;
import com.mayday.utils.ConfigLoadUtils;
import com.mayday.utils.DateUtils;
import com.mayday.utils.XMLUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 动态定时器配置类
 * 这个类主要使用Cron表达式
 */
public class DynamicTaskRunable implements Runnable{

   // private final static Logger LOGGER =Logger.getLogger(DynamicTaskRunable.class);

    private Integer taskId;

    LotteryService lotteryService=(LotteryService) ApplicationContextUtil.getBean("lotteryService");
    TaskListService taskListService=(TaskListService) ApplicationContextUtil.getBean("taskListService");


    public DynamicTaskRunable(Integer taskId) {
        this.taskId = taskId;
    }
    @Override
    public void run() {
       // LOGGER.info("DynamicTaskRunable is running, taskId："+taskId);
        System.out.println("TaskRunable is running, taskId：【"+taskId+"】执行时间为:"+new Date());

        //得到任务编号 : 1 .重庆时时彩 2 .
       if(taskId==1){  //执行重庆时时彩

           String date= DateUtils.getNow("yyyy-MM-dd");   //传入的时间 ，例如2017-08-08
           String url = ConfigLoadUtils.getConfigValueByKey("cqssc.one.api.url");
           String name=ConfigLoadUtils.getConfigValueByKey("cqssc.one.api.name");
           execute(name,date,url,taskId);

           //查询最近一次开奖时间 ，计算出下一次开奖时间
          List<LotteryEntity> list= lotteryService.getLotteryLastTime(new LotteryEntity("","","",1));

          //获取数据库中最近一次开奖时间
          String atime=list.get(0).getAtime();
           //计算下一次开间时间
          long intervalTime=getIntervalTime(atime,720000);

           //修改数据库中的定时器执行时间
          boolean flag= taskListService.updateTaskTime(new TimingTask(taskId,intervalTime));
          if(flag){
              System.out.println("数据库定时器时间修改成功！");
          }



             System.out.println("距离下一次开奖时间为:【"+intervalTime+"】毫秒");
       }
       if(taskId==2){
           System.out.println("开始执行广东快乐十分 ！");
       }

    }

    public Integer getTaskId() {
        return taskId;
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

            System.out.println("两个日期时间差为:"+millisecond);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return millisecond;
    }

    public static void main(String [] args){
        getIntervalTime("2017-08-08 16:14:05",600000);

    }

}