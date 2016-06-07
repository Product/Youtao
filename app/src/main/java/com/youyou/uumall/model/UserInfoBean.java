package com.youyou.uumall.model;

/**
 * Created by Administrator on 2016/5/5.
 * "userId": "bb6f3cae837d4c1287cc45e977550131",
 * "mobile": "18555808691",
 * "invitationCode": "w604qa"
 *
 */
public class UserInfoBean {
    public String userId;
    public String mobile;
    public String invitationCode;
    public String openId;
    public String parentInvitationCode;

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "userId='" + userId + '\'' +
                ", mobile='" + mobile + '\'' +
                ", invitationCode='" + invitationCode + '\'' +
                ", openId='" + openId + '\'' +
                ", parentInvitationCode='" + parentInvitationCode + '\'' +
                '}';
    }
}
