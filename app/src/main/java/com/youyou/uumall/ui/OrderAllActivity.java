package com.youyou.uumall.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.OrderAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.OrderBiz;
import com.youyou.uumall.event.UpdateAllOrder;
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
public class OrderAllActivity extends BaseActivity implements RefreshListView.OnRefreshListener, RefreshListView.OnLoadMoreListener, BaseBusiness.ObjectCallbackInterface {
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
        orderAdapter = new OrderAdapter(this, OrderAdapter.ORDER_ALL);
        order_submit_tv.setText(R.string.order_submit_all_title);
        orderBiz.setObjectCallbackInterface(this);
        order_submit_lv.setAdapter(orderAdapter);
        order_submit_lv.setOnRefreshListener(this);
        order_submit_lv.setOnLoadMoreListener(this);
        order_submit_lv.autoRefresh();
    }

    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (type == OrderBiz.QUERY_ORDER) {
            if (t == null) {
                return;
            }
            Response response = (Response) t;
            List<OrderBean> arrayList = (List<OrderBean>) response.data;
            if (pageNo == 1) {//是第一次调用,也就是默认刷新
                if (arrayList == null) {
                    return;
                }
                if (arrayList.size() != 0 && response.size != 0) {
                    isSatisfy = arrayList.size() >= 10 ? true : false;
                    list.clear();
                    for (int i = 0; i < response.size; i++) {
                        list.add(arrayList.get(i));
                    }
                    orderAdapter.setData(list);
                    order_submit_lv.onRefreshComplete();
                } else {
                    order_submit_lv.onRefreshComplete();
                    order_submit_lv.setVisibility(View.GONE);
                    order_empty.setVisibility(View.VISIBLE);
                }

            } else {//这个是上拉加载更多
                if (arrayList != null && arrayList.size() != 0 && isSatisfy) {
                    list.addAll(arrayList);
                    orderAdapter.setData(list);
                }
                order_submit_lv.onLoadMoreComplete();
            }

        }
    }

    @Click
    void order_submit_iv() {
        MainActivity_.intent(this).start();
        finish();
//        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @Override
    public void onBackPressed() {
        MainActivity_.intent(this).start();
        super.onBackPressed();
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

    @Subscribe(sticky = true)
    public void onFinish(UpdateAllOrder event) {
        order_submit_lv.autoRefresh();
    }

    @Override
    public void onRefreshing(boolean isAuto) {
        pageNo = 1;
        orderBiz.queryOrder(pageNo, 10, "", "");
    }

    @Override
    public void onLoadingMore() {
        pageNo++;
        orderBiz.queryOrder(pageNo, 10, "", "");
    }
}
