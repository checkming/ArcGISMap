package com.gt.cscity.planning.ui.fragments.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.gt.cscity.planning.R;
import com.gt.cscity.planning.ui.fragments.base.BaseFragment;

/**
 * Created by Administrator on 2017/10/24.
 */

public class MapFragment extends BaseFragment {

    private MapView mMapView;
    private ArcGISTiledMapServiceLayer tileLayer;
    private MapView mContentView;

    public void setContentView(MapView contentView) {
        mContentView = contentView;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStae) {
        View view = inflater.inflate(R.layout.main_mapview, container, false);
//        mMapView = view.findViewById(R.id.mapView);
//        MapView map = new MapView(getContext());
//        map.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        tileLayer = new ArcGISTiledMapServiceLayer
//                ("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
//        map.addLayer(tileLayer);
//        setContentView(map);

        return view;

    }


    @Override
    protected void initData() {

    }

    @Override
    protected View initView() {
        return null;
    }


}
