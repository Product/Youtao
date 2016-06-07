package com.youyou.uumall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.ViewHolder;
import com.youyou.uumall.business.CategoryDescBiz;
import com.youyou.uumall.model.CategoryBean;
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
public class CategoryAdapter extends BaseAdapter implements BaseBusiness.ArrayListCallbackInterface {
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

    @AfterInject
    void afterInject() {
        mInflater = LayoutInflater.from(mContext);
        categoryDescBiz.setArrayListCallbackInterface(this);
    }

    public CategoryAdapter() {
        log = MyLogger.getLogger("category");
    }

    public void setData(List<CategoryBean> dictList) {
        this.dictList = dictList;
        if (dictList.size() != 0) {
            for (int i = 0; i < dictList.size(); i++) {
                categoryDescBiz.queryCategory(dictList.get(i).id);
            }
        }
    }


    @Override
    public int getCount() {
        return dictList == null ? 0 : dictList.size() * 3;
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
        CategoryBean bean = dictList.get(dataNum);
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
                LinearLayout item_category_head_ll = ViewHolder.get(convertView, R.id.item_category_head_ll);// TODO: 2016/5/31 点击事件
                break;
            case TYPE_MAIN:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_category_main, null);
                }

                for (int i = 0; i < secondList.size(); i++) {
                    CategoryBean categoryBean = secondList.get(i);
                    if (TextUtils.equals(bean.id,categoryBean.parentId)){
                        temp.add(categoryBean);
                    }
                }
                TextView item_category_main_tv_0 = ViewHolder.get(convertView, R.id.item_category_main_tv_0);
                TextView item_category_main_tv_1 = ViewHolder.get(convertView, R.id.item_category_main_tv_1);
                TextView item_category_main_tv_2 = ViewHolder.get(convertView, R.id.item_category_main_tv_2);
                TextView item_category_main_tv_3 = ViewHolder.get(convertView, R.id.item_category_main_tv_3);
                TextView item_category_main_tv_4 = ViewHolder.get(convertView, R.id.item_category_main_tv_4);
                TextView item_category_main_tv_5 = ViewHolder.get(convertView, R.id.item_category_main_tv_5);
                if (temp.size() != 0) {
                item_category_main_tv_0.setText(temp.get(0).name);
                item_category_main_tv_1.setText(temp.get(1).name);
                item_category_main_tv_2.setText(temp.get(2).name);
                item_category_main_tv_3.setText(temp.get(3).name);
                item_category_main_tv_4.setText(temp.get(4).name);
                item_category_main_tv_5.setText(temp.get(5).name);
                }
                temp.clear();
                ImageView item_category_main_iv_0 = ViewHolder.get(convertView, R.id.item_category_main_iv_0);
                ImageView item_category_main_iv_1 = ViewHolder.get(convertView, R.id.item_category_main_iv_1);
                ImageView item_category_main_iv_2 = ViewHolder.get(convertView, R.id.item_category_main_iv_2);
                LinearLayout item_category_main_ll_0 = ViewHolder.get(convertView, R.id.item_category_main_ll_0);// TODO: 2016/5/31 点击
                LinearLayout item_category_main_ll_1 = ViewHolder.get(convertView, R.id.item_category_main_ll_1);
                LinearLayout item_category_main_ll_2 = ViewHolder.get(convertView, R.id.item_category_main_ll_2);
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
            log.e(list.toString());
        }
        notifyDataSetChanged();
    }
}
