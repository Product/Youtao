package com.youyou.uumall.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.QueryMainAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.business.SearchBiz;
import com.youyou.uumall.model.GoodsDescBean;
import com.youyou.uumall.view.ClearEditText;
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
 * Created by Administrator on 2016/5/12.
 */
@EActivity(R.layout.activity_query_main)
public class QueryMainActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, TextWatcher, QueryMainAdapter.OnItemClickListener, RefreshListView.OnLoadMoreListener, RefreshListView.OnRefreshListener {

    @ViewById
    RefreshListView listview;

    @ViewById
    ClearEditText query_search_et;

    @ViewById
    TextView query_cancel_tv;

    @Bean
    SearchBiz searchBiz;

    @Bean
    QueryMainAdapter adapter;


    private int pageNo = 1;
    private List<GoodsDescBean> list = new ArrayList<>();
    private boolean isSatisfy;
    private boolean isAuto;
    private boolean hasAnimation;

    @AfterViews
    void afterViews() {
        query_cancel_tv.setFocusable(true);
        query_cancel_tv.setFocusableInTouchMode(true);
        searchBiz.setArrayListCallbackInterface(this);
        listview.setAdapter(adapter);
        listview.setOnLoadMoreListener(this);
        listview.setOnRefreshListener(this);
        adapter.setOnItemClickListener(this);
        query_search_et.addTextChangedListener(this);
    }


    private void search() {
        String searchKey = query_search_et.getText().toString();
        searchBiz.queryGoodsById(pageNo, 10, searchKey);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        //下拉刷新
        if (!isAuto) {//第一次搜索也可能进入这里
            if (arrayList != null && arrayList.size() != 0 ) {
                list.clear();
                List<GoodsDescBean> orderBean = (List<GoodsDescBean>) arrayList;
                isSatisfy = orderBean.size() >= 10 ? true : false;
                list.addAll(orderBean);
                adapter.setData(list);
                if (hasAnimation) {//不需要动画
                    listview.onRefreshComplete();
                    hasAnimation = false;
                }
                return;
            }
        }
        //这个是上拉加载更多
        if (arrayList != null && arrayList.size() != 0 && isSatisfy) {
            List<GoodsDescBean> orderBean = (List<GoodsDescBean>) arrayList;
            list.addAll(orderBean);
            adapter.setData(list);
        }
        listview.onLoadMoreComplete();

    }

    @Click
    void query_cancel_tv() {
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        pageNo = 1;
        search();
    }

    @Override
    public void itemClick(View view) {
        String tag = (String) view.getTag();
        Intent intent = new Intent(this, CommodityDescActivity_.class);
        intent.putExtra(BaseConstants.preferencesFiled.GOODS_ID, tag);
        startActivity(intent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onLoadingMore() {
        isAuto = true;
        pageNo++;
        search();
    }

    @Override
    public void onRefreshing(boolean isAuto) {
        hasAnimation = true;
        this.isAuto = isAuto;
        pageNo = 1;
        search();
    }
}
