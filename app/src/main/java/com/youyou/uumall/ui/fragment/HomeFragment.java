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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.adapter.CommodityAdapter;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.base.BaseFragment;
import com.youyou.uumall.business.AddressBiz;
import com.youyou.uumall.business.CommodityBiz;
import com.youyou.uumall.event.CountryCallbackEvent;
import com.youyou.uumall.model.BrandBean;
import com.youyou.uumall.model.DictBean;
import com.youyou.uumall.model.GalleryBean;
import com.youyou.uumall.model.RecommendBean;
import com.youyou.uumall.ui.CategoryActivity_;
import com.youyou.uumall.ui.CategoryDescActivity_;
import com.youyou.uumall.ui.CountryActivity_;
import com.youyou.uumall.ui.QueryMainActivity_;
import com.youyou.uumall.utils.MyUtils;
import com.youyou.uumall.view.GalleryView;
import com.youyou.uumall.view.RefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements BaseBusiness.ArrayListCallbackInterface, View.OnTouchListener {

    public static final String BG_BASE_COUNTRY = "bg_base_country";
    public static final String COUNTRY_CODE = "countryCode";

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
    HashMap map;
    HashMap<String, String> countryMap;
    String[] countryName = {"新加坡", "韩国", "日本"};
    String[] countryValue = {"SG", "KR", "JP"};
    private List<RecommendBean> recommendList;//推荐商品列表集合
    private boolean isRefresh = false;
    private int loadItem = 0;//加在完的条目
    @AfterViews
    void afterViews() {
        initCountryMap();
        mContext = getActivity().getApplicationContext();
        initCountry();
        if (!isRefresh) {
            initHeaderView();
        }
        commodityBiz.setArrayListCallbackInterface(this);
        commodityBiz.getSliderList();
        initBrandList();
        initRecommendList();
        if (!isRefresh) {
            listview.addHeaderView(headerView_0);
            listview.addHeaderView(headerView_1);
            listview.addHeaderView(headerView_2);
        }
        listview.setAdapter(adapter);
        OnItemClickListener listener = new OnItemClickListener();
        adapter.setOnItemClickListener(listener);

        home_search_tv.setOnTouchListener(this);
        listview.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                isRefresh=true;
                afterViews();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            QueryMainActivity_.intent(getActivity()).start();
            getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
        }
        return false;
    }


    class OnItemClickListener implements CommodityAdapter.OnItemClickListener {

        @Override
        public void itemClick(View view) {
            //需要传递id,打开一个新的act
            Intent intent = new Intent(mContext, CategoryDescActivity_.class);
            intent.putExtra(BaseConstants.preferencesFiled.GOODS_ID, (String) view.getTag());
//            CategoryDescActivity_.intent(mContext).extra("goodsId",recommendList.get(position).id).start();
            startActivity(intent);
        }
    }

    private void initBrandList() {
        commodityBiz.getBrandList();
    }


    private void initCountryMap() {// TODO: 2016/5/10 好土的方式
        countryMap = new HashMap<String, String>();
        for (int i = 0; i < countryName.length; i++) {
            countryMap.put(countryName[i], countryValue[i]);
        }
    }

    private void initRecommendList() {
        map = new HashMap();
        String country = MyUtils.getPara(BaseConstants.preferencesFiled.DEFAULT_COUNTRY, mContext);
        String countryValue = countryMap.get(country);
        map.put(COUNTRY_CODE, countryValue);
        commodityBiz.getRecommendList(map);
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

    /**
     * 在此处优先获取到sp里的数据.
     * sp里有两个相关数据:
     * 1.用来存储用户选择的偏好国家信息
     * 2.用来存储访问网络得到的国家列表
     * 在此进行判断:
     * 如果获取到1的数据,直接展示,之后访问网络获取列表,存储到2
     * 没有的话,直接访问网络,拿到列表.将第一个国家数据做展示,并保存1和2
     */
    private void initCountry() {
        addressBiz.setArrayListCallbackInterface(this);
        map = new HashMap();
        map.put("dictType", BG_BASE_COUNTRY);
        addressBiz.queryDict(map);
    }


    private void initHeaderView() {//首先添加头布局
        headerView_0 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_view, null);
        mGalleryView = (GalleryView) headerView_0.findViewById(R.id.gallery);
        headerView_1 = LayoutInflater.from(getActivity()).inflate(R.layout.layou_header_recommed, null);
        headerView_2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_hotcategory, null);

    }

    @Click(R.id.home_country_tv)
    void setCountry() {
        CountryActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Click(R.id.home_category_iv)
    void jumpCategory() {
        CategoryActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
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
                loadItem++;
            }
        } else if (CommodityBiz.GET_RECOMMEND_LIST == type) {//推荐商品
            if (arrayList != null) {
                recommendList = (List<RecommendBean>) arrayList;
                setRecommendPic(recommendList);
                loadItem++;
            }
        } else if (AddressBiz.QUERY_COUNTRY == type) {//城市查询
            if (arrayList != null) {
                List<DictBean> dictList = (List<DictBean>) arrayList;
                // TODO: 2016/5/6 将数据存储到本地
                String defaultCountry = MyUtils.getPara(BaseConstants.preferencesFiled.DEFAULT_COUNTRY, mContext);
//                String defaultCountryCode = MyUtils.getPara(BaseConstants.preferencesFiled.DEFAULT_COUNTRY_CODE, mContext);
                if (TextUtils.isEmpty(defaultCountry)) {
                    //存到默认
                    MyUtils.savePara(mContext, BaseConstants.preferencesFiled.DEFAULT_COUNTRY, defaultCountry == "" ? dictList.get(0).dictName : defaultCountry);
//                    MyUtils.savePara(mContext, BaseConstants.preferencesFiled.DEFAULT_COUNTRY_CODE, defaultCountryCode == "" ? dictList.get(0).dictValue : defaultCountryCode);
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
                loadItem++;
            }
        } else if (CommodityBiz.GET_BRAND_LIST == type) {
            if (arrayList != null) {
                List<BrandBean> brandList = (List<BrandBean>) arrayList;
                setBrandPic(brandList);
                loadItem++;
            }
        }
        if (loadItem >= 4) {
            refreshComplete();
        }
    }

    @Background
    public void refreshComplete() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        refresh();

    }
    @UiThread
    public void refresh() {
        listview.onRefreshComplete();
        loadItem =0;
    }

    //这个是推荐品牌的设置方式
    private void setBrandPic(List<BrandBean> brandList) {
        if (brandList != null) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            ViewGroup viewGroup = (ViewGroup) headerView_1;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                ImageView child = (ImageView) viewGroup.getChildAt(i);
                imageLoader.displayImage(BaseConstants.connection.ROOT_URL + brandList.get(i).imageSrc, child);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBackgroundThread(CountryCallbackEvent event) {
        String type = event.getEvent();
        isRefresh = true;
        afterViews();
    }


    public void setRecommendPic(List<RecommendBean> recommendPic) {
        if (recommendPic != null) {
            adapter.setData(recommendPic);
        }
    }
}
