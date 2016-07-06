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
public class CommodityBiz extends BaseBusiness {

    public static final int GET_SLIDER_LIST = 1; //获取轮播数据
    public static final int GET_RECOMMEND_LIST = 2;//获取推荐商品数据
    public static final int GET_BRAND_LIST = 4;//推荐品牌查询
    public static final int QUERY_GOODS_BY_BRAND = 5;//品牌搜索商品接口
    public static final String COUNTRY_CODE = "countryCode";
    @RootContext
    Context mContext;
    /**
     * 获取用户详细信息
     */
    @Background
    public void getSliderList() {
        String countryCode = MyUtils.getCountryCode(mContext);
        Map map = new HashMap();
        map.put(COUNTRY_CODE, countryCode);
        arrayListCallbackInterface.arrayCallBack(GET_SLIDER_LIST, handleResponse(baseApi.getSliderData(map)));
    }

    /**
     *
     * 推荐商品查询
     */
    @Background
    public void getRecommendList() {
        String countryCode = MyUtils.getCountryCode(mContext);
        Map map = new HashMap();
        map.put(COUNTRY_CODE, countryCode);
//        Response<Object> recommendData = baseApi.getRecommendData(map);
//        log.e(recommendData.toString());
        arrayListCallbackInterface.arrayCallBack(GET_RECOMMEND_LIST, handleResponse(baseApi.getRecommendData(map)));
    }

    /**
     * 推荐品牌查询
     */
    @Background
    public void getBrandList() {
        String countryCode = MyUtils.getCountryCode(mContext);
        Map map = new HashMap();
        map.put("countryCode", countryCode);
        map.put("maxResultSize", "");
        arrayListCallbackInterface.arrayCallBack(GET_BRAND_LIST, handleResponse(baseApi.getBrandData(map)));
    }

    /**
     * 品牌搜索商品接口
     */
    @Background
    public void queryGoodsByBrand(int pageNo,int pageSize, String brandId) {
        String countryCode = MyUtils.getCountryCode(mContext);
        Map map = new HashMap();
        map.put("pageNo", pageNo);//当前页码
        map.put("pageSize", pageSize);//分页大小
        map.put("countryCode", countryCode);//国家编码
        map.put("brandId", brandId);//商品分享ID
        arrayListCallbackInterface.arrayCallBack(QUERY_GOODS_BY_BRAND,baseApi.queryGoodsByBrand(map).data);
    }


}
