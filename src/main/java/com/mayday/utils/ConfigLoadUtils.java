package com.mayday.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Eric on 2017/5/12.
 * 加载配置文件工具类
 */
public class ConfigLoadUtils {

    //需要加载的配置文件名称
    private static final  String ConfigName="config.properties";

    //通过配置文件中的key来获取value
    public static String getConfigValueByKey(String key) {
        InputStream is = null;
        try {
            Properties properties = new Properties();
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(ConfigName);
            properties.load(is);
            return properties.getProperty(key).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }

    public static void main(String[] args) {

        //测试，根据key获取配置文件中的参数信息
        String value=getConfigValueByKey("cqssc.one.api.url");
        System.out.println("value="+value);


    }
}
