package com.youyou.uumall.business;

import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.model.BonusBean;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/5.
 */
@EBean
public class SearchBiz extends BaseBusiness {
    public static final int QUERY_GOODS_BY_KEYWORDS = 1;
    public static final int QUERY_BONUS = 2;

    @Background
    public void queryGoodsById(Map map){
        arrayListCallbackInterface.arrayCallBack(QUERY_GOODS_BY_KEYWORDS, handleResponse(baseApi.queryGoodsByKeywords(map)));
    }

//    @Background
//    public void queryCategory(Map map) {
//        arrayListCallbackInterface.arrayCallBack(QUERY_CATEGORY, handleResponse(baseApi.queryCategory(map)));
//    }

    @Background
    public void queryBonus(){
        // TODO: 2016/6/7 测试
        Response<List<BonusBean>> listResponse = baseApi.queryBonus();
        log.e(listResponse.toString());
        arrayListCallbackInterface.arrayCallBack(QUERY_BONUS, handleResponse(listResponse));
    }
}
