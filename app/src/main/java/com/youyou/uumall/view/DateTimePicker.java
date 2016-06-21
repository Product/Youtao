package com.youyou.uumall.view;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.youyou.uumall.R;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class DateTimePicker extends FrameLayout implements NumberPicker.Formatter, NumberPicker.OnValueChangeListener {

    private final String[] mDate;
    private OnDateSeclected dateListener;
    private OnHourSeclected hourListener;
    private OnMinuteSeclected minuteListener;
    private final NumberPicker date_time_date_np;
    private final NumberPicker date_time_hour_np;
    private final NumberPicker date_time_minute_np;
    private final String[] mMinute;

    public DateTimePicker(Context context) {
        super(context);
        inflate(context, R.layout.view_date_time_dialog, this);
        date_time_date_np = (NumberPicker) findViewById(R.id.date_time_date_np);
        date_time_hour_np = (NumberPicker) findViewById(R.id.date_time_hour_np);
        date_time_minute_np = (NumberPicker) findViewById(R.id.date_time_minute_np);

        date_time_date_np.setMinValue(0);
        date_time_date_np.setMaxValue(364);
        mDate = new String[365];
        date_time_date_np.setWrapSelectorWheel(false);
        for (int i = 0; i < mDate.length; i++) {
            Calendar temp = Calendar.getInstance();
            temp.add(Calendar.DAY_OF_WEEK, i);
            int year = temp.get(Calendar.YEAR);
            int month = temp.get(Calendar.MONTH) + 1;
            int day = temp.get(Calendar.DAY_OF_MONTH);
            int dayOfWeek = temp.get(Calendar.DAY_OF_WEEK) - 1;
            String[] weeks = {"一", "二", "三", "四", "五", "六", "天"};
            String week = weeks[dayOfWeek];
            mDate[i] = year + "年" + month + "月" + day + "日 星期" + week;
        }
        date_time_date_np.setDisplayedValues(mDate);
        date_time_date_np.setValue(0);
        date_time_date_np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        date_time_hour_np.setMinValue(0);
        date_time_hour_np.setMaxValue(23);
        date_time_hour_np.setWrapSelectorWheel(false);
        date_time_hour_np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        date_time_hour_np.setFormatter(this);

        date_time_minute_np.setMinValue(0);
        date_time_minute_np.setMaxValue(5);
        date_time_minute_np.setValue(0);
        mMinute = new String[6];
        for (int i = 0; i < mMinute.length; i++) {
            if (i == 0) {
                mMinute[i] = "00";
            } else {
                mMinute[i] = i * 10 + "";
            }
        }
        date_time_minute_np.setDisplayedValues(mMinute);
        date_time_minute_np.setWrapSelectorWheel(false);
        date_time_minute_np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        date_time_date_np.setOnValueChangedListener(this);
        date_time_hour_np.setOnValueChangedListener(this);
        date_time_minute_np.setOnValueChangedListener(this);
    }

    @Override
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//        System.err.println("oldVal"+oldVal+" newVal "+newVal);
        if (picker == date_time_date_np) {
        dateListener.getDate(mDate[newVal]);
        } else if (picker == date_time_hour_np) {
            if (newVal < 10) {
                hourListener.getHour("0" + newVal);
            } else {
                hourListener.getHour("" + newVal);
            }
        }else if (picker == date_time_minute_np) {
            minuteListener.getminute(mMinute[newVal]);
        }
    }

    public void setOnDateSeclected(OnDateSeclected dateListener) {
        this.dateListener = dateListener;
    }

    interface OnDateSeclected {
        void getDate(String date);
    }

    public void setOnHourSeclected(OnHourSeclected hourListener) {
        this.hourListener = hourListener;
    }

    interface OnHourSeclected {
        void getHour(String hour);
    }

    public void setOnMinuteSeclected(OnMinuteSeclected minuteListener) {
        this.minuteListener = minuteListener;
    }

    interface OnMinuteSeclected {
        void getminute(String minute);
    }
}
