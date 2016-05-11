package com.youyou.shopping.api.config;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


import com.youyou.shopping.utils.MyLogger;
import com.youyou.shopping.view.ToastMaster;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;


/**
 */
@EBean
public class AppErrorHandler implements RestErrorHandler {

    @RootContext
    Context rootContext;

    private MyLogger log = MyLogger.getLogger("AppErrorHandler");
    private String requestUrl = "";

    /**
     * 这里捕获抛出的异常，做相应处理
     * @param e
     */
    @Override
    public void onRestClientExceptionThrown(NestedRuntimeException e) {
        showRestClientError(e.getMessage());
    }

    @UiThread
    protected void showRestClientError(String errorMessage) {
        log.e(requestUrl + "AppErrorHandler:" + errorMessage);
//        if (rootContext != null && rootContext instanceof Activity) {
//            ((Activity) rootContext).progressBar.dismiss();
//        }
//       ToastMaster.makeText(rootContext, errorMessage, Toast.LENGTH_SHORT);
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
