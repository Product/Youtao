package com.youyou.uumall.ui;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.bean.MethodCalledJs;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2016/6/15.
 */
@EActivity(R.layout.activity_web)
public class WebActivity extends BaseActivity {
    @ViewById
    WebView wv;

    private String href;
    private boolean isHide;

    @ViewById
    TextView web_tittle_tv;

    @AfterViews
    void afterViews() {
        log.e(Thread.currentThread());
        Intent intent = getIntent();
        href = intent.getStringExtra("href");
        if (!TextUtils.isEmpty(href)) {
            wv.loadUrl(href);
            MyUtils.setWebViewSetting(wv, this);
            wv.setWebViewClient(new TempWebViewClient());
            wv.setWebChromeClient(new TempWebChromeClient());
            wv.addJavascriptInterface(new MethodCalledJs(this), "JavaScriptInterface");
        }
    }

    class TempWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            web_tittle_tv.setText(title);
        }

//        @Override
//        public boolean onConsoleMessage(ConsoleMessage cm) {
//            return true;
//        }
//
//        @Override
//        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//            return true;
//        }
    }

    class TempWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            log.e(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            isHide = false;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            if (!isHide) {
                loadJs(view);
                isHide = true;
            }
        }
    }

    @UiThread
    public void loadJs(WebView view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.loadUrl("javascript:hideHeader()");
            view.evaluateJavascript("hideHeader()", new ValueCallback<String>() {

                @Override
                public void onReceiveValue(String value) {
                    log.e("-----------------------");
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wv.canGoBack()) {
                wv.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Click
    void web_pro_iv() {
        if (wv.canGoBack()) {
            wv.goBack();
            return;
        }
        finish();
    }
}
