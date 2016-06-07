package com.youyou.uumall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.model.BonusBean;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by Administrator on 2016/6/1.
 */
@EBean
public class BonusAdapter extends BaseAdapter {

    @RootContext
    Context context;

    private List<BonusBean> mData;
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
        return mData == null ? 10 : mData.size();

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
            convertView = View.inflate(context, R.layout.item_bonus,null);
        }
        return convertView;
    }
}
