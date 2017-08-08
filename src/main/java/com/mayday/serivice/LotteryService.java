package com.mayday.serivice;

import com.mayday.dao.LotteryDao;
import com.mayday.entity.LotteryEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotteryService{

    @Autowired
    private LotteryDao lotteryDao;


    public void insertLottery(LotteryEntity LotteryEntity) {

     lotteryDao.insertLottery(LotteryEntity);

    }

    public List<LotteryEntity>  queryLottery(LotteryEntity LotteryEntity){
        return lotteryDao.queryLottery(LotteryEntity);
    }

}
