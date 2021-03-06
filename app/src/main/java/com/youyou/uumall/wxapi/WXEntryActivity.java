package com.youyou.uumall.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youyou.uumall.base.BaseActivity;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.event.WxLoginEvent;
import com.youyou.uumall.model.WxLoginBean;
import com.youyou.uumall.model.WxUserBean;
import com.youyou.uumall.utils.MyLogger;
import com.youyou.uumall.utils.MyUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;

/**
 * Created by Administrator on 2016/5/26.
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;
    private static MyLogger log = MyLogger.getLogger("Wx");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, BaseConstants.ImportantField.APP_ID, false);
        api.registerApp(BaseConstants.ImportantField.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Bundle bundle = new Bundle();
        baseResp.toBundle(bundle);
        SendAuth.Resp resp = new SendAuth.Resp(bundle);
        int errCode = resp.errCode;
        String code = resp.code;
        String state = resp.state;
        String lang = resp.lang;
        String country = resp.country;
//        log.e(errCode + "    " + code + "    " + state + "    " + lang + "    " + country);
        if (errCode == 0 && TextUtils.equals(state, "wechat_login")) {
            getToken(code);
        }else{
            finish();
        }
    }

    //    @Background
    public void getToken(final String code) {
        //用户同意
//        Response<Object> accessToken = otherApi.getAccessToken(BaseConstants.ImportantField.APP_ID,BaseConstants.ImportantField.APP_SECRET,code);
        new Thread(new Runnable() {
            @Override
            public void run() {//{"access_token":"OezXcEiiBSKSxW0eoylIeBQcS_f3MhyGLeabRLJ-ljzdJ4P4KlqrGs1InoYC1LGtmBFZzcoNzO0kc4rRGj8nN8EFhR7rEqT6N0L7SF0hav6D2Ysz7Nrrk9yLbzU72aoaqn43u9QSlahcDZf8h--xEw","expires_in":7200,"refresh_token":"OezXcEiiBSKSxW0eoylIeBQcS_f3MhyGLeabRLJ-ljzdJ4P4KlqrGs1InoYC1LGtLyZYA1bfWmZRGgbZE0FBxKZBbccll3Knal-UzcB5Fr2PQGrrLMaNLtIgtCYn5tlZ7nhFUyUYnkzMCrXSKYBc9g","openid":"o57WywzEK0y2I148V2OTx12Yxoos","scope":"snsapi_userinfo","unionid":"onZd-uLxdo1ReQ8T0TK0NnhpC-l0"}
                String u = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + BaseConstants.ImportantField.APP_ID + "&secret=" + BaseConstants.ImportantField.APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
                try {
                    URL url = new URL(u);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();
                    String json = changeInputStream(inputStream);
                    eventBus.post(new WxLoginEvent(json));//负责发送消息给
                    inputStream.close();
                    finish();
                } catch (Exception e) {
                    log.e(e);
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private  String changeInputStream(InputStream inputStream) {
        String openid = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] data = new byte[1024];
        try {
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
            String jsonString = new String(outputStream.toByteArray());
            Gson gson = new Gson();
            WxLoginBean wxLoginBean = gson.fromJson(jsonString, WxLoginBean.class);
            openid = wxLoginBean.openid;
            String access_token = wxLoginBean.access_token;
//            UserUtils userUtils = new UserUtils();
//            userUtils.saveAccessToken(access_token);
            String u = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
            log.e(u);
            try {
                URL url = new URL(u);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                conn.connect();
                InputStream is = conn.getInputStream();
                getInfo(is);
                is.close();
            } catch (Exception e) {
                log.e(e);
                e.printStackTrace();
            }
        } catch (IOException e) {
            log.e(e);
            e.printStackTrace();
        }
        return openid;
    }

    private  void getInfo(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] data = new byte[1024];
        try {
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
            String jsonString = new String(outputStream.toByteArray());
            Gson gson = new Gson();
            WxUserBean wxUserBean = gson.fromJson(jsonString, WxUserBean.class);
//            UserUtils userUtils = new UserUtils(WXEntryActivity.this);
            MyUtils.savePara(WXEntryActivity.this, BaseConstants.preferencesFiled.PP_USER_ID, wxUserBean.nickname);
            MyUtils.savePara(WXEntryActivity.this, BaseConstants.preferencesFiled.HEAD_IMG_URL, wxUserBean.headimgurl);
//            userUtils.saveUserId(wxUserBean.nickname);
//            userUtils.saveUserInfo(wxUserBean.headimgurl);
            log.e(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理微信发出的向第三方应用请求app message
     * <p>
     * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
     * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
     * 做点其他的事情，包括根本不打开任何页面
     */
    public void onGetMessageFromWXReq(WXMediaMessage msg) {
        Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
        startActivity(iLaunchMyself);
    }

    /**
     * 处理微信向第三方应用发起的消息
     * <p>
     * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
     * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
     * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
     * 回调。
     * <p>
     * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
     */
    public void onShowMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null && msg.mediaObject != null
                && (msg.mediaObject instanceof WXAppExtendObject)) {
            WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
            Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
        }
    }
}
