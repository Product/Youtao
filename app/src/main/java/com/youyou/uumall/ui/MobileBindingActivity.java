package com.youyou.uumall.ui;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.RegisterBiz;
import com.youyou.uumall.event.LoginEvent;
import com.youyou.uumall.event.MineTriggerEvent;
import com.youyou.uumall.event.MobileBindingEvent;
import com.youyou.uumall.event.ShopCartTriggerEvent;
import com.youyou.uumall.event.ShopCartUpdateEvent;
import com.youyou.uumall.utils.MyUtils;

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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
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
        registerBiz.getSmsCode(phone, "3");
        MyUtils.setSmsCodeAnimator(mobile_binding_getsms_tv, this);

    }


    @Click
    void mobile_binding_next_tv() {//跳过本步骤
        sendEvent();
        finish();
    }

    @Click
    void mobile_binding_confirm_btn() {//确认绑定
        String phone = mobile_binding_phone_et.getText().toString();
        String sms = mobile_binding_sms_et.getText().toString();
//        String pwd = mobile_binding_pwd_et.getText().toString();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(sms)) {
            showToastShort("信息填写不完整");
            return;
        }
        registerBiz.mobileBinding(openId, phone, sms);

    }

    @Click
    void mobile_binding_pro_iv() {//回退上一页?// TODO: 2016/5/27 回退到哪里
        sendEvent();
        finish();
//        MainActivity_.intent(this).start();
    }

    @Override
    public void onBackPressed() {
        sendEvent();
        super.onBackPressed();
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
        } else if (type == RegisterBiz.MOBILE_BINDING) {
            Response response = (Response) t;
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                showToastShort("绑定完成");
                sendEvent();
                finish();
            } else {
                showToastShort(response.msg);
            }
        }

    }

    private void sendEvent() {
        eventBus.post(new ShopCartTriggerEvent());
        eventBus.post(new MineTriggerEvent());
        eventBus.post(new ShopCartUpdateEvent());
        eventBus.post(new LoginEvent());
    }
}
