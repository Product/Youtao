package com.youyou.uumall.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/23.
 *
 */
public class OrderBean {
    public Boolean searchFromPage;
    public String createDate;
    public String updateDate;
    public String delFlag;
    public String id;
    public String totalCnt;
    public String totalCoupon;
    public String totalPrice;
    public String linkTel;
    public String deliverType;
    public String name;
    public String pickupTime;
    public String postage;
    public String buyerId;
    public String payAmount;
    public Address delivery;
    public String address;
    public String status;
    public ArrayList<GoodsList> goodsList;

    public class Address {
        public Boolean searchFromPage;
        public String createDate;
        public String updateDate;
        public String delFlag;
        public String id;
        public String countryCode;
        public String name;
        public String description;
        public String img;
        public String title;

        @Override
        public String toString() {
            return "Address{" +
                    "searchFromPage=" + searchFromPage +
                    ", createDate='" + createDate + '\'' +
                    ", updateDate='" + updateDate + '\'' +
                    ", delFlag='" + delFlag + '\'' +
                    ", id='" + id + '\'' +
                    ", countryCode='" + countryCode + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", img='" + img + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "searchFromPage=" + searchFromPage +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", id='" + id + '\'' +
                ", totalCnt='" + totalCnt + '\'' +
                ", totalCoupon='" + totalCoupon + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", linkTel='" + linkTel + '\'' +
                ", deliverType='" + deliverType + '\'' +
                ", name='" + name + '\'' +
                ", pickupTime='" + pickupTime + '\'' +
                ", postage='" + postage + '\'' +
                ", buyerId='" + buyerId + '\'' +
                ", payAmount='" + payAmount + '\'' +
                ", delivery=" + delivery +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}
