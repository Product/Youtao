package com.youyou.uumall.business;

import com.youyou.uumall.base.BaseBusiness;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.HashMap;
import java.util.Map;

@EBean
public class CommodityBiz extends BaseBusiness {

    public static final int GET_SLIDER_LIST = 1; //获取轮播数据
    public static final int GET_RECOMMEND_LIST = 2;//获取推荐商品数据
    public static final int GET_BRAND_LIST = 4;//推荐品牌查询

    /**
     * 获取用户详细信息
     */
    @Background
    public void getSliderList() {
        arrayListCallbackInterface.arrayCallBack(GET_SLIDER_LIST, handleResponse(baseApi.getSliderData()));
    }

    /**
     *
     * 推荐商品查询
     */
    @Background
    public void getRecommendList(Map map) {
        arrayListCallbackInterface.arrayCallBack(GET_RECOMMEND_LIST, handleResponse(baseApi.getRecommendData(map)));
    }

    /**
     * 推荐品牌查询
     */
    @Background
    public void getBrandList() {
        Map map = new HashMap();
        map.put("maxResultSize", "");
        arrayListCallbackInterface.arrayCallBack(GET_BRAND_LIST, handleResponse(baseApi.getBrandData(map)));
    }
}
