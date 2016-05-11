package com.youyou.shopping.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.youyou.shopping.R;


/**
 * @author   
 * @date 2013-4-10 下午2:12:35  
 * 自定义进度条
 */
public class CustomProgressBar extends Dialog{
	private Context context ;
	private String progressText ;

	public CustomProgressBar(Context context) {
		super(context , R.style.loading) ;
		this.context = context ;
	}

	public CustomProgressBar(Context context, String progressText) {
		super(context, R.style.loading) ;
		this.context = context ;
		this.progressText = progressText ;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_progressbar);
		setCanceledOnTouchOutside(false);
	}

	@Override
	public void show() {
		super.show();
		ImageView im = (ImageView) findViewById(R.id.custom_imageview_progress_bar);
		AnimationDrawable aniDrawable = (AnimationDrawable) im.getDrawable();
		if(aniDrawable!=null&&aniDrawable.isRunning()){
			return;
		}
		if(aniDrawable!=null){
			aniDrawable.start();
		}

	}

}
 