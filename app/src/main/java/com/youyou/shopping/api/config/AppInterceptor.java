package com.youyou.shopping.api.config;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.youyou.shopping.base.BaseConstants;
import com.youyou.shopping.utils.MyLogger;
import com.youyou.shopping.utils.MyUtils;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * User: Mrljdx(mrljdx@qq.com)
 * Date: 2015-06-17
 * Time: 12:00
 */
@EBean
public class AppInterceptor implements ClientHttpRequestInterceptor {

    @RootContext
    Context context;

    private MyLogger log = MyLogger.getLogger("AppErrorHandler");
    private static final String SET_COOKIE = "set-cookie";
    public static final String COOKIE = "cookie";
    private static final String COOKIE_STORE = "cookieStore";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders header = request.getHeaders();//请求头
        List<String> requestCookies = header.get(COOKIE);
        // if the header doesn't exist, add any existing, saved cookies
        if (requestCookies == null) {
            String cookie = MyUtils.getPara(COOKIE, context);
            header.add(COOKIE, cookie);
            log.e("cookie is add   "+cookie);
        }
        String requestUrl = request.getURI().toString();//请求url
        String bodyStr = (body == null ? "" : new String(body, "UTF-8"));
        log.e("\nRequest Url is:" + requestUrl + "\nMethod is : " + request.getMethod() + "\nheader is:" + header + "\nRequest Body is:" + bodyStr + "\n");

        //reponse
        ClientHttpResponse reponse = execution.execute(request, body);
        HttpHeaders headers = reponse.getHeaders();
        List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);
        if (cookies != null) {
            String cookie = cookies.get(0);
            if (cookie.startsWith("_SESSIONID")) {
                MyUtils.savePara(context, COOKIE, cookie);
                log.e("cookie is save   "+cookie);
            }
        }
        if (reponse != null) {
            log.d("response is not null");
        } else {
            log.e("response is null");
        }

        return reponse;
    }
}
