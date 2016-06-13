package com.youyou.uumall.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.youyou.uumall.base.BaseGuideHodler;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by Administrator on 2016/6/2.
 */
@EBean
public class GuideAdapter extends PagerAdapter {
    @RootContext
    Context context;
    private List<BaseGuideHodler> mData;

    //    int[] gifRes = {R.drawable.newfeature_page_0,R.drawable.newfeature_page_1,R.drawable.newfeature_page_2};
//    String[] text_0 = {"我的海淘是送到机场","代购省事,友谊的小船才不翻",""};
//    String[] text_1 = {"油桃可以把您的宝贝送到回国机场,\n没转运才低价","小伙伴线上下单,我只要机场代提\n没烦恼,才能任性玩",""};
    public void setData(List<BaseGuideHodler> Data) {
        this.mData=Data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mData.get(position).getView();
//        GifImageView item_guide_giv = ViewHolder.get(view, R.id.item_guide_giv);
//        TextView item_guide_tv_0 = ViewHolder.get(view, R.id.item_guide_tv_0);
//        TextView item_guide_tv_1 = ViewHolder.get(view, R.id.item_guide_tv_1);
//        item_guide_giv.setBackgroundResource(gifRes[position]);
//        item_guide_tv_0.setText(text_0[position]);
//        item_guide_tv_1.setText(text_1[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
