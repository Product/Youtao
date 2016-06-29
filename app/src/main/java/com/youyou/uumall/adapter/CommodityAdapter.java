package com.youyou.uumall.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.ViewHolder;
import com.youyou.uumall.model.RecommendBean;
import com.youyou.uumall.utils.MyUtils;
import com.youyou.uumall.utils.PixelUtil;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class CommodityAdapter extends BaseAdapter {

    @RootContext
    Context context;
    private List<RecommendBean> list;
    private LayoutInflater mInflater;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    OnItemClickListener listener;

    @AfterInject
    void afterInject() {
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        options = MyUtils.getImageOptions();
    }

    //对外提供设置数据的接口
    //设置数据之后刷新界面
    public void setData(List<RecommendBean> list) {
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
        setHeight(left_commodity_img);

        RecommendBean leftitem = list.get(position * 2);
        leftLayout.setTag(leftitem.id);
        left_commodity_name.setText(leftitem.name);
        left_commodity_price.setText("¥" + leftitem.price);
        left_commodity_origin_price.setText(leftitem.customizedPriceName + ":¥" + leftitem.customizedPrice);
        imageLoader.displayImage(BaseConstants.connection.ROOT_URL + leftitem.location, left_commodity_img,options);
        leftLayout.setOnClickListener(new AdapterClickListener());
        /**
         *  position 0    1   2
         *  list     01  23  45
         */
        TextView right_commodity_name = ViewHolder.get(convertView, R.id.right_commodity_name);
        TextView right_commodity_price = ViewHolder.get(convertView, R.id.right_commodity_price);
        TextView right_commodity_origin_price = ViewHolder.get(convertView, R.id.right_commodity_origin_price);
        ImageView right_commodity_img = ViewHolder.get(convertView, R.id.right_commodity_img);
        setHeight(right_commodity_img);
        if (position * 2 + 1 < list.size()) {
            rightLayout.setOnClickListener(new AdapterClickListener());
            RecommendBean rightitem = list.get(position * 2 + 1);
            rightLayout.setTag(rightitem.id);
            right_commodity_name.setText(rightitem.name);
            right_commodity_price.setText("¥" + rightitem.price);
            right_commodity_origin_price.setText(rightitem.customizedPriceName + ":¥" + rightitem.customizedPrice);
            imageLoader.displayImage(BaseConstants.connection.ROOT_URL + rightitem.location, right_commodity_img,options);
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

    private void setHeight(ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        DisplayMetrics metric = new DisplayMetrics();
        Activity context = (Activity) this.context;
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int mScreenWidth = metric.widthPixels;
        int width = mScreenWidth/2- PixelUtil.dp2px(20);
//        int mScreenHeight = metric.heightPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, (int) (width/1.28));
        imageView.setLayoutParams(params);
    }
}
