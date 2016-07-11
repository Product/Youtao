package com.youyou.uumall.business;

import android.content.Context;

import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/3.
 */
@EBean
public class RegisterBiz extends BaseBusiness {

    @RootContext
    Context context;
    public static final int GET_SMS_CODE = 1;
    public static final int USER_REGISTER =2 ;
    public static final int RETRIEVE_PASSWORD =3 ;
    public static final int WECHAT_LOGIN =4 ;
    public static final int MOBILE_BINDING =5 ;
    public static final int LOGOUT =6 ;
    public static final int MOD_USER_INFO =7 ;

    /**
     * 获取验证码接口
     */
    @Background
    public void getSmsCode(String mobile, String type){
        Map map = new HashMap();
        map.put("mobile",mobile);
        map.put("type",type);
        objectCallbackInterface.objectCallBack(GET_SMS_CODE, baseApi.getSMSCode(map));
    }

    /**
     * 用户注册接口
     */
    @Background
    public void userRegister(Map map){
        objectCallbackInterface.objectCallBack(USER_REGISTER, baseApi.userRegister(map));
    }

    /**
     * 用户找回密码接口
     */
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

    /**
     * 微信登录接口
     */
    @Background
    public void wechatLogin(String openId,String parentInvitationCode,String deviceToken,String clientType){
        Map map = new HashMap();
        map.put("openId",openId);
        map.put("parentInvitationCode",parentInvitationCode);
        map.put("deviceToken",deviceToken);
        map.put("clientType",clientType);
        arrayListCallbackInterface.arrayCallBack(WECHAT_LOGIN, handleResponse(baseApi.wechatLogin(map)));
    }

    /**
     * 手机绑定接口
     */
    @Background
    public void mobileBinding(String openId, String mobile, String verifyCode) {
        Map map = new HashMap();
        map.put("openId",openId);
        map.put("mobile",mobile);
        map.put("verifyCode",verifyCode);
        objectCallbackInterface.objectCallBack(MOBILE_BINDING,baseApi.mobileBinding(map));
    }

    /**
     * 用户登出接口
     */
    @Background
    public void logout() {
        objectCallbackInterface.objectCallBack(LOGOUT,baseApi.logout());
    }

    /**
     * 更新用户信息接口
     */
    @Background
    public void modUserInfo(String userName,String userIcon) {
        String deviceToken = MyUtils.getPara(BaseConstants.preferencesFiled.DEVICE_TOKEN, context);
        Map map = new HashMap();
        map.put("deviceToken",deviceToken);
        map.put("clientType","1");
        map.put("userName",userName);
        map.put("userIcon",userIcon);
        objectCallbackInterface.objectCallBack(MOD_USER_INFO,baseApi.modUserInfo(map));
    }

}
