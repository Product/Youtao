package com.youyou.shopping.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.youyou.shopping.R;
import com.youyou.shopping.base.BaseActivity;
import com.youyou.shopping.base.BaseConstants;
import com.youyou.shopping.ui.fragment.CategoryDescFragment_;
import com.youyou.shopping.ui.fragment.ShoppingCatFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Administrator on 2016/5/10.
 */
@EActivity(R.layout.activity_category_desc)
public class CategoryDescActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private String goodsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsId = getIntent().getStringExtra(BaseConstants.preferencesFiled.GOODS_ID);
    }


    @AfterViews
    void afterViews() {

        CategoryDescFragment_ categoryDescFragment = new CategoryDescFragment_();
        Bundle bundle = new Bundle();
        bundle.putString(BaseConstants.preferencesFiled.GOODS_ID,goodsId);
        categoryDescFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.category_fragment_fl, categoryDescFragment).commit();
    }

    @Click
    void category_pro_iv() {
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @Click
    void category_car_iv() {
        ShopCartActivity_.intent(this).start();
        overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    /**
     * 1访问网络,写api
     * http://120.26.75.225:8090/uumall/itf/mall/cartItemMod.json
     * 2创建购物车的biz类,3个方法
     * 3在当先页面调用访问网络方法
     * 4当访问结束时的回调给用户提示
     * 这个方式待定
     */
    @Click
    void category_insertcar_bt() {// TODO: 2016/5/11 加入购物车操作

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }
}
