package com.youyou.uumall.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ListView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.OrderDetailAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.OrderBiz;
import com.youyou.uumall.event.MineTriggerEvent;
import com.youyou.uumall.event.OrderActFinishEvent;
import com.youyou.uumall.event.UpdateAllOrder;
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
@EActivity(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity implements  BaseBusiness.ObjectCallbackInterface, OrderDetailAdapter.OnCancelClickListener, OrderDetailAdapter.OnPayClickListener {
    @Bean
    OrderBiz orderBiz;


    @ViewById
    ListView order_detail_lv;


    @Bean
    OrderDetailAdapter orderDetailAdapter;
    private OrderBean bean;

    @AfterViews
    void afterViews() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        orderBiz.setObjectCallbackInterface(this);
        orderBiz.queryOrder(0, 0, id, "");
        order_detail_lv.setAdapter(orderDetailAdapter);
        orderDetailAdapter.setOnCancelClickListener(this);
        orderDetailAdapter.setOnPayClickListener(this);
    }


    @Click
    void order_detail_pro_iv() {
        finish();
    }

    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (type == OrderBiz.CANCEL_ORDER) {
            if (t != null) {
                Response response = (Response) t;
                log.e(t.toString());
                if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                    eventBus.postSticky(new UpdateAllOrder());
                    eventBus.post(new MineTriggerEvent());//取消成功后重新改变原点状态
                    OrderAllActivity_.intent(this).start();
                    eventBus.post(new OrderActFinishEvent());
                    finish();
                }
            }
        }else if (type == OrderBiz.QUERY_ORDER) {
            if (t != null) {
                Response response = (Response) t;
                List<OrderBean> orderBean = (List<OrderBean>)response.data ;
                bean = orderBean.get(0);
                orderDetailAdapter.setData(orderBean);
//                log.e(bean.toString());
            }
        }
    }

    @Override
    public void cancelClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_cancel_title);
        builder.setMessage(R.string.dialog_cancel_message);
        builder.setPositiveButton(R.string.dialog_cancel_pos, null);
        builder.setNegativeButton(R.string.dialog_cancel_neg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orderBiz.cancelOrder(bean.id);
            }
        });
        builder.show();
    }

    @Override
    public void payClick() {
        Intent intent = new Intent(this, PaymentActivity_.class);
        intent.putExtra("data", bean.id);
        Double price = Double.valueOf(bean.totalPrice);
        intent.putExtra("price", price);
        startActivity(intent);
    }

}
