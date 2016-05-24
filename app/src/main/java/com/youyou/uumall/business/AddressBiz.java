package com.youyou.uumall.business;

import com.youyou.uumall.base.BaseBusiness;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/6.
 */
@EBean
public class AddressBiz extends BaseBusiness {
    public static final int QUERY_COUNTRY = 3; //查询数据字典
    public static final int GET_RECOMMEND_LIST = 2;//获取推荐商品数据

    /**
     * 获取国家信息
     */
    @Background
    public void queryDict(Map map) {
        arrayListCallbackInterface.arrayCallBack(QUERY_COUNTRY, handleResponse(baseApi.queryDict(map)));
    }

    /**
     *
     * 查询推荐商品
     */
    @Background
    public void getRecommendList(Map map) {
        arrayListCallbackInterface.arrayCallBack(GET_RECOMMEND_LIST, handleResponse(baseApi.getRecommendData(map)));
    }

    /**
     *
     * 查询推荐商品
     */
    @Background
    public void queryDelivery(Map map) {
        arrayListCallbackInterface.arrayCallBack(GET_RECOMMEND_LIST, handleResponse(baseApi.queryDelivery(map)));
    }

}
