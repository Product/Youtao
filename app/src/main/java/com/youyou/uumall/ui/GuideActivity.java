package com.youyou.uumall.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ant.liao.GifView;
import com.youyou.uumall.R;
import com.youyou.uumall.adapter.GuideAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2016/6/2.
 */
@EActivity(R.layout.activity_guide)
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @ViewById
    ViewPager guide_vp;

    @ViewById
    Button guide_bt;

    @ViewById
    LinearLayout guide_points_ll;

    @ViewById
    ImageView guide_redpoint_iv;

    @Bean
    GuideAdapter guideAdapter;

    @ViewById
    GifView guide_gv;

    private int[] mData={R.drawable.newfeature_page_0_0_0,R.drawable.newfeature_page_1,R.drawable.newfeature_page_2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MyUtils.savePara(mApp, BaseConstants.preferencesFiled.FIRST_LOGIN,"");// TODO: 2016/6/2 要在主页设置
        String para = MyUtils.getPara(BaseConstants.preferencesFiled.FIRST_LOGIN, mApp);
        if (!TextUtils.isEmpty(para)) {
            MainActivity_.intent(this).start();
        }
        MainActivity_.intent(this).start();
        finish();
    }

    @AfterViews
    void afterViews() {
//        guide_vp.setAdapter(guideAdapter);
//        guide_vp.setOnPageChangeListener(this);
//        guide_gv.setGifImage(mData[0]);
//        guide_gv.setGifImageType(GifView.GifImageType.WAIT_FINISH);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        guide_gv.setGifImage(mData[position]);
        guide_gv.setGifImageType(GifView.GifImageType.SYNC_DECODER);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
