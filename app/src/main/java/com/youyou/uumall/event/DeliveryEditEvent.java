package com.youyou.uumall.event;

/**
 * Created by Administrator on 2016/6/22.
 */
public class DeliveryEditEvent {
    public String name;
    public String phone;
    public String date;
    public String fltNo;
    public DeliveryEditEvent(String mName,String mPhone,String mDate,String mFltNo) {
        this.name=mName;
        this.phone=mPhone;
        this.date=mDate;
        this.fltNo=mFltNo;
    }
}
