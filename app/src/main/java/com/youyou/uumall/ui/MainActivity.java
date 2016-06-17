package com.youyou.uumall.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.ShopcartBiz;
import com.youyou.uumall.event.MineTriggerEvent;
import com.youyou.uumall.event.ShopCartTriggerEvent;
import com.youyou.uumall.event.ShopCartUpdateEvent;
import com.youyou.uumall.model.ShopCartBean;
import com.youyou.uumall.ui.fragment.HomeFragment_;
import com.youyou.uumall.ui.fragment.MineFragment_;
import com.youyou.uumall.ui.fragment.ShoppingCatFragment_;
import com.youyou.uumall.view.ToastMaster;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface {


    @Bean
    ShopcartBiz shopcartBiz;

    @ViewById
    LinearLayout shopping_cart_point_ll;

    @ViewById
    TextView shopping_cart_point_tv;
    private FragmentManager fm;
    private MineFragment_ mineFg;
    private HomeFragment_ homeFg;
    private ShoppingCatFragment_ shoppingCatFg;
    private Fragment[] fragmentArray;

    private int[] mTabsImageView = {R.id.home_img, R.id.shopping_cart_img,
            R.id.mine_img};//下部图标
    private int[] mIconsNormal = {R.drawable.icon_home_normal,
            R.drawable.shopbox_normal_3x, R.drawable.icon_mine_normal};//
    private int[] mIconsPress = {R.drawable.icon_home_pressed,
            R.drawable.shopbox_seleted_3x, R.drawable.icon_mine_pressed};
    private int[] mTabsTextView = {R.id.home_textView, R.id.shopping_cart_textView,
            R.id.mine_textView};
    private int[] mTabsLinearLayout = {R.id.home_tab, R.id.shopping_cart_tab,
            R.id.mine_tab};

    private int mCurrentPostion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShareSDK.initSDK(this);//share初始化
        initViews();
    }

    @AfterViews
    void afterViews() {
        shopcartBiz.setObjectCallbackInterface(this);
        // TODO: 2016/6/15 由于这个接口需要国家码,所以触发在home中的访问之后
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

    @Subscribe(sticky = false,threadMode = ThreadMode.MAIN)
    public void onShopCartUpdate(ShopCartUpdateEvent event) {
        shopcartBiz.getcartList();
    }

    private void initViews() {
        homeFg = new HomeFragment_();
        shoppingCatFg = new ShoppingCatFragment_();
        mineFg = new MineFragment_();
        fragmentArray = new Fragment[]{homeFg, shoppingCatFg, mineFg};
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        int num = fragmentArray.length;
        for (int i = 0; i < num; i++) {
            ft.add(R.id.fragment_container, fragmentArray[i]);
            if (i != mCurrentPostion) {
                ft.hide(fragmentArray[i]);
            }
        }

        ft.commit();
        showSelectedBtn(mCurrentPostion);
    }

    /**
     * 用来展示当前的fragment
     *
     * @param position fragment编号
     */
    private void showSelectedBtn(int position) {
        if (position == mCurrentPostion) {
            return;
        }

        // 隐藏旧的fragment显示新的
        fm.beginTransaction()
                .hide(fragmentArray[mCurrentPostion])
                .show(fragmentArray[position]).commit();

        // newtab
        ImageView newTabsImageView = (ImageView) findViewById(mTabsImageView[position]);
        newTabsImageView.setImageDrawable(getResources().getDrawable(
                mIconsPress[position]));

        TextView newTabsTextView = (TextView) findViewById(mTabsTextView[position]);
        newTabsTextView.setTextColor(getResources().getColor(R.color.bg_btn_login));
        // oldtab
        ImageView currentTabsImageView = (ImageView) findViewById(mTabsImageView[mCurrentPostion]);
        currentTabsImageView.setImageDrawable(getResources().getDrawable(
                mIconsNormal[mCurrentPostion]));

        TextView currentTabsTextView = (TextView) findViewById(mTabsTextView[mCurrentPostion]);
        currentTabsTextView.setTextColor(getResources()
                .getColor(R.color.font_gray));

        mCurrentPostion = position;//现在需要对网络访问进行触发
        //第一个页面,默认进入触发网络访问,第二个点击的时候有加载图片,并做判断.第三个页面进入时做响应的操作

    }

    @Click
    void home_tab() {
        showSelectedBtn(0);
    }

    @Click
    void shopping_cart_tab() {
        if (mCurrentPostion != 1) {
            eventBus.post(new ShopCartTriggerEvent());
        }
        showSelectedBtn(1);
    }

    @Click
    void mine_tab() {
        if (mCurrentPostion != 2) {
            eventBus.post(new MineTriggerEvent());
        }
        showSelectedBtn(2);
    }

    /**
     * 拦截返回 按键事件，防止点击返回误操作退出应用
     */
    private long mExitTime;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        int keyCode = event.getKeyCode();//获取到的是事件的状态类型
        int action = event.getAction();//获取到的是事件的状态
        if (keyCode == KeyEvent.KEYCODE_BACK && action == KeyEvent.ACTION_DOWN) {//首先做双层判断，如果点击是的回退键&&并且动作是按压的时候，触发操作
            if ((System.currentTimeMillis() - mExitTime) > 2000) {//用于防止误操作的判断，如果说第一次点击后继续下一次点击时间间隔过短，不做操作
                ToastMaster.makeText(this, R.string.press_again_to_exit_program, Toast.LENGTH_SHORT);
                mExitTime = System.currentTimeMillis();
            } else {
                ShareSDK.stopSDK(this);//关闭分享
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (ShopcartBiz.GET_CART_LIST == type) {
            Response response = (Response) t;
            if (response.code == 0) {
                List<ShopCartBean> list = (List<ShopCartBean>) response.data;
                int count = 0;
                for (ShopCartBean bean : list) {
                    count += bean.count;
                }
                if (count != 0) {
                    shopping_cart_point_ll.setVisibility(View.VISIBLE);
                    shopping_cart_point_tv.setText("" + count);
                } else {
                    shopping_cart_point_ll.setVisibility(View.GONE);
                }

            }else{
                shopping_cart_point_ll.setVisibility(View.GONE);
            }
        }
    }
}
