package com.youyou.uumall.event;

import com.youyou.uumall.model.GoodsDescBean;

/**
 * Created by Administrator on 2016/7/6.
 */
public class GoodsDescEvent {

    public  GoodsDescBean goodsDescBean;

    public GoodsDescEvent(GoodsDescBean goodsDescBean) {
        this.goodsDescBean = goodsDescBean;
    }
}
