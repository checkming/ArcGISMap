package com.gt.cscity.planning.utils;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class ModuleInfoDao {
	private DBExHelper db;
	public ModuleInfoDao(Context context){
		if(db==null)
			this.db = new DBExHelper(context, CommValues.dbPath);
	}
	public ArrayList<ModuleInfo> getSearchResult(String sql){
		ArrayList<ModuleInfo> list = new ArrayList<ModuleInfo>();
		db.open();
		Cursor query = db.Query(sql);
		while(query.moveToNext()){
			ModuleInfo info = new ModuleInfo();
			info.setMenuid(query.getString(query.getColumnIndex("MODULEID")));//MODULEID
			info.setMenuname(query.getString(query.getColumnIndex("MODULENAME")));
			info.setMenucode(query.getString(query.getColumnIndex("MODULECODE")));
			info.setMenuico(query.getString(query.getColumnIndex("WIDGETICON")));
			info.setUnitname(query.getString(query.getColumnIndex("WIDGETNAME")));
			info.setParentmoduleid(query.getString(query.getColumnIndex("PARENTMODULEID")));
			info.setModuleposition(query.getInt(query.getColumnIndex("MODULEPOSITION")));
			info.setDatatype(query.getString(query.getColumnIndex("DATATYPE")));
			info.setServicetype(query.getString(query.getColumnIndex("SERVICETYPE")));
			list.add(info);
		}
		query.close();
		db.close();
		return list;
	}
}
