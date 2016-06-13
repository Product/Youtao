package com.youyou.uumall.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.GuideAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.base.BaseGuideHodler;
import com.youyou.uumall.bean.FirstGuideHolder;
import com.youyou.uumall.bean.SecondGuideHolder;
import com.youyou.uumall.bean.ThirdGuideHolder;
import com.youyou.uumall.utils.MyUtils;
import com.youyou.uumall.utils.PixelUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2.
 */
@EActivity(R.layout.activity_guide)
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @ViewById
    ViewPager guide_vp;

    @ViewById
    LinearLayout guide_points_ll;

    @ViewById
    ImageView guide_redpoint_iv;

    @ViewById
    Button guide_bt;

    @Bean
    GuideAdapter guideAdapter;

    @Bean
    FirstGuideHolder firstGuideHolder;
    @Bean
    SecondGuideHolder secondGuideHolder;
    @Bean
    ThirdGuideHolder thirdGuideHolder;

    private LinearLayout.LayoutParams linearLayoutParams;
    private RelativeLayout.LayoutParams relativeLayoutParams;
    private List<BaseGuideHodler> allHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        MyUtils.savePara(mApp, BaseConstants.preferencesFiled.FIRST_LOGIN,"");// TODO: 2016/6/2 要在主页设置
        String para = MyUtils.getPara(BaseConstants.preferencesFiled.FIRST_LOGIN, mApp);
        if (!TextUtils.isEmpty(para)) {
            MainActivity_.intent(this).start();
            finish();
        }
    }

    @AfterViews
    void afterViews() {
        allHolder = new ArrayList();
        allHolder.add(firstGuideHolder);
        allHolder.add(secondGuideHolder);
        allHolder.add(thirdGuideHolder);

        for (int i = 0; i < allHolder.size(); i++) {
            ImageView poisiton = new ImageView(this);
            poisiton.setBackgroundResource(R.drawable.guide_point_normal_2x);
            int marginRight = PixelUtil.dp2px(10);
            if (i != allHolder.size()-1) {
                linearLayoutParams.setMargins(0,0,marginRight,0);
            poisiton.setLayoutParams(linearLayoutParams);
            }
            guide_points_ll.addView(poisiton);
        }
        guideAdapter.setData(allHolder);
        guide_vp.setAdapter(guideAdapter);
        guide_vp.setOnPageChangeListener(this);
        allHolder.get(0).starGif();
    }

    @Click
    void guide_bt() {
        MainActivity_.intent(this).start();
        finish();
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        log.e("position:"+position+"    positionOffset:"+positionOffset+"    positionOffsetPixels:"+positionOffsetPixels);
        int width = guide_redpoint_iv.getWidth()+PixelUtil.dp2px(10);
        relativeLayoutParams.setMargins((int) (position*width+positionOffset*width),0,0 ,0);
        guide_redpoint_iv.setLayoutParams(relativeLayoutParams);
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < allHolder.size(); i++) {
            BaseGuideHodler baseGuideHodler = allHolder.get(i);
            if (i == position) {
                baseGuideHodler.starGif();
            }else{
                baseGuideHodler.stopGif();
            }
        }
        if (position == allHolder.size()-1) {
            guide_bt.setVisibility(View.VISIBLE);
        }else {
            guide_bt.setVisibility(View.GONE);
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
