package com.youyou.uumall.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.api.OtherApi;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseFragment;
import com.youyou.uumall.business.OrderBiz;
import com.youyou.uumall.event.MineTriggerEvent;
import com.youyou.uumall.model.OrderBean;
import com.youyou.uumall.ui.BonusActivity_;
import com.youyou.uumall.ui.LoginActivity_;
import com.youyou.uumall.ui.OrderConfirmActivity_;
import com.youyou.uumall.ui.OrderShippingActivity_;
import com.youyou.uumall.ui.OrderSubmitActivity_;
import com.youyou.uumall.ui.RegisterActivity_;
import com.youyou.uumall.ui.SettingActivity_;
import com.youyou.uumall.utils.UserUtils;
import com.youyou.uumall.view.CircleImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

@EFragment(R.layout.fragment_mine)
public class MineFragment extends BaseFragment implements BaseBusiness.ArrayListCallbackInterface {

    @Bean
    UserUtils userUtils;

    @Bean
    OrderBiz orderBiz;

    @RestService
    protected OtherApi otherApi;

    @ViewById
    LinearLayout mine_point_ll_1, mine_point_ll_2, mine_point_ll_3, mine_point_ll_4, mine_login_reg_ll;
    @ViewById
    TextView mine_point_tv_1, mine_point_tv_2, mine_point_tv_3, mine_point_tv_4, mine_userid_tv;

    @ViewById
    CircleImageView mine_head;
    private ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterViews() {
        imageLoader = ImageLoader.getInstance();
        orderBiz.setArrayListCallbackInterface(this);
    }

    @Override
    protected void unRegisterEvent() {
        super.unRegisterEvent();
        eventBus.unregister(this);
    }

    @Override
    protected void registerEvent() {
        super.registerEvent();
        eventBus.register(this);
    }

    @Subscribe
    public void onSelected(MineTriggerEvent event) {
        orderBiz.queryOrder(0, 0, "", "");
    }


    @Click({R.id.mine_head, R.id.mine_login})
    void userLogin() {
        LoginActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Click
    void mine_register() {
        RegisterActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Click
    void all_order_layout() {
        // 转跳到全部订单
        com.youyou.uumall.ui.OrderAllActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Click
    void pickup_layout() {
        // 转跳到待收货 orderConfirm
        OrderShippingActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Click
    void shipped_layout() {
        // 转跳到待发货 orderShipping
        OrderConfirmActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);

    }

    @Click
    void payment_layout() {
        // 转跳到待付款 orderSubmit
//        orderBiz.queryOrder(0,0,"","orderSubmit");
        OrderSubmitActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Click
    void setting_layout() {
        SettingActivity_.intent(getActivity()).start();
    }

    @Click
    void my_reward_layout() {
        BonusActivity_.intent(getActivity()).start();
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        int orderSubmit = 0;
        int orderConfirm = 0;
        int orderShipping = 0;
        if (type == OrderBiz.QUERY_ORDER) {
            if (arrayList != null) {
                List<OrderBean> orderBean = (List<OrderBean>) arrayList;
                for (OrderBean bean :
                        orderBean) {
                    String status = bean.status;
                    switch (status) {
                        case "orderSubmit":
                            orderSubmit++;
                            break;
                        case "orderConfirm":
                            orderConfirm++;
                            break;
                        case "orderShipping":
                            orderShipping++;
                            break;
                        default:

                            break;
                    }
                }
                if (orderSubmit != 0) {
                    mine_point_ll_1.setVisibility(View.VISIBLE);
                    mine_point_tv_1.setText(orderSubmit + "");
                }
                if (orderConfirm != 0) {
                    mine_point_ll_2.setVisibility(View.VISIBLE);
                    mine_point_tv_2.setText(orderConfirm + "");
                }
                if (orderShipping != 0) {
                    mine_point_ll_3.setVisibility(View.VISIBLE);
                    mine_point_tv_3.setText(orderShipping + "");
                }
                if (orderBean.size() != 0) {
                    mine_point_ll_4.setVisibility(View.VISIBLE);
                    mine_point_tv_4.setText(orderBean.size() + "");
                }
                //登录状态,在本地获取缓存头像和姓名,如果是普通登录没有头像,采用默认头像,隐藏线性布局,展示姓名,头像点击事件取消
                mine_login_reg_ll.setVisibility(View.GONE);
                mine_head.setEnabled(false);
                String headimgurl = userUtils.getUserInfo();
                String userId = userUtils.getUserId();
                mine_userid_tv.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(headimgurl)) {
                    imageLoader.displayImage(headimgurl, mine_head);
                }
                if (userId != null) {
                    mine_userid_tv.setText(userId);
                }

            } else {
                mine_point_ll_1.setVisibility(View.GONE);
                mine_point_ll_2.setVisibility(View.GONE);
                mine_point_ll_3.setVisibility(View.GONE);
                mine_point_ll_4.setVisibility(View.GONE);
                userUtils.saveUserId("");
                //用户未登录,采用默认头像,展示线性布局,隐藏姓名,头像点击事件
                mine_userid_tv.setVisibility(View.GONE);
                mine_login_reg_ll.setVisibility(View.VISIBLE);
                mine_head.setImageResource(R.drawable.default_header);
                mine_head.setEnabled(true);
            }

        }
    }
}
