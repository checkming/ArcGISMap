package com.gt.cscity.planning.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.File;


public  class DBExHelper {

    private static final String TAG = DBExHelper.class.getSimpleName();
    private final Context mContext;
    private final String mpath;
    private final CursorFactory mFactory;
    private final int mNewVersion;
    private SQLiteDatabase db = null;
    private boolean mIsInitializing = false;

    public DBExHelper(Context context, String path) {
        mContext = context;
        mpath = path;
        mFactory = null;
        mNewVersion = 2;
    }

    public DBExHelper(Context context, String path, CursorFactory factory, int version) {
        if (version < 1) throw new IllegalArgumentException("Version must be >= 1, was " + version);
        mContext = context;
        mpath = path;
        mFactory = factory;
        mNewVersion = version;
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            return db;  // The database is already open for business
        }
        if (mIsInitializing) {
            throw new IllegalStateException("getWritableDatabase called recursively");
        }
        boolean success = false;
        SQLiteDatabase tempDb = null;
        try {
            mIsInitializing = true;
            if (mpath == null) {
                tempDb = SQLiteDatabase.create(null);
            } else {
                String path = getDatabasePath(mpath).getPath();
                tempDb = SQLiteDatabase.openOrCreateDatabase(path, mFactory);
            }
            int version = tempDb.getVersion();
            if (version != mNewVersion) {
                tempDb.beginTransaction();
                try {
                    if (version == 0) {
                        onCreate(db);
                    } else {
                        onUpgrade(tempDb, version, mNewVersion);
                    }
                    tempDb.setVersion(mNewVersion);
                    tempDb.setTransactionSuccessful();
                } finally {
                    tempDb.endTransaction();
                }
            }
            //onOpen(tempDb);
            success = true;
            return tempDb;
        } finally {
            mIsInitializing = false;
            if (success) {
                if (db != null) {
                    try { db.close(); } catch (Exception e) { }
                }
                db = tempDb;
            } else {
                if (tempDb != null) tempDb.close();
            }
        }
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        if (db != null && db.isOpen()) {
            // The database is already open for business
            return db;
        }
        if (mIsInitializing) {
            throw new IllegalStateException("getReadableDatabase called recursively");
        }
        try {
            return getWritableDatabase();
        } catch (SQLiteException e) {
            // Can't open a temp database read-only!
            if (mpath == null) throw e;
            Log.e(TAG, "Couldn't open " + mpath + " for writing (will try read-only):", e);
        }
        SQLiteDatabase tempDb = null;
        try {
            mIsInitializing = true;
            String path = getDatabasePath(mpath).getPath();
            tempDb = SQLiteDatabase.openDatabase(path, mFactory, SQLiteDatabase.OPEN_READWRITE);
            if (tempDb.getVersion() != mNewVersion) {
                throw new SQLiteException("Can't upgrade read-only database from version " +
                        tempDb.getVersion() + " to " + mNewVersion + ": " + path);
            }
//            onOpen(db);
//            Log.w(TAG, "Opened " + mpath + " in read-only mode");
            db = tempDb;
            return db;
        } finally {
            mIsInitializing = false;
            if (tempDb != null && tempDb != db) tempDb.close();
        }
    }

    public synchronized void close() {
        if (mIsInitializing) throw new IllegalStateException("Closed during initialization");
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
    }

    public File getDatabasePath(String name)
    {
        return new File(name);
    }


    public  void onCreate(SQLiteDatabase db)
    {

    }

    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public SQLiteDatabase open() {
        db =  getWritableDatabase();
        return db;
    }


    // ---执行语句---
    public void Excute(String commtxt)
    {
        if (db==null)
            getReadableDatabase();
        if (db==null)
            return;

        db.execSQL(commtxt);
    }

    public void kaiQiShiWu(){
        db.beginTransaction();
    }

    public void wanChengShiWu(){
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void Excute(String commtxt,Object[] bindArgs)
    {
        if (db==null)
            getReadableDatabase();
        if (db==null)
            return;

        db.execSQL(commtxt, bindArgs);
    }

    // ---查询语句---
    public Cursor Query(String sql)
    {
        Cursor  cursor = db.rawQuery(sql, null);
        return cursor;
    }

    // ---查询语句---
    public Cursor Query(String tableName,String [] columns,String whereCluse,String groupBy,String orderBy)
    {
        Cursor  cursor = db.query(tableName, columns,  whereCluse, null, groupBy, null, orderBy);
        return  cursor;
    }
}
