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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.ViewHolder;
import com.youyou.uumall.business.CategoryDescBiz;
import com.youyou.uumall.model.CategoryBean;
import com.youyou.uumall.ui.CategoryDescActivity_;
import com.youyou.uumall.utils.MyLogger;
import com.youyou.uumall.utils.MyUtils;

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

    private DisplayImageOptions options;
    LayoutInflater mInflater;
    List<CategoryBean> dictList;
    Map typePos;
    Map typeData;
    ArrayList<CategoryBean> secondList = new ArrayList<>();

    private final int TYPE_LINE = 0;
    private final int TYPE_HEAD = 1;
    private final int TYPE_MAIN = 2;
    private final MyLogger log;
    private ImageLoader imageLoader;

    private static final int[] textViewRes = {R.id.item_category_main_tv_0,
            R.id.item_category_main_tv_1,
            R.id.item_category_main_tv_2,
            R.id.item_category_main_tv_3,
            R.id.item_category_main_tv_4,
            R.id.item_category_main_tv_5,};

    private static final int[] imageViewRes = {R.id.item_category_main_iv_0,
            R.id.item_category_main_iv_1,
            R.id.item_category_main_iv_2,
            R.id.item_category_main_iv_3,
            R.id.item_category_main_iv_4,
            R.id.item_category_main_iv_5,};

    private static final int[] linearLayoutRes = {R.id.item_category_main_ll_0,
            R.id.item_category_main_ll_1,
            R.id.item_category_main_ll_2,
            R.id.item_category_main_ll_3,
            R.id.item_category_main_ll_4,
            R.id.item_category_main_ll_5,};

    @AfterInject
    void afterInject() {
        mInflater = LayoutInflater.from(mContext);
        categoryDescBiz.setArrayListCallbackInterface(this);
        imageLoader = ImageLoader.getInstance();
        options = MyUtils.getImageOptions();
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
                item_category_head_ll.setTag(bean.id + "|" + bean.name);
                item_category_head_ll.setOnClickListener(this);
                break;
            case TYPE_MAIN:

                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_category_main, null);
                }
                List<CategoryBean> temp = new ArrayList<>();
                for (int i = 0; i < secondList.size(); i++) {
                    CategoryBean categoryBean = secondList.get(i);
                    if (TextUtils.equals(bean.id, categoryBean.parentId)) {
                        temp.add(categoryBean);
                    }
                }
                log.e(temp.toString());
                List<TextView> textViews = new ArrayList<>();
                for (int i = 0; i < textViewRes.length; i++) {
                    textViews.add((TextView) ViewHolder.get(convertView, textViewRes[i]));
                }

                List<ImageView> imageViews = new ArrayList<>();
                for (int i = 0; i < imageViewRes.length; i++) {
                    imageViews.add((ImageView) ViewHolder.get(convertView, imageViewRes[i]));
                }

                List<LinearLayout> linearLayouts = new ArrayList<>();
                for (int i = 0; i < linearLayoutRes.length; i++) {
                    linearLayouts.add((LinearLayout) ViewHolder.get(convertView, linearLayoutRes[i]));
                }
                if (temp.size() != 0) {
                    for (int i = 0; i < linearLayoutRes.length; i++) {
                        if (temp.size()<=i) {
                            return convertView;
                        }
                        CategoryBean categoryBean = temp.get(i);
                        if (categoryBean != null) {
                            textViews.get(i).setText(categoryBean.name ==null?"":categoryBean.name);
                            String location = categoryBean.location.split("\\|")[0];
                            if (!TextUtils.isEmpty(location)) {
                            imageLoader.displayImage(BaseConstants.connection.ROOT_URL + location, imageViews.get(i), options);
                            }
                            LinearLayout linearLayout = linearLayouts.get(i);
                            linearLayout.setTag(categoryBean.id + "|" + categoryBean.name);
                            linearLayout.setOnClickListener(this);
                        }
                    }
                    temp.clear();
                }


                break;
        }
        return convertView;
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (type == CategoryDescBiz.QUERY_CATEGORY) {
            List<CategoryBean> list = (List<CategoryBean>) arrayList;
//            log.e(list.toString());
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
        if (v.getId() == R.id.item_category_head_ll) {
            String tag = (String) v.getTag();
            String[] split = tag.split("\\|");
            Intent intent = new Intent(mContext, CategoryDescActivity_.class);
            intent.putExtra("name", split[1]);
            intent.putExtra("id", split[0]);
            mContext.startActivity(intent);
        } else {
            String tag = (String) v.getTag();
            String[] split = tag.split("\\|");
            Intent intent = new Intent(mContext, CategoryDescActivity_.class);
            intent.putExtra("name", split[1]);
            intent.putExtra("id", split[0]);
            mContext.startActivity(intent);
        }

    }


}
