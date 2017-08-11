package com.mayday.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mayday.dynamic.DynamicTaskRunable;
import com.mayday.entity.LotteryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON解析工具类
 */
public class JSONUtils {

    public static List<LotteryEntity> getLotteryList(String jsonStr,int lotteryId){

        List<LotteryEntity>  list=new ArrayList<LotteryEntity>();

        JSONObject jsonObject=JSONObject.parseObject(jsonStr);
        JSONObject jsonObject1=JSONObject.parseObject(jsonObject.getString("result"));


        if(lotteryId==2){  //幸运农场，返回非json数组

            JSONObject jsonObject2=JSONObject.parseObject(jsonObject1.getString("data"));
            list.add(new LotteryEntity(jsonObject2.getString("preDrawIssue"),jsonObject2.getString("preDrawCode"),jsonObject2.getString("preDrawTime"),lotteryId));

        }else {  //广西十一选五或者其他返回为json数组


            JSONArray jsonArray=(JSONArray)jsonObject1.getJSONArray("data");
            for(int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject2=(JSONObject)jsonArray.get(0);
                list.add(new LotteryEntity(jsonObject2.getString("preDrawIssue"),jsonObject2.getString("preDrawCode"),jsonObject2.getString("preDrawTime"),lotteryId));

            }


        }

         return list;

    }

    public static void main(String [] args){
        System.out.println("开始解析json文件");
        //获取json字符串
        String urlAll="http://api.1680210.com/klsf/getLotteryInfo.do?lotCode=10009";
       String jsonStr= DynamicTaskRunable.get(urlAll,"UTF-8");


        List<LotteryEntity> list=JSONUtils.getLotteryList(jsonStr,2);
        System.out.println("list"+list);




    }


 }
