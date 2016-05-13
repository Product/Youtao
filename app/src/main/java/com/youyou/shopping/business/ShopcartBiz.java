package com.youyou.shopping.business;

import com.youyou.shopping.base.BaseBusiness;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.Map;

@EBean
public class ShopcartBiz extends BaseBusiness {

    public static final int UPDATE_CART = 1;
    public static final int GET_CART_LIST = 2;
    public static final int GET_BRAND_LIST = 4;//推荐品牌查询

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
        arrayListCallbackInterface.arrayCallBack(GET_CART_LIST, handleResponse(baseApi.getcartList()));
    }
//
//    /**
//     * 推荐品牌查询
//     * @param map
//     */
//    @Background
//    public void getBrandList(Map map) {
//        arrayListCallbackInterface.arrayCallBack(GET_BRAND_LIST, handleResponse(baseApi.getBrandData(map)));
//    }
}
