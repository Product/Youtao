package com.youyou.uumall.event;

/**
 * Created by Administrator on 2016/5/9.
 */
public class MobileBindingEvent {
    private String openId;

    public MobileBindingEvent(String openId) {
        this.openId = openId;
    }

    public String getEvent() {
        return openId;
    }

}
