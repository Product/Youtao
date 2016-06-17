package com.youyou.uumall.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
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

    @AfterViews
    void afterViews() {
        Intent intent = getIntent();
        href = intent.getStringExtra("href");
        if (!TextUtils.isEmpty(href)) {
            wv.loadUrl(href);
            wv.setWebViewClient(new TextWebViewClient());

            log.e(href);
        }
    }
    class TextWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return super.shouldOverrideUrlLoading(view, url);
            view.loadUrl(url);
            log.e(url);
            return true;
        }
    }
}
