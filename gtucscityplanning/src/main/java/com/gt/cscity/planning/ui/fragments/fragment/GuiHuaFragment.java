package com.gt.cscity.planning.ui.fragments.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gt.cscity.planning.R;
import com.gt.cscity.planning.ui.fragments.base.BaseFragment;

/**
 * Created by Administrator on 2017/10/11.
 */

public class GuiHuaFragment extends BaseFragment {

    @Override
    protected void initData() {

    }

    @Override
    protected View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab04, container, false);
    }
}
