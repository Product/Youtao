package com.youyou.uumall.bean;

import android.content.Context;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseGuideHodler;

import org.androidannotations.annotations.EBean;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by Administrator on 2016/6/8.
 */
@EBean
public class FirstGuideHolder extends BaseGuideHodler {


    private GifDrawable drawable;

    public FirstGuideHolder(Context mContext) {
        super(mContext);
    }


    @Override
    public void starGif() {
        try {
//            log.e("1开始");
            drawable = new GifDrawable(mContext.getResources(), R.drawable.newfeature_page_0);
            item_guide_giv.setBackground(drawable);
            drawable.setLoopCount(1);
        } catch (IOException e) {
//            log.e("1错误");
            e.printStackTrace();
        }
    }

    @Override
    public void stopGif() {
        if (drawable!=null){
//            log.e("1结束");
            drawable.reset();
        drawable.stop();
        }
    }

    @Override
    public void setData() {
        item_guide_tv_0.setText("我的海淘是送到机场");
        item_guide_tv_1.setText("油桃可以把您的宝贝送到回国机场,\n没转运才低价");
    }
}
