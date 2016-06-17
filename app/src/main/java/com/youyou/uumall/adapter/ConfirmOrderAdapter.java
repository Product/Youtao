package com.youyou.uumall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.ViewHolder;
import com.youyou.uumall.model.GoodsList;
import com.youyou.uumall.model.ShopCartBean;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
@EBean
public class ConfirmOrderAdapter extends BaseAdapter{
    @RootContext
    Context mContext;

    List mData;
    private ImageLoader imageLoader;

    @AfterInject
    void afterInject() {
        imageLoader = ImageLoader.getInstance();
    }

    public void setData(List mData){
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData ==null? 0 : mData.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext,R.layout.item_confirm_order, null);
        }
            ImageView item_confirm_order_pic_iv = ViewHolder.get(convertView, R.id.item_confirm_order_pic_iv);
            TextView item_confirm_order_name_tv = ViewHolder.get(convertView, R.id.item_confirm_order_name_tv);
            TextView item_confirm_order_price_tv = ViewHolder.get(convertView, R.id.item_confirm_order_price_tv);
            TextView item_confirm_order_count_tv = ViewHolder.get(convertView, R.id.item_confirm_order_count_tv);
            View item_confirm_order_line = ViewHolder.get(convertView, R.id.item_confirm_order_line);
        Object data = mData.get(position);
        if (data instanceof  ShopCartBean){
        ShopCartBean bean = (ShopCartBean) data;
        String[] pics = bean.image.split("\\|");
        item_confirm_order_name_tv.setText(bean.goodsName);
        item_confirm_order_price_tv.setText("￥"+bean.subtotal);
        item_confirm_order_count_tv.setText("x"+bean.count);
        imageLoader.displayImage(BaseConstants.connection.ROOT_URL + pics[0],item_confirm_order_pic_iv);
        }else if (data instanceof GoodsList){
            GoodsList bean = (GoodsList) data;
            item_confirm_order_name_tv.setText(bean.title);
            item_confirm_order_price_tv.setText("￥"+bean.coupon);
            item_confirm_order_count_tv.setText("x"+bean.cnt);
            String[] pics = bean.img.split("\\|");
            imageLoader.displayImage(BaseConstants.connection.ROOT_URL + pics[0],item_confirm_order_pic_iv);
        }
        if (getCount() - 1 == position) {
            item_confirm_order_line.setVisibility(View.GONE);
        }
        return convertView;
    }
}
