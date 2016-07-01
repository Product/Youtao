package com.youyou.uumall.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.OrderAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.OrderBiz;
import com.youyou.uumall.event.OrderActFinishEvent;
import com.youyou.uumall.event.UpdateSubmitOrder;
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
 * Created by Administrator on 2016/5/23.
 */
@EActivity(R.layout.activity_order_submit)
public class OrderSubmitActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, BaseBusiness.ObjectCallbackInterface, RefreshListView.OnRefreshListener, RefreshListView.OnLoadMoreListener {

    @ViewById
    RefreshListView order_submit_lv;

    @Bean
    OrderBiz orderBiz;
    @ViewById
    LinearLayout order_empty;
    @ViewById
    TextView order_submit_tv;
    private OrderAdapter orderAdapter;

    private boolean isAuto;
    private int pageNo = 1;
    private List<OrderBean> list = new ArrayList<>();

    @AfterViews
    void afterViews() {
        order_submit_tv.setText(R.string.order_submit_title);
        orderAdapter = new OrderAdapter(this, OrderAdapter.ORDER_SUBMIT);
//        orderAdapter.setOnCancelClickedListener(this);
        orderBiz.setArrayListCallbackInterface(this);
        orderBiz.setObjectCallbackInterface(this);

        order_submit_lv.setAdapter(orderAdapter);
        order_submit_lv.setOnRefreshListener(this);
        order_submit_lv.setOnLoadMoreListener(this);
        order_submit_lv.autoRefresh();
    }

    @Click
    void order_submit_iv() {//首先要把
        finish();
//        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
//        log.e(arrayList.toString());
        if (type == OrderBiz.QUERY_ORDER) {
            if (!isAuto) {
                isAuto = !isAuto;
                list.clear();
                List<OrderBean> orderBean = (List<OrderBean>) arrayList;
                list.addAll(orderBean);
                orderAdapter.setData(list);
                order_submit_lv.onRefreshComplete();
                return;
            }
            if (pageNo == 1) {//是第一次调用,也就是默认刷新
                if (arrayList != null && arrayList.size() != 0) {
                    List<OrderBean> orderBean = (List<OrderBean>) arrayList;
                    orderAdapter.setData(orderBean);
                    order_submit_lv.onRefreshComplete();
//                    log.e(orderBean.toString());
                } else {
                    order_submit_lv.onRefreshComplete();
                    order_submit_lv.setVisibility(View.GONE);
                    order_empty.setVisibility(View.VISIBLE);
                }
            } else {//这个是上拉加载更多
                if (arrayList != null && arrayList.size() != 0) {
                    List<OrderBean> orderBean = (List<OrderBean>) arrayList;
                    list.addAll(orderBean);
                    orderAdapter.setData(list);
                }
                order_submit_lv.onLoadMoreComplete();
            }
        }
    }

    @Override
    public void objectCallBack(int type, Object t) {
        if (type == OrderBiz.CANCEL_ORDER) {
            if (t != null) {
                Response response = (Response) t;
                if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                    isAuto = false;
                    orderBiz.queryOrder(1, 10, "", "orderSubmit");
                }
            }
        }
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

    @Subscribe(sticky = true)
    public void onFinish(UpdateSubmitOrder event) {
        order_submit_lv.autoRefresh();
    }

    @Override
    public void onRefreshing(boolean isAuto) {
        this.isAuto = isAuto;
        if (isAuto) {
            orderBiz.queryOrder(pageNo, 10, "", "orderSubmit");
        } else {
            pageNo = 1;
            orderBiz.queryOrder(pageNo, 10, "", "orderSubmit");
        }
    }

    @Override
    public void onLoadingMore() {
        pageNo++;
        orderBiz.queryOrder(pageNo, 10, "", "orderSubmit");
    }
}
