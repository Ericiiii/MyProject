package com.mayday.controller;

import com.mayday.entity.UserEntity;
import com.mayday.serivice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value ="user")
public class UserController {

    @Autowired
    public UserService userService;

    @RequestMapping(value="/getUser")
    @ResponseBody
    public String getUser(){
        List<UserEntity> list= userService.getUser();
        String account="";
        for (int i=0;i<list.size();i++){
            account=list.get(i).getAccount();
        }
       return "查询到的账户信息为"+account;

    }

    @RequestMapping(value="insertUser")
    @ResponseBody
    public String insertUser(){

        try {
            userService.insertUser(new UserEntity("陈信宏","mayday"));
            return "数据插入成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "数据插入异常";
        }
    }

}
