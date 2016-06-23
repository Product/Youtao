package com.youyou.uumall.bean;

import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.youyou.uumall.utils.MyLogger;


/**
 * Viewholder的简化
 *
 * @author smile
 * @ClassName: ViewHolder
 * @Description: TODO
 * @date 2014-5-28 上午9:56:29
 */
public class ViewHolder {
    private static MyLogger log = MyLogger.getLogger("ViewHolder");
    private static SparseArray<View> viewHolder;

    public static <T extends View> T get(View view, int id) {
        viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
//			log.d("ViewHolder is Null");
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public static <T extends View> T get(View view, @Nullable int id, boolean isSpecialImageView) {
        viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
//			log.d("ViewHolder is Null");
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        if (childView instanceof ImageView) {
            ImageView imageView = (ImageView) childView;
            if (!isSpecialImageView) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                return (T) imageView;
            }

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            log.e("没改变之前height"+imageView.getMeasuredHeight()+"没改变之前"+imageView.getHeight());
//            imageView.measure(0, 0);
            imageView.invalidate();
            int width = imageView.getMeasuredWidth();
            int height = imageView.getMeasuredHeight();
				log.e("没改变之前height"+height+"没改变之前"+imageView.getHeight());
            if (imageView.getParent() instanceof LinearLayout) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, (int) (width / 1.28));
                imageView.setLayoutParams(params);
                imageView.invalidate();
            } else if (imageView.getParent() instanceof RelativeLayout) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, (int) (width / 1.28));
                imageView.setLayoutParams(params);
                imageView.invalidate();
            }

				log.e("改变之后的height"+imageView.getLayoutParams().height);
            return (T) imageView;
        } else {
            throw new ClassCastException(childView.getClass() + "not extends ImageView");
        }

//        return (T) childView;
    }
}
