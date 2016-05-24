package com.youyou.uumall.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.business.AddressBiz;
import com.youyou.uumall.model.DictBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/17.
 */
@EActivity(R.layout.activity_delivery_info)
public class DeliveryInfoActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, View.OnClickListener {
    public static final int DOWN_ANIMATION = 0;
    public static final int UP_ANIMATION = 1;
    private boolean isOpen;

    @ViewById
    EditText delivery_info_name_et;

    @ViewById
    EditText delivery_info_phone_et;

    @ViewById
    EditText delivery_info_date_et;

    @ViewById
    EditText delivery_info_flt_no_et;

    @ViewById
    LinearLayout delivery_info_delivery_ll;

    @ViewById
    LinearLayout delivery_info_add_ll;

    @ViewById
    TextView delivery_info_delivery_tv;

    @ViewById
    TextView delivery_info_delivery_tv2;

    @ViewById
    ImageView delivery_info_delivery_iv;

    @ViewById
    Button delivery_info_confirm_btn;


    @Bean
    AddressBiz addressBiz;
    private List<DictBean> mDeliveryList;

    @AfterViews
    void afterViews() {
        addressBiz.setArrayListCallbackInterface(this);
        getDelivery();
    }

    private void getDelivery() {
        Map param = new HashMap();
        param.put("countryCode", "KR");// TODO: 2016/5/18 未完待续
        addressBiz.queryDelivery(param);
    }

    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        mDeliveryList = (List<DictBean>) arrayList;
    }


    @Click
    void delivery_info_delivery_ll() {
        if (mDeliveryList != null) {
            if (!isOpen) {//如果不是开启状态,就选择开启
                for (int i = 0; i < mDeliveryList.size(); i++) {
                    DictBean dictBean = mDeliveryList.get(i);
                    View item = View.inflate(mApp, R.layout.item_delivery_info, null);
                    TextView tv = (TextView) item.findViewById(R.id.item_delivery_info_tv);
                    tv.setText(dictBean.deliveryName);

                    Animation rotateAnimation = getRotateAnimation(DOWN_ANIMATION);
                    delivery_info_delivery_iv.setAnimation(rotateAnimation);
                    delivery_info_delivery_tv.setText("");
                    delivery_info_add_ll.addView(item);
                    item.setOnClickListener(this);
                }
                isOpen = true;
            } else {
                String delivery = delivery_info_delivery_tv2.getText().toString();
                if (TextUtils.isEmpty(delivery)) {
                    delivery_info_delivery_tv.setText("请选择自提点");
                }
                Animation rotateAnimation = getRotateAnimation(UP_ANIMATION);
                delivery_info_delivery_iv.setAnimation(rotateAnimation);
                delivery_info_add_ll.removeAllViews();
                isOpen = false;
            }

        } else {
            showToastLong("网络数据获取异常");
        }
    }

    public Animation getRotateAnimation(int type) {
        RotateAnimation downrotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downrotateAnimation.setDuration(100);
        downrotateAnimation.setFillAfter(true);
        downrotateAnimation.start();

        RotateAnimation uprotateAnimation = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        uprotateAnimation.setDuration(100);
        uprotateAnimation.setFillAfter(true);
        uprotateAnimation.start();
        switch (type) {
            case DOWN_ANIMATION:
                return downrotateAnimation;
            case UP_ANIMATION:
                return uprotateAnimation;
        }
        return null;
    }

    @Click
    void delivery_info_confirm_btn() {
        String name = delivery_info_name_et.getText().toString();
        String phone = delivery_info_phone_et.getText().toString();
        String date = delivery_info_date_et.getText().toString();
        String fltNO = delivery_info_flt_no_et.getText().toString();
        String delivery = delivery_info_delivery_tv2.getText().toString();
        Intent intent = new Intent();
        for (int i = 0; i < mDeliveryList.size(); i++) {
            DictBean dictBean = mDeliveryList.get(i);
            if (TextUtils.equals(dictBean.deliveryName,delivery)){
                intent.putExtra("deliveryId",dictBean.deliveryId);
            }
        }
        intent.putExtra("name",name);
        intent.putExtra("phone",phone);
        intent.putExtra("date",date);
        intent.putExtra("fltNO",fltNO);
        intent.putExtra("delivery",delivery);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        isOpen = false;
        delivery_info_add_ll.removeAllViews();
        Animation rotateAnimation = getRotateAnimation(UP_ANIMATION);
        delivery_info_delivery_iv.setAnimation(rotateAnimation);
        delivery_info_delivery_tv.setText("");
        TextView tv = (TextView) v.findViewById(R.id.item_delivery_info_tv);
        String info = tv.getText().toString();
        delivery_info_delivery_tv2.setText(info);
        delivery_info_confirm_btn.setEnabled(true);
    }

    @Click
    void delivery_info_pro_iv() {
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }
}
