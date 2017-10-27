package com.gt.cscity.planning.utils;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;


public class UserDao {

	Context context = null;
	Cursor cursor=null;
	DBExHelper mDBAdapter = null;
	public UserDao(Context context){
		this.context=context;
		if (mDBAdapter==null)
			mDBAdapter=new DBExHelper(context,CommValues.dbPath);
	}
	//获取用户信息
	public ArrayList<User> getSearchReslut(String sql){
		ArrayList<User> ulist=new ArrayList<User>();
		mDBAdapter.open();
		cursor=mDBAdapter.Query(sql);
		while(cursor.moveToNext()){
			User tuser=new User();
			tuser.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("USERID"))));
			tuser.setUname(cursor.getString(cursor.getColumnIndex("LOGINNAME")));
			tuser.setUpassword(cursor.getString(cursor.getColumnIndex("LOGINPASSWORD")));
			ulist.add(tuser);
		}
		cursor.close();
		return ulist;
	}
	public boolean ExcuteSql(String sql)
	{
		try
		{
			mDBAdapter.open();
			mDBAdapter.Excute(sql);
			mDBAdapter.close();
			return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
}
