package com.youyou.uumall.ui;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.bean.EventBean;
import com.youyou.uumall.utils.MyUtils;
import com.youyou.uumall.adapter.CountryAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
@EActivity(R.layout.activity_country)
public class CountryActivity extends BaseActivity {
    @ViewById
    GridView country_list_grid;

    @Bean
    CountryAdapter adapter;

    @AfterViews
    public void initGridView() {
        String dictList = MyUtils.getPara(BaseConstants.preferencesFiled.DICT_LIST, this);
        List<String> list= getDictList(dictList);
        adapter.setData(list);
        country_list_grid.setAdapter(adapter);
        country_list_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item_country_bt = (TextView) view.findViewById(R.id.item_country_tv);
                String country = item_country_bt.getText().toString();
                MyUtils.savePara(getApplicationContext(),BaseConstants.preferencesFiled.DEFAULT_COUNTRY,country);
                country_cancel_iv();
                eventBus.post(new EventBean("refresh"));
            }
        });
    }

    private List<String> getDictList(String dictList) {
        String[] split = dictList.split(";");
        List list = new ArrayList();
        for (int i = 0; i < split.length; i++) {
            list.add(split[i]);
        }
        return list;
    }

    @Click
    void country_cancel_iv(){
           finish();
        overridePendingTransition(R.anim.anim_none, R.anim.to_center_exit);
    }



}
