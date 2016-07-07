package com.youyou.uumall.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.bean.ViewHolder;
import com.youyou.uumall.model.BonusBean;
import com.youyou.uumall.utils.PixelUtil;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            convertView = View.inflate(context, R.layout.item_bonus,null);
        }
        TextView bonus_price_sign = ViewHolder.get(convertView, R.id.bonus_price_sign);
        TextView bonus_price = ViewHolder.get(convertView, R.id.bonus_price);
        TextView bonus_expiry_date = ViewHolder.get(convertView, R.id.bonus_expiry_date);
        TextView bonus_explain = ViewHolder.get(convertView, R.id.bonus_explain);
        ImageView bonus_expiry_iv = ViewHolder.get(convertView, R.id.bonus_expiry_iv);

        BonusBean bonus = mData.get(position);
        String[] expirys = bonus.expiryDate.split(" ");
        bonus_expiry_date.setText("有效期至"+expirys[0]);
        //设置价格
        double price = bonus.value;
        if (price >= 100) {
            bonus_price.setTextSize(TypedValue.COMPLEX_UNIT_PX,PixelUtil.dp2px(40));
        } else if ((price < 100)) {
            bonus_price.setTextSize(TypedValue.COMPLEX_UNIT_PX,PixelUtil.dp2px(63));
        }
        String str = String.valueOf(price);
        str = str.replace(".0","");
        bonus_price.setText(str);

        //判断过期时间修改样式
        String expiryDate =  bonus.expiryDate;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//2016-06-17 16:19:56
        try {
            Date expiry = formatter.parse(expiryDate);
            if (System.currentTimeMillis() - expiry.getTime() >= 0) {//过期了
                bonus_price_sign.setTextColor(context.getResources().getColor(R.color.bg_line_gray));
                bonus_price.setTextColor(context.getResources().getColor(R.color.bg_line_gray));
                bonus_expiry_date.setTextColor(context.getResources().getColor(R.color.bg_line_gray));
                bonus_explain.setVisibility(View.GONE);
                bonus_expiry_iv.setVisibility(View.VISIBLE);
            } else {//没过期
                bonus_price_sign.setTextColor(context.getResources().getColor(R.color.font_register_smscode));
                bonus_price.setTextColor(context.getResources().getColor(R.color.font_register_smscode));
                bonus_expiry_date.setTextColor(context.getResources().getColor(R.color.font_black));
                bonus_explain.setVisibility(View.VISIBLE);
                bonus_expiry_iv.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
