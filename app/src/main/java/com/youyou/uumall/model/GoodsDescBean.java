package com.youyou.uumall.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/10.
 *
 * */
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
    public String sales;
    public String limits;
    public String price;
    public String coupon;
    public String customizedPriceName;
    public String customizedPrice;
    public String description;
    public String content;
    public ArrayList<GoodsPrice> goodsPrice;
    class GoodsPrice {
        @Override
        public String toString() {
            return "GoodsPrice{" +
                    "searchFromPage=" + searchFromPage +
                    ", createDate='" + createDate + '\'' +
                    ", updateDate='" + updateDate + '\'' +
                    ", delFlag='" + delFlag + '\'' +
                    ", id='" + id + '\'' +
                    ", goodsId='" + goodsId + '\'' +
                    ", name='" + name + '\'' +
                    ", discountPriceCny=" + discountPriceCny +
                    '}';
        }

        public boolean searchFromPage;
        public String createDate;
        public String updateDate;
        public String delFlag;
        public String id;
        public String goodsId;
        public String name;
        public double discountPriceCny;
    }

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
                ", sales='" + sales + '\'' +
                ", limits='" + limits + '\'' +
                ", price='" + price + '\'' +
                ", coupon='" + coupon + '\'' +
                ", customizedPriceName='" + customizedPriceName + '\'' +
                ", customizedPrice='" + customizedPrice + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", goodsPrice=" + goodsPrice +
                '}';
    }
    /**
     * code=0,
     * msg='请求成功',
     * size=2,
     *
     * data=[{id=cf739208eb4d43db92a2534b3926a0e1,
     *        categoryName=V领,
     *        titile=韩束 (KanS)红石榴鲜活水盈四件套,
     *        color=red,
     *        image=/uumall/userfiles/1/images/mall/goods/2016/06/pro001.jpg|/uumall/userfiles/1/images/mall/goods/2016/06/pro002.jpg|/uumall/userfiles/1/images/mall/goods/2016/06/pro003.jpg,
     *        brandName=品牌测试01名称,
     *        keywords=爆款,
     *        stock=11.0,
     *        stockDate=,
     *        sales=69.0,
     *        limits=0.0,
     *        price=666.0,
     *        coupon=10.0,
     *        goodsPrice=[{searchFromPage=false,
     *                     createDate=2016-06-13 03:11:29,
     *                     updateDate=2016-06-13 03:11:29,
     *                     delFlag=0,
     *                     id=9f30c9b137f84192b6f5d73633537824,
     *                     goodsId=cf739208eb4d43db92a2534b3926a0e1,
     *                     name=新罗价,
     *                     discountPriceCny=777.0},
     *
     *                     {searchFromPage=false,
     *                     createDate=2016-06-13 03:11:29,
     *                     updateDate=2016-06-13 03:11:29,
     *                     delFlag=0,
     *                     id=fae323ecf46b4bb0973f077b66e4f27e,
     *                     goodsId=cf739208eb4d43db92a2534b3926a0e1,
     *                     name=乐天价,
     *                     discountPriceCny=888.0}]},
     *        {id=f21fd412266c4e55b14ec59d28f4d8fa,
     *        categoryName=V领,
     *        titile=韩束 (KanS)红石榴鲜活水盈四件套,
     *        color=,
     *        image=/uumall/userfiles/1/images/mall/goods/2016/06/pro002.jpg|/uumall/userfiles/1/images/mall/goods/2016/06/pro003.jpg,
     *        brandName=品牌测试01名称,
     *        keywords=发，爱迪生，,
     *        stock=1000.0,
     *        stockDate=2016-06-30 00:00:00,
     *        sales=0.0,
     *        limits=5.0,
     *        price=90.0,
     *        coupon=10.0,
     *        goodsPrice=[]}],
     *  totalPrice=null,
     *  prev=null,
     *  next=null
     */
}
