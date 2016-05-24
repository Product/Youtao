package com.youyou.uumall.model;

/**
 * Created by Administrator on 2016/5/23.
 */
public class GoodsList {
    public Boolean searchFromPage;
    public String id;
    public String orderId;
    public String goodsId;
    public String cnt;
    public String coupon;
    public String price;
    public String img;
    public String title;


    @Override
    public String toString() {
        return "GoodsList{" +
                "searchFromPage=" + searchFromPage +
                ", id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", cnt='" + cnt + '\'' +
                ", coupon='" + coupon + '\'' +
                ", price='" + price + '\'' +
                ", img='" + img + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
