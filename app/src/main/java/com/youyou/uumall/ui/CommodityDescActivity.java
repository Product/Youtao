package com.youyou.uumall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.CategoryDescBiz;
import com.youyou.uumall.business.ShopcartBiz;
import com.youyou.uumall.event.LoginEvent;
import com.youyou.uumall.event.ShopCartUpdateEvent;
import com.youyou.uumall.model.GoodsDescBean;
import com.youyou.uumall.model.ShopCartBean;
import com.youyou.uumall.ui.fragment.CategoryDescFragment_;
import com.youyou.uumall.utils.MyUtils;
import com.youyou.uumall.view.ShareDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2016/5/10.
 */
@EActivity(R.layout.activity_category_desc)
public class CommodityDescActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface, AdapterView.OnItemClickListener, View.OnClickListener, PlatformActionListener {

    private FragmentManager fragmentManager;
    private String goodsId;
    @Bean
    ShopcartBiz shopcartBiz;

    @Bean
    CategoryDescBiz categoryDescBiz;
    private boolean isLogined = true;

    @ViewById
    LinearLayout category_menu_point_ll;

    @ViewById
    TextView category_menu_point_tv;
    private ShareDialog shareDialog;

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
            Intent intent = new Intent(this, LoginActivity_.class);
            startActivity(intent);
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
            Map[] map = MyUtils.insertOneGoods(goodsId);
            shopcartBiz.updatecart(map, 0);
        } else {
            Intent intent = new Intent(this, LoginActivity_.class);
            startActivity(intent);
        }
    }

    @Click
    void category_menu_iv() {
        shareDialog = new ShareDialog(this);
        shareDialog.setCancelButtonOnClickListener(this);
        shareDialog.setOnItemClickListener(this);
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
//            log.e(bean.toString());
            Bundle bundle = new Bundle();
            Bundle item = new Bundle();
            item.putString("goodsId", bean.id);
            item.putString("goodsName", bean.titile);
            item.putString("count", 1 + "");
            item.putString("subtotal", bean.coupon == null ? bean.price : bean.coupon);
            item.putString("image", bean.image);
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
                isLogined = true;
                List<ShopCartBean> list = (List<ShopCartBean>) response.data;
                int count = 0;
                for (ShopCartBean bean : list) {
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

    @Override
    protected void registerEvent() {
        super.registerEvent();
        eventBus.register(this);
    }

    @Override
    protected void unRegisterEvent() {
        super.unRegisterEvent();
        eventBus.unregister(this);
    }

    @Subscribe
    public void onLoginSuceess(LoginEvent event) {//eventBus接收数据,并后台调用
        shopcartBiz.getcartList();
    }

    /**
     * 商品详情
     发给朋友
     标题：推荐个宝贝给你 副标题：商品名称
     图片：商品第一张主图
     分享到朋友圈
     标题：商品名称
      图片：商品第一张主图
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
        if (item.get("ItemText").equals("微信好友")) {
//            Toast.makeText(this, "您点中了" + item.get("ItemText"), Toast.LENGTH_LONG).show();
            //2、设置分享内容
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
            sp.setTitle("推荐个宝贝给你");  //分享标题
            sp.setText("我是分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
            sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
            sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情

            //3、非常重要：获取平台对象
            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
            wechat.setPlatformActionListener(this); // 设置分享事件回调
            // 执行分享
            wechat.share(sp);


        } else if (item.get("ItemText").equals("朋友圈")) {
            //2、设置分享内容
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
            sp.setTitle("我是分享标题");  //分享标题
            sp.setText("我是分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
            sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
            sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情

            //3、非常重要：获取平台对象
            Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
            wechatMoments.setPlatformActionListener(this); // 设置分享事件回调
            // 执行分享
            wechatMoments.share(sp);

        }
    }

    @Override
    public void onClick(View v) {
        shareDialog.dismiss();
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {//分享回调
        log.e("成功");
        shareDialog.dismiss();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        log.e("失败");
        shareDialog.dismiss();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        log.e("取消");
        shareDialog.dismiss();
    }
}
