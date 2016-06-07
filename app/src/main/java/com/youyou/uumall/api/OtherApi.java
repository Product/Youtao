package com.youyou.uumall.api;

import com.youyou.uumall.api.config.AppGsonHttpMessageConverter;
import com.youyou.uumall.api.config.AppInterceptor;
import com.youyou.uumall.api.config.AppRequestFactory;
import com.youyou.uumall.bean.Response;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.androidannotations.api.rest.RestClientHeaders;
import org.androidannotations.api.rest.RestClientRootUrl;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

/**
 * Created by Administrator on 2016/5/26.
 */
@Rest(  requestFactory = AppRequestFactory.class,
        converters = {FormHttpMessageConverter.class, AppGsonHttpMessageConverter.class, StringHttpMessageConverter.class},
        interceptors = {AppInterceptor.class})
@Accept(MediaType.APPLICATION_JSON_VALUE)
public interface OtherApi extends RestClientErrorHandling, RestClientRootUrl, RestClientSupport, RestClientHeaders {

    /*主页面*/
    //"https://api.weixin.qq.com/sns/userinfo?access_token=%@&openid=%@",token, openId
    @Get("https://api.weixin.qq.com/sns/oauth2/userinfo?access_token={access_token}&openid={openid}")
    Response<Object> getAccessToken(String access_token,String openid);
}
