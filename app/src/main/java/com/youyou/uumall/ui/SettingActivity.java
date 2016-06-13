package com.youyou.uumall.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.umeng.message.PushAgent;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.RegisterBiz;
import com.youyou.uumall.event.MineTriggerEvent;
import com.youyou.uumall.utils.UserUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2016/5/30.
 */
@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface, CompoundButton.OnCheckedChangeListener {

    @ViewById
    CheckBox setting_push_cb;//接收push

    @ViewById
    Button setting_exit_btn;
    @Bean
    RegisterBiz registerBiz;

    @Bean
    UserUtils userUtils;

    @AfterViews
    void afterViews() {
        registerBiz.setObjectCallbackInterface(this);
        if (userUtils.getUserId() == null) {
            setting_exit_btn.setVisibility(View.GONE);
        }else {
            setting_exit_btn.setVisibility(View.VISIBLE);
        }

        setting_push_cb.setOnCheckedChangeListener(this);
    }

    @Click
    void setting_pro_im() {//回退
        finish();
    }

    @Click
    void setting_appraise_ll() {//求好评

    }

    @Click
    void setting_cache_ll() {//清除缓存

    }

    @Click
    void setting_about_ll() {//关于
        AboutActivity_.intent(this).start();
    }

    @Click
    void setting_exit_btn() {//退出登录
        registerBiz.logout();
    }


    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (RegisterBiz.LOGOUT == type) {
            Response response = (Response) t;
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                eventBus.post(new MineTriggerEvent());
                userUtils.saveUserId("");//清空
                userUtils.saveUserInfo("");
                MainActivity_.intent(this).start();
            } else {
                showToastShort(response.msg);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        if (!mPushAgent.isEnabled()&&isChecked){//点击之后是开
            mPushAgent.enable();
            showToastShort("启用推送服务");
        }
        if (mPushAgent.isEnabled()&&!isChecked){
            mPushAgent.disable();
            showToastShort("关闭推送服务");
        }
    }
}
