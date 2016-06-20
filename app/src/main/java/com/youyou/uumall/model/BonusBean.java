package com.youyou.uumall.model;

/**
 * Created by Administrator on 2016/6/7.
 * {
 "id" : "6fd804980cc346d4a4f8bafe1be21a17",//红包ID
 "userId" : "0189d01a63024e1f86fe478d2ef52c73",//所属用户ID
 "value" : 10.0,//红包面额
 "expiryDate" : "2016-06-17 16:19:56",//失效时间
 "createDate" : "2016-05-18 16:19:56",//创建时间
 "updateDate" : "2016-05-18 16:19:56"//更新时间
 }
 */
public class BonusBean {
    public String id;
    public String userId;
    public double value;
    public String expiryDate;
    public String createDate;
    public String updateDate;

    @Override
    public String toString() {
        return "BonusBean{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", value='" + value + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
