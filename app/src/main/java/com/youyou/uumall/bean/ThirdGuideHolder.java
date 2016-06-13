package com.youyou.uumall.bean;

import android.content.Context;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseGuideHodler;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by Administrator on 2016/6/8.
 */
@EBean
public class ThirdGuideHolder extends BaseGuideHodler {

    @RootContext
    Context mContext;
    private GifDrawable drawable;

    public ThirdGuideHolder(Context mContext) {
        super(mContext);
    }


    @Override
    public void starGif() {
        try {
            drawable = new GifDrawable(mContext.getResources(), R.drawable.newfeature_page_2);
            item_guide_giv.setBackground(drawable);
            drawable.setLoopCount(1);
//            log.e("3开始");
        } catch (IOException e) {
//            log.e("3错误");
            e.printStackTrace();
        }
    }

    @Override
    public void stopGif() {
        if (drawable!=null){
//            log.e("3结束");
            drawable.reset();
            drawable.stop();
        }
    }

    @Override
    public void setData() {
        item_guide_tv_0.setText("");
        item_guide_tv_1.setText("");
    }
}
