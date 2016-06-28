package com.youyou.uumall.api;


import com.youyou.uumall.api.config.AppGsonHttpMessageConverter;
import com.youyou.uumall.api.config.AppInterceptor;
import com.youyou.uumall.api.config.AppRequestFactory;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.model.BonusBean;
import com.youyou.uumall.model.BrandBean;
import com.youyou.uumall.model.CategoryBean;
import com.youyou.uumall.model.DictBean;
import com.youyou.uumall.model.GalleryBean;
import com.youyou.uumall.model.GoodsDescBean;
import com.youyou.uumall.model.GoodsPrice;
import com.youyou.uumall.model.OrderBean;
import com.youyou.uumall.model.RecommendBean;
import com.youyou.uumall.model.ShopCartBean;
import com.youyou.uumall.model.UserInfoBean;
import com.youyou.uumall.model.WxPrepayOrderBean;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.androidannotations.api.rest.RestClientHeaders;
import org.androidannotations.api.rest.RestClientRootUrl;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

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
    Response<List<UserInfoBean>> userLogin(Map map);

    /*登录测试*/
    @Post("/malluser/userLogin.json")
    Object textLogin(Map map);

    /*重置密码*/
    @Post("/malluser/retrievePassword.json")
    Response<Object> retrievePassword(Map map);

    /*查询字典*/
    @Post("/sys/queryDict.json")
    Response<List<DictBean>> queryDict(Map map);

    /*商品详情*/
    @Post("/mall/queryGoodsById.json")
    Response<GoodsDescBean> queryGoodsById(Map map);

    /*添加购物车*/
    @Post("/mall/cartItemMod.json")
    Response<Object> updatecart(Map map);

    /*商品分类*/
    @Post("/mall/category.json")
    Response<List<CategoryBean>> queryCategory(Map map);
//    Response<List<Object>> queryCategory(Map map);

    /*关键字搜索商品*/
    @Post("/mall/queryGoodsByKeywords.json")
    Response<List<GoodsDescBean>> queryGoodsByKeywords(Map map);

    /*购物车查询*/
    @Post("/mall/cartItemList.json")
    Response<List<ShopCartBean>> getcartList(Map map);

    /*购物车删除*/
    @Post("/mall/cartItemDel.json")
    Response<Object> cartItemDel(Map map);//废弃

    /*自提点查询*/
    @Post("/sys/queryDelivery.json")
    Response<List<DictBean>> queryDelivery(Map map);

    /*提交订单*/
    @Post("/mall/order/orderSubmit.json")
    Response<Object> orderSubmit(Map map);

    /*预处理微信订单*/
    @Post("/mall/createWxPrepayOrder.json")
    Response<List<WxPrepayOrderBean>> createWxPrepayOrder(Map map);
//    Response<List<WxPrepayOrderBean>> createWxPrepayOrder(Map map);

    /*查询订单*/
    @Post("/mall/order/queryOrder.json")
//    Response<Object> queryOrder(Map map);
    Response<List<OrderBean>> queryOrder(Map map);

    /*取消订单*/
    @Post("/mall/order/cancel.json")
    Response<Object> cancelOrder(Map map);

    /*微信注册*/
    @Post("/malluser/wechatLogin.json")
    Response<List<UserInfoBean>> wechatLogin(Map map);

    /*绑定手机*/
    @Post("/malluser/mobileBinding.json")
    Response<Object> mobileBinding(Map map);

    /*退出登录*/
    @Post("/malluser/logout.json")
    Response<Object> logout();

    /*红包信息查询*/
    @Post("/mall/queryBonus.json")
    Response<List<BonusBean>> queryBonus();

    /*品牌搜索商品接口*/
    @Post("/mall/queryGoodsByBrand.json")
    Response<List<GoodsDescBean>> queryGoodsByBrand(Map map);

    /*品牌搜索商品接口*/
    @Post("/mall/queryGoodsByCategory.json")
    Response<List<GoodsDescBean>> queryGoodsByCategory(Map map);
//    Response<List<GoodsDescBean>> queryGoodsByCategory(Map map);

    /*品牌搜索商品接口*/
    @Post("/mall/updateGoodsPrice.json")
    Response<List<GoodsPrice>> updateGoodsPrice(Map map);
//    Response<Object> updateGoodsPrice(Map map);
//
// /*品牌搜索商品接口*/
    @Post("/malluser/modUserInfo.json")
//    Response<List<GoodsPrice>> modUserInfo(Map map);
    Response<Object> modUserInfo(Map map);
}