package com.youyou.uumall.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.adapter.DeliveryAdapter;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.business.AddressBiz;
import com.youyou.uumall.event.DeliveryEditEvent;
import com.youyou.uumall.model.DictBean;
import com.youyou.uumall.view.DateTimePickerDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
@EActivity(R.layout.activity_delivery_info2)
public class DeliveryInfoActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface, View.OnClickListener, TextWatcher, DateTimePickerDialog.OnClickPositive,  AdapterView.OnItemClickListener {
    public static final int DOWN_ANIMATION = 0;
    public static final int UP_ANIMATION = 1;
    private boolean isOpen;

    @ViewById
    EditText delivery_info_name_et;

    @ViewById
    EditText delivery_info_phone_et;

    @ViewById
    TextView delivery_info_date_tv;

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

    @ViewById
    ListView delivery_info_lv;

    @Bean
    AddressBiz addressBiz;

    @Bean
    DeliveryAdapter deliveryAdapter;
    private List<DictBean> mDeliveryList;
    private String name;
    private String phone;
    private String date;
    private String fltNO;
    private String delivery;
    private DeliveryEditEvent event;

    @AfterViews
    void afterViews() {
        addressBiz.setArrayListCallbackInterface(this);
        addressBiz.queryDelivery();
        delivery_info_name_et.addTextChangedListener(this);
        delivery_info_phone_et.addTextChangedListener(this);
        delivery_info_flt_no_et.addTextChangedListener(this);
        delivery_info_date_tv.setOnClickListener(this);
        delivery_info_delivery_ll.setOnClickListener(this);
        delivery_info_lv.setAdapter(deliveryAdapter);
        delivery_info_lv.setOnItemClickListener(this);
        if (event != null) {
            delivery_info_name_et.setText(event.name);
            delivery_info_phone_et.setText(event.phone);
            delivery_info_flt_no_et.setText(event.fltNo);
            delivery_info_date_tv.setText(event.date);
        }
    }

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

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onRestart(DeliveryEditEvent event) {
        this.event = event;
    }


    @UiThread
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        mDeliveryList = (List<DictBean>) arrayList;
        deliveryAdapter.setData(mDeliveryList);
    }


//    @Click
//    void delivery_info_delivery_ll() {//选择自提点的ll
//        if (mDeliveryList != null) {
//            if (!isOpen) {//如果不是开启状态,就选择开启
//                for (int i = 0; i < mDeliveryList.size(); i++) {
//                    DictBean dictBean = mDeliveryList.get(i);
//                    View item = View.inflate(mApp, R.layout.item_delivery_info, null);
//                    TextView tv = (TextView) item.findViewById(R.id.item_delivery_info_tv);
//                    tv.setText(dictBean.deliveryName);
//
//                    Animation rotateAnimation = getRotateAnimation(DOWN_ANIMATION);
//                    delivery_info_delivery_iv.setAnimation(rotateAnimation);
//                    delivery_info_delivery_tv.setText("");
//                    delivery_info_add_ll.addView(item);
//                    item.setOnClickListener(this);
//                }
//                isOpen = true;
//            } else {
//                String delivery = delivery_info_delivery_tv2.getText().toString();
//                if (TextUtils.isEmpty(delivery)) {
//                    delivery_info_delivery_tv.setText("请选择自提点");
//                }
//                Animation rotateAnimation = getRotateAnimation(UP_ANIMATION);
//                delivery_info_delivery_iv.setAnimation(rotateAnimation);
//                delivery_info_add_ll.removeAllViews();
//                isOpen = false;
//            }
//
//        } else {
//            showToastLong("网络数据获取异常");
//        }
//    }

//    public Animation getRotateAnimation(int type) {
//        RotateAnimation downrotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        downrotateAnimation.setDuration(100);
//        downrotateAnimation.setFillAfter(true);
//        downrotateAnimation.start();
//
//        RotateAnimation uprotateAnimation = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        uprotateAnimation.setDuration(100);
//        uprotateAnimation.setFillAfter(true);
//        uprotateAnimation.start();
//        switch (type) {
//            case DOWN_ANIMATION:
//                return downrotateAnimation;
//            case UP_ANIMATION:
//                return uprotateAnimation;
//        }
//        return null;
//    }

    @Click
    void delivery_info_confirm_btn() {//提交信息
        Intent intent = new Intent();
        for (int i = 0; i < mDeliveryList.size(); i++) {
            DictBean dictBean = mDeliveryList.get(i);
            if (TextUtils.equals(dictBean.name,delivery)){
                intent.putExtra("deliveryId",dictBean.id);
                intent.putExtra("description",dictBean.description);
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
        if (v.getId() == R.id.delivery_info_date_tv) {//选择日期和时间
            DateTimePickerDialog dialog = new DateTimePickerDialog(this);
            dialog.setOnClickPositive(this);
            dialog.show();
        }
        else{//选择自提点
            if (delivery_info_lv.getVisibility() == View.VISIBLE) {//可见状态
                String info = delivery_info_delivery_tv2.getText().toString();
                if (TextUtils.isEmpty(info)) {
                    delivery_info_delivery_tv.setVisibility(View.VISIBLE);
                }else {
                    delivery_info_delivery_tv.setVisibility(View.INVISIBLE);
                }
                delivery_info_delivery_iv.setBackgroundResource(R.mipmap.ic_arrow_down);
                delivery_info_lv.setVisibility(View.INVISIBLE);
            }else{//不可见状态
                delivery_info_lv.setVisibility(View.VISIBLE);
                delivery_info_delivery_iv.setBackgroundResource(R.mipmap.ic_arrow_up);
                delivery_info_delivery_tv.setVisibility(View.INVISIBLE);
            }
//            delivery_info_add_ll.removeAllViews();
//            Animation rotateAnimation = getRotateAnimation(UP_ANIMATION);
//            delivery_info_delivery_iv.setAnimation(rotateAnimation);
//            delivery_info_delivery_tv.setText("");
//            TextView tv = (TextView) v.findViewById(R.id.item_delivery_info_tv);
//            String info = tv.getText().toString();
//            delivery_info_delivery_tv2.setText(info);
//        isDeliveryChecked=true;
//        delivery_info_confirm_btn.setEnabled(true);
            setButtonState();
        }

    }

    private void setButtonState() {
        name = delivery_info_name_et.getText().toString();
        phone = delivery_info_phone_et.getText().toString();
        date = delivery_info_date_tv.getText().toString();
        fltNO = delivery_info_flt_no_et.getText().toString();
        delivery = delivery_info_delivery_tv2.getText().toString();// TODO: 2016/6/30
        if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(phone)&&!TextUtils.equals(date,"选择机场提货时间")&&!TextUtils.isEmpty(fltNO)&&!TextUtils.isEmpty(delivery)) {
            delivery_info_confirm_btn.setEnabled(true);
        }else{
            delivery_info_confirm_btn.setEnabled(false);
        }
    }

    @Click
    void delivery_info_pro_iv() {
        finish();
//        overridePendingTransition(R.anim.anim_none, R.anim.from_right_exit);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        log.e("beforeTextChanged-->CharSequence:"+s+";start:"+start+";count:"+count+";after:"+after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        log.e("onTextChanged-->CharSequence:"+s+";start:"+start+";count:"+count+";before:"+before);
    }

    @Override
    public void afterTextChanged(Editable s) {
//        log.e("afterTextChanged-->Editable:"+s);
        setButtonState();
    }
    //当选择好时间的时候
    @Override
    public void getDateTime(String date, String hour, String minute) {
        delivery_info_date_tv.setText(date+" "+hour+"时"+minute);
        setButtonState();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        delivery_info_delivery_iv.setBackgroundResource(R.mipmap.ic_arrow_down);
        delivery_info_delivery_tv.setVisibility(View.INVISIBLE);
        delivery_info_lv.setVisibility(View.INVISIBLE);
        delivery_info_delivery_tv2.setText(mDeliveryList.get(position).name);
        setButtonState();
    }
}
