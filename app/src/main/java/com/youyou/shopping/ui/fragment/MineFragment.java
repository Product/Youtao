package com.youyou.shopping.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.youyou.shopping.R;
import com.youyou.shopping.base.BaseFragment;
import com.youyou.shopping.ui.LoginActivity_;
import com.youyou.shopping.ui.RegisterActivity_;
import com.youyou.shopping.utils.UserUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_mine)
public class MineFragment extends BaseFragment{

    @Bean
    UserUtils userUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterViews() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Click({R.id.mine_head,R.id.mine_login})
    void userLogin(){
        if(TextUtils.isEmpty(userUtils.getUserId())){
            LoginActivity_.intent(getActivity()).start();
            getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
        }
    }

    @Click
    void mine_register(){//登录页面
        RegisterActivity_.intent(getActivity()).start();
        getActivity().overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }
}
