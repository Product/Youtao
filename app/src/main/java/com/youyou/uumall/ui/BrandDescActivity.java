package com.youyou.uumall.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.BrandDescGridAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.business.CommodityBiz;
import com.youyou.uumall.model.GoodsDescBean;

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
public class BrandDescActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, AdapterView.OnItemClickListener {
    @ViewById
    TextView brand_desc_title_tv;

    @ViewById
    GridView brand_desc_gv;
    @Bean
    BrandDescGridAdapter gridAdapter;
    @Bean
    CommodityBiz commodityBiz;
    private List<GoodsDescBean> goodsDescBeanList;

    @AfterViews
    void afterViews() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");
        brand_desc_title_tv.setText(name);
        commodityBiz.setArrayListCallbackInterface(this);
        commodityBiz.queryGoodsByBrand(id);
        brand_desc_gv.setOnItemClickListener(this);

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
        }
    }

    @Click
    void brand_desc_pro_iv() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CommodityDescActivity_.class);
        intent.putExtra(BaseConstants.preferencesFiled.GOODS_ID, goodsDescBeanList.get(position).id);
        startActivity(intent);
    }
}
