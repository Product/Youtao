package com.youyou.uumall.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.OrderAdapter;
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
 * Created by Administrator on 2016/5/23.
 */
@EActivity(R.layout.activity_order_submit)
public class OrderSubmitActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, BaseBusiness.ObjectCallbackInterface{

    @ViewById
    ListView order_submit_lv;

    @Bean
    OrderBiz orderBiz;
    @ViewById
    LinearLayout order_empty;

    private OrderAdapter orderAdapter;


    @AfterViews
    void afterViews() {
        orderAdapter = new OrderAdapter(this, OrderAdapter.ORDER_SUBMIT);
//        orderAdapter.setOnCancelClickedListener(this);
        orderBiz.setArrayListCallbackInterface(this);
        orderBiz.setObjectCallbackInterface(this);
        orderBiz.queryOrder(0, 0, "", "orderSubmit");
        order_submit_lv.setAdapter(orderAdapter);

    }

    @Click
    void order_submit_iv() {
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (type == OrderBiz.QUERY_ORDER) {
            if (arrayList != null&&arrayList.size()!=0) {
                List<OrderBean> orderBean = (List<OrderBean>) arrayList;
//                if (orderBean == null) {
//                    order_submit_lv.setVisibility(View.GONE);
//                    order_empty.setVisibility(View.VISIBLE);
//                    return ;
//                }
                orderAdapter.setData(orderBean);
                log.e(orderBean.toString());
            }else{
                order_submit_lv.setVisibility(View.GONE);
                order_empty.setVisibility(View.VISIBLE);
                return ;
            }
        }
    }

    @Override
    public void objectCallBack(int type, Object t) {
        if (type == OrderBiz.CANCEL_ORDER) {
            if (t != null) {
                Response response = (Response) t;
                if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                    orderBiz.queryOrder(0, 0, "", "orderSubmit");
                }
            }
        }
    }

//    @Override
//    public void cancel(final String orderId) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.dialog_cancel_title);
//        builder.setMessage(R.string.dialog_cancel_message);
//        builder.setPositiveButton(R.string.dialog_cancel_pos, null);
//        builder.setNegativeButton(R.string.dialog_cancel_neg, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                orderBiz.cancelOrder(orderId);
//            }
//        });
//        builder.show();
//    }
}
