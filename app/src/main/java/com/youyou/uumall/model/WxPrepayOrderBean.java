package com.youyou.uumall.model;

/**
 * Created by Administrator on 2016/5/19.
 * 	data = [
 {
 result_code = SUCCESS,
 signRs = 0B119964F6DBA9000908707FCC69483C,
 mch_id = 1236000202,  商户号
 return_code = SUCCESS,
 nonce_str = d1IrnIAvjqLQMjbz,  随机字符串
 prepay_id = wx201605191518350b788ea7bf0948324087,  预支付交易会话ID
 return_msg = OK,
 timestamp = 1463642315452,     时间戳
 sign = 791AD210A18B9671D3954BD849231330,
 appid = wx6e80d6bd17425b2a,  appID
 trade_type = APP
 }
 ]
 */
public class WxPrepayOrderBean {
    public String result_code;
    public String signRs;
    public String mch_id;
    public String return_code;
    public String nonce_str;
    public String prepay_id;
    public String return_msg;
    public String timestamp;
    public String sign;
    public String appid;
    public String trade_type;

    @Override
    public String toString() {
        return "WxPrepayOrderBean{" +
                "result_code='" + result_code + '\'' +
                ", signRs='" + signRs + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", return_code='" + return_code + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                ", appid='" + appid + '\'' +
                ", trade_type='" + trade_type + '\'' +
                '}';
    }
}
