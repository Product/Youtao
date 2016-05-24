package com.youyou.uumall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.ViewHolder;
import com.youyou.uumall.model.ShopCartBean;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
@EBean
public class ShopcartAdapter extends BaseAdapter implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener{
    public static final String DEL_ONE = "delOne";
    public static final String DEL_ALL = "delAll";
    @RootContext
    Context mContext;

    LayoutInflater mInflater;
    List<ShopCartBean> dictList;
    private ImageLoader imageLoader;
    private OnInsertClickListener insertclicklistener;
    private OnDeleteClickListener deleteclicklistener;
    private OnItemCheckedListener checkedListener;

    @AfterInject
    void afterInject() {
        mInflater = LayoutInflater.from(mContext);
        imageLoader = ImageLoader.getInstance();
    }

    public void setData(List dictList) {
        this.dictList = dictList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dictList == null ? 0 : dictList.size();
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
        TextView item_shopcart_name_tv = ViewHolder.get(convertView, R.id.item_shopcart_name_tv);
        TextView item_shopcart_price_tv = ViewHolder.get(convertView, R.id.item_shopcart_price_tv);
        TextView item_shopcart_mid_tv = ViewHolder.get(convertView, R.id.item_shopcart_mid_tv);
        TextView item_shopcart_delete_tv = ViewHolder.get(convertView, R.id.item_shopcart_delete_tv);
        ImageView item_shopcart_pic_iv = ViewHolder.get(convertView, R.id.item_shopcart_pic_iv);
        //给这个图片添加个tag用于传递数据信息
        ImageView item_shopcart_up_iv = ViewHolder.get(convertView, R.id.item_shopcart_up_iv);
        ImageView item_shopcart_down_iv = ViewHolder.get(convertView, R.id.item_shopcart_down_iv);
        CheckBox item_shopcart_check_cb = ViewHolder.get(convertView, R.id.item_shopcart_check_cb);

        ShopCartBean item = dictList.get(position);
        item_shopcart_delete_tv.setTag(item.goodsId);
        item_shopcart_check_cb.setTag(item.goodsId);
        item_shopcart_up_iv.setTag(item.goodsId);
        item_shopcart_down_iv.setTag(item.goodsId);
        item_shopcart_check_cb.setChecked(item.isCheck);
        item_shopcart_name_tv.setText(item.goodsName);
        item_shopcart_price_tv.setText(item.subtotal);
        item_shopcart_mid_tv.setText(item.count+"");

        String[] split = item.image.split("\\|");
        imageLoader.displayImage(BaseConstants.connection.ROOT_URL + split[0], item_shopcart_pic_iv);

        item_shopcart_up_iv.setOnClickListener(this);
        item_shopcart_down_iv.setOnClickListener(this);
        item_shopcart_delete_tv.setOnClickListener(this);
        item_shopcart_check_cb.setOnCheckedChangeListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_shopcart_up_iv:
                if (insertclicklistener != null) {
                    insertclicklistener.insertGoods((String) v.getTag());
                }
                break;
            case R.id.item_shopcart_down_iv:
                if (deleteclicklistener != null) {
                    deleteclicklistener.deleteGoods((String) v.getTag(), DEL_ONE);
                }
                break;
            case R.id.item_shopcart_delete_tv:
                if (deleteclicklistener != null) {
                    deleteclicklistener.deleteGoods((String) v.getTag(), DEL_ALL);
                }
                break;
            default:

                break;
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (checkedListener != null) {
            checkedListener.onCheckedChanged(buttonView,isChecked);
        }
    }

    public void setOnInsertClickListener(OnInsertClickListener listener) {
        this.insertclicklistener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteclicklistener = listener;
    }

    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        this.checkedListener=listener;
    }

    public interface OnItemCheckedListener{
        void onCheckedChanged(View view ,boolean isChecked);
    }


    public interface OnInsertClickListener {
        void insertGoods(String tag);
    }

    public interface OnDeleteClickListener {
        void deleteGoods(String tag,String view);
    }
}
