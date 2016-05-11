package com.youyou.shopping.business;

import com.youyou.shopping.base.BaseBusiness;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/5.
 */
@EBean
public class LoginBiz extends BaseBusiness {
    public static final int USER_LOGIN = 1;
    private static final int USER_REGISTER =2 ;

    @Background
    public void userLogin(Map map){
        objectCallbackInterface.objectCallBack(USER_LOGIN, handleResponse(baseApi.userLogin(map)));
    }


}
