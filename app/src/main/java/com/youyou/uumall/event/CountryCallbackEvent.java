package com.youyou.uumall.event;

/**
 * Created by Administrator on 2016/5/9.
 */
public class CountryCallbackEvent {
    private String type;

    public CountryCallbackEvent(String type) {
        this.type = type;
    }

    public String getEvent() {
        return type;
    }

}
