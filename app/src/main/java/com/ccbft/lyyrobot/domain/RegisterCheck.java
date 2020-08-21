package com.ccbft.lyyrobot.domain;

import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class RegisterCheck extends BmobObject {
    private String ip;
    private String mac;
    private BmobDate registerTime;


    public RegisterCheck(String ip, String mac, BmobDate registerTime) {
        this.ip = ip;
        this.mac = mac;
        this.registerTime = registerTime;
    }

    public RegisterCheck(String tableName, String ip, String mac, BmobDate registerTime) {
        super(tableName);
        this.ip = ip;
        this.mac = mac;
        this.registerTime = registerTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


    public BmobDate getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(BmobDate registerTime) {
        this.registerTime = registerTime;
    }
}
