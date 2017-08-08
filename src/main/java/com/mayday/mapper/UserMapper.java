package com.mayday.mapper;

import com.mayday.entity.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface UserMapper {
        //查询账户信息
        @Select("SELECT * FROM user")
        @Results({
                @Result(property = "account", column = "userName", javaType =String.class),
                @Result(property = "password", column = "password")
        })
        List<UserEntity> getUser();

        //插入一条数据
        @Insert("INSERT INTO USER(USERNAME,PASSWORD) VALUES(#{account},#{password})")
       void insertUser(UserEntity userEntity);


}
