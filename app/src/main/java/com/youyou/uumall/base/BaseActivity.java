/*
 * Copyright (C) 2007 The Android  Source Project
 *
 * Licensed under the ZenMall License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.zenmall.cn/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youyou.uumall.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.youyou.uumall.R;
import com.youyou.uumall.SPApplication;
import com.youyou.uumall.business.LoginBiz;
import com.youyou.uumall.business.LoginBiz_;
import com.youyou.uumall.business.RegisterBiz;
import com.youyou.uumall.business.RegisterBiz_;
import com.youyou.uumall.system.ActivityManager;
import com.youyou.uumall.system.SystemBarTintManager;
import com.youyou.uumall.utils.MyLogger;
import com.youyou.uumall.utils.MyUtils;
import com.youyou.uumall.view.CustomProgressBar;
import com.youyou.uumall.view.ToastMaster;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * @ClassName: BaseActivity
 * @Description: 所有的activity继承该类，可以将通用的东西放到该类
 */
public class BaseActivity extends FragmentActivity  {
    LoginBiz login;
    RegisterBiz register;
    public SPApplication mApp;

    public CustomProgressBar progressBar;

    /**
     * 屏幕的宽度、高度、密度
     */
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected float mDensity;

    protected MyLogger log = MyLogger.getLogger("BaseActivity");

    protected EventBus eventBus;
    //点击别的区域是否关闭键盘
    private boolean isCloseKeyboardOnClickOtherPlace = true;

    protected SystemBarTintManager tintManager;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected View statusView;

    protected void setIsCloseKeyboardOnClickOtherPlace(boolean isClose) {
        isCloseKeyboardOnClickOtherPlace = isClose;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        log.e(this.getClass().getSimpleName());
        /**
         * 屏幕长宽密度比
         */
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;

        eventBus = EventBus.getDefault();
        registerEvent();
        ActivityManager.getActivityManager().pushActivity(this);
        log = MyLogger.getLogger(this.getClass().getSimpleName());
        mApp = (SPApplication) getApplication();
        mApp.setCurrentContext(this);

        progressBar = new CustomProgressBar(this);
        progressBar.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int arg1, KeyEvent event) {

                int keyCode = event.getKeyCode();
                int action = event.getAction();
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && action == KeyEvent.ACTION_DOWN) {
                    return onProgressBarBack();
                }
                return false;
            }
        });
        PushAgent.getInstance(this).onAppStart();//umeng推送 统计应用启动数据
    }

    @Override
    protected void onStart() {
        super.onStart();
        login = LoginBiz_.getInstance_(this);
        register = RegisterBiz_.getInstance_(this);

        initStatusBar();
        String last = MyUtils.getPara(BaseConstants.preferencesFiled.LAST_LOAD, this);
        String info = MyUtils.getPara(BaseConstants.preferencesFiled.USER_INFO, this);
        String[] infos = info.split(",");
        if (!TextUtils.isEmpty(last)) {
            if (System.currentTimeMillis() - Long.valueOf(last) > 1000 * 1800) {
                register.setArrayListCallbackInterface(new BaseBusiness.ArrayListCallbackInterface() {
                    @Override
                    public void arrayCallBack(int type, List<? extends Object> arrayList) {

                    }
                });
                login.setObjectCallbackInterface(new BaseBusiness.ObjectCallbackInterface() {
                    @Override
                    public void objectCallBack(int type, Object t) {

                    }
                });
                String openId = MyUtils.getPara(BaseConstants.preferencesFiled.OPEN_ID, this);
                if (!TextUtils.isEmpty(openId)) {
                    register.wechatLogin(openId, "", MyUtils.getPara(BaseConstants.preferencesFiled.DEVICE_TOKEN, this), "3");

                } else if (!TextUtils.isEmpty(info)) {
                    String deviceToken = MyUtils.getPara(BaseConstants.preferencesFiled.DEVICE_TOKEN, this);
                    login.userLogin(infos[0], infos[1], deviceToken);
                    MyUtils.savePara(getApplication(), BaseConstants.preferencesFiled.PP_USER_ID, infos[0]);
                }
            }
        }

    }

    @Override
    protected void onStop() {
        MyUtils.savePara(this, BaseConstants.preferencesFiled.LAST_LOAD, "" + System.currentTimeMillis());
        super.onStop();
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            statusView = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    statusBarHeight);
            statusView.setLayoutParams(params);
            statusView.setBackgroundColor(getResources().getColor(R.color.font_country_conuntry));

            ViewGroup decorView = (ViewGroup) window.getDecorView();
            decorView.addView(statusView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            if (rootView != null) {
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
            }
        }
    }

    /**
     * 是否设置全屏显示内容，需要判断状态栏的高度来设置
     *
     * @param tag
     */
    public void setTintStatus(boolean tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(tag);
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(tag);
        tintManager.setNavigationBarTintEnabled(tag);
        if (!tag) {
            setTintColor();
        }
    }

    protected void setTintColor() {
        if (tintManager != null) {
            tintManager.setNavigationBarTintColor(R.color.bg_btn_login);
        }
    }

    public void setTintColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        if (tintManager == null) {
            tintManager = new SystemBarTintManager(this);
        }
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(color);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * TODO
     *
     * @see Activity#onRestart()
     */
    @Override
    protected void onResume() {
        mApp.setCurrentContext(this);
        super.onResume();
        MobclickAgent.onResume(this);//友盟统计
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);//友盟统计
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN && isCloseKeyboardOnClickOtherPlace) {
                // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {
                    hideSoftInput(v.getWindowToken());
                    onClickWindowOther();
                    ViewGroup vg = (ViewGroup) v.getParent();
                    vg.setFocusable(true);
                    vg.setFocusableInTouchMode(true);
                    vg.requestFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            log.e(e.getMessage());
            return false;
        }
    }

    /**
     * onClickWindowOther:当手指点击了窗口中其他非焦点区域的回调 <br/>
     *
     * @return void
     * @throws
     * @author Zaffy
     * @since JDK 1.6
     */
    protected void onClickWindowOther() {

    }


    /**
     * registerEvent:如果需要注册事件，请在子类覆盖该方法 <br/>
     *
     * @return void
     * @throws
     * @author Zaffy
     * @since JDK 1.6
     */
    protected void registerEvent() {

    }

    /**
     * unRegisterEvent:如果需要解绑的注册事件，请在子类覆盖该方法 <br/>
     *
     * @return void
     * @throws
     * @author Zaffy
     * @since JDK 1.6
     */
    protected void unRegisterEvent() {

    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘
     * hideSoftInputView
     *
     * @param
     * @return void
     * @throws
     * @Title: hideSoftInputView
     * @Description: TODO
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * onProgressBarBack:dialog显示过程中，监听返回键的发生 <br/>
     *
     * @return boolean
     * @throws
     * @author Zaffy
     * @since JDK 1.6
     */
    protected boolean onProgressBarBack() {
        return false;
    }

    public SPApplication getMyApplication() {
        return mApp;
    }

    /**
     * TODO
     *
     * @see FragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {

        super.onDestroy();
        unRegisterEvent();
        ActivityManager.getActivityManager().popActivity(this);
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
        }

    }

    protected void showAltertDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.hint)
                .setMessage(message)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    protected void showToastShort(String message) {
        ToastMaster.makeText(this, message, Toast.LENGTH_SHORT);
    }

    protected void showToastShort(int msgResId) {
        ToastMaster.makeText(this, msgResId, Toast.LENGTH_SHORT);
    }

    protected void showToastLong(String message) {
        ToastMaster.makeText(this, message, Toast.LENGTH_LONG);
    }

    protected void showToastLong(int msgResId) {
        ToastMaster.makeText(this, msgResId, Toast.LENGTH_LONG);
    }

    protected void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    protected void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

}
