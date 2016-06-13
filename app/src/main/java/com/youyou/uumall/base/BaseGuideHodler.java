package com.youyou.uumall.base;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.youyou.uumall.R;
import com.youyou.uumall.bean.ViewHolder;
import com.youyou.uumall.utils.MyLogger;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/6/8.
 */

public abstract class BaseGuideHodler {

    public Context mContext;
    protected final View view;
    protected final GifImageView item_guide_giv;
    protected final TextView item_guide_tv_0;
    protected final TextView item_guide_tv_1;
    protected final MyLogger log;

    public BaseGuideHodler(Context mContext) {
        this.mContext = mContext;
        view = View.inflate(mContext, R.layout.item_guide_vp, null);
        item_guide_giv = ViewHolder.get(view, R.id.item_guide_giv);
        item_guide_tv_0 = ViewHolder.get(view, R.id.item_guide_tv_0);
        item_guide_tv_1 = ViewHolder.get(view, R.id.item_guide_tv_1);
        log = MyLogger.getLogger("guide");
        setData();
    }

    public abstract void starGif();
    public abstract void stopGif();
    public abstract void setData();

    public View getView() {
        return view;
    }
}
