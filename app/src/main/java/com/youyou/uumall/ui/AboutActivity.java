package com.youyou.uumall.ui;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Administrator on 2016/6/7.
 */
@EActivity(R.layout.activity_about)
public class AboutActivity extends BaseActivity {
    @Click
    void about_pro_iv() {
        finish();
    }
}
