package com.youyou.shopping.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ListView;

import com.youyou.shopping.R;
import com.youyou.shopping.adapter.ShopcartAdapter;
import com.youyou.shopping.base.BaseActivity;
import com.youyou.shopping.base.BaseBusiness;
import com.youyou.shopping.base.BaseConstants;
import com.youyou.shopping.business.ShopcartBiz;
import com.youyou.shopping.model.ShopCartBean;
import com.youyou.shopping.ui.fragment.CategoryDescFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
@EActivity(R.layout.activity_shopcart)
public class ShopCartActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface {

    @Bean
    ShopcartBiz shopcartBiz;

    @Bean
    ShopcartAdapter adapter;

    @ViewById
    ListView shopcart_fragment_lv;
    /**
     * 1访问购物车接口,对listview进行展示
     */
    @AfterViews
    void afterViews() {
        shopcartBiz.setArrayListCallbackInterface(this);
        shopcartBiz.getcartList();
        shopcart_fragment_lv.setAdapter(adapter);

    }


    @Click
    void shopcart_pro_iv() {
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        List<ShopCartBean> list = (List<ShopCartBean>) arrayList;
        adapter.setData(list);
    }
}

