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
import com.youyou.uumall.bean.Response;
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
public class QueryMainActivity extends BaseActivity implements TextWatcher, QueryMainAdapter.OnItemClickListener, RefreshListView.OnLoadMoreListener, RefreshListView.OnRefreshListener, BaseBusiness.ObjectCallbackInterface {

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
        searchBiz.setObjectCallbackInterface(this);
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
        isAuto = false;
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

    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        //下拉刷新
        if (t == null) {
            return;
        }
        Response response = (Response) t;
        List<GoodsDescBean> arrayList = (List<GoodsDescBean>) response.data;
        if (arrayList == null) {
            list.clear();
            adapter.setData(null);
            if (hasAnimation) {
                listview.onRefreshComplete();
                hasAnimation = false;
            }
            return;
        }
        if (!isAuto) {//第一次搜索也可能进入这里
            if (response.size != 0 && arrayList.size() != 0) {
                list.clear();
                isSatisfy = arrayList.size() >= 10 ? true : false;
                for (int i = 0; i < response.size; i++) {
                    list.add(arrayList.get(i));
                }
                adapter.setData(list);
            } else {
                list.clear();
                adapter.setData(null);
            }
            if (hasAnimation) {
                listview.onRefreshComplete();
                hasAnimation = false;
            }
            return;
        }
        //这个是上拉加载更多
        if (response.size != 0 && arrayList.size() != 0 && isSatisfy) {
            list.addAll(arrayList);
            adapter.setData(list);
        }
            listview.onLoadMoreComplete();
    }
}
