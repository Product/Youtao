package com.youyou.uumall.ui;

import android.content.Intent;
import android.widget.TextView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseBusiness;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.business.OrderBiz;
import com.youyou.uumall.model.WxPrepayOrderBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
@EActivity(R.layout.activtiy_payment)
public class PaymentActivity extends BaseActivity implements BaseBusiness.ArrayListCallbackInterface {

    private String mOrder;
    private double mPrice;
    private IWXAPI api;

    @Bean
    OrderBiz orderBiz;

    @ViewById
    TextView payment_price_tv;

    @AfterViews
    void afterViews() {
        Intent intent = getIntent();
        mOrder = intent.getStringExtra("data");
        mPrice = intent.getDoubleExtra("price", 0.0);
        api = WXAPIFactory.createWXAPI(this, BaseConstants.ImportantField.APP_ID, false);
        api.registerApp(BaseConstants.ImportantField.APP_ID);
        orderBiz.setArrayListCallbackInterface(this);
        payment_price_tv.setText(mPrice + "");
    }

    @Click
    void payment_weixin_rl() {
        orderBiz.createWxPrepayOrder(mOrder,mPrice);
    }

    /**
     * request.appId = "wxd930ea5d5a258f4f";
     * <p>
     * request.partnerId = "1900000109";
     * <p>
     * request.prepayId= "1101000000140415649af9fc314aa427",;
     * <p>
     * request.packageValue = "Sign=WXPay";
     * <p>
     * request.nonceStr= "1101000000140429eb40476f8896f4c9";
     * <p>
     * request.timeStamp= "1398746574";
     * <p>
     * request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
     *
     * @param type
     * @param arrayList
     */
    @Override
    public void arrayCallBack(int type, List<? extends Object> arrayList) {
        log.e(arrayList.get(0).toString());

        if (arrayList != null && type == OrderBiz.CREATE_WX_PREPAY_ORDER) {
//        if (false){
            List<WxPrepayOrderBean> list = (List<WxPrepayOrderBean>) arrayList;
            WxPrepayOrderBean wxPrepayOrderBean = list.get(0);
            String appId = wxPrepayOrderBean.appid;
//            String appId = BaseConstants.ImportantField.APP_ID;
            String partnerId = wxPrepayOrderBean.partnerid;
            String prepayId = wxPrepayOrderBean.prepayid;
            String packageValue = "Sign=WXPay";
            String nonceStr = wxPrepayOrderBean.noncestr;
            String timeStamp = wxPrepayOrderBean.timestamp;
//            String sign = wxPrepayOrderBean.sign;
            //appid noncestr package partnerid prepayid timestamp
//            String stringA = "appid="+appId+"&noncestr="+nonceStr+"&package="+packageValue+"&partnerid="+partnerId+"&prepayid="+prepayId+"&timestamp="+timeStamp;
//            String stringSignTemp=stringA+"&key=jMetopvfeiUKKVrVKZLsdxnfTOPmPDSF";
//            stringSignTemp = MD5.md5(stringSignTemp);
//            stringSignTemp=stringSignTemp.toUpperCase();
//            log.e(wxPrepayOrderBean.toString());
            if (api != null) {
                PayReq req = new PayReq();
                req.appId = appId;
                req.partnerId = partnerId;
                req.prepayId = prepayId;
                req.packageValue = packageValue;
                req.nonceStr = nonceStr;
                req.timeStamp = timeStamp;
                req.sign = wxPrepayOrderBean.sign;
//                req.sign = stringSignTemp;
                boolean b = api.sendReq(req);
            }
        }
    }
}
