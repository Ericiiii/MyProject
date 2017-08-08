package com.mayday.dao;

import com.mayday.entity.UserEntity;
import com.mayday.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    @Autowired
    private UserMapper userMapper;

    public List<UserEntity> getUser(){
     return   userMapper.getUser();

    }

    //插入一条用户信息
    public void insertUser(UserEntity userEntity){
        userMapper.insertUser(userEntity);
    }
}
