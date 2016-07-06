package com.youyou.uumall.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.BrandDescGridAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.business.CategoryDescBiz;
import com.youyou.uumall.model.GoodsDescBean;
import com.youyou.uumall.view.RefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 */
@EActivity(R.layout.activity_brand_desc)
public class CategoryDescActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, BrandDescGridAdapter.OnItemClickListener, RefreshListView.OnRefreshListener, RefreshListView.OnLoadMoreListener {
    @ViewById
    TextView brand_desc_title_tv;

    @ViewById
    RefreshListView brand_desc_gv;
    @Bean
    BrandDescGridAdapter gridAdapter;
    @Bean
    CategoryDescBiz categoryDescBiz;
    private List<GoodsDescBean> list = new ArrayList<>();
    private String mId;
    private boolean isAuto;
    private int pageNo = 1;
    private boolean isSatisfy;

    @AfterViews
    void afterViews() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        mId = intent.getStringExtra("id");
        brand_desc_title_tv.setText(name);
        brand_desc_gv.setAdapter(gridAdapter);
        categoryDescBiz.setArrayListCallbackInterface(this);
        gridAdapter.setOnItemClickListener(this);
        brand_desc_gv.setOnRefreshListener(this);
        brand_desc_gv.setOnLoadMoreListener(this);
        brand_desc_gv.autoRefresh();
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (CategoryDescBiz.QUERY_GOODS_BY_CATEGORY == type) {
            if (!isAuto) {//手动刷新
                if (arrayList != null && arrayList.size() != 0 && isSatisfy) {
                    isAuto = !isAuto;
                    list.clear();
                    List<GoodsDescBean> goodsDescBeanList = (List<GoodsDescBean>) arrayList;
                    list.addAll(goodsDescBeanList);
                    gridAdapter.setData(list);
                    brand_desc_gv.onRefreshComplete();
                    return;
                }
            }
            if (pageNo == 1) {//是第一次调用,也就是默认刷新
                if (arrayList != null && arrayList.size() != 0) {
                    List<GoodsDescBean> goodsDescBeanList = (List<GoodsDescBean>) arrayList;
                    isSatisfy = goodsDescBeanList.size() >= 10 ? true : false;
                    list.addAll(goodsDescBeanList);
                    gridAdapter.setData(list);
                    brand_desc_gv.onRefreshComplete();
                } else {
                    brand_desc_gv.onRefreshComplete();
                    brand_desc_gv.setVisibility(View.GONE);
//                    order_empty.setVisibility(View.VISIBLE);
                }
            } else {//这个是上拉加载更多
                if (arrayList != null && arrayList.size() != 0 && isSatisfy) {
                    List<GoodsDescBean> goodsDescBeanList = (List<GoodsDescBean>) arrayList;
                    list.addAll(goodsDescBeanList);
                    gridAdapter.setData(list);
                }
                brand_desc_gv.onLoadMoreComplete();
            }
        }
    }

    @Click
    void brand_desc_pro_iv() {
        finish();
    }


    @Override
    public void itemClick(View view) {
        Intent intent = new Intent(this, CommodityDescActivity_.class);
        intent.putExtra(BaseConstants.preferencesFiled.GOODS_ID, view.getTag() + "");
        startActivity(intent);
    }

    @Override
    public void onRefreshing(boolean isAuto) {
        this.isAuto = isAuto;
        if (isAuto) {
            categoryDescBiz.queryGoodsByCategory(pageNo, 10, mId);
        } else {
            pageNo = 1;
            categoryDescBiz.queryGoodsByCategory(pageNo, 10, mId);
        }

    }

    @Override
    public void onLoadingMore() {
        pageNo++;
        categoryDescBiz.queryGoodsByCategory(pageNo, 10, mId);
    }
}
