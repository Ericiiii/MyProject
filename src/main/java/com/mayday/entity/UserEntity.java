package com.mayday.entity;

/**
 * 用户实体类
 */


public class UserEntity {
    private String account;  //账户
    private String password; //密码

    public UserEntity(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public UserEntity() {
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
