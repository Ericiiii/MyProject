package com.mayday.serivice;

import com.mayday.dao.UserDao;
import com.mayday.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<UserEntity> getUser(){

        return  userDao.getUser();
    }

    public void insertUser(UserEntity userEntity){
        userDao.insertUser(
                userEntity
        );
    }
}
