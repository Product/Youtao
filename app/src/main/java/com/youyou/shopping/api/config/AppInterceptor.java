package com.youyou.shopping.api.config;

import android.content.Context;


import com.youyou.shopping.utils.MyLogger;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;


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

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        HttpHeaders header = request.getHeaders();//请求头
        String requestUrl = request.getURI().toString();//请求url
        String bodyStr = (body == null ? "" : new String(body, "UTF-8"));
        log.e("Request Url is:" + requestUrl + "\nMethod is : " + request.getMethod() + "\nRequest Body is:" + bodyStr + "\n");

        ClientHttpResponse reponse = execution.execute(request, body);
        if (reponse != null) {
            log.d("response is not null");
        } else {
            log.e("response is null");
        }

        return reponse;
    }
}
