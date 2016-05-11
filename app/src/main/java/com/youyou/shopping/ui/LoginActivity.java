package com.youyou.shopping.ui;

import android.text.TextUtils;
import android.widget.EditText;

import com.youyou.shopping.R;
import com.youyou.shopping.base.BaseActivity;
import com.youyou.shopping.base.BaseBusiness;
import com.youyou.shopping.business.LoginBiz;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface {

    @ViewById
    EditText login_phone_et;
    @ViewById
    EditText login_pwd_et;
    @Bean
    LoginBiz loginBiz;

    Map paramMap ;
    @AfterViews
    void afterViews() {
        loginBiz.setObjectCallbackInterface(this);
    }

    @Click
    void login_login_bt() {
        String userPhone = login_phone_et.getText().toString();
        String userPwd = login_pwd_et.getText().toString();
        if (TextUtils.isEmpty(userPhone)) {
            showToastShort("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(userPwd)) {
            showToastShort("密码不能为空");
            return;
        }
        paramMap = new HashMap();
        paramMap.put("mobile",userPhone);
        paramMap.put("password",userPwd);
        loginBiz.userLogin(paramMap);
        showToastShort("登录成功");
        login_cancel();
    }
    @Click
    void login_forgetpwd_tv(){
        RetrievePasswordActivity_.intent(this).start();
        overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Click
    void login_cancel() {
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.to_center_exit);
    }

    @Click
    void register() {
        RegisterActivity_.intent(this).start();
        overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @Override
    public void objectCallBack(int type, Object t) {
        //// TODO: 2016/5/5 用户登录的操作,可以拿取到数据
    }
}
