package com.youyou.shopping.base;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;


import com.youyou.shopping.api.BaseApi;
import com.youyou.shopping.api.config.AppErrorHandler;
import com.youyou.shopping.bean.Response;
import com.youyou.shopping.utils.MyLogger;
import com.youyou.shopping.utils.UserUtils;
import com.youyou.shopping.view.ToastMaster;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import java.util.List;


@EBean
public class BaseBusiness {

    @RootContext
    protected Context rootContext;

    @RestService
    protected BaseApi baseApi;

    @Bean
    protected AppErrorHandler errorHandle;

    @Bean
    protected UserUtils userUtils;

    protected String tokenStr;

    protected ObjectCallbackInterface objectCallbackInterface;

    protected ArrayListCallbackInterface arrayListCallbackInterface;

    protected MyLogger log = MyLogger.getLogger("BaseBusiness");

    @AfterViews
    protected void initRestErrorHandler() {
        baseApi.setRestErrorHandler(errorHandle);
        tokenStr = userUtils.getAccessToken();
    }

    /**
     * 设置errorHandler中报错的URL地址
     */
    protected void setErrorHandlerUrl(String requestUrl){
        if(errorHandle!=null){
            errorHandle.setRequestUrl(requestUrl);
        }
    }

    /**
     * object值的回调
     *
     * @author dean
     */
    public interface ObjectCallbackInterface {

        /**
         * @param type 同一个页面不同的逻辑
         * @param t    通过business处理好的结果类
         */
        void objectCallBack(int type, Object t);

    }

    /**
     * 数组格式的回调接口
     *
     * @author dean
     */
    public interface ArrayListCallbackInterface {
        void arrayCallBack(int type, List<? extends Object> arrayList);
    }


    /**
     * 构造business的时候需要同时初始化需要回调的接口
     *
     * @param objectCallbackInterface
     */
    public void setObjectCallbackInterface(ObjectCallbackInterface objectCallbackInterface) {
        this.objectCallbackInterface = objectCallbackInterface;
    }

    public void setArrayListCallbackInterface(ArrayListCallbackInterface arrayListCallbackInterface) {
        this.arrayListCallbackInterface = arrayListCallbackInterface;
    }

    /**
     * 验证服务器返回的数据是否为空或者其他异常,默认关闭对话框
     *
     * @param response
     * @return
     */
    public <G> G handleResponse(Response<G> response) {
        return handleResponse(response, true);
    }

    /**
     * 验证服务器返回的数据是否为空或者其他异常
     *
     * @param response
     * @param isNeedHandleDialog 是否在验证完后关闭加载对话框
     * @return
     */
    public <G> G handleResponse(Response<G> response, boolean isNeedHandleDialog) {
        dismissDialog(isNeedHandleDialog);
        if (response == null) {
            log.e("response is null,net work error");
            return null;
        }

        if (response.code != 0) {
            if (response.msg != null) {
                showErrorMessage(response.msg);
            }
            return null;
        }
        return response.data;
    }

//    public Response handleResponse(Response response){
//
//        return null;
//    }

    @UiThread
    public void showErrorMessage(Object message) {
        if (message instanceof String) {
            String messageStr = message.toString();
      //      log.e(messageStr);
            //暂时屏蔽为空显示
            ToastMaster.makeText(rootContext, messageStr, Toast.LENGTH_SHORT);
        }
    }

    @UiThread
    protected void dismissDialog(boolean isNeedHandleDialog) {
        if (rootContext != null && rootContext instanceof BaseActivity && isNeedHandleDialog) {
    //        ((BaseActivity) rootContext).progressBar.dismiss();
        }
    }

    protected void showToastLong(String message) {
        Looper.prepare();
        ToastMaster.makeText(rootContext, message, Toast.LENGTH_LONG);
        Looper.loop();
    }

    protected void showToastLong(int msgResId) {
        showToastLong(rootContext.getString(msgResId));
    }

    protected void showToastShort(String message) {
        Looper.prepare();
        ToastMaster.makeText(rootContext, message, Toast.LENGTH_LONG);
        Looper.loop();
    }

    protected void showToastShort(int msgResId) {
        showToastLong(rootContext.getString(msgResId));
    }

    protected boolean isTokenEmpty() {
        tokenStr = userUtils.getAccessToken();
        log.e("tokenStr is :" + tokenStr);
        if (TextUtils.isEmpty(tokenStr)) {
            dismissProgressBar();
            showToastLong("token is empty");
            return false;
        }
        return true;
    }

    @UiThread
    protected void dismissProgressBar() {
        if (rootContext != null && rootContext instanceof BaseActivity) {
     //       ((BaseActivity) rootContext).progressBar.dismiss();
        }
    }
}
