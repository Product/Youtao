package com.youyou.uumall.model;

/**
 * Created by Administrator on 2016/5/13.
 * {
 updateDate = 2016-05-11 18:04:37,
 goodsName = 粉红色兰蔻,
 id = cf0318ad16b4474b93876d980b655136,
 goodsId = acd2abb8b8b04eef9f19e83caae8b442,
 price = 500,
 image = /uumall/userfiles/1/images/mall/goods/2016/04/IMG_1414.JPG,
 count = 2,
 subtotal = 1000,
 createDate = 2016-05-11 18:04:37
 }
 */
public class ShopCartBean {
    public String updateDate;
    public String goodsName;
    public String id;
    public String goodsId;
    public String price;
    public String image;
    public int count;
    public String subtotal;
    public String createDate;
    public boolean isCheck;

    @Override
    public String toString() {
        return "ShopCartBean{" +
                "updateDate='" + updateDate + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", id='" + id + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", count=" + count +
                ", subtotal='" + subtotal + '\'' +
                ", createDate='" + createDate + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}

