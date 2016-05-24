package com.youyou.uumall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseConstants;

/**
 * Created by Administrator on 2016/5/19.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    private Button gotoBtn, regBtn, launchBtn, checkBtn;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, BaseConstants.ImportantField.APP_ID, false);

        regBtn = (Button) findViewById(R.id.reg_btn);
        regBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 将该app注册到微信
                api.registerApp(BaseConstants.ImportantField.APP_ID);
            }
        });

        gotoBtn = (Button) findViewById(R.id.goto_send_btn);
        gotoBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                startActivity(new Intent(WXEntryActivity.this, SendToWXActivity.class));
//                finish();
            }
        });

        launchBtn = (Button) findViewById(R.id.launch_wx_btn);
        launchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "launch result = " + api.openWXApp(), Toast.LENGTH_LONG).show();
            }
        });

        checkBtn = (Button) findViewById(R.id.check_timeline_supported_btn);
        checkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int wxSdkVersion = api.getWXAppSupportAPI();
                if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
                    Toast.makeText(getApplication(), "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline supported", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline not supported", Toast.LENGTH_LONG).show();
                }
            }
        });

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
                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                goToShowMsg((ShowMessageFromWX.Req) req);
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
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                break;
            default:
                result = "发送返回";
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    private void goToGetMsg() {
//        Intent intent = new Intent(this, GetFromWXActivity.class);
//        intent.putExtras(getIntent());
//        startActivity(intent);
//        finish();
    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

        StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);

//        Intent intent = new Intent(this, ShowFromWXActivity.class);
//        intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//        intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//        intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//        startActivity(intent);
//        finish();
    }
}

