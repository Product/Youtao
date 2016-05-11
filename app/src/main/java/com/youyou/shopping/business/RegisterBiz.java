package com.youyou.shopping.business;

import com.youyou.shopping.base.BaseBusiness;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/3.
 */
@EBean
public class RegisterBiz extends BaseBusiness {
    public static final int GET_SMS_LIST = 1;
    private static final int USER_REGISTER =2 ;

    @Background
    public void getSmsCode(Map map){
        objectCallbackInterface.objectCallBack(GET_SMS_LIST, handleResponse(baseApi.getSMSCode(map)));
    }

    @Background
    public void userRegister(Map map){
        objectCallbackInterface.objectCallBack(USER_REGISTER, handleResponse(baseApi.userRegister(map)));
    }

    @Background
    public void retrievePassword(Map map){
        objectCallbackInterface.objectCallBack(USER_REGISTER, handleResponse(baseApi.retrievePassword(map)));
    }
}
