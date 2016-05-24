package com.youyou.uumall.business;

import com.youyou.uumall.base.BaseBusiness;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/3.
 */
@EBean
public class RegisterBiz extends BaseBusiness {
    public static final int GET_SMS_LIST = 1;
    public static final int USER_REGISTER =2 ;

    @Background
    public void getSmsCode(Map map){
        objectCallbackInterface.objectCallBack(GET_SMS_LIST, baseApi.getSMSCode(map));
    }

    @Background
    public void userRegister(Map map){
        objectCallbackInterface.objectCallBack(USER_REGISTER, baseApi.userRegister(map));
    }

    @Background
    public void retrievePassword(Map map){
        objectCallbackInterface.objectCallBack(USER_REGISTER, handleResponse(baseApi.retrievePassword(map)));
    }
}
