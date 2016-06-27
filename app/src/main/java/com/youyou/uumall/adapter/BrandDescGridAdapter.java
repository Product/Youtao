package com.youyou.uumall.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
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
import com.youyou.uumall.model.GoodsDescBean;
import com.youyou.uumall.utils.PixelUtil;

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
    OnItemClickListener listener;
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
        return mData == null ? 0 : (mData.size()+1)/2;

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
            convertView = View.inflate(context, R.layout.item_commodity, null);
        }
        RelativeLayout rightLayout = ViewHolder.get(convertView, R.id.right_layout);
        RelativeLayout leftLayout = ViewHolder.get(convertView, R.id.left_layout);
        TextView left_commodity_name = ViewHolder.get(convertView, R.id.left_commodity_name);
        TextView left_commodity_price = ViewHolder.get(convertView, R.id.left_commodity_price);
        TextView left_commodity_origin_price = ViewHolder.get(convertView, R.id.left_commodity_origin_price);
        ImageView left_commodity_img = ViewHolder.get(convertView, R.id.left_commodity_img);
        setHeight(left_commodity_img);

        GoodsDescBean goodsDescBean = mData.get(position* 2);
        leftLayout.setTag(goodsDescBean.id);
        String image = goodsDescBean.image.split("\\|")[0];
        imageLoader.displayImage(BaseConstants.connection.ROOT_URL+image,left_commodity_img,options);
        left_commodity_name.setText(goodsDescBean.titile);
        left_commodity_price.setText("￥"+goodsDescBean.price);
        left_commodity_origin_price.setText("乐天价：￥"+goodsDescBean.price);
        leftLayout.setOnClickListener(new AdapterClickListener());

        TextView right_commodity_name = ViewHolder.get(convertView, R.id.right_commodity_name);
        TextView right_commodity_price = ViewHolder.get(convertView, R.id.right_commodity_price);
        TextView right_commodity_origin_price = ViewHolder.get(convertView, R.id.right_commodity_origin_price);
        ImageView right_commodity_img = ViewHolder.get(convertView, R.id.right_commodity_img);
        setHeight(right_commodity_img);
        if (position * 2 + 1 < mData.size()) {
            rightLayout.setOnClickListener(new AdapterClickListener());
            GoodsDescBean rightitem = mData.get(position * 2 + 1);
            rightLayout.setTag(rightitem.id);
            right_commodity_name.setText(rightitem.titile);
            right_commodity_price.setText("¥" + rightitem.price);
            right_commodity_origin_price.setText("乐天价：￥"+rightitem.price);
            String rightImage = rightitem.image.split("\\|")[0];
            imageLoader.displayImage(BaseConstants.connection.ROOT_URL + rightImage, right_commodity_img,options);
            rightLayout.setVisibility(View.VISIBLE);
        } else {
            rightLayout.setVisibility(View.INVISIBLE);
        }
        return convertView;
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
