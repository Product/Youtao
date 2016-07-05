package com.youyou.uumall.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.base.BaseFragment;
import com.youyou.uumall.business.CategoryDescBiz;
import com.youyou.uumall.model.GalleryBean;
import com.youyou.uumall.model.GoodsDescBean;
import com.youyou.uumall.model.GoodsPrice;
import com.youyou.uumall.utils.MyUtils;
import com.youyou.uumall.view.GalleryView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
@EFragment(R.layout.fragment_cate_desc)
public class CategoryDescFragment extends BaseFragment implements BaseBusiness.ObjectCallbackInterface, BaseBusiness.ArrayListCallbackInterface {

    @ViewById
    TextView cate_frag_name;
    @ViewById
    TextView cate_frag_price;
    @ViewById
    TextView cate_frag_price1;
    @ViewById
    TextView cate_frag_price2;
    @ViewById
    GalleryView cate_frag_vp;
    @ViewById
    LinearLayout cate_frag_ll;

    @ViewById
    LinearLayout cate_frag_price_ll;

    @Bean
    CategoryDescBiz categoryDescBiz;
    private FragmentActivity activity;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private int galleryHeight;

    @AfterViews
    void afterViews() {
        imageLoader = ImageLoader.getInstance();
        DisplayMetrics metric =  new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        galleryHeight = (int) (mScreenWidth / 1.28);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mScreenWidth, galleryHeight);
        cate_frag_vp.setLayoutParams(params);
        cate_frag_vp.invalidate();
        options = MyUtils.getImageOptions();
        activity = getActivity();
        Bundle bundle = getArguments();
        String goodsId = bundle.getString(BaseConstants.preferencesFiled.GOODS_ID);
        //访问网络的操作
        categoryDescBiz.setObjectCallbackInterface(this);
        categoryDescBiz.setArrayListCallbackInterface(this);
        categoryDescBiz.queryGoodsById(goodsId);
        categoryDescBiz.updateGoodsPrice(goodsId);
    }

    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (CategoryDescBiz.QUERY_GOODS_BY_ID == type) {
            GoodsDescBean bean = (GoodsDescBean) t;
//            log.e(bean.toString());
            initData(bean);
        }
    }

    private void initData(GoodsDescBean bean) {
        if (bean != null) {
            cate_frag_name.setText(bean.brandName);
            cate_frag_price.setText("￥"+bean.price);
            String description = bean.description;
            String[] pics = description.split("\\|");
            for (String
                    pic : pics) {
                ImageView imageView = new ImageView(activity);
                String uRl = MyUtils.formatURl(pic);
                imageLoader.displayImage(BaseConstants.connection.ROOT_URL + uRl, imageView,options,listener);
                cate_frag_ll.addView(imageView);
            }
            String image = bean.image;
            String[] gallery = image.split("\\|");
            List<GalleryBean> list = new ArrayList<>();
            for (int i = 0; i < gallery.length; i++) {
                GalleryBean galleryBean = new GalleryBean();
                galleryBean.location = gallery[i];
                list.add(galleryBean);
            }
            cate_frag_vp.setParams(list);
        }
    }

    private  SimpleImageLoadingListener listener = new SimpleImageLoadingListener(){
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            super.onLoadingComplete(imageUri, view, loadedImage);
            int width = loadedImage.getWidth();
            int height = loadedImage.getHeight();
            double scale = (double)height/width;
            ImageView imageView = (ImageView) view;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mScreenWidth, (int) (mScreenWidth*scale));
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    };

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (CategoryDescBiz.UPDATE_GOODS_PRICE == type){
            if (arrayList != null && arrayList.size() != 0) {
                cate_frag_price_ll.setVisibility(View.VISIBLE);
                List<GoodsPrice> list = (List<GoodsPrice>) arrayList;
                GoodsPrice goodsPrice1 = list.get(0);
                if (list.size() == 1) {
                    cate_frag_price1.setText(goodsPrice1.name+":￥"+goodsPrice1.priceCny);
                    cate_frag_price1.setVisibility(View.VISIBLE);
                    cate_frag_price2.setVisibility(View.GONE);
                } else if (list.size() == 2) {
                GoodsPrice goodsPrice2 = list.get(1);
                    cate_frag_price1.setText(goodsPrice1.name+":￥"+goodsPrice1.priceCny);
                    cate_frag_price2.setText(goodsPrice2.name+":￥"+goodsPrice2.priceCny);
                    cate_frag_price1.setVisibility(View.VISIBLE);
                    cate_frag_price2.setVisibility(View.VISIBLE);
                }
            } else {
                cate_frag_price_ll.setVisibility(View.GONE);
            }
        }
    }
}
