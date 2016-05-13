package com.youyou.shopping.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.shopping.R;
import com.youyou.shopping.base.BaseConstants;
import com.youyou.shopping.bean.ViewHolder;
import com.youyou.shopping.model.ShopCartBean;
import com.youyou.shopping.utils.MyUtils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
@EBean
public class ShopcartAdapter extends BaseAdapter{
    @RootContext
    Context mContext;

    LayoutInflater mInflater;
    List<ShopCartBean> dictList;
    private ImageLoader imageLoader;


    @AfterInject
    void afterInject() {
        mInflater = LayoutInflater.from(mContext);
        imageLoader = ImageLoader.getInstance();
    }

    public void setData(List dictList){
        this.dictList = dictList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dictList==null? 0 : dictList.size();
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
            convertView = mInflater.inflate(R.layout.item_shopcart, null);
        }
        TextView item_shopcart_name_tv = ViewHolder.get(convertView, R.id.item_shopcart_name_tv);
        TextView item_shopcart_price_tv = ViewHolder.get(convertView, R.id.item_shopcart_price_tv);
        TextView item_shopcart_mid_tv = ViewHolder.get(convertView, R.id.item_shopcart_mid_tv);
        ImageView item_shopcart_pic_iv = ViewHolder.get(convertView, R.id.item_shopcart_pic_iv);
        ShopCartBean item = dictList.get(position);
        item_shopcart_name_tv.setText(item.goodsName);
        item_shopcart_price_tv.setText(item.price);
        item_shopcart_mid_tv.setText(item.count);
        imageLoader.displayImage(BaseConstants.connection.ROOT_URL+item.image,item_shopcart_pic_iv);
        return convertView;
    }
}
