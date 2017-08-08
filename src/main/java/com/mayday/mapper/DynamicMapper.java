package com.mayday.mapper;

import com.mayday.entity.LotteryEntity;
import com.mayday.entity.TimingTask;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DynamicMapper {

    @Select("SELECT * FROM TASKLIST")
    @Results({
            @Result(property = "taskId", column = "taskId", javaType =Integer.class),
            @Result(property = "expression", column = "expression" ,javaType=Long.class),


    })
    List<TimingTask> getTaskList();
}
