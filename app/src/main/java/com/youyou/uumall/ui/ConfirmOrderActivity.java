package com.youyou.uumall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.ConfirmOrderAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.OrderBiz;
import com.youyou.uumall.event.ShopCartUpdateEvent;
import com.youyou.uumall.model.ShopCartBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
@EActivity(R.layout.activity_confirm_order)
public class ConfirmOrderActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface {


    @ViewById
    LinearLayout confirm_order_info_ll;

    @ViewById
    LinearLayout confirm_order_addinfo_ll;

    @ViewById
    ListView confirm_order_lv;

    @ViewById
    TextView confirm_order_name_tv;

    @ViewById
    TextView confirm_order_address_tv;

    @ViewById
    TextView confirm_order_time_tv;

    @ViewById
    TextView confirm_total_tv;

    @ViewById
    Button confirm_buynow_bt;

    @Bean
    OrderBiz orderBiz;

    @Bean
    ConfirmOrderAdapter adapter;
    private List<ShopCartBean> mData;
    private String mName;
    private String mPhone;
    private String mDate;
    private String mFltNo;
    private String mDelivery;
    private String mDeliveryId;
       private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = new ArrayList<>();
        Intent intent = getIntent();
        int size = intent.getIntExtra("size",0);
        for (int i = 0; i < size; i++) {
            ShopCartBean bean = new ShopCartBean();
            Bundle bundleExtra = intent.getBundleExtra(i + "");
            bean.goodsId = bundleExtra.getString("goodsId");
            bean.goodsName = bundleExtra.getString("goodsName");
            bean.count = Integer.valueOf(bundleExtra.getString("count"));
            bean.subtotal = bundleExtra.getString("subtotal");
            bean.image = bundleExtra.getString("image");
            totalPrice+=Double.valueOf(bean.subtotal);
            mData.add(bean);
        }
    }

    @AfterViews
    void afterViews() {
        confirm_total_tv.setText("￥"+totalPrice);
        orderBiz.setObjectCallbackInterface(this);
        adapter.setData(mData);
        confirm_order_lv.setAdapter(adapter);
    }

    @Click
    void confirm_order_addinfo_ll() {//准备用回传数据的方式来完成下个页面数据的回传
        Intent intent = new Intent(this, DeliveryInfoActivity_.class);
        startActivityForResult(intent,0);
//        DeliveryInfoActivity_.intent(this).start();
        overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                mName = data.getStringExtra("name");
                mPhone = data.getStringExtra("phone");
                mDate = data.getStringExtra("date");
                mFltNo = data.getStringExtra("fltNO");
                mDelivery = data.getStringExtra("delivery");
                mDeliveryId = data.getStringExtra("deliveryId");
                confirm_order_addinfo_ll.setVisibility(View.GONE);
                confirm_order_info_ll.setVisibility(View.VISIBLE);
                showToastLong(mName + mPhone + mDate + mFltNo + mDelivery);
                confirm_order_name_tv.setText(mName +"    "+ mPhone);
//                confirm_order_time_tv.setText(mDate +"    "+ mFltNo);
                confirm_order_time_tv.setText(mDate);
                confirm_order_address_tv.setText(mDelivery);
                confirm_buynow_bt.setText("确认("+mData.size()+"件)");
                break;
            default:
                break;
        }
    }

    @Click
    void confirm_buynow_bt() {
        if (mData == null || mData.size() == 0) {
            return ;
        }
        if (TextUtils.isEmpty(mName)||TextUtils.isEmpty(mPhone)||TextUtils.isEmpty(mDate)||TextUtils.isEmpty(mFltNo)||TextUtils.isEmpty(mDeliveryId)){
            showToastShort("信息填写不完整");
            return ;
        }
        orderBiz.orderSubmit(mData,mName,mPhone,mDate,"1",mDeliveryId,mFltNo,"");
    }


    @Click
    void confirm_pro_iv() {
        finish();
//        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }


    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        Response response = (Response) t;
        if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
            eventBus.post(new ShopCartUpdateEvent());
            log.e("code:"+response.code+"msg:"+response.msg+"data:"+response.data+"size:"+response.size);
            Intent intent = new Intent(this,PaymentActivity_.class);
            intent.putExtra("data",(String)response.data);
            if (totalPrice == 0.0) {
                showToastShort("订单异常");
                return ;
            }
            intent.putExtra("name","油桃");
            intent.putExtra("price",totalPrice);
            startActivity(intent);
//            PaymentActivity_.intent(this).start();
//            overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);

        }else{
            showToastLong(response.msg);
        }
    }
}
