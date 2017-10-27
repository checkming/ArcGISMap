package com.gt.cscity.planning.dialog;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class AppClose extends Application {
  
    private List<Activity> mList = new LinkedList<Activity>();
    private static AppClose instance;  
  
    private AppClose() {  
  
    }  
  
    public synchronized static AppClose getInstance() {  
        if (null == instance) {  
            instance = new AppClose();  
        }  
        return instance;  
    }  
  
    public void addActivity(Activity activity) {  
        mList.add(activity);  
    }  
  
    public void exit() {  
        try {  
            for (Activity activity : mList) {  
                if (activity != null)  
                    activity.finish();  
            }  
            mList = null;  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            System.exit(0);  
        }  
    }  
  
    @Override  
    public void onLowMemory() {  
        super.onLowMemory();  
        System.gc();  
    }  
  
}  