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
 *
 * {searchFromPage=false,
 * createDate=2016-06-14 06:47:19,
 * updateDate=2016-06-14 06:47:19,
 * delFlag=0,
 * id=20160614669880,
 * buyerId=bb6f3cae837d4c1287cc45e977550131,
 * totalCnt=1.0,
 * totalCoupon=400.0,
 * totalPrice=500.0,
 * linkTel=13795376899,
 * name=陈冰,
 * pickupTime=2016年5月2日  01:07  多发发,
 * address=23f4c2dbadf545c2b21d0d69cb7e2196,
 * deliverType=1,
 * status=orderCancel,
 * goodsList=[{searchFromPage=false,
 *             id=8f2be0481bd54c56852b3fb9ada4f063,
 *             orderId=20160614669880,
 *             goodsId=acd2abb8b8b04eef9f19e83caae8b442,
 *             cnt=1.0,
 *             coupon=400.0,
 *             price=500.0,
 *             img=/uumall/userfiles/1/images/mall/goods/2016/04/IMG_1414.JPG,
 *             title=粉红色兰蔻}]}
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
    public String deliverType;
    public String name;
    public String pickupTime;
//    public Address address;
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
                ", buyerId='" + buyerId + '\'' +
                ", totalCnt='" + totalCnt + '\'' +
                ", totalCoupon='" + totalCoupon + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", linkTel='" + linkTel + '\'' +
                ", deliverType='" + deliverType + '\'' +
                ", name='" + name + '\'' +
                ", pickupTime='" + pickupTime + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}
