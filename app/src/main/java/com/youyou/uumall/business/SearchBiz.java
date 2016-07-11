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

    /**
     * 搜索商品接口
     */
    @Background
    public void queryGoodsById(int pageNo,int pageSize, String  searchKey){
        String countryCode = MyUtils.getCountryCode(context);
        Map map = new HashMap();
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("countryCode", countryCode);
        map.put("searchKeywords", searchKey);
        objectCallbackInterface.objectCallBack(QUERY_GOODS_BY_KEYWORDS, baseApi.queryGoodsByKeywords(map));
    }

    /**
     * 查询红包接口
     */
    @Background
    public void queryBonus(){
        Response<List<BonusBean>> listResponse = baseApi.queryBonus();
        arrayListCallbackInterface.arrayCallBack(QUERY_BONUS, listResponse.data);
    }
}
