package com.youyou.shopping.ui;

import android.widget.ListView;

import com.youyou.shopping.R;
import com.youyou.shopping.adapter.CategoryAdapter;
import com.youyou.shopping.base.BaseActivity;
import com.youyou.shopping.base.BaseBusiness;
import com.youyou.shopping.business.CategoryDescBiz;
import com.youyou.shopping.model.CategoryBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/12.
 */
@EActivity(R.layout.activity_category)
public class CategoryActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface {
    @Bean
    CategoryDescBiz categoryDescBiz;

    @Bean
    CategoryAdapter adapter;

    @ViewById
    ListView category_lv;

    Map paramMap ;

    @AfterViews
    void afterViews() {
        initData();
        category_lv.setAdapter(adapter);
    }

    private void initData() {
        categoryDescBiz.setArrayListCallbackInterface(this);
        paramMap = new HashMap();
        paramMap.put("parentId","");
        categoryDescBiz.queryCategory(paramMap);
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (type == CategoryDescBiz.QUERY_CATEGORY) {
            List<CategoryBean>  list = (List<CategoryBean>) arrayList;
            adapter.setData(list);
        }
    }
}
