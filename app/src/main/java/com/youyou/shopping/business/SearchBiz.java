package com.youyou.shopping.business;

import com.youyou.shopping.base.BaseBusiness;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/5.
 */
@EBean
public class SearchBiz extends BaseBusiness {
    public static final int QUERY_GOODS_BY_KEYWORDS = 1;
//    public static final int QUERY_CATEGORY = 2;

    @Background
    public void queryGoodsById(Map map){
        arrayListCallbackInterface.arrayCallBack(QUERY_GOODS_BY_KEYWORDS, handleResponse(baseApi.queryGoodsByKeywords(map)));
    }

//    @Background
//    public void queryCategory(Map map) {
//        arrayListCallbackInterface.arrayCallBack(QUERY_CATEGORY, handleResponse(baseApi.queryCategory(map)));
//    }


}
