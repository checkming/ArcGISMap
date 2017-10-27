package com.gt.cscity.planning.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CommonHelper {
	public String getSDcardPath(String path){
		String type=path.substring(1,2);
		String rootpath="";
		String relativepath="";
		File sdCardDir = Environment.getExternalStorageDirectory();
		rootpath =  sdCardDir.getPath();

		if(type.equals("0")){
			relativepath=path.substring(2);
		}
		else if(type.equals("1")){
			rootpath= getExSDCardPath();//获取目录路径
			relativepath=path.substring(2);
			String writeperssion="/media_rw";
			if (rootpath.contains(writeperssion))
			{
				rootpath=rootpath.replace(writeperssion, "");
			}
		}else{
			relativepath=path;
		}
		return  rootpath+relativepath;
	}


	public String getVMapPath(String path){
		String relativepath=path.substring(2, path.length()-2);
		String rootpath="/storage/sdcard0";
		return rootpath+relativepath;
	}
	public int  getInt(String path){
		int position=path.lastIndexOf("/");
		String str=path.substring(position+1);
		int i=Integer.parseInt(str);
		return i;
	}
	public static String result;

	public static String getExSDCardPath() {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			String mount = new String();
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line.contains("secure"))
					continue;
				if (line.contains("asec"))
					continue;
				if (line.contains("fat")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						mount = mount.concat("*" + columns[1]);
					}
				} else if (line.contains("fuse")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						mount = mount.concat(columns[1]);
					}
				}
			}
			String s = new String(mount);
			String a[] = s.split("\\*");
			String s1 = "";
			if(a.length>=2)
				s1 = a[1];
			result = getLastExtensionName(mount).compareTo(s1) > 0 ? getLastExtensionName(mount)
					: s1;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * Java文件操作 获取url地址中的文件名
	 */
	public static String getLastExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('*');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	public Bitmap GetBitMapByFile(String filePath){
		Bitmap bmp=null ;
		try {
			File file=new File(filePath);
			if (file.exists()){
				bmp = getimage(filePath,1280,720);//.copy(Bitmap.Config.RGB_565, true);
			}
			else{
				bmp=null ;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bmp;
	}
	public static Bitmap getimage(String srcPath,float hight,float weight) {//
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		newOpts.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh;
		if (hight==0) {
			hh= 640f;//这里设置高度为800f
		}else{
			hh=hight;
		}
		float ww;
		if (weight==0) {
			ww=640f;//这里设置宽度为480f
		}else{
			ww=weight;
		}
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}
	private static  Bitmap compressImage(Bitmap image) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inPurgeable = true;
		newOpts.inInputShareable = true;
		newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	//更新表数据
	@SuppressWarnings("unchecked")
	public static void jsonDate2Table(Context context,String tabName,JSONArray jsonArray){
		DBExHelper db = new DBExHelper(context, CommValues.dbPath);
		db.open();
		ArrayList<String> jsonFieldList = new ArrayList<String>();
		ArrayList<String> dbFieldList = new ArrayList<String>();
		ArrayList<String> originalFieldList = new ArrayList<String>();
		ArrayList<String> keyList = new ArrayList<String>();
		String dbSql = "PRAGMA table_info("+tabName+");";
		Cursor query = db.Query(dbSql);
		while(query.moveToNext()){
			dbFieldList.add(query.getString(query.getColumnIndex("name")).toUpperCase(Locale.US));
		}
		query.close();
		Iterator<String> keys = null;
		try {
			db.kaiQiShiWu();
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonObj = (JSONObject)jsonArray.opt(i);
				if(keys==null){
					keys = jsonObj.keys();
					while(keys.hasNext()){
						String next = keys.next();
						if(next!=null){
							originalFieldList.add(next.toString());
							jsonFieldList.add(next.toString().toUpperCase(Locale.US));
						}
					}
					dbFieldList.retainAll(jsonFieldList);
					for(int n=0;n<originalFieldList.size();n++){
						String original = originalFieldList.get(n);
						for(int m=0;m<dbFieldList.size();m++){
							if(original.equalsIgnoreCase(dbFieldList.get(m))){
								keyList.add(original);
								break;
							}
						}
					}
				}
				StringBuilder fieldBuilder = new StringBuilder();
				StringBuilder valueBuilder = new StringBuilder();
				for(int m=0;m<dbFieldList.size();m++){
					if(m==0){
						fieldBuilder.append(keyList.get(m));
						valueBuilder.append("'"+jsonObj.getString(keyList.get(m)).replace("'", "''")+"'");
					}else{
						fieldBuilder.append(","+keyList.get(m));
						valueBuilder.append(",'"+jsonObj.getString(keyList.get(m)).replace("'", "''")+"'");
					}
				}
				String instrtSql = "insert INTO "+tabName+" ("+fieldBuilder.toString()+")values("+valueBuilder.toString()+")";
				Log.i("zdd", instrtSql);
				db.Excute(instrtSql);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			db.wanChengShiWu();
			db.close();
		}
	}
	//字典翻译
	public static String dictionaryValue(Context context,String dic_name,String dic_code_code){
		DBExHelper db = new DBExHelper(context,CommValues.dbPath);
		db.open();
		String sql = "SELECT dic.DICTIONARYCODENAME as DICTIONARYCODENAME FROM (SELECT * FROM ONEMAP_SYS_DICTIONARYCODE dc WHERE trim(dc.DICTIONARYID)IN(SELECT d.DICTIONARYID FROM ONEMAP_SYS_DICTIONARY d WHERE trim(d.DICTIONARYCODE)='"+dic_name+"'))AS dic WHERE dic.DICTIONARYCODECODE='"+dic_code_code+"'";
		Cursor query = db.Query(sql);
		if(query.moveToFirst()){
			return query.getString(query.getColumnIndex("DICTIONARYCODENAME"));
		}
		query.close();
		db.close();
		return "";
	}
	public static String dictionaryValue2(Context context,String dic_code_code){
		DBExHelper db = new DBExHelper(context,CommValues.dbPath);
		db.open();
		String sql = "select NODENAME from ONEMAP_PLANNINGDESIGN_NODE where NODEID = '"+dic_code_code+"' ";
		Cursor query = db.Query(sql);
		if(query.moveToFirst()){
			return query.getString(query.getColumnIndex("NODENAME"));
		}
		query.close();
		db.close();
		return "";
	}
	//图片资源缩放
	public static Bitmap zoomPicture(Bitmap bmp,float newWidth,float newHeight){
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		// 计算缩放比例     DisplayUtil.dip2px(30, MainActivity.scale)
		float scaleWidth = newWidth / width;
		float scaleHeight = newHeight / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
		return newbm;
	}

	public static Drawable zoomPicture(Context context,Drawable dra,float newWidth,float newHeight){
		Bitmap bmp = drawableToBitmap(dra);
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		// 计算缩放比例     DisplayUtil.dip2px(30, MainActivity.scale)
		float scaleWidth = newWidth / width;
		float scaleHeight = newHeight / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
		return new BitmapDrawable(context.getResources(),newbm);
	}

	//Drawable转bitmap
	public static Bitmap drawableToBitmap(Drawable drawable){// drawable 转换成bitmap
		int width = drawable.getIntrinsicWidth(); // 取drawable的长宽
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取drawable的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应bitmap的画布
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把drawable内容画到画布中
		return bitmap;
	}

	public static String md5(String value) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(value.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		String base64 = Base64.encodeBase64URLSafeString(hash);
		System.out.println(base64);
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return base64;
	}

	public static boolean isChineseChar(String str){
		boolean temp = false;
		Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m=p.matcher(str);
		if(m.find()){
			temp =  true;
		}
		return temp;
	}
	public static String getMaxProjectDate(Context context){
		DBExHelper db = new DBExHelper(context,CommValues.dbPath);
		db.open();
		String sql = "SELECT max(RECORDTIME) FROM ONEMAP_PLANNINGDESIGN_PROJECT";
		Cursor query = db.Query(sql);
		String maxDate = "2016-11-09";
		if(query.moveToFirst()){
			maxDate = query.getString(0);
		}
		query.close();
		db.close();
		return maxDate;
	}

	public static int getMaxOpId(Context context){
		DBExHelper db = new DBExHelper(context,CommValues.dbPath);
		db.open();
		String sql = "SELECT max(OPID) FROM TBOP_01T";
		int opId = 101747;
		Cursor query = db.Query(sql);
		if(query.moveToFirst()){
			opId = query.getInt(0);
		}
		query.close();
		db.close();
		return opId;
	}


	public static void writeFileToSD(String context) {
		//使用RandomAccessFile 搜索写文件 还是蛮好用的..推荐给大家使用...
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			Log.d("TestFile", "SD card is not avaiable/writeable right now.");
			return;
		}
		try {
			String pathName = "/sdcard/";
			String fileName = "log.txt";
			File path = new File(pathName);
			File file = new File(pathName + fileName);
			if (!path.exists()) {
				Log.d("TestFile", "Create the path:" + pathName);
				path.mkdir();
			}
			if (!file.exists()) {
				Log.d("TestFile", "Create the file:" + fileName);
				file.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.seek(file.length());
			raf.write(context.getBytes());
			raf.close();
		} catch (Exception e) {
			Log.e("TestFile", "Error on writeFilToSD.");
		}
	}
}
