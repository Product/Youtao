package com.youyou.uumall.model;

/**
 * Created by Administrator on 2016/5/10.
 * id='523319c1372a4a5b815f3a2a32ac59a6',
 * name='品牌测试01名称',
 * description='品牌测试01描述',
 * sort='1',
 * imageSrc='/uumall/userfiles/1/images/mall/brand/2016/06/banner03.png',
 * createBy='null',
 * createDate='2016-06-13 02:23:19',
 * updateBy='null',
 * updateDate='2016-06-13 02:29:29',
 * remarks='品牌测试备注'

 */
public class BrandBean {
    public String id;
    public String name;
    public String description;
    public String sort;
    public String imageSrc;
    public String createBy;
    public String createDate;
    public String updateBy;
    public String updateDate;
    public String remarks;

    @Override
    public String toString() {
        return "BrandBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sort='" + sort + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
