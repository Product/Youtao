package com.youyou.uumall.ui;

import android.widget.ListView;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.OrderAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.business.OrderBiz;
import com.youyou.uumall.model.OrderBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
@EActivity(R.layout.activity_order_submit)
public class OrderAllActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface {
    @Bean
    OrderBiz orderBiz;

    OrderAdapter orderAdapter;

    @ViewById
    ListView order_submit_lv;

    @ViewById
    TextView order_submit_tv;

    @AfterViews
    void afterViews() {
        orderAdapter = new OrderAdapter(this,OrderAdapter.ORDER_ALL);
        order_submit_tv.setText(R.string.order_submit_all_title);
        orderBiz.setArrayListCallbackInterface(this);
        orderBiz.queryOrder(0,0,"","");
        order_submit_lv.setAdapter(orderAdapter);
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (type == OrderBiz.QUERY_ORDER) {
            if (arrayList != null) {
                List<OrderBean> orderBean = (List<OrderBean>) arrayList;
                orderAdapter.setData(orderBean);
            }
        }
    }

    @Click
    void order_submit_iv() {
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }
}
