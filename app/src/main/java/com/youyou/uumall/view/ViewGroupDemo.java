package com.youyou.uumall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ViewGroupDemo extends ViewGroup {
    public ViewGroupDemo(Context context) {
        super(context);
    }

    public ViewGroupDemo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet att) {
        return new MarginLayoutParams(getContext(),att);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //从父控件上获取到了相关规范
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int hieghtMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int hieghtSize = MeasureSpec.getSize(heightMeasureSpec);
        //测量子控件
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        //获取到子控件的数量
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);



        }

    }
}
