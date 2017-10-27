package com.gt.cscity.planning.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;

import com.gt.cscity.planning.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



/**
 * copy数据库到apk包
 *
 * @author NGJ
 *
 */
public class DataBaseUtil {

    private Context context;
    public String dbName = "cspocketPlan.db";// 数据库的名字
    public String xmlName = "csghPocketPlan.xml";

    private String DATABASE_PATH;// 数据库在手机里的路径
    private String XML_PATH;
    private String MAP_PATH;

    public DataBaseUtil(Context context) {
        this.context = context;
//        String packageName = context.getPackageName();Environment.getExternalStorageDirectory()
       /* String mypath = context.getFilesDir().getAbsolutePath();
        DATABASE_PATH=mypath+"/databases/";
        XML_PATH=mypath+"/xmlit/"; */
        String mypath = Environment.getExternalStorageDirectory().getPath();
        DATABASE_PATH=mypath+"/CSGHPocketPlan/database/";
        XML_PATH=mypath+"/CSGHPocketPlan/";
        MAP_PATH=mypath+"/CSGHPocketPlan/map/";
    }

    /**
     * 判断数据库是否存在
     *
     * @return false or true
     */
    public boolean checkDataBase() {
        SQLiteDatabase db = null;
        try {
            String databaseFilename = DATABASE_PATH + dbName;
            db = SQLiteDatabase.openDatabase(databaseFilename, null,SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {

        }
        if (db != null) {
            db.close();
        }
        return db != null ? true : false;
    }

    /**
     * 复制数据库到手机指定文件夹下
     *
     * @throws IOException
     */
    public void copyDataBase() throws IOException {
        String databaseFilenames = DATABASE_PATH + dbName;
        File dir = new File(DATABASE_PATH);
        if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
            dir.mkdirs();
        FileOutputStream os = new FileOutputStream(databaseFilenames);// 得到数据库文件的写入流
        InputStream is = context.getResources().openRawResource(R.raw.plantel);// 得到数据库文件的数据流
        byte[] buffer = new byte[8192];
        int count = 0;
        while ((count = is.read(buffer)) > 0) {
            os.write(buffer, 0, count);
            os.flush();
        }
        is.close();
        os.close();
    }

    public void copyXML() throws IOException {
        String databaseFilenames = XML_PATH + xmlName;
        File dir = new File(XML_PATH);
        if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
            dir.mkdirs();
        FileOutputStream os = new FileOutputStream(databaseFilenames);// 得到数据库文件的写入流
        InputStream is = context.getResources().openRawResource(R.raw.csghpocketplan);// 得到数据库文件的数据流
        byte[] buffer = new byte[8192];
        int count = 0;
        while ((count = is.read(buffer)) > 0) {
            os.write(buffer, 0, count);
            os.flush();
        }
        is.close();
        os.close();
    }

    public void copyMap(String path,String srcName,String fileName) throws IOException {
        String databaseFilenames = MAP_PATH+path+"/"+ fileName;
        File dir = new File(MAP_PATH+path);
        if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
            dir.mkdirs();
        FileOutputStream os = new FileOutputStream(databaseFilenames);// 得到GeoDataBase文件的写入流
        InputStream is = context.getResources().openRawResource(context.getResources().getIdentifier(srcName, "raw", "com.gt.cscity.planning"));// 得到GeoDataBase文件的数据流
        byte[] buffer = new byte[8192];
        int count = 0;
        while ((count = is.read(buffer)) > 0) {
            os.write(buffer, 0, count);
            os.flush();
        }
        is.close();
        os.close();
    }
}  