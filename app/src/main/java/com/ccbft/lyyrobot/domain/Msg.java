package com.ccbft.lyyrobot.domain;

import org.litepal.crud.DataSupport;

public class Msg {
    public static final int TYPE_RECEIVED=0;//表示消息是接收到的
    public static final int TYPE_SENT=1;//表示消息是发送的

    private String content;
    private int type;
    private int imageId;
    private String time;
    private String name;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Msg() {
    }

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public Msg(String content, int type, int imageId, String time, String name, int id) {
        this.content = content;
        this.type = type;
        this.imageId = imageId;
        this.time = time;
        this.name = name;
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
