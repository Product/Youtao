package com.youyou.uumall.ui;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.BonusAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.business.SearchBiz;
import com.youyou.uumall.model.BonusBean;
import com.youyou.uumall.view.RefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Administrator on 2016/6/7.
 */
@EActivity(R.layout.activity_bonus)
public class BonusActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface {

    @ViewById
    RefreshListView bonus_lv;

    @Bean
    BonusAdapter adapter;

    @Bean
    SearchBiz searchBiz;
    @Click
    void bonus_pro_im() {
        finish();
    }

    @AfterViews
    void afterViews() {
        bonus_lv.setAdapter(adapter);
        searchBiz.setArrayListCallbackInterface(this);
        searchBiz.queryBonus();
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        if (type == SearchBiz.QUERY_BONUS) {
            if (arrayList != null) {
                List<BonusBean> list = (List<BonusBean>) arrayList;
                adapter.setData(list);
            }
        }
    }
}
