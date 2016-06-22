package com.youyou.uumall.business;

import android.content.Context;

import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.HashMap;
import java.util.Map;

@EBean
public class ShopcartBiz extends BaseBusiness {

    public static final int UPDATE_CART = 1;
    public static final int GET_CART_LIST = 2;
    public static final int CART_ITEM_DEL = 3;

    @RootContext
    Context mContext;

    /**
     * 跟新购物车
     */
    @Background
    public void updatecart(Map[] dataArray,int isUpdate) {
        String countryCode = new MyUtils().getCountryCode(mContext);
        Map map = new HashMap();
        map.put("countryCode", countryCode);
        map.put("dataArray", dataArray);
        map.put("isUpdate", isUpdate);
        objectCallbackInterface.objectCallBack(UPDATE_CART, baseApi.updatecart(map));
    }

    /**
     * 购物车查询
     */
    @Background
    public void getcartList() {
        String countryCode = new MyUtils().getCountryCode(mContext);
        Map map = new HashMap();
        map.put("countryCode", countryCode);
        objectCallbackInterface.objectCallBack(GET_CART_LIST, baseApi.getcartList(map));
    }

    /**
     * 删除购物车(废弃)
     */
    @Background
    public void cartItemDel(Map map) {
        objectCallbackInterface.objectCallBack(CART_ITEM_DEL, baseApi.cartItemDel(map));
    }
}
