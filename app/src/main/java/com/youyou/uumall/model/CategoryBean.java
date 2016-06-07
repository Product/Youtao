package com.youyou.uumall.model;

/**
 * Created by Administrator on 2016/5/12.
 *
 {id=2, parentId=1, parentIds=0,1,,
 name=热销产品,
 location=,
 code=000001,
 icon=,
 sort=10.0,
 isShow=1,
 createBy=1,
 createDate=2016-04-06 13:31:46,
 updateBy=1,
 updateDate=2016-05-10 16:48:19,
 remarks=衣服}
 */
public class CategoryBean {
    public String id;
    public String parentId;
    public String parentIds;
    public String name;
    public String location;
    public String code;
    public String icon;
    public String sort;
    public String isShow;
    public String createBy;
    public String updateDate;
    public String createDate;
    public String updateBy;
    public String remarks;

    @Override
    public String toString() {
        return "CategoryBean{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentIds='" + parentIds + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", icon='" + icon + '\'' +
                ", sort='" + sort + '\'' +
                ", isShow='" + isShow + '\'' +
                ", createBy='" + createBy + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
