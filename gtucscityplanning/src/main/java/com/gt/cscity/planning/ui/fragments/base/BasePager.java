package com.gt.cscity.planning.ui.fragments.base;

import android.content.Context;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

/**
 * Created by Administrator on 2017/9/30.
 */

public abstract class BasePager {
    protected Context context;
    public SlidingMenu slidingMenu;
    public View rootView;

    public BasePager(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
        slidingMenu = ((SlidingActivity)context).getSlidingMenu();

        //对各个子fragment进行初始化
        rootView = initView();
    }

    protected abstract View initView();


    /**
     * 让子类更新控件,将数据放在布局文件中
     */
    public void initData(){

    }
}
