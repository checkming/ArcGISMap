package com.gt.cscity.planning.ui.fragments.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by Administrator on 2017/9/29.
 */

public abstract class BaseFragment extends Fragment {
    protected SlidingMenu slidingMenu;

    //此方法在onCreateView之后调用
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //让子类更新控件
        initData();
    }

    /**
     * 让子类更新控件
     */
    protected abstract void initData();

    /**
     * 让子类必须实现,返回控件
     * @return
     */
    protected abstract View initView();
}
