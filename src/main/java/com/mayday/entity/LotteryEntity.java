package com.mayday.entity;

import java.util.Date;

public class LotteryEntity {



    private String pid;  //彩票期数
    private String acode;  //开奖结果
    private String atime;  //开奖时间

    public LotteryEntity() {
    }

    public LotteryEntity(String pid, String acode, String atime) {
        this.pid = pid;
        this.acode = acode;
        this.atime = atime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAcode() {
        return acode;
    }

    public void setAcode(String acode) {
        this.acode = acode;
    }

    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }

    @Override
    public String toString() {
        return "LotteryEntity{" +
                "pid='" + pid + '\'' +
                ", acode='" + acode + '\'' +
                ", atime='" + atime + '\'' +
                '}';
    }
}
