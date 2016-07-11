package com.youyou.uumall.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.BrandDescGridAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.CommodityBiz;
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
public class BrandDescActivity extends BaseActivity implements BrandDescGridAdapter.OnItemClickListener, RefreshListView.OnRefreshListener, RefreshListView.OnLoadMoreListener, BaseBusiness.ObjectCallbackInterface {
    @ViewById
    TextView brand_desc_title_tv;

    @ViewById
    RefreshListView brand_desc_gv;
    @Bean
    BrandDescGridAdapter gridAdapter;
    @Bean
    CommodityBiz commodityBiz;
    private List<GoodsDescBean> list = new ArrayList<>();
    private String mId;
    private int pageNo = 1;
    private boolean isSatisfy;

    @AfterViews
    void afterViews() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        mId = intent.getStringExtra("id");
        brand_desc_title_tv.setText(name);
        brand_desc_gv.setAdapter(gridAdapter);
        commodityBiz.setObjectCallbackInterface(this);
        gridAdapter.setOnItemClickListener(this);
        brand_desc_gv.setOnLoadMoreListener(this);
        brand_desc_gv.setOnRefreshListener(this);
        brand_desc_gv.autoRefresh();
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
            pageNo = 1;
            commodityBiz.queryGoodsByBrand(pageNo, 10, mId);
    }

    @Override
    public void onLoadingMore() {
        pageNo++;
        commodityBiz.queryGoodsByBrand(pageNo, 10, mId);
    }

    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (CommodityBiz.QUERY_GOODS_BY_BRAND == type) {
            if (t == null) {
                return;
            }
            Response response = (Response) t;
            List<GoodsDescBean> arrayList = (List<GoodsDescBean>) response.data;
            if (arrayList == null) {
                return;
            }
            if (pageNo == 1) {//是第一次调用,也就是默认刷新
                if (response.size != 0 && arrayList.size() != 0) {
                    isSatisfy = arrayList.size() >= 10 ? true : false;
                    list.clear();
                    for (int i = 0; i < response.size; i++) {
                        list.add(arrayList.get(i));
                    }
                    gridAdapter.setData(list);
                    brand_desc_gv.onRefreshComplete();
                } else {
                    brand_desc_gv.onRefreshComplete();
                    brand_desc_gv.setVisibility(View.GONE);
//                    order_empty.setVisibility(View.VISIBLE);
                }
            } else {//这个是上拉加载更多
                if (response.size != 0 && arrayList.size() != 0 && isSatisfy) {
                    list.addAll(arrayList);
                    gridAdapter.setData(list);
                }
                brand_desc_gv.onLoadMoreComplete();
            }
        }
    }
}
