package com.youyou.uumall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.youyou.uumall.ui.OrderDetailActivity_;
import com.youyou.uumall.ui.PaymentActivity_;
import com.youyou.uumall.utils.MyUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/23.
 */

public class OrderAdapter extends BaseAdapter implements View.OnClickListener {

    private final DisplayImageOptions options;
    Context mContext;
    private int type = -1;
    public static final int ORDER_SUBMIT = 0;
    public static final int ORDER_CCONFIRM = 1;
    public static final int ORDER_SHIPPING = 2;
    public static final int ORDER_ALL = 3;


    public static final int HEAD_TYPE = 0;
    public static final int MAIN_TYPE = 1;
    public static final int FOOT_TYPE = 2;
    public static final int SUBMIT_TYPE = 3;
    private List<OrderBean> mData;
    private Map<Integer, Integer> typeData;
    private Map<Integer, Integer> itemPosition;
//    private OnCancelClickedListener listener;
    private ImageLoader imageLoader;

    public OrderAdapter(Context mContext, int type) {
        this.mContext = mContext;
        this.type = type;
        imageLoader = ImageLoader.getInstance();
        options = MyUtils.getImageOptions();
    }


    public void setData(List<OrderBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mData != null) {
            switch (type) {
                case ORDER_SUBMIT:
                    for (OrderBean bean : mData) {
                        count += 3 + bean.goodsList.size();
                    }
                    break;
                case ORDER_SHIPPING:
                case ORDER_CCONFIRM:
                case ORDER_ALL:
                    for (OrderBean bean : mData) {
                        count += 2 + bean.goodsList.size();
                    }
                    break;
            }


        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        // 0:头布局    1:正文布局  2:脚布局   3:付款布局
        if (mData == null) {
            return 0;
        }
        int pos = 0;
        Map<Integer, Integer> typeMap = new HashMap();
        typeData = new HashMap();
        itemPosition = new HashMap();
        for (int i = 0; i < mData.size(); i++) {
            OrderBean bean = mData.get(i);
            int size = bean.goodsList.size();
            typeData.put(pos, i);
            typeMap.put(pos++, HEAD_TYPE);
            for (int j = 0; j < size; j++) {
                itemPosition.put(pos, j);
                typeData.put(pos, i);
                typeMap.put(pos++, MAIN_TYPE);
            }
            typeData.put(pos, i);
            typeMap.put(pos++, FOOT_TYPE);
            if (type == ORDER_SUBMIT) {//如果是未付款,就加上这个条目
                typeData.put(pos, i);
                typeMap.put(pos++, SUBMIT_TYPE);
            }
        }
        return typeMap.get(position);
    }

    @Override
    public int getViewTypeCount() {
        int count = 0;
        switch (type) {
            case ORDER_SUBMIT:
                count = 4;
                break;
            case ORDER_SHIPPING:
            case ORDER_CCONFIRM:
            case ORDER_ALL:
                count = 3;
                break;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        OrderBean bean = mData.get(typeData.get(position));
        switch (type) {
            case HEAD_TYPE:
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_order_head, null);
                }
                TextView item_order_date_tv = ViewHolder.get(convertView, R.id.item_order_date_tv);
                TextView item_order_state_tv = ViewHolder.get(convertView, R.id.item_order_state_tv);
                item_order_date_tv.setText(bean.createDate);
                switch (bean.status) {
                    case "orderSubmit":
                        item_order_state_tv.setText(R.string.order_submit_title);
                        break;
                    case "orderConfirm":
                        item_order_state_tv.setText(R.string.order_submit_confirm_title);
                        break;
                    case "orderShipping":
                        item_order_state_tv.setText(R.string.order_submit_shipping_title);
                        break;
                    case "orderCancle":
                        item_order_state_tv.setText(R.string.order_submit_cancel_title);
                        break;
                    case "orderFinish":
                        item_order_state_tv.setText(R.string.order_submit_done_title);
                        break;
                    default:
                        item_order_state_tv.setText("");
                        break;
                }
                break;
            case MAIN_TYPE:
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_confirm_order, null);
                }
                int itemPos = itemPosition.get(position);
                ImageView item_confirm_order_pic_iv = ViewHolder.get(convertView, R.id.item_confirm_order_pic_iv);
                item_confirm_order_pic_iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                TextView item_confirm_order_name_tv = ViewHolder.get(convertView, R.id.item_confirm_order_name_tv);
                TextView item_confirm_order_count_tv = ViewHolder.get(convertView, R.id.item_confirm_order_count_tv);
                TextView item_confirm_order_price_tv = ViewHolder.get(convertView, R.id.item_confirm_order_price_tv);
                LinearLayout item_confirm_order_ll = ViewHolder.get(convertView, R.id.item_confirm_order_ll);
                GoodsList goodsList = bean.goodsList.get(itemPos);
                item_confirm_order_ll.setTag(bean.id);
                item_confirm_order_ll.setOnClickListener(this);
                item_confirm_order_name_tv.setText(goodsList.title == null ? "" : goodsList.title);
                item_confirm_order_count_tv.setText("x" + goodsList.cnt);
                item_confirm_order_price_tv.setText("￥" + goodsList.coupon);
                String[] pics = goodsList.img.split("\\|");
                imageLoader.displayImage(BaseConstants.connection.ROOT_URL + pics[0], item_confirm_order_pic_iv,options);
                break;
            case FOOT_TYPE:
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_order_foot, null);
                }
                TextView item_order_count_tv = ViewHolder.get(convertView, R.id.item_order_count_tv);
                TextView item_order_price_tv = ViewHolder.get(convertView, R.id.item_order_price_tv);
                OrderBean orderBean = bean;
                item_order_count_tv.setText("共" + orderBean.totalCnt + "件");
                item_order_price_tv.setText("合计:￥" + orderBean.totalPrice);

                break;
            case SUBMIT_TYPE:
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_order_submit, null);
                }
                Button item_order_submit_btn = ViewHolder.get(convertView, R.id.item_order_submit_btn);
                item_order_submit_btn.setTag(position);
                item_order_submit_btn.setOnClickListener(this);
//                Button item_order_cancel_btn = ViewHolder.get(convertView, R.id.item_order_cancel_btn);
//                item_order_cancel_btn.setTag(position);
//                item_order_cancel_btn.setOnClickListener(this);
                break;

        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        Activity activity = (Activity) mContext;
        if (v.getId() == R.id.item_order_submit_btn) {
            OrderBean orderBean = mData.get(typeData.get(v.getTag()));
            Intent intent = new Intent(mContext, PaymentActivity_.class);
            intent.putExtra("data", orderBean.id);
            Double price = Double.valueOf(orderBean.totalCoupon);
            intent.putExtra("price", price);
            mContext.startActivity(intent);
        }
//        else if (v.getId() == R.id.item_order_cancel_btn) {
//            OrderBean orderBean = mData.get(typeData.get(v.getTag()));
//            String orderId = orderBean.id;
//            if (listener != null) {
//                listener.cancel(orderId);
//            }
//        }
        else if (v.getId() == R.id.item_confirm_order_ll) {
            String id = (String) v.getTag();
            Intent intent = new Intent(activity, OrderDetailActivity_.class);
            intent.putExtra("id", id);
            activity.startActivity(intent);
        }
//        activity.overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

//    public void setOnCancelClickedListener(OnCancelClickedListener listener) {
//        this.listener = listener;
//    }

//    public interface OnCancelClickedListener {
//        void cancel(String orderId);
//    }
}
