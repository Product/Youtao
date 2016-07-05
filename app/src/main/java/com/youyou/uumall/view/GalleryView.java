package com.youyou.uumall.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.model.GalleryBean;
import com.youyou.uumall.ui.WebActivity_;
import com.youyou.uumall.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GalleryView extends RelativeLayout implements
        ViewPager.OnPageChangeListener {

    private Context mContext;
    private List<GalleryBean> mViews = new ArrayList<>();
    private List<View> mGalleryDotViewArray = new ArrayList<>();
    private List<ImageView> mGalleryImageViewArray = new ArrayList<>();
    private ViewPager mViewPager;
    private GalleryPagerAdapter mGalleryPagerAdapter;
    private View mGallery;
    private LinearLayout mGalleryDotLayout;
    private int mGalleryNumber;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private long mShowTime = 3000;
    private DisplayImageOptions options;

    public GalleryView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mGallery = LayoutInflater.from(mContext).inflate(R.layout.layout_gallery,
                null, true);
        // 幻灯片图片
        mViewPager = (ViewPager) mGallery.findViewById(R.id.view_pager);
        mViewPager.setOnPageChangeListener(this);
        mGalleryDotLayout = (LinearLayout) mGallery
                .findViewById(R.id.gallery_dot);
        addView(mGallery, 0);
        options = MyUtils.getImageOptions();
    }

    public void setParams(List<GalleryBean> list) {
        mGalleryNumber = list.size();

        int num = mGalleryNumber + 2;

        mViews = new ArrayList<GalleryBean>();

        /**
         * mGalleryData[0]放list[N-1], mGalleryData[i]放list[i-1],
         * mGalleryData[N+1]放list[0]
         * mGalleryData[1~N]用于循环；首位之前的mViews[0]和末尾之后的mGalleryData[N+1]用于跳转
         * 首位之前的mGalleryData[0]，跳转到末尾（N）；末位之后的mGalleryData[N+1]，跳转到首位（1）
         */
        if (mGalleryNumber!=0){
            for (int i = 0; i < num; i++) {
                if (i == 0) {
                    mViews.add(list.get(mGalleryNumber - 1));
                } else if (i == num - 1) {
                    mViews.add(list.get(0));
                } else {
                    mViews.add(list.get(i - 1));
                }
            }
        }


        initDot();
        initGallery();
    }

    private void initDot() {
        // 幻灯片点
        mGalleryDotLayout.removeAllViews();
        mGalleryDotViewArray = new ArrayList<View>();

        for (int i = 0; i < mGalleryNumber; i++) {
            View dotView = new View(mContext);
            if (i == 0) {
                dotView.setBackgroundResource(R.drawable.dot_focused_ad);
            } else {
                dotView.setBackgroundResource(R.drawable.dot_normal_ad);
            }
            android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    10, 10);
            params.setMargins(7, 4, 0, 7);
            dotView.setLayoutParams(params);
            mGalleryDotLayout.addView(dotView);
            mGalleryDotViewArray.add(dotView);
        }
    }

    private void setDotIndex(int position){
        for(int i = 0; i<mGalleryDotViewArray.size(); i++){
            if(i == position){
                mGalleryDotViewArray.get(i).setBackgroundResource(R.drawable.dot_focused_ad);
            }else {
                mGalleryDotViewArray.get(i).setBackgroundResource(R.drawable.dot_normal_ad);
            }
        }
    }

    private void initGallery() {
        mGalleryImageViewArray = new ArrayList<ImageView>();

        for (int i = 0; i < mViews.size(); i++) {
            ImageView galleryImageView = new ImageView(mContext);
            galleryImageView.setTag(i);
            galleryImageView.setScaleType(ScaleType.FIT_XY);
            galleryImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String href = mViews.get((Integer) v.getTag()).href;
                    if (!TextUtils.isEmpty(href)) {
                    Intent intent = new Intent(mContext, WebActivity_.class);
                    intent.putExtra("href", href);
                    mContext.startActivity(intent);
                    }
                }
            });

            galleryImageView.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            stopSilde();
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            startSilde();
                            break;
                        case MotionEvent.ACTION_MOVE:
                        default:
                            break;
                    }
                    return false;
                }
            });

            ImageLoader.getInstance().displayImage(BaseConstants.connection.ROOT_URL + mViews.get(i).location,
                    galleryImageView,options);

            mGalleryImageViewArray.add(galleryImageView);
        }

        mGalleryPagerAdapter = new GalleryPagerAdapter();
        mViewPager.setAdapter(mGalleryPagerAdapter);

        startSilde();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

        if(arg2 == 0){
            // 拉到下方会出现mPageIndex一直增长的情况
            if (mPageIndex > mViews.size()) {
                mPageIndex = 0;
            }

            if (mPageIndex == 0) {
                mPageIndex = mGalleryNumber;
                mViewPager.setCurrentItem(mPageIndex, false);
            } else if (mPageIndex == mViews.size() - 1 || mPageIndex == mViews.size()) {
                mPageIndex = 1;
                mViewPager.setCurrentItem(mPageIndex, false);
            } else {
                mViewPager.setCurrentItem(mPageIndex);
            }
        }

        setDotIndex(mPageIndex - 1);
    }

    @Override
    public void onPageSelected(int position) {

        mPageIndex = position;
    }

    private int mPageIndex = 0;

    public class GalleryPagerAdapter extends PagerAdapter {

        public GalleryPagerAdapter() {

        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {

            ((ViewPager) arg0).addView(mGalleryImageViewArray.get(arg1));
            return mGalleryImageViewArray.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
    //定时发送数据
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
//                    int count = mGalleryNumber;
//                    if (count > 2) { // 实际上，多于1个，就多于3个
                        int index = mViewPager.getCurrentItem();
                        index++;
                        mViewPager.setCurrentItem(index, true);
//                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
//    开始轮播
    public void startSilde() {

        if (mTimer != null) {
            stopSilde();
        }

        mTimer = new Timer();
        mTimerTask = new TimerTask() {

            @Override
            public void run() {

                Message msg = new Message();
                msg.what = 100;
                mHandler.sendMessage(msg);
            }

        };

        mTimer.schedule(mTimerTask, mShowTime, mShowTime);// 使用了定时调度
    }
    //停止轮播
    public void stopSilde() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

}
