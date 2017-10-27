package com.gt.cscity.planning.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gt.cscity.planning.R;

/**
 * Created by Administrator on 2017/9/27.
 */

public class MyGridAdapter extends BaseAdapter {
    private Context mContext;

    public MyGridAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    //图标文字
    public final String[] image_text = {"图层控制", "开启i查询", "附近搜索", "量算绘测", "添加书签", "添加标记", "校准地图", "移除操作"};
    //图标
    public final int[] images = {R.drawable.map_manage, R.drawable.i_query, R.drawable.nearby, R.drawable.meter,
            R.drawable.bookmark, R.drawable.sign, R.drawable.earth, R.drawable.cans};

    @Override
    public int getCount() {
        return image_text.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //加载图标
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item, viewGroup, false);
        }
        TextView textView = BaseViewHolder.get(view, R.id.tv_item);
        ImageView imageView = BaseViewHolder.get(view, R.id.iv_item);

        imageView.setBackgroundResource(images[position]);
        textView.setText(image_text[position]);

        return view;
    }

    public static class BaseViewHolder {
        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View view, int id) {
            SparseArray<View> viewSparseArray = (SparseArray<View>) view.getTag();
            if (viewSparseArray == null) {
                viewSparseArray = new SparseArray<View>();
                view.setTag(viewSparseArray);
            }

            View childView = viewSparseArray.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewSparseArray.put(id, childView);
            }
            return (T) childView;
        }

    }
}
