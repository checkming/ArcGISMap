package com.gt.cscity.planning.ui.fragments.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gt.cscity.planning.R;
import com.gt.cscity.planning.ui.activity.DetailsActivity;
import com.gt.cscity.planning.ui.activity.MainActivity;
import com.gt.cscity.planning.ui.fragments.base.BaseFragment;

/**
 * Created by Administrator on 2017/10/11.
 * 点击各自的fragment跳转到不同的子目录中
 */

public class CityFragment extends BaseFragment {

    public CityFragment() {
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab01,container,false);

    }


    @Override
    protected void initData() {


    }

    @Override
    protected View initView() {
        return null;
    }


}
