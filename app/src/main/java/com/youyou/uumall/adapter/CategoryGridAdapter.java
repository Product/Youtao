package com.youyou.uumall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.model.BrandBean;
import com.youyou.uumall.utils.PixelUtil;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by Administrator on 2016/6/1.
 */
@EBean
public class CategoryGridAdapter extends BaseAdapter {

    @RootContext
    Context context;

    private List<BrandBean> mData;
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
            convertView = new ImageView(context);
        }
        ImageView imageView = (ImageView) convertView;
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        int px = PixelUtil.dp2px(94);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,px);
        imageView.setLayoutParams(params);
        String imageSrc = mData.get(position).imageSrc;
        imageLoader.displayImage(BaseConstants.connection.ROOT_URL+imageSrc,  imageView);
        return convertView;
    }
}
