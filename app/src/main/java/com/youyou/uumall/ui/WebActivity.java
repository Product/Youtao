package com.youyou.uumall.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2016/6/15.
 */
@EActivity(R.layout.activity_web)
public class WebActivity extends BaseActivity{
    @ViewById
    WebView wv;
    private String href;

    @ViewById
    TextView web_tittle_tv;

    @AfterViews
    void afterViews() {
        Intent intent = getIntent();
        href = intent.getStringExtra("href");
        if (!TextUtils.isEmpty(href)) {
            wv.loadUrl(href);
            MyUtils.setWebViewSetting(wv);
            wv.setWebViewClient(new TempWebViewClient());
            wv.setWebChromeClient(new TempWebChromeClient());
        }
    }

    class TempWebChromeClient extends WebChromeClient{
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            web_tittle_tv.setText(title);
        }
    }

    class TempWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            log.e(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(wv.canGoBack())
            {
                wv.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Click
    void web_pro_iv() {
        if(wv.canGoBack())
        {
            wv.goBack();
            return;
        }
        finish();
    }
}
