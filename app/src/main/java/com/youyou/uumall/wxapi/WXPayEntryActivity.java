package com.youyou.uumall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.event.MineTriggerEvent;
import com.youyou.uumall.ui.MainActivity_;
import com.youyou.uumall.ui.OrderAllActivity_;
import com.youyou.uumall.utils.MyLogger;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/5/19.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler, View.OnClickListener {
    MyLogger log = MyLogger.getLogger("Wx");
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_callback);
        Button wxpay_callback_home_btn = (Button) findViewById(R.id.wxpay_callback_home_btn);
        Button wxpay_callback_order_btn = (Button) findViewById(R.id.wxpay_callback_order_btn);
        wxpay_callback_home_btn.setOnClickListener(this);
        wxpay_callback_order_btn.setOnClickListener(this);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, BaseConstants.ImportantField.APP_ID, false);
        api.registerApp(BaseConstants.ImportantField.APP_ID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:

                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:

                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        String result;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                EventBus.getDefault().post(new MineTriggerEvent());//取消成功后重新改变原点状态
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                finish();
                break;
            default:
                result = "发送返回";
                finish();
                break;
        }
        log.e(result);
    }

    @Override
   public void onClick(View v) {

        if (v.getId() == R.id.wxpay_callback_home_btn) {
            MainActivity_.intent(this).start();
//            overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
            finish();
        }
        else if (v.getId() == R.id.wxpay_callback_order_btn) {
            OrderAllActivity_.intent(this).start();
//            overridePendingTransition(R.anim.from_right_enter, R.anim.anim_none);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity_.intent(this).start();
        finish();
    }
}

