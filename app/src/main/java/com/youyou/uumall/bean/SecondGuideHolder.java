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
public class SecondGuideHolder extends BaseGuideHodler {

    @RootContext
    Context mContext;
    private GifDrawable drawable;

    public SecondGuideHolder(Context mContext) {
        super(mContext);
    }


    @Override
    public void starGif() {
        try {
//            log.e("2开始");
            drawable = new GifDrawable(mContext.getResources(), R.drawable.newfeature_page_1);
            item_guide_giv.setBackground(drawable);
            drawable.setLoopCount(0);
        } catch (IOException e) {
//            log.e("2错误");
            e.printStackTrace();
        }
    }

    @Override
    public void stopGif() {
        if (drawable!=null){
//            log.e("2结束");
            drawable.reset();
            drawable.stop();
        }
    }

    @Override
    public void setData() {
        item_guide_tv_0.setText("代购省事,友谊的小船才不翻");
        item_guide_tv_1.setText("小伙伴线上下单,我只要机场代提\n没烦恼,才能任性玩");
    }
}
