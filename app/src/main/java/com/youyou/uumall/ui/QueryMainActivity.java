package com.youyou.uumall.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.QueryMainAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.business.SearchBiz;
import com.youyou.uumall.model.GoodsDescBean;
import com.youyou.uumall.view.ClearEditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
@EActivity(R.layout.activity_query_main)
public class QueryMainActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, TextWatcher, QueryMainAdapter.OnItemClickListener {

    @ViewById
    ListView listview;

    @ViewById
    ClearEditText query_search_et;

    @ViewById
    TextView query_cancel_tv;

    @Bean
    SearchBiz searchBiz;

    @Bean
    QueryMainAdapter adapter;


    @AfterViews
    void afterViews() {
        query_cancel_tv.setFocusable(true);
        query_cancel_tv.setFocusableInTouchMode(true);
//        InputMethodManager systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        systemService.showSoftInput(query_cancel_tv, InputMethodManager.SHOW_FORCED);

        searchBiz.setArrayListCallbackInterface(this);
        listview.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        query_search_et.addTextChangedListener(this);
    }


    private void search() {
        String searchKey = query_search_et.getText().toString();
        searchBiz.queryGoodsById(searchKey);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (SearchBiz.QUERY_GOODS_BY_KEYWORDS == type) {
            List<GoodsDescBean> list = (List<GoodsDescBean>) arrayList;
            adapter.setData(list);
        }
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

}
