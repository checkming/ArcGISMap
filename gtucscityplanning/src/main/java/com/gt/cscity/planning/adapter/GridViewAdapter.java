package com.gt.cscity.planning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gt.cscity.planning.R;

import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */

public class GridViewAdapter extends BaseAdapter{
    Context context;
    private List<String> gridList;

    public GridViewAdapter(Context context, List<String> gridList) {
        this.context = context;
        this.gridList = gridList;
    }

    public GridViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return gridList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setText(gridList.get(i));
        return convertView;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
