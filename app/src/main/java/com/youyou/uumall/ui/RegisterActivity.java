package com.youyou.uumall.ui;

import android.text.TextUtils;
import android.view.ViewGroup;
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
//    private GoogleApiClient client;
    private Map map;

    @AfterViews
    void afterViews() {
        registerBiz.setObjectCallbackInterface(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (statusView != null) {
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.removeView(statusView);
        }
    }

    @Click
    void register_getSmsCode() {
        String phone = register_phone.getText().toString();
        if (TextUtils.isEmpty(phone) ) {
            showToastShort("手机号输入有误");
            return;
        }
        registerBiz.getSmsCode(phone,"1");
        MyUtils.setSmsCodeAnimator(register_getSmsCode,this);

    }


    @Click
    void register_register_bt() {
        String phoneNum = register_phone.getText().toString();
        String SMSCode = register_smscode.getText().toString();
        String passWord = register_password.getText().toString();
        String invCode = register_invitation_code.getText().toString();
        if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(SMSCode) || TextUtils.isEmpty(passWord)) {
            showToastShort("输入有误");
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
            if (response == null) {
                return;
            }
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                showToastShort("注册成功");
                finish();
            } else {
                showToastShort(response.msg);
            }
        } else if (type == RegisterBiz.GET_SMS_CODE) {
            Response response = (Response) t;
            if (response == null) {
                return;
            }
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
//                showToastShort("验证码已发送");
            } else {
                showToastShort(response.msg);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_none,R.anim.from_top_exit);
    }
}

