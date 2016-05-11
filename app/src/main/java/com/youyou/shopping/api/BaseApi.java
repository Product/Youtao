package com.youyou.shopping.api;


import com.youyou.shopping.api.config.AppGsonHttpMessageConverter;
import com.youyou.shopping.api.config.AppInterceptor;
import com.youyou.shopping.api.config.AppRequestFactory;
import com.youyou.shopping.base.BaseConstants;
import com.youyou.shopping.bean.Response;
import com.youyou.shopping.model.BrandBean;
import com.youyou.shopping.model.DictBean;
import com.youyou.shopping.model.GalleryBean;
import com.youyou.shopping.model.GoodsDescBean;
import com.youyou.shopping.model.RecommendBean;
import com.youyou.shopping.model.SmsCodeBean;
import com.youyou.shopping.model.UserInfoBean;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.androidannotations.api.rest.RestClientHeaders;
import org.androidannotations.api.rest.RestClientRootUrl;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;


/**
 * @Rest(converters = ??) 用来转换成接收的 json，String等数据
 * rootUrl = ？ 用来匹配链接跟路径
 * interceptors = ？　自己定义的拦截器，我们这里是拦截每一次http 请求，打印出 接口的链接地址，相应信息来
 * requestFactory = ?   配置请求参数，我们这里设置请求超时，接收数据超时
 * <p/>
 * 这些接口，他们提供的一些方法让你可以简洁你的代码
 * RestClientHeaders  提供 setHeader, getHeader, getHeader, setHeader, setAuthentication, setHttpBasicAuth and setBearerAuth 方法
 * RestClientSupport  提供 getRestTemplate和setRestTemplate 方法  (使用rest的基本方法，有了他，才可以添加各种数据转换器)
 * RestClientRootUrl  提供 getRootUrl和getRootUrl方法。 （匹配链接根路径）
 * @Accept 只能用于 @get和@post   MediaType.APPLICATION_JSON_VALUE 表示只接收json 传值
 */
@Rest(rootUrl = BaseConstants.connection.API_ROOT_URL,
        requestFactory = AppRequestFactory.class,
        converters = {FormHttpMessageConverter.class, AppGsonHttpMessageConverter.class, StringHttpMessageConverter.class},
        interceptors = {AppInterceptor.class})
@Accept(MediaType.APPLICATION_JSON_VALUE) //这里过滤出来，表示只是接受json类型的数据
public interface BaseApi extends RestClientErrorHandling, RestClientRootUrl, RestClientSupport, RestClientHeaders {
    /*主页面*/
    @Post("/mall/slider.json")
    Response<List<GalleryBean>> getSliderData();

    @Post("/mall/recommend.json?mall/recommend.json")
    Response<List<RecommendBean>> getRecommendData(Map map);

    @Post("/mall/brand.json")
    Response<List<BrandBean>> getBrandData(Map map);

    /*用户注册*/
    @Post("/malluser/sendSMSCode.json")
    Response<Object> getSMSCode(Map map);

    @Post("/malluser/userRegister.json")
    Response<Object> userRegister(Map map);

    /*用户登录*/
    @Post("/malluser/userLogin.json")
    Response<UserInfoBean> userLogin(Map map);

    /*重置密码*/
    @Post("/malluser/retrievePassword.json")
    Response<Object> retrievePassword(Map map);

    /*查询字典*/
    @Post("/sys/queryDict.json")
    Response<List<DictBean>> queryDict(Map map);

    /*商品详情*/
    @Post("/mall/queryGoodsById.json")
    Response<GoodsDescBean> queryGoodsById(Map map);

    /*商品详情*/
    @Post("/mall/cartItemMod.json")
    Response<GoodsDescBean> updatecart(Map map);
}