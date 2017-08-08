package com.mayday.dao;

import com.mayday.entity.LotteryEntity;
import com.mayday.mapper.LotteryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LotteryDao {
    @Autowired
    private LotteryMapper lotteryMapper;

    public void insertLottery(LotteryEntity LotteryEntity){
        lotteryMapper.insertLottery(LotteryEntity);

    }

    public List<LotteryEntity> queryLottery(LotteryEntity LotteryEntity){
     return   lotteryMapper.queryLottery(LotteryEntity);
    }

    public List<LotteryEntity> getLotteryLastTime(LotteryEntity lotteryEntity){
        return   lotteryMapper.getLotteryLastTime(lotteryEntity);
    }



}
