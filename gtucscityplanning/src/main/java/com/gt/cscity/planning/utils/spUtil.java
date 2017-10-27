package com.gt.cscity.planning.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 利用SharedPreferences对用户进入页面的信息状态进行存取,然后进行展示
 * 
 * @author checkming
 *
 */
public class spUtil {
	
	private static String NAME = "ZHBJ";
	private static SharedPreferences sp;
	
	
	public static final String IS_FIRST_OPEN = "is_first_open";
	public static final String NEWSCENTER_CACHE_JSON ="NEWSCENTER_CACHE_JSON";
	public static final String READED_NEWS_CACHE_JSON ="READED_NEWS_CACHE_JSON";

	// private String
	// 对用户操作的状态信息进行存储
	public static void putBoolean(Context context, String key, Boolean value) {
		if (sp == null) {
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		//然后进行存储
		sp.edit().putBoolean(key, value).commit();
	}
	// 对用户操作的状态信息进行取
	public static boolean getBoolean(Context context, String key, Boolean defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}
	public static void putString(Context context, String key, String value) {
		if (sp == null) {
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		//然后进行取数据
		sp.edit().putString(key, value).commit();
	}
	// 对用户操作的状态信息进行取
	public static String getString(Context context, String key, String defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}
}
