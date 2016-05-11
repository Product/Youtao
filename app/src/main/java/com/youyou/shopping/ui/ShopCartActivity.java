package com.youyou.shopping.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.youyou.shopping.R;
import com.youyou.shopping.base.BaseActivity;
import com.youyou.shopping.base.BaseConstants;
import com.youyou.shopping.ui.fragment.CategoryDescFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Administrator on 2016/5/11.
 */
@EActivity(R.layout.activity_shopcart)
public class ShopCartActivity extends BaseActivity {

    private FragmentManager fragmentManager;
//    private String goodsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        goodsId = getIntent().getStringExtra(BaseConstants.preferencesFiled.GOODS_ID);
    }


    @AfterViews
    void afterViews() {

//        CategoryDescFragment_ categoryDescFragment = new CategoryDescFragment_();
////        Bundle bundle = new Bundle();
////        bundle.putString(BaseConstants.preferencesFiled.GOODS_ID,goodsId);
////        categoryDescFragment.setArguments(bundle);
//        fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//        transaction.replace(R.id.shopcart_fragment_fl, categoryDescFragment).commit();
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
}

