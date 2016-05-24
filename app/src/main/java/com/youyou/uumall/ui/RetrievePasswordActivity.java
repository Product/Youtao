package com.youyou.uumall.ui;

import android.widget.EditText;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.business.RegisterBiz;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/5.
 */
@EActivity(R.layout.activity_retrieve)
public class RetrievePasswordActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface {
    @ViewById
    EditText retrieve_phone_et;
    @ViewById
    EditText retrieve_password_et;
    @ViewById
    EditText retrieve_smscode_et;
    @ViewById
    TextView retrieve_get_smscode_tv;

    @Bean
    RegisterBiz registerBiz;

    Map paramMap;
    @AfterViews
    void afterViews(){
        registerBiz.setObjectCallbackInterface(this);

    }
    @Click
    void retrieve_get_smscode_tv(){
        String phone = retrieve_phone_et.getText().toString();
        paramMap = new HashMap();
        paramMap.put("mobile", phone);
        paramMap.put("type", "2");
        registerBiz.getSmsCode(paramMap);
    }

    @Click
    void retrieve_get_password_tv(){
        String phone = retrieve_phone_et.getText().toString();
        String newPwd = retrieve_password_et.getText().toString();
        String SmsCode = retrieve_smscode_et.getText().toString();
//        {mobile:"18555808691",newPassword:"123123",verifyCode:"7839"}
        paramMap = new HashMap();
        paramMap.put("mobile", phone);
        paramMap.put("newPassword", newPwd);
        paramMap.put("verifyCode", SmsCode);
        registerBiz.retrievePassword(paramMap);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @Override
    public void objectCallBack(int type, Object t) {

    }
}
