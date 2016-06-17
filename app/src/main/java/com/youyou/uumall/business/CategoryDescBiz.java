package com.youyou.uumall.business;

import android.content.Context;

import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/5.
 */
@EBean
public class CategoryDescBiz extends BaseBusiness {
    public static final int QUERY_CATEGORY = 5;
    public static final int QUERY_GOODS_BY_ID = 3;
    public static final int QUERY_GOODS_BY_CATEGORY = 6;//商品分类查询接口
    @RootContext
    Context mContext;

    @Background
    public void queryGoodsById(String goodsId) {
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

    /**
     * 商品分类查询接口
     */
    @Background
    public void queryGoodsByCategory(String categoryId) {
        String countryCode = "";
        String country = MyUtils.getPara(BaseConstants.preferencesFiled.DEFAULT_COUNTRY, mContext);
        String dictList = MyUtils.getPara("dictList", mContext);
        String[] split = dictList.split(";");
        for (String s :
                split) {
            if (s.contains(country)) {
                countryCode = s.split(",")[1];
            }
        }
        Map map = new HashMap();
        map.put("pageNo", "");//当前页码
        map.put("pageSize", "");//分页大小
        map.put("countryCode", countryCode);//国家编码
        map.put("categoryId", categoryId);//商品分类ID
        arrayListCallbackInterface.arrayCallBack(QUERY_GOODS_BY_CATEGORY, handleResponse(baseApi.queryGoodsByCategory(map)));
//        objectCallbackInterface.objectCallBack(QUERY_GOODS_BY_CATEGORY, baseApi.queryGoodsByCategory(map));
    }
}
