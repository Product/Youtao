package com.youyou.shopping.api.config;

import org.androidannotations.annotations.EBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

/**
 * 解决Gson转换问题
 */
@EBean
public class AppGsonHttpMessageConverter extends GsonHttpMessageConverter {

    /**
     * TODO
     * @see GsonHttpMessageConverter#canRead(Class, MediaType)
     */
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return true;
    }
}
