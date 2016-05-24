package com.youyou.uumall.business;

import com.youyou.uumall.base.BaseBusiness;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.Map;

@EBean
public class ShopcartBiz extends BaseBusiness {

    public static final int UPDATE_CART = 1;
    public static final int GET_CART_LIST = 2;
    public static final int CART_ITEM_DEL = 3;

    /**
     * 跟新购物车
     */
    @Background
    public void updatecart(Map map) {
        objectCallbackInterface.objectCallBack(UPDATE_CART, baseApi.updatecart(map));
    }

    /**
     *
     * 购物车查询
     */
    @Background
    public void getcartList() {
        objectCallbackInterface.objectCallBack(GET_CART_LIST, baseApi.getcartList());
    }

    /**
     *
     * 删除购物车
     */
    @Background
    public void cartItemDel(Map map) {
        objectCallbackInterface.objectCallBack(CART_ITEM_DEL, baseApi.cartItemDel(map));
    }
}
