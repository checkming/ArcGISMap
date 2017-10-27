package com.gt.cscity.planning.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.gt.cscity.planning.R;
import com.gt.cscity.planning.adapter.MyGridAdapter;
import com.gt.cscity.planning.ui.view.MyGridView;

import static android.R.attr.name;

/**
 * Created by Administrator on 2017/9/28.
 * 底部弹出框
 */

public class DialogPopActivity extends Activity {
    private Context mContext;
    private MyGridView gridView;
    public final String[] image_text = {"图层控制", "开启i查询", "附近搜索", "量算绘测", "添加书签",
            "添加标记", "校准地图", "移除操作"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = DialogPopActivity.this;
        setContentView(R.layout.actiivty_dialogpop);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        gridView = (MyGridView) findViewById(R.id.gridView);
        gridView.setAdapter(new MyGridAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(mContext,image_text[position], Toast.LENGTH_SHORT).show();
                // Toast.makeText(mContext, "你点击了第" + position + "项", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
