package com.youyou.shopping.api.config;


import com.youyou.shopping.base.BaseConstants;
import com.youyou.shopping.utils.MyLogger;

import org.androidannotations.annotations.EBean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * User: Mrljdx(mrljdx@qq.com)
 * Date: 2015-06-17
 * Time: 12:06
 */
@EBean
public class AppRequestFactory extends SimpleClientHttpRequestFactory {

    private MyLogger log = MyLogger.getLogger("AppRequestFactory");
    private static RestTemplate restTemplate;

    public AppRequestFactory() {
        super();
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
//        restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory();
        ClientHttpRequestFactory requestFactory = restTemplate.getRequestFactory();
        /**
         * 这里设置请求超时等,如果超时，会抛出异常到AppErrorHandler捕获
         */
        if (requestFactory instanceof SimpleClientHttpRequestFactory) {
//            log.e("HttpUrlConnection is used");

            ((SimpleClientHttpRequestFactory) requestFactory).setConnectTimeout(BaseConstants.connection.TIMEOUT);
            ((SimpleClientHttpRequestFactory) requestFactory).setReadTimeout(BaseConstants.connection.TIMEOUT);
        } else if (requestFactory instanceof HttpComponentsClientHttpRequestFactory) {
//            log.e("HttpClient is used");
            ((HttpComponentsClientHttpRequestFactory) requestFactory).setReadTimeout(BaseConstants.connection.TIMEOUT);
            ((HttpComponentsClientHttpRequestFactory) requestFactory).setConnectTimeout(BaseConstants.connection.TIMEOUT);
        }
    }

}
