package com.mayday.mapper;

import com.mayday.entity.LotteryEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LotteryMapper {


    @Insert("INSERT INTO LOTTERYIGKBET(PID,ACODE,ATIME,LOTTERYID) VALUES(#{pid},#{acode},#{atime},#{lotteryId})")
     void insertLottery(LotteryEntity LotteryEntity);

    //查询彩票结果是否存在

    @Select("SELECT * FROM LOTTERYIGKBET WHERE LOTTERYID=#{lotteryId} AND PID=#{pid} AND ACODE=#{acode}")
    @Results({
            @Result(property = "pid", column = "pid", javaType =String.class),
            @Result(property = "acode", column = "acode" ,javaType=String.class),
            @Result(property = "atime", column = "atime" ,javaType=String.class),
            @Result(property = "lotteryId", column = "lotteryId" ,javaType=Integer.class)

    })
    List<LotteryEntity> queryLottery(LotteryEntity LotteryEntity);

    //获取某个彩种的最后一次开奖时间
    @Select("select * from lotteryigkbet where lotteryId=#{lotteryId} GROUP BY id DESC LIMIT 1")
    @Results({
            @Result(property = "pid", column = "pid", javaType =String.class),
            @Result(property = "acode", column = "acode" ,javaType=String.class),
            @Result(property = "atime", column = "atime" ,javaType=String.class),
            @Result(property = "lotteryId", column = "lotteryId" ,javaType=Integer.class)

    })
    List<LotteryEntity> getLotteryLastTime(LotteryEntity LotteryEntity);



}
