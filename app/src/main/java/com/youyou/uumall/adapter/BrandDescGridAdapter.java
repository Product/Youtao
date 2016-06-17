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
import com.youyou.uumall.model.GoodsDescBean;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by Administrator on 2016/6/1.
 */
@EBean
public class BrandDescGridAdapter extends BaseAdapter {

    @RootContext
    Context context;
    private List<GoodsDescBean> mData;
    private ImageLoader imageLoader;

    public void setData(List mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @AfterInject
    void afterInject() {
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();

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
            convertView = View.inflate(context, R.layout.item_brand_desc, null);
        }
        ImageView item_brand_desc_iv = ViewHolder.get(convertView, R.id.item_brand_desc_iv);
        TextView item_brand_desc_title_tv = ViewHolder.get(convertView, R.id.item_brand_desc_title_tv);
        TextView item_brand_desc_price1_tv = ViewHolder.get(convertView, R.id.item_brand_desc_price1_tv);
        TextView item_brand_desc_price2_tv = ViewHolder.get(convertView, R.id.item_brand_desc_price2_tv);

        GoodsDescBean goodsDescBean = mData.get(position);
        String image = goodsDescBean.image.split("\\|")[0];
        imageLoader.displayImage(BaseConstants.connection.ROOT_URL+image,item_brand_desc_iv);
        item_brand_desc_title_tv.setText(goodsDescBean.titile);
        item_brand_desc_price1_tv.setText("￥"+goodsDescBean.price);
        item_brand_desc_price2_tv.setText("乐天价：￥"+goodsDescBean.price);
        return convertView;
    }
}
