package com.youyou.uumall.ui;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.RegisterBiz;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2016/5/5.
 */
@EActivity(R.layout.activity_retrieve)
public class RetrievePasswordActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface {
    @ViewById
    EditText retrieve_phone;
    @ViewById
    EditText retrieve_code;
    @ViewById
    EditText retrieve_pwd;
    @ViewById
    EditText retrieve_repwd;

    @ViewById
    TextView retrieve_code_tv;
    @Bean
    RegisterBiz registerBiz;
    @AfterViews
    void afterViews(){
        registerBiz.setObjectCallbackInterface(this);

    }
    @Click
    void retrieve_code_tv(){
        String phone = retrieve_phone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showToastShort("请正确输入手机号");
            return;
        }
            registerBiz.getSmsCode(phone,"2");
        MyUtils.setSmsCodeAnimator(retrieve_code_tv,this);
    }

    @Click
    void retrieve_confirm(){
        String phone = retrieve_phone.getText().toString();
        String SmsCode = retrieve_code.getText().toString();
        String pwd = retrieve_pwd.getText().toString();
        String repwd = retrieve_repwd.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showToastShort("请正确输入手机号");
            return;
        }
        if (TextUtils.isEmpty(SmsCode)) {
            showToastShort("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showToastShort("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(repwd)) {
            showToastShort("请重复输入密码");
            return;
        }
        if (!TextUtils.equals(repwd, pwd)) {
            showToastShort("两次密码输入不一致,请重试");
            return;
        }
        registerBiz.retrievePassword(phone,pwd,SmsCode);
    }




    @Click
    void retrieve_pro() {
        finish();
//        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (type == RegisterBiz.GET_SMS_CODE) {
            Response response = (Response) t;
            if (response.code == 0) {
                showToastShort("验证码以发送");
            }else{
                showToastShort(response.msg);
            }
        }
        if (type == RegisterBiz.RETRIEVE_PASSWORD) {
            Response response = (Response) t;
            if (response.code == 0) {
                showToastShort("修改完成");
                finish();
            }else{
                showToastShort(response.msg);
            }
        }
    }
}
