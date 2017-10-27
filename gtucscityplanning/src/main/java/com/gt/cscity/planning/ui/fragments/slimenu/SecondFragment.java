package com.gt.cscity.planning.ui.fragments.slimenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gt.cscity.planning.R;

/**
 * 第二个菜单栏主 智慧北京
 * Created by donkor
 */
public class SecondFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_slidingmenu_secondcontent,container, false);
    }
}