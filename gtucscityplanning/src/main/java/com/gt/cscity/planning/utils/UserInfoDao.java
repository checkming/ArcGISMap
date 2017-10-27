package com.gt.cscity.planning.utils;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class UserInfoDao {
	private DBExHelper db;
	public UserInfoDao(Context context) {
		if(db==null)
			this.db = new DBExHelper(context, CommValues.dbPath);
	}
	
	public ArrayList<UserInfo> getSearchResult(String sql){
		ArrayList<UserInfo> list = new ArrayList<UserInfo>();
		db.open();
		Cursor query = db.Query(sql);
		while(query.moveToNext()){
			UserInfo user = new UserInfo();
			user.setUserid(query.getInt(query.getColumnIndex("USERID")));
			user.setUsername(query.getString(query.getColumnIndex("USERNAME")));
			user.setLoginname(query.getString(query.getColumnIndex("LOGINNAME")));
			user.setLoginpassword(query.getString(query.getColumnIndex("LOGINPASSWORD")));
			user.setCreatetime(query.getString(query.getColumnIndex("CREATETIME")));
			user.setCancellationtime(query.getString(query.getColumnIndex("CANCELLATIONTIME")));
			user.setIsused(query.getInt(query.getColumnIndex("ISUSED")));
			user.setUsercode(query.getString(query.getColumnIndex("USERCODE")));
			list.add(user);
		}
		query.close();
		db.close();
		return list;
	}
}
