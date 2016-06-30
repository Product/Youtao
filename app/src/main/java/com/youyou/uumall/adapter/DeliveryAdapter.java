package com.youyou.uumall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.bean.ViewHolder;
import com.youyou.uumall.model.DictBean;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30.
 */
@EBean
public class DeliveryAdapter extends BaseAdapter {

    @RootContext
    Context context;

    private List<DictBean> dicts;

    public void setData(List<DictBean> dicts) {
        this.dicts = dicts;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return dicts == null ? 0 : dicts.size();
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
            convertView = View.inflate(context, R.layout.item_delivery_child,null);
        }
        TextView delivery_info_child_tv = ViewHolder.get(convertView, R.id.delivery_info_child_tv);
        delivery_info_child_tv.setText(dicts.get(position).deliveryName);
        return convertView;
    }
}
