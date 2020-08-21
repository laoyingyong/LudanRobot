package com.ccbft.lyyrobot.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class User extends BmobUser implements Serializable {
    private String nickname;//昵称
    private Integer age;
    private String securityQuestion;
    private String secretAnswer;

    private  String viewedPassword;

    public User() {

    }

    public User(String nickname, Integer age) {
        this.nickname = nickname;
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public String getViewedPassword() {
        return viewedPassword;
    }

    public void setViewedPassword(String viewedPassword) {
        this.viewedPassword = viewedPassword;
    }
}
