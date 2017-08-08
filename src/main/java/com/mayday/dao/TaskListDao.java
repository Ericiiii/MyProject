package com.mayday.dao;

import com.mayday.entity.TimingTask;
import com.mayday.mapper.DynamicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskListDao {

    @Autowired
    private DynamicMapper dynamicMapper;

    public List<TimingTask> getTaskList(){
        return dynamicMapper.getTaskList();
    }

    //修改定时器执行策略
    public Boolean updateTaskTime(TimingTask timingTask){
        Boolean flag;
        try {
            dynamicMapper.updateTaskTime(timingTask);
            flag=true;
        }catch (Exception e) {
            e.printStackTrace();
            flag=false;
        }
        return flag;
    }
}
