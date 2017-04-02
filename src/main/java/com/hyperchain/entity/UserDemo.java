package com.hyperchain.entity;

/**
 * by chenyufeng on 2017/4/1 .
 */
public class UserDemo {

    private String id;
    private String nickname;
    private String password;
    private String phoneNum;

    public UserDemo(String id, String nickname, String password, String phoneNum) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.phoneNum = phoneNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
