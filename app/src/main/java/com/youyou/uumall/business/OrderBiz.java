package com.youyou.uumall.business;

import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.model.OrderBean;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.HashMap;
import java.util.List;
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
    public void createWxPrepayOrder(String orderId,double totalFee){
        Map param = new HashMap();
        param.put("orderId", orderId);
        param.put("totalFee", totalFee);
        param.put("body", "油桃扫货");
        param.put("tradeType", "APP");
        arrayListCallbackInterface.arrayCallBack(CREATE_WX_PREPAY_ORDER,handleResponse(baseApi.createWxPrepayOrder(param)));
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
        map.put("id",orderId);
        map.put("status",status);
        Response<List<OrderBean>> listResponse = baseApi.queryOrder(map);
//        log.e(listResponse);
        if (listResponse!=null) {
            arrayListCallbackInterface.arrayCallBack(QUERY_ORDER,listResponse.data);
        }
    }
}
