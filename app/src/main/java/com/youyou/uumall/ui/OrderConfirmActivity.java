package com.youyou.uumall.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.OrderAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.business.OrderBiz;
import com.youyou.uumall.event.OrderActFinishEvent;
import com.youyou.uumall.model.OrderBean;
import com.youyou.uumall.view.RefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
@EActivity(R.layout.activity_order_submit)
public class OrderConfirmActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, RefreshListView.OnRefreshListener, RefreshListView.OnLoadMoreListener {
    @Bean
    OrderBiz orderBiz;


    OrderAdapter orderAdapter;

    @ViewById
    RefreshListView order_submit_lv;

    @ViewById
    TextView order_submit_tv;

    @ViewById
    LinearLayout order_empty;

    private int pageNo = 1;
    private List<OrderBean> list = new ArrayList<>();
    private boolean isSatisfy;

    @AfterViews
    void afterViews() {
        orderAdapter = new OrderAdapter(this, OrderAdapter.ORDER_CCONFIRM);
        order_submit_tv.setText(R.string.order_submit_confirm_title);
        orderBiz.setArrayListCallbackInterface(this);
        order_submit_lv.setAdapter(orderAdapter);
        order_submit_lv.setOnRefreshListener(this);
        order_submit_lv.setOnLoadMoreListener(this);
        order_submit_lv.autoRefresh();
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (type == OrderBiz.QUERY_ORDER) {
            if (pageNo == 1) {//下拉刷新
                if (arrayList != null && arrayList.size() != 0) {
                    List<OrderBean> orderBean = (List<OrderBean>) arrayList;
                    isSatisfy = orderBean.size()>=10?true:false;
                    list.clear();
                    list.addAll(orderBean);
                    orderAdapter.setData(list);
                    order_submit_lv.onRefreshComplete();
                } else {
                    order_submit_lv.onRefreshComplete();
                    order_submit_lv.setVisibility(View.GONE);
                    order_empty.setVisibility(View.VISIBLE);
                }
            } else {//这个是上拉加载更多
                if (arrayList != null && arrayList.size() != 0 &&isSatisfy) {
                    List<OrderBean> orderBean = (List<OrderBean>) arrayList;
                    list.addAll(orderBean);
                    orderAdapter.setData(list);
                }
                order_submit_lv.onLoadMoreComplete();
            }

        }
    }

    @Click
    void order_submit_iv() {
        finish();
//        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
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
    public void onFinish(OrderActFinishEvent event) {
        finish();
    }

    @Override
    public void onRefreshing(boolean isAuto) {
            pageNo = 1;
            orderBiz.queryOrder(pageNo, 10, "", "orderConfirm");
    }

    @Override
    public void onLoadingMore() {
        pageNo++;
        orderBiz.queryOrder(pageNo, 10, "", "orderConfirm");
    }
}
