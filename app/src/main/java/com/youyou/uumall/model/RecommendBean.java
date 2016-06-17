package com.youyou.uumall.model;

/**
 * Created by Administrator on 2016/5/4.
 *
 * id='acd2abb8b8b04eef9f19e83caae8b442',
 * countryCode='KR',
 * name='SK-II嫩肤清莹露套装',
 * price='500.0',
 * customizedPriceName='null',
 * customizedPrice='null',
 * location='/uumall/userfiles/1/images/bg/recommend/2016/05/%E9%A6%96%E9%A1%B5_11.png',
 * sort='10',
 * enable='1',
 * effectiveDate='2016-05-17 14:40:24',
 * expiryDate='2016-07-31 14:40:26',
 * description='',
 * createBy='1',
 * createDate='2016-04-25 18:00:59',
 * updateBy='1',
 * updateDate='2016-05-17 14:40:36'
 */
public class RecommendBean {
    public String id;
    public String countryCode;
    public String name;
    public String price;
    public String customizedPriceName;
    public String customizedPrice;
    public String location;
    public String sort;
    public String enable;
    public String effectiveDate;
    public String expiryDate;
    public String description;
    public String createBy;
    public String createDate;
    public String updateBy;
    public String updateDate;

    @Override
    public String toString() {
        return "RecommendBean{" +
                "id='" + id + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", customizedPriceName='" + customizedPriceName + '\'' +
                ", customizedPrice='" + customizedPrice + '\'' +
                ", location='" + location + '\'' +
                ", sort='" + sort + '\'' +
                ", enable='" + enable + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", description='" + description + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
