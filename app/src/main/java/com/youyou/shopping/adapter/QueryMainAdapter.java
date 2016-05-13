package com.youyou.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.shopping.R;
import com.youyou.shopping.base.BaseConstants;
import com.youyou.shopping.bean.ViewHolder;
import com.youyou.shopping.model.GoodsDescBean;
import com.youyou.shopping.model.RecommendBean;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class QueryMainAdapter extends BaseAdapter {

    @RootContext
    Context context;
    private List<GoodsDescBean> list;
    private LayoutInflater mInflater;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    OnItemClickListener listener;

    @AfterInject
    void afterInject() {
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
    }

    //对外提供设置数据的接口
    //设置数据之后刷新界面
    public void setData(List<GoodsDescBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //重写获取数量的方法
    @Override
    public int getCount() {
        return list == null ? 0 : ((list.size() + 1) / 2);
    }

    //重写获取位置的方法
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    //同上
    @Override
    public long getItemId(int position) {
        return position;
    }

    //getView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_commodity, null);
        }
        RelativeLayout rightLayout = ViewHolder.get(convertView, R.id.right_layout);
        RelativeLayout leftLayout = ViewHolder.get(convertView, R.id.left_layout);
        TextView left_commodity_name = ViewHolder.get(convertView, R.id.left_commodity_name);
        TextView left_commodity_price = ViewHolder.get(convertView, R.id.left_commodity_price);
        TextView left_commodity_origin_price = ViewHolder.get(convertView, R.id.left_commodity_origin_price);
        ImageView left_commodity_img = ViewHolder.get(convertView, R.id.left_commodity_img);

        GoodsDescBean leftitem = list.get(position * 2);
        leftLayout.setTag(leftitem.id);
        left_commodity_name.setText(leftitem.categoryName);
        left_commodity_price.setText("¥" + leftitem.price);
        left_commodity_origin_price.setText(leftitem.customizedPriceName + ":¥" + leftitem.customizedPrice);
        imageLoader.displayImage(BaseConstants.connection.ROOT_URL + leftitem.image, left_commodity_img);
        leftLayout.setOnClickListener(new AdapterClickListener());
        /**
         *  position 0    1   2
         *  list     01  23  45
         */
        TextView right_commodity_name = ViewHolder.get(convertView, R.id.right_commodity_name);
        TextView right_commodity_price = ViewHolder.get(convertView, R.id.right_commodity_price);
        TextView right_commodity_origin_price = ViewHolder.get(convertView, R.id.right_commodity_origin_price);


        if (position * 2 + 1 < list.size()) {
            rightLayout.setOnClickListener(new AdapterClickListener());
            GoodsDescBean rightitem = list.get(position * 2 + 1);
            rightLayout.setTag(rightitem.id);
            right_commodity_name.setText(rightitem.categoryName);
            right_commodity_price.setText("¥" + rightitem.price);
            right_commodity_origin_price.setText(rightitem.customizedPriceName + ":¥" + rightitem.customizedPrice);
            imageLoader.displayImage(BaseConstants.connection.ROOT_URL + rightitem.image, left_commodity_img);
            rightLayout.setVisibility(View.VISIBLE);
        } else {
            rightLayout.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    class AdapterClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.itemClick(v);
            }
        }
    }

    public interface OnItemClickListener {
        void itemClick(View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
