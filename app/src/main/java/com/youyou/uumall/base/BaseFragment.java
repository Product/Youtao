/*
 * Copyright (C) 2007 The Android  Source Project
 *
 * Licensed under the ZenMall License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.zenmall.cn/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youyou.uumall.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youyou.uumall.utils.MyLogger;
import com.youyou.uumall.view.ToastMaster;

import de.greenrobot.event.EventBus;


/**
 * ClassName: BaseFragment <br/>
 * Function: 所有fragment的基类 <br/>
 * date: 2013-4-15 上午11:29:27 <br/>
 * 
 * @author dean
 * @version
 * @since JDK 1.6
 */
public class BaseFragment extends Fragment {

	/**
	 * 屏幕的宽度、高度、密度
	 */
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;

	protected BaseActivity baseActivity;
	protected EventBus eventBus;
	protected MyLogger log;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	/**
	 * TODO
	 * 
	 * @see Fragment#onActivityCreated(Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		//Fragment created
		log = MyLogger.getLogger(this.getClass().getSimpleName());
		eventBus =  EventBus.getDefault(); //获取EventBus的实例
		baseActivity = (BaseActivity) getActivity();//
		DisplayMetrics metric = new DisplayMetrics();
		baseActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);

		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
		registerEvent();

		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * TODO
	 * @see Fragment#onAttach(Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		log = MyLogger.getLogger(this.getClass().getName());
	}

	

	/**
	 * init: activity中传值初始化fragment<br/>
	 * @author Zaffy
	 * @param object
	 * @return void
	 * @throws
	 * @since JDK 1.6
	 */
	public void init(Object... object) {
	}

	
	/**
	 * performBack: fragment中监听返回键<br/>
	 * @author Zaffy
	 * @return void
	 * @throws
	 * @since JDK 1.6
	 */
	public void performBack() {

	}

	@Override
	public void onResume() {
		super.onResume();
		//统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
	}

	@Override
	public void onPause() {
		super.onPause();
		// （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPageEnd(this.getClass().getSimpleName());
	}

	/**
	 * registerEvent:如果需要注册事件，请在子类覆盖该方法 <br/>
	 * @author Zaffy
	 * @return void
	 * @throws
	 * @since JDK 1.6
	 */
	protected void registerEvent(){
		
	}
	
	/**
	 * unRegisterEvent:如果需要解绑的注册事件，请在子类覆盖该方法 <br/>
	 * @author Zaffy
	 * @return void
	 * @throws
	 * @since JDK 1.6
	 */
	protected void unRegisterEvent(){
		
	}
	
	/**
	 * refreshUI:activity中通知子fragment刷新 <br/>
	 * @author Zaffy
	 * @param object
	 * @return void
	 * @throws
	 * @since JDK 1.6
	 */
	public void refreshUI(Object... object) {
		// IndexFragment.indexFragment.refreshUI();
	}
	
	public void onDestroyView() {
		unRegisterEvent();
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		imageLoader.clearMemoryCache();
	}
	
	protected void showToastShort(String message) {
		ToastMaster.makeText(getActivity(), message, Toast.LENGTH_SHORT);
	}

	protected void showToastShort(int msgResId) {
		ToastMaster.makeText(getActivity(), msgResId, Toast.LENGTH_SHORT);
	}

	protected void showToastLong(String message) {
		ToastMaster.makeText(getActivity(), message, Toast.LENGTH_LONG);
	}

	protected void showToastLong(int msgResId) {
		ToastMaster.makeText(getActivity(), msgResId, Toast.LENGTH_LONG);
	}
}
