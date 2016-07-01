package com.youyou.uumall.business;

import android.content.Context;

import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.model.BonusBean;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/5.
 */
@EBean
public class SearchBiz extends BaseBusiness {

    @RootContext
    Context context;
    public static final int QUERY_GOODS_BY_KEYWORDS = 1;
    public static final int QUERY_BONUS = 2;

    @Background
    public void queryGoodsById(String  searchKey){
        String countryCode = MyUtils.getCountryCode(context);
        Map map = new HashMap();
        map.put("countryCode", countryCode);
        map.put("searchKeywords", searchKey);
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
//        log.e(listResponse.toString());
        arrayListCallbackInterface.arrayCallBack(QUERY_BONUS, listResponse.data);
    }
}
