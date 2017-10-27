package com.gt.cscity.planning.utils;

import android.app.Application;
import android.util.Log;

public class CangShuPlanning extends Application{
    private static CangShuPlanning instance;

    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public CangShuPlanning() {
        instance = this;
    	Log.e("zdd", "zhixingmei?");
    }

    public static CangShuPlanning getContext() {
        return instance;
    }
}
