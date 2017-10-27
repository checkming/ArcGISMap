package com.gt.cscity.planning.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class FullScreenVideoView extends VideoView {

	public FullScreenVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置宽高,以至于全屏播放
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//根据父类给我们提供的比例获取屏幕的宽高
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		System.out.println("***********************************************");
		System.out.println("width:" + width);
		System.out.println("height:" + height);
		setMeasuredDimension(width, height);
	}

}
