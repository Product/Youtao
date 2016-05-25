package com.youyou.uumall.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.ConfirmOrderAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
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
@EActivity(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, BaseBusiness.ObjectCallbackInterface {
    @Bean
    OrderBiz orderBiz;

    @ViewById
    TextView order_detail_name_tv;

    @ViewById
    TextView order_detail_address_tv;

    @ViewById
    TextView order_detail_time_tv;

    @ViewById
    ListView order_detail_lv;

    @Bean
    ConfirmOrderAdapter confirmOrderAdapter;
    private OrderBean bean;

    @AfterViews
    void afterViews() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        orderBiz.setArrayListCallbackInterface(this);
        orderBiz.setObjectCallbackInterface(this);
        orderBiz.queryOrder(0,0,id,"");
        order_detail_lv.setAdapter(confirmOrderAdapter);
    }

    @Click
    void order_detail_cancel_bt() {
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

    @Click
    void order_detail_pay_bt() {
        Intent intent = new Intent(this, PaymentActivity_.class);
        intent.putExtra("data", bean.id);
        Double price = Double.valueOf(bean.totalPrice);
        intent.putExtra("price", price);
        startActivity(intent);
    }

    @Override
    public void objectCallBack(int type, Object t) {
        if (type == OrderBiz.CANCEL_ORDER) {
            if (t != null) {
                Response response = (Response) t;
                log.e(t.toString());
                if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                    OrderSubmitActivity_.intent(this).start();
                    this.overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
                    finish();
                }
            }
        }
    }


    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (type == OrderBiz.QUERY_ORDER) {
            if (arrayList != null) {
                List<OrderBean> orderBean = (List<OrderBean>) arrayList;
                bean = orderBean.get(0);
                order_detail_name_tv.setText(bean.name +"   "+ bean.linkTel);
                order_detail_address_tv.setText(bean.address.name);
                order_detail_time_tv.setText(bean.pickupTime);
                confirmOrderAdapter.setData(bean.goodsList);
                log.e(bean.toString());
            }
        }
    }
}
