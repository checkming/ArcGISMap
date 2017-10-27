package com.gt.cscity.planning.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gt.cscity.planning.R;

import java.util.List;

/**
 * Created by Administrator on 2017/10/13.
 */

public class DetailsAdapter extends BaseAdapter{
    Context context;
    List<String> list;

    public DetailsAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DetailsAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new DetailsAdapter.ViewHolder();
            convertView = View.inflate(context, R.layout.mdetailsadapter_item, null);
            viewHolder.title =  convertView.findViewById(R.id.mDetailsAdapter_item_title);
            viewHolder.year =  convertView.findViewById(R.id.mDetailsAdapter_item_year);
            viewHolder.situation =  convertView.findViewById(R.id.mDetailsAdapter_item_situation);
            viewHolder.money =  convertView.findViewById(R.id.mDetailsAdapter_item_money);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DetailsAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText("常熟市辛庄镇总体规划(2010-2030)");
        viewHolder.year.setText("2016");
        viewHolder.situation.setText("计划");
        viewHolder.money.setText("0");
        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView year;
        TextView situation;
        TextView money;
    }
}
