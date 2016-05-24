package com.youyou.uumall.bean;

/**
 * Created by Administrator on 2016/5/9.
 */
public class EventBean {
    private String type;
    public EventBean(String type) {
        this.type = type;
    }

    public String getEvent() {
        return type;
    }
}
