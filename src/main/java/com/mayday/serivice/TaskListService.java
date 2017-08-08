package com.mayday.serivice;

import com.mayday.dao.TaskListDao;
import com.mayday.entity.TimingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TimerTask;

@Service
public class TaskListService {
    @Autowired
    private TaskListDao taskListDao;

    //获取要执行的任务列表
    public List<TimingTask> getTaskList(){
        return taskListDao.getTaskList();
    }

    public Boolean updateTaskTime(TimingTask timingTask){
        boolean flag;

        try {
            taskListDao.updateTaskTime(timingTask);
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
            flag=false;
        }
        return flag;
    }

}
