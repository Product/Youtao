package com.youyou.uumall.ui;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.CategoryAdapter;
import com.youyou.uumall.adapter.CategoryGridAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.business.CategoryDescBiz;
import com.youyou.uumall.business.CommodityBiz;
import com.youyou.uumall.model.BrandBean;
import com.youyou.uumall.model.CategoryBean;

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
@EActivity(R.layout.activity_category)
public class CategoryActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, View.OnTouchListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener {
    @Bean
    CategoryDescBiz categoryDescBiz;

    @Bean
    CommodityBiz commodityBiz;

    @Bean
    CategoryAdapter adapter;

    @Bean
    CategoryGridAdapter categoryGridAdapter;

    @ViewById
    ListView category_lv;

    @ViewById
    GridView category_gv;

    @ViewById
    EditText home_search_tv;

    @ViewById
    RadioButton category_rb1;

    @ViewById
    RadioButton category_rb2;

    @ViewById
    RadioGroup category_rg;

    @ViewById
    View category_view1;

    @ViewById
    View category_view2;

    @ViewById
    RelativeLayout category_rl;
    private List<BrandBean> brandList;

    @AfterViews
    void afterViews() {
        initData();
        category_lv.setAdapter(adapter);
        category_gv.setAdapter(categoryGridAdapter);
        home_search_tv.setOnTouchListener(this);
        category_rg.setOnCheckedChangeListener(this);
        category_gv.setOnItemClickListener(this);
    }


    private void initData() {
        commodityBiz.setArrayListCallbackInterface(this);
        commodityBiz.getBrandList();
        categoryDescBiz.setArrayListCallbackInterface(this);
        categoryDescBiz.queryCategory("");
    }

//    @Click
//    void category_cart_iv() {
//        ShopCartActivity_.intent(this).start();
//    }

    @Click
    void category_pro_iv() {
        onBackPressed();
    }

    @Click
    void home_search_tv() {

    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (type == CategoryDescBiz.QUERY_CATEGORY) {
            List<CategoryBean> list = (List<CategoryBean>) arrayList;
//            log.e(arrayList.toString());
            adapter.setData(list);
        } else if (type == CommodityBiz.GET_BRAND_LIST) {
            if (arrayList != null) {
                brandList = (List<BrandBean>) arrayList;
                categoryGridAdapter.setData(brandList);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            QueryMainActivity_.intent(this).start();
        }
        return false;
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.category_rb1:
                category_view1.setVisibility(View.VISIBLE);
                category_view2.setVisibility(View.INVISIBLE);

                category_lv.setVisibility(View.VISIBLE);
                category_gv.setVisibility(View.GONE);
                break;
            case R.id.category_rb2:
                category_view2.setVisibility(View.VISIBLE);
                category_view1.setVisibility(View.INVISIBLE);

                category_lv.setVisibility(View.GONE);
                category_gv.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BrandBean brandBean = brandList.get(position);
        Intent intent = new Intent(this,BrandDescActivity_.class);
        intent.putExtra("name", brandBean.name);
        intent.putExtra("id", brandBean.id);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
