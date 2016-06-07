package com.youyou.uumall.ui;

import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.event.MobileBindingEvent;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.RegisterBiz;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2016/5/27.
 */
@EActivity(R.layout.activity_mobile_binding)
public class MobileBindingActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface {
    @Bean
    RegisterBiz registerBiz;

    @ViewById
    ImageView mobile_binding_pro_iv;

    @ViewById
    TextView mobile_binding_next_tv, mobile_binding_getsms_tv;


    @ViewById
    EditText mobile_binding_phone_et, mobile_binding_sms_et, mobile_binding_pwd_et;


    private boolean isTimeout = true;

   static Handler handler = new Handler();

    private String openId;

    @Override
    protected void registerEvent() {
        super.registerEvent();
        eventBus.register(this);
    }

    @Override
    protected void unRegisterEvent() {
        super.unRegisterEvent();
        eventBus.unregister(this);
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MobileBindingEvent event) {
        openId = event.getEvent();
        showToastShort("消息收到");
    }

    @AfterViews
    void afterViews() {
        registerBiz.setObjectCallbackInterface(this);
    }

    @Click
    void mobile_binding_getsms_tv() {//获取验证码
        String phone = mobile_binding_phone_et.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showToastLong("请正确输入手机号");
            return;
        }
        if (isTimeout) {
            registerBiz.getSmsCode(phone, "3");
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
    void mobile_binding_next_tv() {//跳过本步骤
        MainActivity_.intent(this).start();
    }

    @Click
    void mobile_binding_confirm_btn() {//确认绑定
        String phone = mobile_binding_phone_et.getText().toString();
        String sms = mobile_binding_sms_et.getText().toString();
//        String pwd = mobile_binding_pwd_et.getText().toString();
        if (TextUtils.isEmpty(phone)||TextUtils.isEmpty(sms)){
            showToastShort("信息填写不完整");
            return;
        }
        registerBiz.mobileBinding(openId,phone,sms);

    }

    @Click
    void mobile_binding_pro_iv() {//回退上一页?// TODO: 2016/5/27 回退到哪里
        MainActivity_.intent(this).start();
    }

    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (type == RegisterBiz.GET_SMS_CODE) {
            Response response = (Response) t;
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                showToastShort("验证码已发送");
            } else {
                showToastShort(response.msg);
            }
        }else
        if (type == RegisterBiz.MOBILE_BINDING) {
            Response response = (Response) t;
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                showToastShort("绑定完成");
                MainActivity_.intent(this).start();// TODO: 2016/5/27 暂时转跳至主页面
            } else {
                showToastShort(response.msg);
            }
        }

    }
}
