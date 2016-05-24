package com.youyou.uumall.business;

import com.youyou.uumall.base.BaseBusiness;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/18.
 */
@EBean
public class OrderBiz extends BaseBusiness{
    public static final int ORDER_SUBMIT = 0;
    public static final int CREATE_WX_PREPAY_ORDER =1 ;
    public static final int QUERY_ORDER =2 ;
    public static final int CANCEL_ORDER =3 ;

    @Background
    public void orderSubmit(Map map){
        objectCallbackInterface.objectCallBack(ORDER_SUBMIT,baseApi.orderSubmit(map));
    }

    @Background
    public void createWxPrepayOrder(Map map){
        arrayListCallbackInterface.arrayCallBack(CREATE_WX_PREPAY_ORDER,handleResponse(baseApi.createWxPrepayOrder(map)));
    }

    @Background
    public void cancelOrder(String orderId){
        Map map = new HashMap();
        map.put("orderId",orderId);
        objectCallbackInterface.objectCallBack(CANCEL_ORDER,baseApi.cancelOrder(map));
    }

    @Background
    public void queryOrder(Integer pageNo,Integer pageSize,String orderId,String status){
        Map map = new HashMap();
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("orderId",orderId);
        map.put("status",status);
//        Response<Object> listResponse = baseApi.queryOrder(map);
//        log.e(listResponse.toString());
        arrayListCallbackInterface.arrayCallBack(QUERY_ORDER,handleResponse(baseApi.queryOrder(map)));
    }
}
