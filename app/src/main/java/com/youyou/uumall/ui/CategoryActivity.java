package com.youyou.uumall.ui;

import android.widget.ListView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.CategoryAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.business.CategoryDescBiz;
import com.youyou.uumall.model.CategoryBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
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
