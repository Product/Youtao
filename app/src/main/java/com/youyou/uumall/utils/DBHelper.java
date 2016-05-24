package com.youyou.uumall.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/5/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="youtao.db"; //数据库名
    private static final int DATABASE_VERSION=1;//版本号
    private static final String SHOP_CART_LIST = "create table shopcartlist (id int primary key,name varchar(20),goodsId varchar(20),pic varchar(20),count int,totalPrice varchar(20),isCheck int)";
    //图片  名字  数量  总价

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SHOP_CART_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

    }
}
