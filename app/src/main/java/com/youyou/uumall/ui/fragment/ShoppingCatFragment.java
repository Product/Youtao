package com.youyou.uumall.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.ShopcartAdapter;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseFragment;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.SearchBiz;
import com.youyou.uumall.business.ShopcartBiz;
import com.youyou.uumall.event.ShopCartTriggerEvent;
import com.youyou.uumall.event.ShopCartUpdateEvent;
import com.youyou.uumall.model.BonusBean;
import com.youyou.uumall.model.ShopCartBean;
import com.youyou.uumall.ui.ConfirmOrderActivity_;
import com.youyou.uumall.ui.LoginActivity_;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_shopcart)
public class ShoppingCatFragment extends BaseFragment implements BaseBusiness.ArrayListCallbackInterface, BaseBusiness.ObjectCallbackInterface, ShopcartAdapter.OnDeleteClickListener, ShopcartAdapter.OnInsertClickListener, ShopcartAdapter.OnItemCheckedListener {
    @Bean
    ShopcartBiz shopcartBiz;

    @Bean
    ShopcartAdapter adapter;

    @Bean
    SearchBiz searchBiz;

    @ViewById
    ListView shopcart_fragment_lv;

    @ViewById
    LinearLayout shopcart_bg_ll;

    @ViewById
    TextView shopcart_total_tv;

    @ViewById
    Button shopcart_buynow_bt;

    @ViewById
    CheckBox shopcart_total_cb;

    @ViewById
    TextView shopcart_bonus_tv;

    @ViewById
    RelativeLayout shopcart_title_rl;

    private Double totalPriceForResponse;//总金额
    private int sumChecked;//总选择数
    private List<ShopCartBean> mData;
    Map<String, Boolean> mCheckedMap = new HashMap();//用于暂存选中条目
    private FragmentActivity mContext;
    private Map param;
    private Double totalPrice;

    @Override
    protected void registerEvent() {
        super.registerEvent();
        eventBus.register(this);
    }

    @Override
    protected void unRegisterEvent() {
        super.unRegisterEvent();
        eventBus.unregister(this);
    }

    @AfterViews
    void afterViews() {
        mContext = getActivity();
        shopcart_fragment_lv.setAdapter(adapter);
        searchBiz.setArrayListCallbackInterface(this);
        shopcartBiz.setObjectCallbackInterface(this);
        adapter.setOnDeleteClickListener(this);
        adapter.setOnInsertClickListener(this);
        adapter.setOnItemCheckedListener(this);
        shopcart_bg_ll.setVisibility(View.VISIBLE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Boolean isAct = bundle.getBoolean("isAct", false);
            if (isAct) {
                shopcartBiz.getcartList();
                searchBiz.queryBonus();
                shopcart_title_rl.setVisibility(View.GONE);
            }
        }

    }

    @Subscribe
    public void onSelected(ShopCartTriggerEvent event) {
        shopcartBiz.getcartList();
        searchBiz.queryBonus();
    }


    @Click
    void shopcart_buynow_bt() {//根据check总数判断是否选中商品,未选中不执行转跳操作,选中将商品信息全部传递给订单页面
        if (sumChecked == 0) {
            showToastShort("请选择商品");
            return;
        }
        Bundle bundle = new Bundle();
        int size = 0;
        for (int i = 0; i < mData.size(); i++) {
            ShopCartBean data = mData.get(i);
            if (data.isCheck) {
                Bundle item = new Bundle();
                item.putString("goodsId", data.goodsId);
                item.putString("goodsName", data.goodsName);
                item.putString("count", data.count + "");
                item.putString("subtotal", data.subtotal);
                item.putString("image", data.image);
                bundle.putBundle(size + "", item);
                size++;
            }
        }
        bundle.putInt("size", size);
        Intent intent = new Intent(getActivity(), ConfirmOrderActivity_.class);
        intent.putExtras(bundle);
        startActivity(intent);
//        ConfirmOrderActivity_.intent(this).start();
    }

    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (type == ShopcartBiz.GET_CART_LIST) {
            Response response = (Response) t;
            log.e(response.toString());
            if (t != null) {

                if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {//已经登录过账号
                    totalPriceForResponse = response.totalPrice;
                    mData = (List<ShopCartBean>) response.data;
                    if (mData.size() == 0) {
                        shopcart_bg_ll.setVisibility(View.VISIBLE);
                        return;
                    }
                    shopcart_bg_ll.setVisibility(View.GONE);
                    for (int i = 0; i < mData.size(); i++) {//循环请求到的数据,把存在在选中集合里的值改true
                        ShopCartBean data = mData.get(i);
                        Boolean isCheck = mCheckedMap.get(data.goodsId);
                        if (isCheck != null) {
                            data.isCheck = true;
                        }
                    }
                    adapter.setData(mData);
                    shopcart_buynow_bt.setText("结算(" +  getSumCount(mData) + ")");
                    setTotalChecked();
                } else if (response.code == 46000 && TextUtils.equals(response.msg, "用户登录状态异常，请重新登录！")) {//如果没有登录就显示空列表
                    shopcart_bg_ll.setVisibility(View.VISIBLE);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setTitle(R.string.dialog_login_title);
//                    builder.setMessage(R.string.dialog_login_message);
//                    builder.setPositiveButton(R.string.dialog_login_pos, null);
//                    builder.setNegativeButton(R.string.dialog_login_neg, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(mContext, LoginActivity_.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.from_below_enter,R.anim.anim_none);
//                            LoginActivity_.intent(mContext).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
//                        }
//                    });
//                    builder.show();
//                    log.e(t.toString());
                }

            }
        } else if (type == ShopcartBiz.UPDATE_CART) {
            Response response = (Response) t;
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                shopcartBiz.getcartList();
            } else {
                showToastShort(response.msg);
            }
        } else if (type == ShopcartBiz.CART_ITEM_DEL) {
            Response response = (Response) t;
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                shopcartBiz.getcartList();
            } else {
                showToastShort(response.msg);
            }
        }
        eventBus.post(new ShopCartUpdateEvent());
    }

    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
//        List<ShopCartBean> list = (List<ShopCartBean>) arrayList;
//        adapter.setData(list);
        if (type == SearchBiz.QUERY_BONUS) {
            if (arrayList != null && arrayList.size() != 0) {
                List<BonusBean> list = (List<BonusBean>) arrayList;
                double bonus = 0;
                for (BonusBean bean : list) {
                    bonus += bean.value;
                }
                shopcart_bonus_tv.setVisibility(View.VISIBLE);
                shopcart_bonus_tv.setText("红包抵扣" + bonus + "元");
            } else {
                shopcart_bonus_tv.setVisibility(View.GONE);
            }

        }
    }


    @Override
    public void deleteGoods(String tag, String view) {
        switch (view) {
            case ShopcartAdapter.DEL_ALL:
                Map[] dataArray1 = MyUtils.deleteAllGoods(mData, tag);
                shopcartBiz.updatecart(dataArray1, 1);
                //总价计算
                for (int i = 0; i < mData.size(); i++) {
                    ShopCartBean data = mData.get(i);
                    if (TextUtils.equals(tag, data.goodsId) && data.isCheck) {//如果id一致,说明是同样的商品
                        totalPrice -= Double.valueOf(data.subtotal);
                        shopcart_total_tv.setText("￥" + totalPrice);
                        sumChecked -= 1;
                    }
                }
//                shopcart_buynow_bt.setText("结算(" + sumChecked + ")");
                break;
            case ShopcartAdapter.DEL_ONE:
                Map[] dataArray = MyUtils.deleteOneGoods(mData, tag);
                shopcartBiz.updatecart(dataArray, 1);

                //总价计算
                for (int i = 0; i < mData.size(); i++) {
                    ShopCartBean data = mData.get(i);
                    if (TextUtils.equals(tag, data.goodsId) && data.isCheck) {
                        totalPrice -= Double.valueOf(data.price);
                        shopcart_total_tv.setText("￥" + totalPrice);
                        if (data.count == 1) {
                            sumChecked -= 1;
//                            shopcart_buynow_bt.setText("结算(" + sumChecked + ")");
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void insertGoods(String tag) {
        Map[] dataArray = MyUtils.insertOneGoods(tag);
        shopcartBiz.updatecart(dataArray, 0);
        //总价计算
        for (int i = 0; i < mData.size(); i++) {
            ShopCartBean data = mData.get(i);
            if (TextUtils.equals(tag, data.goodsId) && data.isCheck) {
                totalPrice += Double.valueOf(data.price);
                shopcart_total_tv.setText("￥" + totalPrice);
            }
        }

    }

    @Override
    public void onCheckedChanged(View view, boolean isChecked) {
        mCheckedMap.clear();
        totalPrice = 0.0;
        sumChecked = 0;
        for (int i = 0; i < mData.size(); i++) {
            ShopCartBean shopCartBean = mData.get(i);
            if (TextUtils.equals(shopCartBean.goodsId, (String) view.getTag())) {//将点击状态赋值给集合
                shopCartBean.isCheck = isChecked;
            }
            if (shopCartBean.isCheck) {//计算总价,并计算选中总数
                Double sub = Double.valueOf(shopCartBean.subtotal);
                totalPrice += sub;
                sumChecked += 1;
                mCheckedMap.put(shopCartBean.goodsId, true);
                /**
                 * 下次点击+ - 刷新时,选中状态会恢复默认.
                 * 选择在本次保存该值
                 */
            }
        }
        shopcart_buynow_bt.setText("结算(" + getSumCount(mData) + ")");
        shopcart_total_tv.setText("￥" + totalPrice);
        setTotalChecked();
    }

    private void setTotalChecked() {
        if (sumChecked == mData.size()) {//如果相等就让总选值变色
            shopcart_total_cb.setChecked(true);
        } else {
            shopcart_total_cb.setChecked(false);
        }
    }

    @Click
    void shopcart_total_cb() {
//        boolean checked = shopcart_total_cb.isChecked();
        if (sumChecked != mData.size()) {//如果说不等于总数,说明没有全选,进行全选操作.价格就是total
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).isCheck = true;
                adapter.setData(mData);
                sumChecked = mData.size();
            }
                shopcart_total_tv.setText("￥" + totalPriceForResponse);
        } else {//全选了,进行反选操作,把所有的check值改为false,从新设置给适配器.价格就是0
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).isCheck = false;
                adapter.setData(mData);
                totalPrice = 0.0;
                sumChecked = 0;
            }
                shopcart_total_tv.setText("￥" + totalPrice);
        }
        shopcart_buynow_bt.setText("结算(" + getSumCount(mData) + ")");
    }

    public Integer getSumCount( List<ShopCartBean> data) {
        Integer sumCount = 0;
        for (int i = 0;i<data.size();i++) {
            ShopCartBean bean = data.get(i);
            if (bean.isCheck) {
                sumCount =  sumCount+bean.count;
            }
        }
        return sumCount;
    }
}