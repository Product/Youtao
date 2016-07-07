package com.youyou.uumall.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.ViewHolder;
import com.youyou.uumall.model.GoodsList;
import com.youyou.uumall.model.OrderBean;
import com.youyou.uumall.ui.CommodityDescActivity_;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/29.
 */
@EBean
public class OrderDetailAdapter extends BaseAdapter implements View.OnClickListener {

    private ArrayList<GoodsList> mData;
    private static final int DELIVERY = 0;
    private static final int DATE_STATUS = 1;
    private static final int MAIN = 2;
    private static final int FOOT = 3;

    @RootContext
    Context mContext;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private OnCancelClickListener cancelListener;
    private OnPayClickListener payListener;
    private OrderBean orderBean;
    private Map dataType;
    private Map dataNumbers;


    @AfterInject
    void afterInject() {
        imageLoader = ImageLoader.getInstance();
        options = MyUtils.getImageOptions();
    }

    public void setData(List<OrderBean> data) {
        orderBean = data.get(0);
        mData = orderBean.goodsList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mData != null) {
            return mData.size() == 0 ? 0 : mData.size() + 3;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        int pos = 0;
        int dataNumber = 0;
        dataType = new HashMap();
        dataNumbers = new HashMap();
        dataType.put(pos++, DELIVERY);
        dataType.put(pos++, DATE_STATUS);
        for (int i = 0; i < mData.size(); i++) {
            dataNumbers.put(pos, dataNumber++);
            dataType.put(pos++, MAIN);
        }
        dataType.put(pos++, FOOT);


        return (int) dataType.get(position);
    }


    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case DELIVERY:
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_orderdetail_delivery, null);
                }
                TextView delivery_name_tv = ViewHolder.get(convertView, R.id.delivery_name_tv);
                TextView delivery_address_tv = ViewHolder.get(convertView, R.id.delivery_address_tv);
                TextView delivery_time_tv = ViewHolder.get(convertView, R.id.delivery_time_tv);
                delivery_name_tv.setText(orderBean.name + "   " + orderBean.linkTel);
                String desc = "";
                if (orderBean.delivery.description != null) {
                    desc = orderBean.delivery.description.replace("\r\n","  ");
                }
                delivery_address_tv.setText(orderBean.delivery.name+"  "+desc);
                delivery_time_tv.setText(orderBean.pickupTime);
                break;
            case DATE_STATUS:
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_orderdetail_date_status, null);
                }
                TextView delivery_order_status_tv = ViewHolder.get(convertView, R.id.delivery_order_status_tv);
                String orderStatus = "";
                switch (orderBean.status) {
                    case "orderSubmit":
                        orderStatus = mContext.getResources().getString(R.string.order_submit_title);
                        break;
                    case "orderConfirm":
                        orderStatus = mContext.getResources().getString(R.string.order_submit_confirm_title);
                        break;
                    case "orderCancel":
                        orderStatus = mContext.getResources().getString(R.string.order_submit_cancel_title);
                        break;
                    case "orderShipping":
                        orderStatus = mContext.getResources().getString(R.string.order_submit_shipping_title);
                        break;
                    case "orderFinish":
                        orderStatus = mContext.getResources().getString(R.string.order_submit_done_title);
                        break;
                }
                delivery_order_status_tv.setText(orderStatus);
                break;
            case FOOT:
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_orderdetail_foot, null);
                }
                TextView delivery_total_count = ViewHolder.get(convertView, R.id.delivery_total_count);
                TextView delivery_total_price = ViewHolder.get(convertView, R.id.delivery_total_price);
                TextView delivery_order_id = ViewHolder.get(convertView, R.id.delivery_order_id);
                TextView delivery_create_date = ViewHolder.get(convertView, R.id.delivery_create_date);
                Button delivery_cancel_bt = ViewHolder.get(convertView, R.id.delivery_cancel_bt);
                Button delivery_pay_bt = ViewHolder.get(convertView, R.id.delivery_pay_bt);
                Button delivery_pay_bt2 = ViewHolder.get(convertView, R.id.delivery_pay_bt2);
                LinearLayout delivery_cancel_ll = ViewHolder.get(convertView, R.id.delivery_cancel_ll);

                delivery_total_count.setText("共" + orderBean.totalCnt + "件");
                delivery_total_price.setText("合计￥" + orderBean.totalPrice);
                delivery_order_id.setText("订单编号:    " + orderBean.id);
                delivery_create_date.setText("下单时间:    " + orderBean.createDate);

                if (TextUtils.equals(orderBean.status, "orderSubmit")) {//提交:有付款和取消界面
                    delivery_cancel_ll.setVisibility(View.VISIBLE);
                    delivery_pay_bt2.setVisibility(View.GONE);
                } else if (TextUtils.equals(orderBean.status, "orderConfirm")) {//待发货:有取消界面
                    delivery_cancel_ll.setVisibility(View.GONE);
                    delivery_pay_bt2.setVisibility(View.VISIBLE);
                } else {//其它界面,两个布局都消失
                    delivery_cancel_ll.setVisibility(View.GONE);
                    delivery_pay_bt2.setVisibility(View.GONE);
                }

                delivery_cancel_bt.setOnClickListener(this);
                delivery_pay_bt.setOnClickListener(this);
                delivery_pay_bt2.setOnClickListener(this);
                break;

            case MAIN:
//                GoodsList goods = orderBean.goodsList.get((Integer) map.get(position));
                GoodsList goods = orderBean.goodsList.get((Integer) dataNumbers.get(position));
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_confirm_order, null);
                }
                LinearLayout item_confirm_order_ll = ViewHolder.get(convertView, R.id.item_confirm_order_ll);
                item_confirm_order_ll.setTag(goods.goodsId);
                item_confirm_order_ll.setOnClickListener(this);

                ImageView item_confirm_order_pic_iv = ViewHolder.get(convertView, R.id.item_confirm_order_pic_iv);
                item_confirm_order_pic_iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                TextView item_confirm_order_name_tv = ViewHolder.get(convertView, R.id.item_confirm_order_name_tv);
                TextView item_confirm_order_price_tv = ViewHolder.get(convertView, R.id.item_confirm_order_price_tv);
                TextView item_confirm_order_count_tv = ViewHolder.get(convertView, R.id.item_confirm_order_count_tv);
                item_confirm_order_name_tv.setText(goods.title);
                item_confirm_order_price_tv.setText("￥" + goods.price);
//                item_confirm_order_price_tv.setText("￥" + goods.coupon==null?goods.price:goods.coupon);
                item_confirm_order_count_tv.setText("x" + goods.cnt);
                String[] pics = goods.img.split("\\|");
                imageLoader.displayImage(BaseConstants.connection.ROOT_URL + pics[0], item_confirm_order_pic_iv, options);
                break;
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delivery_pay_bt) {
            payListener.payClick();
        } else if (v.getId() == R.id.delivery_cancel_bt || v.getId() == R.id.delivery_pay_bt2) {
            cancelListener.cancelClick();
        }else if (v.getId() == R.id.item_confirm_order_ll) {
            String tag = (String) v.getTag();
            Intent intent = new Intent(mContext, CommodityDescActivity_.class);
            intent.putExtra(BaseConstants.preferencesFiled.GOODS_ID, tag);
            mContext.startActivity(intent);
        }
    }

    public interface OnPayClickListener {
        void payClick();
    }

    public void setOnPayClickListener(OnPayClickListener listener) {
        this.payListener = listener;
    }

    public interface OnCancelClickListener {
        void cancelClick();
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        this.cancelListener = listener;
    }
}
