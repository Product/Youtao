package com.youyou.uumall.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.ShopcartAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.bean.Response;
import com.youyou.uumall.business.ShopcartBiz;
import com.youyou.uumall.event.ShopCartUpdateEvent;
import com.youyou.uumall.manager.DataBaseManager;
import com.youyou.uumall.model.ShopCartBean;
import com.youyou.uumall.utils.MyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/11.
 */
@EActivity(R.layout.activity_shopcart)
public class ShopCartActivity extends BaseActivity implements BaseBusiness.ObjectCallbackInterface, ShopcartAdapter.OnDeleteClickListener, ShopcartAdapter.OnInsertClickListener, ShopcartAdapter.OnItemCheckedListener {

    @Bean
    ShopcartBiz shopcartBiz;

    @Bean
    ShopcartAdapter adapter;

    @ViewById
    ListView shopcart_fragment_lv;

    @ViewById
    ImageView item_shopcart_up_iv;

    @ViewById
    LinearLayout shopcart_bg_ll;

    @ViewById
    TextView shopcart_total_tv;

    @ViewById
    CheckBox shopcart_total_cb;

    @ViewById
    Button shopcart_buynow_bt;

    Map<String, Boolean> mCheckedMap = new HashMap();//用于暂存选中条目
    Map param;
    private List<ShopCartBean> mData;
    private DataBaseManager dbManager;
    private double totalPrice;
    private int sumChecked;
    private Double totalPriceForResponse;

    @AfterViews
    void afterViews() {
        shopcartBiz.setObjectCallbackInterface(this);
        shopcartBiz.getcartList();
        shopcart_fragment_lv.setAdapter(adapter);
        adapter.setOnDeleteClickListener(this);
        adapter.setOnInsertClickListener(this);
        adapter.setOnItemCheckedListener(this);
        shopcart_bg_ll.setVisibility(View.VISIBLE);
    }

    @Click
    void shopcart_pro_iv() {
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @Click
    void shopcart_buynow_bt() {
        //转跳到下个界面需要拿到一串数据传递到下个act 名字  数量 总价 图片 id传递给下一个页面
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
        Intent intent = new Intent(this, ConfirmOrderActivity_.class);
        intent.putExtras(bundle);
        startActivity(intent);
//        ConfirmOrderActivity_.intent(this).start();
        overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }


    @UiThread
    @Override
    public void objectCallBack(int type, Object t) {
        if (type == ShopcartBiz.UPDATE_CART) {
            Response response = (Response) t;
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                shopcartBiz.getcartList();
            } else {
                showToastShort(response.msg);
            }
        } else if (type == ShopcartBiz.GET_CART_LIST) {
            Response response = (Response) t;
            if (response == null) {
                return;
            }
            if (response.code == 0 && TextUtils.equals(response.msg, "请求成功")) {
                totalPriceForResponse = response.totalPrice;
                mData = (List<ShopCartBean>) response.data;//第一次进入时,那个集合是空的
                if (mData.size() == 0) {
                    shopcart_bg_ll.setVisibility(View.VISIBLE);
                    return;
                }
                shopcart_bg_ll.setVisibility(View.GONE);
                for (int i = 0; i < mData.size(); i++) {
                    ShopCartBean data = mData.get(i);
                    Boolean isCheck = mCheckedMap.get(data.goodsId);
                    if (isCheck != null) {
                        data.isCheck = true;
                    }
                }
                adapter.setData(mData);
            } else if (response.code == 46000 && TextUtils.equals(response.msg, "用户登录状态异常，请重新登录！")) {//如果没有登录就显示空列表
                shopcart_bg_ll.setVisibility(View.VISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.dialog_login_title);
                builder.setMessage(R.string.dialog_login_message);
                builder.setPositiveButton(R.string.dialog_login_pos, null);
                builder.setNegativeButton(R.string.dialog_login_neg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity_.intent(ShopCartActivity.this).start();
                    }
                });
                builder.show();
//                log.e(t.toString());
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

    // TODO: 2016/5/16  暂且废弃
    @Background
    public void createDB(List<ShopCartBean> mData) {//数据库操作
        if (dbManager == null) {
            dbManager = DataBaseManager.getInstance(mApp);
        }
        for (int i = 0; i < mData.size(); i++) {
            //如果加减量,都需要对数据库删除重写.这样效率很低.
            //现在最好的方式,是在回调的时候将
            //id int primary key,name varchar(20),goodsId varchar(20),pic varchar(20),count int,totalPrice int,isCheck int
            ShopCartBean data = mData.get(i);
            ContentValues values = new ContentValues();
            values.put("name", data.goodsName);
            values.put("goodsId", data.goodsId);
            values.put("pic", data.image);
            values.put("count", data.count);
            values.put("totalPrice", data.subtotal);
            values.put("isCheck", 0);
            dbManager.insertData("shopcartlist", values);
        }
        dbManager.close();
    }


    @Override
    public void deleteGoods(String tag, String view) {//适配器里的回调
        switch (view) {
            case ShopcartAdapter.DEL_ALL:
                param = new HashMap<>();
                String[] id = {tag};
                param.put("idArray", id);
                param.put("idType", 1);
                //总价计算
                for (int i = 0; i < mData.size(); i++) {
                    ShopCartBean data = mData.get(i);
                    if (TextUtils.equals(tag, data.goodsId) && data.isCheck) {
                        totalPrice -= Double.valueOf(data.subtotal);
                        shopcart_total_tv.setText("￥" + totalPrice);
                        sumChecked -= 1;
                    }
                }
                shopcart_buynow_bt.setText("结算(" + sumChecked + ")");
                shopcartBiz.cartItemDel(param);
                setTotalChecked();
                break;
            case ShopcartAdapter.DEL_ONE:
                /**
                 * 现在只需要拿到整张data表里goodsId数据和count数据
                 * 传递的时候把要单独删除的数据id作为标记传递
                 *
                 */
                String[] goodsId = new String[mData.size()];
                int[] count = new int[mData.size()];
                for (int i = 0; i < mData.size(); i++) {
                    ShopCartBean shopCartBean = mData.get(i);
                    goodsId[i] = shopCartBean.goodsId;
                    count[i] = shopCartBean.count;
                }
                param = MyUtils.deleteOneGoods(goodsId, count, tag);
                //总价计算
                for (int i = 0; i < mData.size(); i++) {
                    ShopCartBean data = mData.get(i);
                    if (TextUtils.equals(tag, data.goodsId) && data.isCheck) {
                        totalPrice -= Double.valueOf(data.price);
                        shopcart_total_tv.setText("￥" + totalPrice);
                        if (data.count == 1) {
                            sumChecked -= 1;
                            shopcart_buynow_bt.setText("结算(" + sumChecked + ")");
                        }
                    }
                }
                shopcartBiz.updatecart(param);

                setTotalChecked();
                break;
            default:

                break;
        }
    }

    @Override
    public void insertGoods(String tag) {
        param = MyUtils.insertOneGoods(tag);
        //总价计算
        for (int i = 0; i < mData.size(); i++) {
            ShopCartBean data = mData.get(i);
            if (TextUtils.equals(tag, data.goodsId) && data.isCheck) {
                totalPrice += Double.valueOf(data.price);
                shopcart_total_tv.setText("￥" + totalPrice);
            }
        }
        shopcartBiz.updatecart(param);
    }

    @Override
    public void onCheckedChanged(View view, boolean isChecked) {
        mCheckedMap.clear();
        totalPrice = 0;
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
        shopcart_buynow_bt.setText("结算(" + sumChecked + ")");
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
                shopcart_total_tv.setText("￥" + totalPriceForResponse);
                sumChecked = mData.size();
                shopcart_buynow_bt.setText("结算(" + sumChecked + ")");
            }
        } else {//全选了,进行反选操作,把所有的check值改为false,从新设置给适配器.价格就是0
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).isCheck = false;
                adapter.setData(mData);
                totalPrice = 0.0;
                sumChecked = 0;
                shopcart_buynow_bt.setText("结算(" + sumChecked + ")");
                shopcart_total_tv.setText("￥" + totalPrice);
            }
        }
    }


}

//而点击了确定之后需要将 名字  数量 总价 图片 id传递给下一个页面
//从下个页面回来时,这个act并没有消失,数据还是原来的数据,所以不需要做数据库保存
/**
 * 第一次应该在某个
 * 选中事件的处理:
 * <p/>
 * 可以保存在本地中进行处理
 * 首先传递到下一层需要   图片  名字  数量  总价
 * 当选中checkbox时:状态值 是:拿到价格  不是:不拿
 * 把价格给总数保存,并且有一个被选中的值
 * 2个全局值:一个总价;一个被选中条目总量.
 * 每当点击的时候,先把全局值改变,在判断总选中值是否等于data条目总数,如果是的话,判断改变总值.
 * <p/>
 * 点击总check时:判断当前是否选中
 * 选中:总选中值变成最大,循环拿取所以价格
 * 不选中:值为0,价格为0
 * <p/>
 * 需要一个数据库保存保存所有情况,最后在点击确定按钮的时候拿到所有被选中的条目
 * 在开始的时候
 */

