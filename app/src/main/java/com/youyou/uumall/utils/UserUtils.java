package com.youyou.uumall.utils;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.maps.model.LatLng;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.model.UserDetailBean;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;


@EBean
public class UserUtils {

    @RootContext
    Context context;

    /**
     * 保存用户的位置数据
     *
     * @param lat
     * @param lng
     */
    public void saveLocations(String address, double lat, double lng) {
        MyUtils.savePara(context, BaseConstants.preferencesFiled.ADDRESS, address);
        MyUtils.savePara(context, BaseConstants.preferencesFiled.LAT, String.valueOf(lat));
        MyUtils.savePara(context, BaseConstants.preferencesFiled.LNG, String.valueOf(lng));
    }
    public LatLng getCurrentLatLng() {
        double lat = getLat();
        double lng = getLng();
        if (lat == -1 || lng == -1) {
            return null;
        }
        return new LatLng(lat, lng);
    }

    public double getLat() {
        String latStr = MyUtils.getPara(BaseConstants.preferencesFiled.LAT, context);
        if (TextUtils.isEmpty(latStr)) {
            return -1;
        }
        return Double.valueOf(latStr);
    }

    public double getLng() {
        String lngStr = MyUtils.getPara(BaseConstants.preferencesFiled.LNG, context);
        if (TextUtils.isEmpty(lngStr)) {
            return -1;
        }
        return Double.valueOf(lngStr);
    }

    public String getCurrentAddress() {
        return MyUtils.getPara(BaseConstants.preferencesFiled.ADDRESS, context);
    }

    @Background
    public void saveAccessToken(String token) {
        MyUtils.savePara(context, BaseConstants.preferencesFiled.ACCESS_TOKEN, token);
    }

    @Background
    public void saveUserDetails(UserDetailBean data) {
        if (data == null) {
            return;
        }
        MyUtils.savePara(context, BaseConstants.preferencesFiled.ACCESS_TOKEN, data.access_token);
        MyUtils.savePara(context, BaseConstants.preferencesFiled.PP_USER_ID, "" + data.uid);
        MyUtils.savePara(context, BaseConstants.preferencesFiled.PP_USER_AVATAR, data.avatar);
        MyUtils.savePara(context, BaseConstants.preferencesFiled.PP_USER_NICK, data.nick);
    }

    public UserDetailBean getUserDetails() {
        UserDetailBean userDetail = new UserDetailBean();
        userDetail.avatar = MyUtils.getPara(BaseConstants.preferencesFiled.PP_USER_AVATAR, context);
        userDetail.nick = MyUtils.getPara(BaseConstants.preferencesFiled.PP_USER_NICK, context);
        userDetail.uid = MyUtils.getPara(BaseConstants.preferencesFiled.PP_USER_ID, context);
        userDetail.access_token = MyUtils.getPara(BaseConstants.preferencesFiled.ACCESS_TOKEN, context);
        return userDetail;
    }

    @Background
    public void saveUserId(long user_id) {
        String userId = String.valueOf(user_id);
        MyUtils.savePara(context, BaseConstants.preferencesFiled.PP_USER_ID, userId);
    }

    public String getUserId() {
        String userId = MyUtils.getPara(BaseConstants.preferencesFiled.PP_USER_ID, context);
        if (TextUtils.isEmpty(userId)) {
            return null;
        } else {
            return userId;
        }
    }

    public String getAccessToken() {
        return MyUtils.getPara(BaseConstants.preferencesFiled.ACCESS_TOKEN, context);
    }

    public void userLogout() {
        MyUtils.savePara(context, BaseConstants.preferencesFiled.ACCESS_TOKEN, "");
        MyUtils.savePara(context, BaseConstants.preferencesFiled.PP_USER_ID, "");
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
}
