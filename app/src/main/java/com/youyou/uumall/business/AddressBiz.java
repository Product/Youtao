package com.youyou.uumall.business;

import android.content.Context;

import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/6.
 */
@EBean
public class AddressBiz extends BaseBusiness {

    public static final int QUERY_COUNTRY = 3; //查询数据字典

    public static final int GET_RECOMMEND_LIST = 2;//获取推荐商品数据
    public static final String BG_BASE_COUNTRY = "bg_base_country";
    @RootContext
    Context mContext;

    /**
     * 获取国家信息
     */
    @Background
    public void queryDict() {
        Map map = new HashMap();
        map.put("dictType", BG_BASE_COUNTRY);
        arrayListCallbackInterface.arrayCallBack(QUERY_COUNTRY, handleResponse(baseApi.queryDict(map)));
    }

    /**
     * 自提点查询
     */
    @Background
    public void queryDelivery() {
        String countryCode = MyUtils.getCountryCode(mContext);
        Map map = new HashMap();
        map.put("countryCode", countryCode);
//        Response<Object> objectResponse = baseApi.queryDelivery(map);
//        log.e(objectResponse.toString());
        arrayListCallbackInterface.arrayCallBack(GET_RECOMMEND_LIST, handleResponse(baseApi.queryDelivery(map)));
    }

}
