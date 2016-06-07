package com.youyou.uumall.business;

import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/3.
 */
@EBean
public class RegisterBiz extends BaseBusiness {
    public static final int GET_SMS_CODE = 1;
    public static final int USER_REGISTER =2 ;
    public static final int RETRIEVE_PASSWORD =3 ;
    public static final int WECHAT_LOGIN =4 ;
    public static final int MOBILE_BINDING =5 ;
    public static final int LOGOUT =6 ;

    @Background
    public void getSmsCode(String mobile, String type){
        Map map = new HashMap();
        map.put("mobile",mobile);
        map.put("type",type);
        objectCallbackInterface.objectCallBack(GET_SMS_CODE, baseApi.getSMSCode(map));
    }

    @Background
    public void userRegister(Map map){
        objectCallbackInterface.objectCallBack(USER_REGISTER, baseApi.userRegister(map));
    }

    @Background
    public void retrievePassword(String phone,String newPwd,String SmsCode){
        Map map = new HashMap();
        map.put("mobile", phone);
        map.put("newPassword", newPwd);
        map.put("verifyCode", SmsCode);
        Response<Object> objectResponse = baseApi.retrievePassword(map);
        log.e(objectResponse.toString());
        objectCallbackInterface.objectCallBack(RETRIEVE_PASSWORD, objectResponse);
    }

    @Background
    public void wechatLogin(String openId,String parentInvitationCode,String deviceToken,String clientType){
        Map map = new HashMap();
        map.put("openId",openId);
        map.put("parentInvitationCode",parentInvitationCode);
        map.put("deviceToken",deviceToken);
        map.put("clientType",clientType);
        arrayListCallbackInterface.arrayCallBack(WECHAT_LOGIN, handleResponse(baseApi.wechatLogin(map)));
    }

    @Background
    public void mobileBinding(String openId, String mobile, String verifyCode) {
        Map map = new HashMap();
        map.put("openId",openId);
        map.put("mobile",mobile);
        map.put("verifyCode",verifyCode);
        objectCallbackInterface.objectCallBack(MOBILE_BINDING,baseApi.mobileBinding(map));
    }

    @Background
    public void logout() {
        objectCallbackInterface.objectCallBack(LOGOUT,baseApi.logout());
    }

}
