package com.youyou.shopping.bean;

import android.util.SparseArray;
import android.view.View;

import com.youyou.shopping.utils.MyLogger;


/** Viewholder的简化
 * @ClassName: ViewHolder
 * @Description: TODO
 * @author smile
 * @date 2014-5-28 上午9:56:29
 */
public class ViewHolder {
	private static MyLogger log = MyLogger.getLogger("ViewHolder");
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
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
}
