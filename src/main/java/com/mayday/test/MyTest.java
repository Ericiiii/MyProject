package com.mayday.test;

import com.mayday.serivice.UserService;
import com.mayday.utils.ApplicationContextUtil;

public class MyTest {

    public static  void  main(String [] args){

        //通过这种方式就可以获取到userService ，相当于@Autowired
   //    UserService userService=(UserService) ApplicationContextUtil.getBean("userService");

       String str="20170810035";
       String sss=str.substring(0,4);
        if(sss.equals("2017")){  //如果期号是以2017xxx开头，那么修改成17xxx开头
            String pid=str.substring(2);
           System.out.println("修改期号"+str+"为"+"【"+pid+"】");
        }


    }
}
