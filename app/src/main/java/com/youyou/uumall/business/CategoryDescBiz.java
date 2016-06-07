package com.youyou.uumall.business;

import com.youyou.uumall.base.BaseBusiness;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/5.
 */
@EBean
public class CategoryDescBiz extends BaseBusiness {
    public static final int QUERY_CATEGORY = 1;
    public static final int QUERY_GOODS_BY_ID = 3;

    @Background
    public void queryGoodsById(String goodsId){
        Map map = new HashMap();
        map.put("goodsId", goodsId);
        objectCallbackInterface.objectCallBack(QUERY_GOODS_BY_ID, handleResponse(baseApi.queryGoodsById(map)));
    }

    @Background
    public void queryCategory(String parentId) {
        Map map = new HashMap();
        map.put("parentId", parentId);
        arrayListCallbackInterface.arrayCallBack(QUERY_CATEGORY, handleResponse(baseApi.queryCategory(map)));
    }


}
