package com.youyou.uumall.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.ViewHolder;
import com.youyou.uumall.business.CategoryDescBiz;
import com.youyou.uumall.model.CategoryBean;
import com.youyou.uumall.ui.CategoryDescActivity_;
import com.youyou.uumall.utils.MyLogger;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/9.
 */
@EBean
public class CategoryAdapter extends BaseAdapter implements BaseBusiness.ArrayListCallbackInterface, View.OnClickListener {
    @RootContext
    Context mContext;

    @Bean
    CategoryDescBiz categoryDescBiz;

    LayoutInflater mInflater;
    List<CategoryBean> dictList;
    Map typePos;
    Map typeData;
    ArrayList<CategoryBean> secondList = new ArrayList<>();
    List<CategoryBean> temp = new ArrayList<>();
    private final int TYPE_LINE = 0;
    private final int TYPE_HEAD = 1;
    private final int TYPE_MAIN = 2;
    private final MyLogger log;
    private ImageLoader imageLoader;

    @AfterInject
    void afterInject() {
        mInflater = LayoutInflater.from(mContext);
        categoryDescBiz.setArrayListCallbackInterface(this);
        imageLoader = ImageLoader.getInstance();
    }

    public CategoryAdapter() {
        log = MyLogger.getLogger("category");
    }

    public void setData(List<CategoryBean> dictList) {
        this.dictList = dictList;
        if (dictList.size() != 0) {
            for (int i = 0; i < dictList.size(); i++) {//拿到二级的数据
                categoryDescBiz.queryCategory(dictList.get(i).id);
            }
        }
    }


    @Override
    public int getCount() {
        return dictList == null ? 0 : dictList.size() * 3;
    }//三种类型,线,头,正文

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        typePos = new HashMap();
        typeData = new HashMap();
        int pos = 0;//默认的positioin
        int dataNum = 0;//隶属于哪个数据
        for (int i = 0; i < dictList.size(); i++) {
            typeData.put(pos, dataNum);
            typePos.put(pos++, TYPE_LINE);
            typeData.put(pos, dataNum);
            typePos.put(pos++, TYPE_HEAD);
            typeData.put(pos, dataNum++);
            typePos.put(pos++, TYPE_MAIN);
        }
        return (int) typePos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        int dataNum = (int) typeData.get(position);
        CategoryBean bean = dictList.get(dataNum);//一级bean
        switch (type) {
            case TYPE_LINE:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_category_line, null);
                }
                break;
            case TYPE_HEAD:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_category_head, null);
                }
                TextView item_category_head_tv = ViewHolder.get(convertView, R.id.item_category_head_tv);
                item_category_head_tv.setText(bean.name);
                LinearLayout item_category_head_ll = ViewHolder.get(convertView, R.id.item_category_head_ll);
                item_category_head_ll.setTag(bean.id+"|"+bean.name);
                item_category_head_ll.setOnClickListener(this);
                break;
            case TYPE_MAIN:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_category_main, null);
                }

                for (int i = 0; i < secondList.size(); i++) {
                    CategoryBean categoryBean = secondList.get(i);
                    if (TextUtils.equals(bean.id, categoryBean.parentId)) {
                        temp.add(categoryBean);
                    }
                }
                TextView item_category_main_tv_0 = ViewHolder.get(convertView, R.id.item_category_main_tv_0);
                TextView item_category_main_tv_1 = ViewHolder.get(convertView, R.id.item_category_main_tv_1);
                TextView item_category_main_tv_2 = ViewHolder.get(convertView, R.id.item_category_main_tv_2);
                TextView item_category_main_tv_3 = ViewHolder.get(convertView, R.id.item_category_main_tv_3);
                TextView item_category_main_tv_4 = ViewHolder.get(convertView, R.id.item_category_main_tv_4);
                TextView item_category_main_tv_5 = ViewHolder.get(convertView, R.id.item_category_main_tv_5);
                ImageView item_category_main_iv_0 = ViewHolder.get(convertView, R.id.item_category_main_iv_0);
                ImageView item_category_main_iv_1 = ViewHolder.get(convertView, R.id.item_category_main_iv_1);
                ImageView item_category_main_iv_2 = ViewHolder.get(convertView, R.id.item_category_main_iv_2);
                ImageView item_category_main_iv_3 = ViewHolder.get(convertView, R.id.item_category_main_iv_3);
                ImageView item_category_main_iv_4 = ViewHolder.get(convertView, R.id.item_category_main_iv_4);
                ImageView item_category_main_iv_5 = ViewHolder.get(convertView, R.id.item_category_main_iv_5);
                if (temp.size() != 0) {
                    item_category_main_tv_0.setText(temp.get(0).name);
                    item_category_main_tv_1.setText(temp.get(1).name);
                    item_category_main_tv_2.setText(temp.get(2).name);
                    item_category_main_tv_3.setText(temp.get(3).name);
                    item_category_main_tv_4.setText(temp.get(4).name);
                    item_category_main_tv_5.setText(temp.get(5).name);

                    imageLoader.displayImage(BaseConstants.connection.ROOT_URL+temp.get(0).location.split("\\|")[0],item_category_main_iv_0);
                    imageLoader.displayImage(BaseConstants.connection.ROOT_URL+temp.get(1).location.split("\\|")[0],item_category_main_iv_1);
                    imageLoader.displayImage(BaseConstants.connection.ROOT_URL+temp.get(2).location.split("\\|")[0],item_category_main_iv_2);
                    imageLoader.displayImage(BaseConstants.connection.ROOT_URL+temp.get(3).location.split("\\|")[0],item_category_main_iv_3);
                    imageLoader.displayImage(BaseConstants.connection.ROOT_URL+temp.get(4).location.split("\\|")[0],item_category_main_iv_4);
                    imageLoader.displayImage(BaseConstants.connection.ROOT_URL+temp.get(5).location.split("\\|")[0],item_category_main_iv_5);
                    LinearLayout item_category_main_ll_0 = ViewHolder.get(convertView, R.id.item_category_main_ll_0);
                    LinearLayout item_category_main_ll_1 = ViewHolder.get(convertView, R.id.item_category_main_ll_1);
                    LinearLayout item_category_main_ll_2 = ViewHolder.get(convertView, R.id.item_category_main_ll_2);
                    LinearLayout item_category_main_ll_3 = ViewHolder.get(convertView, R.id.item_category_main_ll_3);
                    LinearLayout item_category_main_ll_4 = ViewHolder.get(convertView, R.id.item_category_main_ll_4);
                    LinearLayout item_category_main_ll_5 = ViewHolder.get(convertView, R.id.item_category_main_ll_5);
                    item_category_main_ll_0.setTag(temp.get(0).id+"|"+temp.get(0).name);
                    item_category_main_ll_1.setTag(temp.get(1).id+"|"+temp.get(1).name);
                    item_category_main_ll_2.setTag(temp.get(2).id+"|"+temp.get(2).name);
                    item_category_main_ll_3.setTag(temp.get(3).id+"|"+temp.get(3).name);
                    item_category_main_ll_4.setTag(temp.get(4).id+"|"+temp.get(4).name);
                    item_category_main_ll_5.setTag(temp.get(5).id+"|"+temp.get(5).name);
                    item_category_main_ll_0.setOnClickListener(this);
                    item_category_main_ll_1.setOnClickListener(this);
                    item_category_main_ll_2.setOnClickListener(this);
                    item_category_main_ll_3.setOnClickListener(this);
                    item_category_main_ll_4.setOnClickListener(this);
                    item_category_main_ll_5.setOnClickListener(this);
                }
                temp.clear();

                break;
        }

        return convertView;
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (type == CategoryDescBiz.QUERY_CATEGORY) {
            List<CategoryBean> list = (List<CategoryBean>) arrayList;
            secondList.addAll(list);//将网络获取到的二级数据全部给存储
//            log.e(list.toString());
        }
//        else if (type == CategoryDescBiz.QUERY_GOODS_BY_CATEGORY) {
//            if (arrayList != null) {
//                List<GoodsDescBean> list = (List<GoodsDescBean>) arrayList;
//
//            }
//        }
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() ==R.id.item_category_head_ll) {
            String tag = (String) v.getTag();
            String[] split = tag.split("\\|");
            Intent intent = new Intent(mContext, CategoryDescActivity_.class);
            intent.putExtra("name", split[1]);
            intent.putExtra("id", split[0]);
            mContext.startActivity(intent);
        }else{
            String tag = (String) v.getTag();
            String[] split = tag.split("\\|");
            Intent intent = new Intent(mContext, CategoryDescActivity_.class);
            intent.putExtra("name", split[1]);
            intent.putExtra("id", split[0]);
            mContext.startActivity(intent);
        }

    }


}
