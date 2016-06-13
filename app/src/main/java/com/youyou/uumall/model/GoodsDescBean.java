package com.youyou.uumall.model;

/**
 * Created by Administrator on 2016/5/10.
 * {
 "msg": "请求成功",
 "code": "0",
 "size": 1,
 "data": {
 "id": "acd2abb8b8b04eef9f19e83caae8b442",
 "categoryName": "口红",
 "titile": "粉红色兰蔻",
 "color": "",
 "image": "/uumall/userfiles/1/images/mall/goods/2016/04/IMG_1414.JPG",
 "brandName": "LV",
 "keywords": "44发  发",
 "stock": 99837,
 "stockDate": "2016-07-21 00:00:00",
 "limits": 5,
 "price": 500,
 "coupon": 400,
 "customizedPriceName": "乐天价",
 "customizedPrice": 600,
 "description": "/uumall/userfiles/1/images/mall/brand/2016/04/56a4ca7cc3757f88d8009444ac5e60e4.jpeg|/uumall/userfiles/1/images/mall/brand/2016/04/b8d0989e4bd1815fb7fbcf04a87f1808.jpg",
 "content": "订单"
 }
 */
public class GoodsDescBean {
    public String id;
    public String categoryName;
    public String titile;
    public String color;
    public String image;
    public String brandName;
    public String keywords;
    public String stock;
    public String stockDate;
    public String limits;
    public String price;
    public String coupon;
    public String customizedPriceName;
    public String customizedPrice;
    public String description;
    public String content;

    @Override
    public String toString() {
        return "GoodsDescBean{" +
                "id='" + id + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", titile='" + titile + '\'' +
                ", color='" + color + '\'' +
                ", image='" + image + '\'' +
                ", brandName='" + brandName + '\'' +
                ", keywords='" + keywords + '\'' +
                ", stock='" + stock + '\'' +
                ", stockDate='" + stockDate + '\'' +
                ", limits='" + limits + '\'' +
                ", price='" + price + '\'' +
                ", coupon='" + coupon + '\'' +
                ", customizedPriceName='" + customizedPriceName + '\'' +
                ", customizedPrice='" + customizedPrice + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
