package com.youyou.uumall.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.adapter.CommodityAdapter;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.base.BaseFragment;
import com.youyou.uumall.business.AddressBiz;
import com.youyou.uumall.business.CommodityBiz;
import com.youyou.uumall.event.CountryCallbackEvent;
import com.youyou.uumall.event.ShopCartUpdateEvent;
import com.youyou.uumall.model.BrandBean;
import com.youyou.uumall.model.DictBean;
import com.youyou.uumall.model.GalleryBean;
import com.youyou.uumall.model.RecommendBean;
import com.youyou.uumall.ui.BrandDescActivity_;
import com.youyou.uumall.ui.CategoryActivity_;
import com.youyou.uumall.ui.CommodityDescActivity_;
import com.youyou.uumall.ui.CountryActivity_;
import com.youyou.uumall.ui.QueryMainActivity_;
import com.youyou.uumall.utils.MyUtils;
import com.youyou.uumall.view.GalleryView;
import com.youyou.uumall.view.RefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements BaseBusiness.ArrayListCallbackInterface, View.OnTouchListener, View.OnClickListener, CommodityAdapter.OnItemClickListener {

    Context mContext;
    @ViewById
    RefreshListView listview;

    @ViewById
    TextView home_country_tv;
    @ViewById
    TextView home_search_tv;

    @Bean
    CommodityAdapter adapter;

    @Bean
    CommodityBiz commodityBiz;

    @Bean
    AddressBiz addressBiz;

    private View headerView_0;
    private View headerView_1;
    private View headerView_2;
    private GalleryView mGalleryView;
    private List<RecommendBean> recommendList;//推荐商品列表集合
    private int loadItem = 0;//加在完的条目
    private List<BrandBean> brandList;
    private DisplayImageOptions options;
    private  boolean isFirstLoading  = true;
    @AfterViews
    void afterViews() {
        mContext = getActivity().getApplicationContext();
        initHeaderView();
        commodityBiz.setArrayListCallbackInterface(this);
        addressBiz.setArrayListCallbackInterface(this);
        listview.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        home_search_tv.setOnTouchListener(this);
        listview.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshing(boolean isAuto) {
                loadData();
            }
        });
        eventBus.postSticky(new ShopCartUpdateEvent());
        options = MyUtils.getImageOptions();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoading) {
            listview.autoRefresh();
            isFirstLoading=false;
        }
    }

    public void loadData() {
        addressBiz.queryDict();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            QueryMainActivity_.intent(getActivity()).start();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        String parentId = (String) v.getTag();
        for (int i = 0; i < brandList.size(); i++) {
            BrandBean brandBean = brandList.get(i);
            if (TextUtils.equals(brandBean.id, parentId)) {
                Intent intent = new Intent(getActivity(),BrandDescActivity_.class);
                intent.putExtra("name", brandBean.name);
                intent.putExtra("id", brandBean.id);
                startActivity(intent);
            }
        }
    }

        @Override
        public void itemClick(View view) {
            //需要传递id,打开一个新的act
            Intent intent = new Intent(mContext, CommodityDescActivity_.class);
            intent.putExtra(BaseConstants.preferencesFiled.GOODS_ID, (String) view.getTag());
//            CategoryDescActivity_.intent(mContext).extra("goodsId",recommendList.get(position).id).start();
            startActivity(intent);
        }


    @Override
    protected void registerEvent() {
        super.registerEvent();
        eventBus.register(this);
    }

    @Override
    protected void unRegisterEvent() {
        super.unRegisterEvent();
        eventBus.unregister(this);
    }


    private void initHeaderView() {//首先添加头布局
        headerView_0 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_view, null);
        mGalleryView = (GalleryView) headerView_0.findViewById(R.id.gallery);
        headerView_1 = LayoutInflater.from(getActivity()).inflate(R.layout.layou_header_recommed, null);
        headerView_2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_hotcategory, null);
        listview.addHeaderView(headerView_0);
        listview.addHeaderView(headerView_1);
        listview.addHeaderView(headerView_2);
    }

    @Click(R.id.home_country_tv)
    void setCountry() {
        CountryActivity_.intent(getActivity()).start();
//        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Click(R.id.home_category_iv)
    void jumpCategory() {
        CategoryActivity_.intent(getActivity()).start();
//        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

//    @Click(R.id.home_search_tv)
//    void jumpSearch() {
//        QueryMainActivity_.intent(getActivity()).start();
//        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
//    }


    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (CommodityBiz.GET_SLIDER_LIST == type) {//轮播图
            if (arrayList != null) {
                List<GalleryBean> slideList = (List<GalleryBean>) arrayList;
                mGalleryView.setParams(slideList);
            }
                loadItem++;
        } else if (CommodityBiz.GET_RECOMMEND_LIST == type) {//推荐商品
            if (arrayList != null) {
                recommendList = (List<RecommendBean>) arrayList;
                if (recommendList != null) {
                    adapter.setData(recommendList);
                }
            }
                loadItem++;
        } else if (AddressBiz.QUERY_COUNTRY == type) {//城市查询
            if (arrayList != null) {
                List<DictBean> dictList = (List<DictBean>) arrayList;
                // TODO: 2016/5/6 将数据存储到本地
                String defaultCountry = MyUtils.getPara(BaseConstants.preferencesFiled.DEFAULT_COUNTRY, mContext);
                if (TextUtils.isEmpty(defaultCountry)) {//存到默认
                    MyUtils.savePara(mContext, BaseConstants.preferencesFiled.DEFAULT_COUNTRY, defaultCountry == "" ? dictList.get(0).dictName : defaultCountry);
                }
                //存列表
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < dictList.size(); i++) {
                    DictBean dictBean = dictList.get(i);
                    stringBuffer.append(dictBean.dictName + "," + dictBean.dictValue + ";");

                }
                MyUtils.savePara(mContext, "dictList", stringBuffer.toString());
                defaultCountry = MyUtils.getPara(BaseConstants.preferencesFiled.DEFAULT_COUNTRY, mContext);
                home_country_tv.setText(defaultCountry);
                eventBus.postSticky(new ShopCartUpdateEvent());
                commodityBiz.getRecommendList();//在拿到数值之后再访问
                commodityBiz.getBrandList();
                commodityBiz.getSliderList();
            }
                loadItem++;
        } else if (CommodityBiz.GET_BRAND_LIST == type) {//平拍商品
            if (arrayList != null) {
                brandList = (List<BrandBean>) arrayList;
//                log.e(brandList.toString());
                setBrandPic(brandList);
            }
                loadItem++;
        }
        if (loadItem >= 4) {
            listview.onRefreshComplete();
            loadItem =0;
        }
    }


    //这个是推荐品牌的设置方式
    private void setBrandPic(List<BrandBean> brandList) {
        if (brandList != null&&brandList.size()!=0) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            ViewGroup viewGroup = (ViewGroup) headerView_1;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                ImageView child = (ImageView) viewGroup.getChildAt(i);
                child.setTag(brandList.get(i).id);
                child.setOnClickListener(this);
                imageLoader.displayImage(BaseConstants.connection.ROOT_URL + brandList.get(i).imageSrc, child,options);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCountrySeleced(CountryCallbackEvent event) {
        listview.autoRefresh();
    }

}
