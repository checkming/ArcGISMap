package com.gt.cscity.planning.utils;

import android.content.Context;

/**
 * 密度比:解决当屏幕的分辨率发生改变时,导致移动出现错误
 * 
 * @author checkming
 *
 */
public class pxUtil {

	public static int dp2px(Context context, int dpValue) {
		// px = dp * 密度比
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (density * dpValue + +0.5f);// 四舍五入
	}

}
