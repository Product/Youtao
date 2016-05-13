package com.youyou.shopping.ui;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.youyou.shopping.R;
import com.youyou.shopping.adapter.CommodityAdapter;
import com.youyou.shopping.adapter.QueryMainAdapter;
import com.youyou.shopping.base.BaseActivity;
import com.youyou.shopping.base.BaseBusiness;
import com.youyou.shopping.base.BaseConstants;
import com.youyou.shopping.business.SearchBiz;
import com.youyou.shopping.model.GoodsDescBean;
import com.youyou.shopping.model.RecommendBean;
import com.youyou.shopping.utils.MyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/12.
 */
@EActivity(R.layout.activity_query_main)
public class QueryMainActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, View.OnClickListener, TextWatcher {
    @ViewById
    ListView listview;
    @ViewById
    EditText query_search_et;
    @ViewById
    Button query_search_bt;
    @Bean
    SearchBiz searchBiz;
    @Bean
    QueryMainAdapter adapter;
    private Map param;
    private String para;

    @AfterViews
    void afterViews() {
        searchBiz.setArrayListCallbackInterface(this);
        listview.setAdapter(adapter);
        para = MyUtils.getPara(BaseConstants.preferencesFiled.DEFAULT_COUNTRY, mApp);
        query_search_bt.setOnClickListener(this);
        query_search_et.addTextChangedListener(this);
    }

    private void search() {
        String searchKey = query_search_et.getText().toString();
        if (TextUtils.equals("韩国",para)){
            para = "KR";
        }

        if (param == null) {
            param = new HashMap();
        }
        param.put("countryCode", para);
        param.put("searchKeywords", searchKey);
        searchBiz.queryGoodsById(param);
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (SearchBiz.QUERY_GOODS_BY_KEYWORDS == type) {
            List<GoodsDescBean> list = (List<GoodsDescBean>) arrayList;
            adapter.setData(list);
        }
    }

    @Override
    public void onClick(View v) {
        search();
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
}
