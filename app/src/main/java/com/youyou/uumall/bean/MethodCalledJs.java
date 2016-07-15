package com.youyou.uumall.bean;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.youyou.uumall.ui.CommodityDescActivity_;
import com.youyou.uumall.utils.MyLogger;

/**
 * Created by Administrator on 2016/7/14.
 */
public class MethodCalledJs {
    Context mContext;
    private final MyLogger log;

    public MethodCalledJs(Context context) {
        log = MyLogger.getLogger("js");
        mContext = context;
    }

    @JavascriptInterface
    public String getDeviceType() {
        return "android";
    }

    @JavascriptInterface
    public void jumpToCommodityDesc(String id) {
        log.e("转跳页面,id为:"+id);
        Intent intent = new Intent(mContext, CommodityDescActivity_.class);
        intent.putExtra("goodsId", id);
        mContext.startActivity(intent);
    }
}
