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
public class LoginBiz extends BaseBusiness {
    public static final int USER_LOGIN = 1;
    public static final int TEXT_LOGIN =2 ;

    @Background
    public void userLogin(String userPhone, String userPwd,String deviceToken){
        Map paramMap = new HashMap<>();
        paramMap.put("mobile", userPhone);
        paramMap.put("password", userPwd);
        paramMap.put("deviceToken", deviceToken);
        paramMap.put("clientType", "1");
        objectCallbackInterface.objectCallBack(USER_LOGIN, baseApi.userLogin(paramMap));
    }


}
