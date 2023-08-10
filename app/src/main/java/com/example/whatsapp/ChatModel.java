package com.example.whatsapp;

public class ChatModel {
    String image;
    String name;
    String msg;
    String noofmsg;
    String time;

    public ChatModel(String image, String name, String msg, String noofmsg, String time) {
        this.image = image;
        this.name = name;
        this.msg = msg;
        this.noofmsg = noofmsg;
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNoofmsg() {
        return noofmsg;
    }

    public void setNoofmsg(String noofmsg) {
        this.noofmsg = noofmsg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
