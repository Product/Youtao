package com.youyou.uumall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.CategoryDescBiz;
import com.youyou.uumall.business.ShopcartBiz;
import com.youyou.uumall.model.GoodsDescBean;
import com.youyou.uumall.ui.fragment.CategoryDescFragment_;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/10.
 */
@EActivity(R.layout.activity_category_desc)
public class CategoryDescActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface {

    private FragmentManager fragmentManager;
    private String goodsId;
    @Bean
    ShopcartBiz shopcartBiz;

    @Bean
    CategoryDescBiz categoryDescBiz;
    Map map;
    private boolean isLogined= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsId = getIntent().getStringExtra(BaseConstants.preferencesFiled.GOODS_ID);
    }


    @AfterViews
    void afterViews() {
        categoryDescBiz.setObjectCallbackInterface(this);
        shopcartBiz.setObjectCallbackInterface(this);
        shopcartBiz.getcartList();
        CategoryDescFragment_ categoryDescFragment = new CategoryDescFragment_();
        Bundle bundle = new Bundle();
        bundle.putString(BaseConstants.preferencesFiled.GOODS_ID, goodsId);
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

    @Click
    void category_buynow_bt() {
        if (isLogined) {
        categoryDescBiz.queryGoodsById(goodsId);
        }else {
            showToastShort("您还未登录");
        }

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
    void category_insertcar_bt() {
        if (isLogined) {
        map = MyUtils.insertOneGoods(goodsId);
        shopcartBiz.updatecart(this.map);
        }else {
            showToastShort("您还未登录");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }
    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (ShopcartBiz.UPDATE_CART == type) {
            Response response = (Response) t;
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                showToastShort("成功加入购物车");
            }else{
                showToastShort(response.msg);
            }
        }else if (CategoryDescBiz.QUERY_GOODS_BY_ID == type) {
            GoodsDescBean bean = (GoodsDescBean) t;
            Bundle bundle = new Bundle();
            Bundle item = new Bundle();
            item.putString("goodsId", bean.id);
            item.putString("goodsName", bean.categoryName);
            item.putString("count", 1 + "");
            item.putString("subtotal", bean.coupon);
            item.putString("image", bean.description);
            bundle.putBundle(0 + "", item);
            bundle.putInt("size", 1);
            Intent intent = new Intent(this, ConfirmOrderActivity_.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (ShopcartBiz.GET_CART_LIST == type) {
            Response response = (Response) t;
            if (response.code != 0) {
                isLogined = false;
            }
        }
    }
}
