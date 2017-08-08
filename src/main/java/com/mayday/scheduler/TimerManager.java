package com.mayday.scheduler;

import com.mayday.entity.LotteryEntity;
import com.mayday.execute.GetLottery;
import com.mayday.serivice.LotteryService;
import com.mayday.utils.ApplicationContextUtil;
import com.mayday.utils.ConfigLoadUtils;
import com.mayday.utils.DateUtils;
import com.mayday.utils.XMLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TimerManager {



     ScheduledExecutorService scheduExec =Executors.newScheduledThreadPool(2);

  //  public long start;
  /*  public TimerManager() {

        this.scheduExec =  Executors.newScheduledThreadPool(2);

        this.start = System.currentTimeMillis();
        long oneDay = 24 * 60 * 60 * 1000;
        long initDelayOne  = getTimeMillis("3:00:00") - System.currentTimeMillis();
        initDelayOne = initDelayOne > 0 ? initDelayOne : oneDay + initDelayOne;
        long initDelayTwo  = getTimeMillis("3:00:00") - System.currentTimeMillis();
        initDelayTwo = initDelayTwo > 0 ? initDelayTwo : oneDay + initDelayTwo;

    }*/



    public void timerOne(){
        long time1=20000;
        scheduExec.scheduleWithFixedDelay(new Runnable() {
            public void run() {

                System.out.println("timerOne invoked ....."+new Date());

                System.out.println("开始执行定时器，当前时间为:"+new Date());

                String name = ConfigLoadUtils.getConfigValueByKey("cqssc.one.api.name");// 彩种 ,04为重庆时时彩

                String date= DateUtils.getNow("yyyy-MM-dd");   //传入的时间 ，例如2017-08-08
                String url = ConfigLoadUtils.getConfigValueByKey("cqssc.one.api.url");

                execute(name,date,url,01);

            }
        },time1,60000, TimeUnit.MILLISECONDS);
    }

  /*  public void timerTwo(){
        long time2=2000;
        scheduExec.scheduleAtFixedRate(new Runnable() {
            public void run() {

                System.out.println("timerTwo invoked ....."+new Date());
            }
        },time2,5000, TimeUnit.MILLISECONDS);

    }*/



    /**
     * @param time "HH:mm:ss"
     * @return
     */
    private static long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        TimerManager test = new TimerManager();
        test.timerOne();
       // test.timerTwo();
    }

    //执行
    public  void execute(String name,String data,String url,int lotteryId){


        String urlAll =url+name+"/"+data+".xml";
        String charset = "UTF-8";
        String jsonResult = get(urlAll, charset);// 得到一个xml字符串

        List<LotteryEntity> list= XMLUtils.getLotteryList(jsonResult);

        LotteryService lotteryService=(LotteryService) ApplicationContextUtil.getBean("lotteryService");

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