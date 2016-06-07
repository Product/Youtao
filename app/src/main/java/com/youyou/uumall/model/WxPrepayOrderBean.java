package com.youyou.uumall.model;

/**
 * Created by Administrator on 2016/5/19.
 * 	data = [
 {
 result_code = SUCCESS,
 signRs = 0B119964F6DBA9000908707FCC69483C,
 partnerId = 1236000202,  商户号
 return_code = SUCCESS,
 nonceStr = d1IrnIAvjqLQMjbz,  随机字符串
 prepayid = wx201605191518350b788ea7bf0948324087,  预支付交易会话ID
 return_msg = OK,
 timestamp = 1463642315452,     时间戳
 sign = 791AD210A18B9671D3954BD849231330,
 appid = wx6e80d6bd17425b2a,  appID
 trade_type = APP
 }
 ]
    新接口数据
 appid=wxdfc1314988fb84a9,
 noncestr=1638592385,
 package=Sign=WXPay,
 partnerid=1338028301,
 prepayid=wx20160606163858737ddee7580460685507,
 sign=D63AF5518D395B687BD0B6A456CF62EC,
 timestamp=1465202339047
 */
public class WxPrepayOrderBean {
//    public String package;
//    public String result_code;
//    public String signRs;
    public String partnerid;
//    public String return_code;
    public String noncestr;
    public String prepayid;
//    public String return_msg;
    public String timestamp;
    public String sign;
    public String appid;
//    public String trade_type;


    @Override
    public String toString() {
        return "WxPrepayOrderBean{" +
                "partnerid='" + partnerid + '\'' +
                ", nonceStr='" + noncestr + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                ", appid='" + appid + '\'' +
                '}';
    }
}
