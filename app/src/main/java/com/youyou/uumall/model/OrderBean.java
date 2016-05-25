package com.youyou.uumall.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/23.
 * {
 * "searchFromPage" : false,
 * "createDate" : "2016-05-12 08:39:42",
 * "updateDate" : "2016-05-12 08:39:42",
 * "delFlag" : "0",
 * "id" : "82f0b8f1f9ed4511a897c1769d83c426",//唯一ID
 * "buyerId" : "d4f1698ea70741029c9bfd72ebf2952b",//购买者商城用户ID
 * "totalCnt" : 2,//商品总件数
 * "totalCoupon" : 80.0,//总计优惠价
 * "totalPrice" : 180.0,//总计价格
 * "linkTel" : "18677008888",//联系电话
 * "name" : "顾客",//顾客姓名
 * "pickupTime" : "2016-12-12 12:00:00~13:00:00",
 * "address" : "a77c6d1f0f6a4945ba8f041204edd258",
 * "status" : "orderSubmit",//订单状态
 * "goodsList" : [ {//商品信息
 * "searchFromPage" : false,
 * "id" : "58a37ab8bb03478cafee7afb1b4f7b76",
 * "orderId" : "82f0b8f1f9ed4511a897c1769d83c426",//所属订单ID
 * "goodsId" : "5bd27ca19c3d4949982f3aae19dbf878",//商品ID
 * "cnt" : 2,//件数
 * "coupon" : 80.0,//优惠价格
 * "price" : 90.0//实际价格
 * }
 */
public class OrderBean {
    public Boolean searchFromPage;
    public String createDate;
    public String updateDate;
    public String delFlag;
    public String id;
    public String buyerId;
    public String totalCnt;
    public String totalCoupon;
    public String totalPrice;
    public String linkTel;
    public String name;
    public String pickupTime;
    public Address address;
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
                ", buyerId='" + buyerId + '\'' +
                ", totalCnt='" + totalCnt + '\'' +
                ", totalCoupon='" + totalCoupon + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", linkTel='" + linkTel + '\'' +
                ", name='" + name + '\'' +
                ", pickupTime='" + pickupTime + '\'' +
                ", address=" + address +
                ", status='" + status + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}
