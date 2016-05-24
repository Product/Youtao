package com.youyou.uumall.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseFragment;
import com.youyou.uumall.business.OrderBiz;
import com.youyou.uumall.model.OrderBean;
import com.youyou.uumall.ui.LoginActivity_;
import com.youyou.uumall.ui.OrderConfirmActivity_;
import com.youyou.uumall.ui.OrderShippingActivity_;
import com.youyou.uumall.ui.OrderSubmitActivity_;
import com.youyou.uumall.ui.RegisterActivity_;
import com.youyou.uumall.utils.UserUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import java.util.List;

@EFragment(R.layout.fragment_mine)
public class MineFragment extends BaseFragment implements BaseBusiness.ArrayListCallbackInterface {

    @Bean
    UserUtils userUtils;

    @Bean
    OrderBiz orderBiz;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterViews() {
        orderBiz.setArrayListCallbackInterface(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Click({R.id.mine_head,R.id.mine_login})
    void userLogin(){
        if(TextUtils.isEmpty(userUtils.getUserId())){
            LoginActivity_.intent(getActivity()).start();
            getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
        }
    }

    @Click
    void mine_register(){//登录页面
        RegisterActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Click
    void all_order_layout() {
        // TODO: 2016/5/23 转跳到全部订单
        orderBiz.queryOrder(0,0,"","");
    }

    @Click
    void pickup_layout() {
        // TODO: 2016/5/23 转跳到待收货 orderConfirm
        OrderShippingActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Click
    void shipped_layout() {
        // TODO: 2016/5/23 转跳到待发货 orderShipping
        OrderConfirmActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);

    }

    @Click
    void payment_layout() {
        // TODO: 2016/5/23 转跳到待付款 orderSubmit
//        orderBiz.queryOrder(0,0,"","orderSubmit");
        OrderSubmitActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (type == OrderBiz.QUERY_ORDER) {
            if (arrayList != null) {
            List<OrderBean> orderBean = (List<OrderBean>) arrayList;
                for (OrderBean bean :
                        orderBean) {
                    log.e(bean.toString());
                }
            }
        }
    }
}
