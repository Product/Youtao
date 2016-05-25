package com.youyou.uumall.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseFragment;
import com.youyou.uumall.business.OrderBiz;
import com.youyou.uumall.model.OrderBean;
import com.youyou.uumall.ui.*;
import com.youyou.uumall.utils.UserUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_mine)
public class MineFragment extends BaseFragment implements BaseBusiness.ArrayListCallbackInterface {

    @Bean
    UserUtils userUtils;

    @Bean
    OrderBiz orderBiz;

    @ViewById
    LinearLayout mine_point_ll_1, mine_point_ll_2, mine_point_ll_3, mine_point_ll_4;
    @ViewById
    TextView mine_point_tv_1, mine_point_tv_2, mine_point_tv_3, mine_point_tv_4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterViews() {
        orderBiz.setArrayListCallbackInterface(this);
        orderBiz.queryOrder(0, 0, "", "");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Click({R.id.mine_head, R.id.mine_login})
    void userLogin() {
        if (TextUtils.isEmpty(userUtils.getUserId())) {
            LoginActivity_.intent(getActivity()).start();
            getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
        }
    }

    @Click
    void mine_register() {//登录页面
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
            }

        }
    }
}
