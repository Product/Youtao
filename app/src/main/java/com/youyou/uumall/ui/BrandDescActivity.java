package com.youyou.uumall.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.BrandDescGridAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.business.CommodityBiz;
import com.youyou.uumall.model.GoodsDescBean;
import com.youyou.uumall.view.RefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 */
@EActivity(R.layout.activity_brand_desc)
public class BrandDescActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, BrandDescGridAdapter.OnItemClickListener, RefreshListView.OnRefreshListener {
    @ViewById
    TextView brand_desc_title_tv;

    @ViewById
    RefreshListView brand_desc_gv;
    @Bean
    BrandDescGridAdapter gridAdapter;
    @Bean
    CommodityBiz commodityBiz;
    private List<GoodsDescBean> goodsDescBeanList;
    private String mId;

    @AfterViews
    void afterViews() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        mId = intent.getStringExtra("id");
        brand_desc_title_tv.setText(name);
        commodityBiz.setArrayListCallbackInterface(this);
        gridAdapter.setOnItemClickListener(this);
        brand_desc_gv.setOnRefreshListener(this);
        brand_desc_gv.autoRefresh();
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (CommodityBiz.QUERY_GOODS_BY_BRAND == type) {
            if (arrayList != null) {
                goodsDescBeanList = (List<GoodsDescBean>) arrayList;
                brand_desc_gv.setAdapter(gridAdapter);
                gridAdapter.setData(goodsDescBeanList);
            }
            brand_desc_gv.onRefreshComplete();
        }
    }

    @Click
    void brand_desc_pro_iv() {
        finish();
    }

    @Override
    public void itemClick(View view) {
        Intent intent = new Intent(this, CommodityDescActivity_.class);
        intent.putExtra(BaseConstants.preferencesFiled.GOODS_ID, view.getTag()+"");
        startActivity(intent);
    }

    @Override
    public void onRefreshing(boolean isAuto) {
        commodityBiz.queryGoodsByBrand(mId);
    }
}
