package com.youyou.uumall.ui;

import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.PushAgent;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.RegisterBiz;
import com.youyou.uumall.event.MineTriggerEvent;
import com.youyou.uumall.event.ShopCartUpdateEvent;
import com.youyou.uumall.utils.FileUtils;
import com.youyou.uumall.utils.MyUtils;
import com.youyou.uumall.utils.UserUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;



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

    @ViewById
    TextView setting_cache_tv;

    @AfterViews
    void afterViews() {
        registerBiz.setObjectCallbackInterface(this);
        registerBiz.modUserInfo("","");
        setting_push_cb.setOnCheckedChangeListener(this);
        getPathDirSize();
    }

    @Click
    void setting_pro_im() {//回退
        finish();
    }

    @Click
    void setting_appraise_ll() {//求好评

    }

    @Background
    public void getPathDirSize() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String[] dirs = {BaseConstants.path.FILE_DIR,
                    BaseConstants.path.IMAGE_DIR, BaseConstants.path.LOG_DIR,
                    BaseConstants.path.PHOTO_DIR, BaseConstants.path.STICKER_DIR,
                    BaseConstants.path.WEB_DIR};
            File dir;
            for (String path : dirs) {
                dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                } else {
                    try {
                        long size = FileUtils.getFolderSize(dir);
                        log.e(path + "  占用空间大小" + size);
                        if (path.equals(BaseConstants.path.IMAGE_DIR)) {
                            showImageCacheSize(size);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @UiThread
    void showImageCacheSize(long size) {
        setting_cache_tv.setText( FileUtils.formatFileSize(size));
    }

    @Click
    void setting_cache_ll() {//清除缓存
        ImageLoader.getInstance().clearMemoryCache();  // 清除内存缓存
        ImageLoader.getInstance().clearDiskCache();  // 清除本地缓存
        getPathDirSize();
        MyUtils.clearWebViewCeche(this);
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
            if (response == null) {
                return;
            }
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                eventBus.post(new MineTriggerEvent());
                MyUtils.savePara(this, BaseConstants.preferencesFiled.OPEN_ID,"");
                MyUtils.savePara(this, BaseConstants.preferencesFiled.USER_INFO,"");
                userUtils.saveUserId("");//清空
                userUtils.saveUserInfo("");
                eventBus.post(new ShopCartUpdateEvent());
                MainActivity_.intent(this).start();
            } else {
                showToastShort(response.msg);
            }
        } else if (RegisterBiz.MOD_USER_INFO == type) {
            Response response = (Response) t;
//            log.e(response.toString());
            if (response == null) {
                return;
            }
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                setting_exit_btn.setVisibility(View.VISIBLE);
            } else {
                setting_exit_btn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        if (!mPushAgent.isEnabled()&&isChecked){//点击之后是开
            mPushAgent.enable();
//            showToastShort("启用推送服务");
        }
        if (mPushAgent.isEnabled()&&!isChecked){
            mPushAgent.disable();
//            showToastShort("关闭推送服务");
        }
    }
}
