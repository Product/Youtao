package com.youyou.uumall.ui;

import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.RegisterBiz;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
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
//        if (TextUtils.isEmpty(phone) || !UserUtils.isMobileNO(phone)) {
        if (TextUtils.isEmpty(phone) ) {
            showToastShort("请正确输入手机号");
            return;
        }
        //过了三十秒才可以再次发送请求,用handler来处理整个事件
        if (isTimeout) {
            registerBiz.getSmsCode(phone,"1");//请求完数据后
            isTimeout = false;
            handler.postDelayed(runable, 30000);
        } else {
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
    void register_register_bt() {
        String phoneNum = register_phone.getText().toString();
        String SMSCode = register_smscode.getText().toString();
        String passWord = register_password.getText().toString();
        String invCode = register_invitation_code.getText().toString();
        if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(SMSCode) || TextUtils.isEmpty(passWord)) {
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

//    @Click
//    void register_cancel() {
//        finish();
//        overridePendingTransition(R.anim.anim_none, R.anim.to_center_exit);
//    }



    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (type == RegisterBiz.USER_REGISTER) {
            Response response = (Response) t;
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                showToastShort("注册成功");
                finish();
            } else {
                showToastShort(response.msg);
            }
        } else if (type == RegisterBiz.GET_SMS_CODE) {
            Response response = (Response) t;
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                showToastShort("验证码已发送");
            } else {
                showToastShort(response.msg);
            }
        }
    }
}

