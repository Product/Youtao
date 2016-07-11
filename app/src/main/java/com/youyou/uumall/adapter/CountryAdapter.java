package com.youyou.uumall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.ViewHolder;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
@EBean
public class CountryAdapter extends BaseAdapter{
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
        return dictList ==null? 0 : dictList.size();
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
            convertView = mInflater.inflate(R.layout.item_country, null);
        }
        TextView item_country_tv = ViewHolder.get(convertView, R.id.item_country_tv);
        LinearLayout item_country_ll = ViewHolder.get(convertView, R.id.item_country_ll);
        String defaultCountry = MyUtils.getPara(BaseConstants.preferencesFiled.DEFAULT_COUNTRY, mContext);
        String currentCountry = dictList.get(position).split(",")[0];
        if (TextUtils.equals(defaultCountry,currentCountry)){
            item_country_tv.setTextColor(mContext.getResources().getColor(R.color.font_country_conuntry));
        }
        item_country_tv.setText(currentCountry);
        return convertView;
    }
}
