package com.mayday.dynamic;

import com.mayday.entity.LotteryEntity;
import com.mayday.serivice.LotteryService;
import com.mayday.utils.ApplicationContextUtil;
import com.mayday.utils.ConfigLoadUtils;
import com.mayday.utils.DateUtils;
import com.mayday.utils.XMLUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class DynamicTaskRunable implements Runnable{

   // private final static Logger LOGGER =Logger.getLogger(DynamicTaskRunable.class);

    private Integer taskId;

    public DynamicTaskRunable(Integer taskId) {
        this.taskId = taskId;
    }
    @Override
    public void run() {
       // LOGGER.info("DynamicTaskRunable is running, taskId："+taskId);
        System.out.println("TaskRunable is running, taskId："+taskId+"执行时间为:"+new Date());
        String date= DateUtils.getNow("yyyy-MM-dd");   //传入的时间 ，例如2017-08-08
        String url = ConfigLoadUtils.getConfigValueByKey("cqssc.one.api.url");
        execute("0"+taskId.toString(),date,url);

        //查询最近一次开奖时间 ，计算出下一次开奖时间


    }

    public Integer getTaskId() {
        return taskId;
    }


    //执行
    public  void execute(String name,String data,String url){


        String urlAll =url+name+"/"+data+".xml";
        String charset = "UTF-8";
        String jsonResult = get(urlAll, charset);// 得到一个xml字符串

        List<LotteryEntity> list= XMLUtils.getLotteryList(jsonResult);

        LotteryService lotteryService=(LotteryService) ApplicationContextUtil.getBean("lotteryService");

        //首先查询开奖结果是否在数据库中存在
        List<LotteryEntity> queryList= lotteryService.queryLottery(new LotteryEntity(list.get(0).getPid(),list.get(0).getAcode(),list.get(0).getAtime()));
        if(queryList.size()<1){
            lotteryService.insertLottery(new LotteryEntity(list.get(0).getPid(),list.get(0).getAcode(),list.get(0).getAtime()));
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