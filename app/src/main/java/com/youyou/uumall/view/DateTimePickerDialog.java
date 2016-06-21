package com.youyou.uumall.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/6/21.
 */
public class DateTimePickerDialog extends AlertDialog implements DateTimePicker.OnDateSeclected ,DialogInterface.OnClickListener, DateTimePicker.OnHourSeclected, DateTimePicker.OnMinuteSeclected {
    private OnClickPositive listener;
    private String date;
    private String hour;
    private String minute;

    public DateTimePickerDialog(Context context) {
        super(context);
        DateTimePicker dateTimePicker = new DateTimePicker(context);
        setView(dateTimePicker);
        setButton(BUTTON_POSITIVE, "确定", this);
        setButton(BUTTON_NEGATIVE, "取消", (OnClickListener) null);

        dateTimePicker.setOnDateSeclected(this);
        dateTimePicker.setOnHourSeclected(this);
        dateTimePicker.setOnMinuteSeclected(this);
    }

    @Override
    public void getDate(String date) {
        this.date =date;
    }
    @Override
    public void getHour(String hour) {
        this.hour = hour;
    }

    @Override
    public void getminute(String minute) {
        this.minute = minute;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        Calendar calendar = Calendar.getInstance();
        if (listener != null) {
            if (TextUtils.isEmpty(date)) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                String[] weeks = {"一", "二", "三", "四", "五", "六", "天"};
                String week = weeks[dayOfWeek];
                date = year + "年" + month + "月" + day + "日 星期" + week;
            }
            if (TextUtils.isEmpty(hour)) {
                hour = "00";
            }
            if (TextUtils.isEmpty(minute)) {
                minute ="00";
            }
            listener.getDateTime(date,hour,minute);
        }
    }



    public interface OnClickPositive {
        void getDateTime(String date, String hour, String minute);
    }

    public void setOnClickPositive(OnClickPositive listener) {
        this.listener = listener;
    }

}
