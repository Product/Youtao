package com.youyou.uumall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.CategoryDescBiz;
import com.youyou.uumall.business.ShopcartBiz;
import com.youyou.uumall.event.ShopCartUpdateEvent;
import com.youyou.uumall.model.GoodsDescBean;
import com.youyou.uumall.model.ShopCartBean;
import com.youyou.uumall.ui.fragment.CategoryDescFragment_;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2016/5/10.
 */
@EActivity(R.layout.activity_category_desc)
public class CommodityDescActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface {

    private FragmentManager fragmentManager;
    private String goodsId;
    @Bean
    ShopcartBiz shopcartBiz;

    @Bean
    CategoryDescBiz categoryDescBiz;
    Map map;
    private boolean isLogined = true;

    @ViewById
    LinearLayout category_menu_point_ll;

    @ViewById
    TextView category_menu_point_tv;

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
//        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @Click
    void category_car_iv() {
        ShopCartActivity_.intent(this).start();
//        overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Click
    void category_buynow_bt() {
        if (isLogined) {
            categoryDescBiz.queryGoodsById(goodsId);
        } else {
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
        } else {
            showToastShort("您还未登录");
        }
    }

    @Click
    void category_menu_iv() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("油桃扫货\n代购没烦恼\n才能任性玩");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.youyoumob.com/");
        oks.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
// 启动分享GUI
        oks.show(this);
    }


    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (ShopcartBiz.UPDATE_CART == type) {
            Response response = (Response) t;
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                showToastShort("成功加入购物车");
                shopcartBiz.getcartList();
                eventBus.post(new ShopCartUpdateEvent());//发送给main
            } else {
                showToastShort(response.msg);
            }
        } else if (CategoryDescBiz.QUERY_GOODS_BY_ID == type) {
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
            } else {
                List<ShopCartBean> list = (List<ShopCartBean>) response.data;
                int count = 0;
                for (ShopCartBean bean :list) {
                    count += bean.count;
                }
                if (count != 0) {
                    category_menu_point_ll.setVisibility(View.VISIBLE);
                    category_menu_point_tv.setText("" + count);
                } else {
                    category_menu_point_ll.setVisibility(View.GONE);
                }

            }
        }
    }
}
