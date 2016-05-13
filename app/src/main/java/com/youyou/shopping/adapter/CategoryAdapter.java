package com.youyou.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.youyou.shopping.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
@EBean
public class CategoryAdapter extends BaseAdapter{
    @RootContext
    Context mContext;

    LayoutInflater mInflater;
    List<String> dictList;


    @AfterInject
    void afterInject() {
        mInflater = LayoutInflater.from(mContext);
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
        return convertView;
    }
}
