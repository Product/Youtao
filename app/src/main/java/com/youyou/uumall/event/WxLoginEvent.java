package com.youyou.uumall.event;

/**
 * Created by Administrator on 2016/5/9.
 */
public class WxLoginEvent {
    private String openId;

    public WxLoginEvent(String openId) {
        this.openId = openId;
    }

    public String getEvent() {
        return openId;
    }

}
