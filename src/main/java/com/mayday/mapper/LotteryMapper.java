package com.mayday.mapper;

import com.mayday.entity.LotteryEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LotteryMapper {


    @Insert("INSERT INTO LOTTERYIGKBET(PID,ACODE,ATIME) VALUES(#{pid},#{acode},#{atime})")
     void insertLottery(LotteryEntity LotteryEntity);

    //查询彩票结果是否存在

    @Select("SELECT * FROM LOTTERYIGKBET WHERE PID=#{pid} AND ACODE=#{acode}")
    @Results({
            @Result(property = "pid", column = "pid", javaType =String.class),
            @Result(property = "acode", column = "acode" ,javaType=String.class),
            @Result(property = "atime", column = "atime" ,javaType=String.class)

    })
    List<LotteryEntity> queryLottery(LotteryEntity LotteryEntity);

}
