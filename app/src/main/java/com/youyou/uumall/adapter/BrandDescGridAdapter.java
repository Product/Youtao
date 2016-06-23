package com.youyou.uumall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
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
    private DisplayImageOptions options;

    public void setData(List mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @AfterInject
    void afterInject() {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.order_empty_3x) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.order_empty_3x) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.order_empty_3x) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build(); // 构建完成
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
        ImageView item_brand_desc_iv = ViewHolder.get(convertView, R.id.item_brand_desc_iv,true);
        TextView item_brand_desc_title_tv = ViewHolder.get(convertView, R.id.item_brand_desc_title_tv);
        TextView item_brand_desc_price1_tv = ViewHolder.get(convertView, R.id.item_brand_desc_price1_tv);
        TextView item_brand_desc_price2_tv = ViewHolder.get(convertView, R.id.item_brand_desc_price2_tv);

        GoodsDescBean goodsDescBean = mData.get(position);
        String image = goodsDescBean.image.split("\\|")[0];
        imageLoader.displayImage(BaseConstants.connection.ROOT_URL+image,item_brand_desc_iv,options);
        item_brand_desc_title_tv.setText(goodsDescBean.titile);
        item_brand_desc_price1_tv.setText("￥"+goodsDescBean.price);
        item_brand_desc_price2_tv.setText("乐天价：￥"+goodsDescBean.price);
        return convertView;
    }
}
