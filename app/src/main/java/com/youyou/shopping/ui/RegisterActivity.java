package com.youyou.shopping.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.youyou.shopping.R;
import com.youyou.shopping.base.BaseActivity;
import com.youyou.shopping.base.BaseBusiness;
import com.youyou.shopping.business.RegisterBiz;
import com.youyou.shopping.model.SmsCodeBean;
import com.youyou.shopping.utils.UserUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface {
    private static final String SMSCODE_TYPE = "1";
    @ViewById
    EditText register_phone;
    @ViewById
    EditText register_smscode;
    @ViewById
    TextView register_getSmsCode;
    @ViewById
    EditText register_password;
    @ViewById
    EditText register_invitation_code;
    @Bean
    RegisterBiz registerBiz;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Map map;
    private boolean isTimeout = true;

    static Handler handler = new Handler();

    @AfterViews
    void afterViews() {
        registerBiz.setObjectCallbackInterface(this);
    }

    @Click
    void register_getSmsCode() {

        String phone = register_phone.getText().toString();
        if (TextUtils.isEmpty(phone) || !UserUtils.isMobileNO(phone)) {
            showToastShort("请正确输入手机号");
            return;
        }
        //过了三十秒才可以再次发送请求,用handler来处理整个事件
        if (isTimeout) {
            map = new HashMap();
            map.put("mobile", phone);
            map.put("type", "1");
            registerBiz.getSmsCode(map);//请求完数据后
//            registerBiz.showErrorMessage("验证码已发送");// TODO: 2016/5/5 这里不是很完善,到后面可能要改动接口方法,给接口方法设置返回值 
            isTimeout = false;
            handler.postDelayed(runable,30000);
        }else{
            showToastShort("30秒后重试");
        }

    }

    Runnable runable = new Runnable() {
        @Override
        public void run() {
            isTimeout = true;
        }
    };

    @Click
    void register_register_bt(){
        String phoneNum = register_phone.getText().toString();
        String SMSCode = register_smscode.getText().toString();
        String passWord = register_password.getText().toString();
        String invCode = register_invitation_code.getText().toString();
        if (TextUtils.isEmpty(phoneNum)||TextUtils.isEmpty(SMSCode)||TextUtils.isEmpty(passWord)){
            showToastShort("值不能为空");
            return;
        }
        map = new HashMap();
        map.put("mobile", phoneNum);
        map.put("password", passWord);
        map.put("verifyCode", SMSCode);
        map.put("parentInvitationCode", invCode);
        registerBiz.userRegister(map);
    }
    @Click
    void register_cancel() {
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.to_center_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @Override
    public void objectCallBack(int type, Object t) {
        if (type == RegisterBiz.GET_SMS_LIST) {

        }
    }
}

