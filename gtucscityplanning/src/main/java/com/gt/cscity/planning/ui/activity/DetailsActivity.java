package com.gt.cscity.planning.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gt.cscity.planning.R;
import com.gt.cscity.planning.adapter.DetailsAdapter;
import com.gt.cscity.planning.adapter.GridViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private ListView mListView;
    private DetailsAdapter mDetailsAdapter;
    private List<String> testList = new ArrayList<>();
    private List<String> yearList = new ArrayList<>();
    private List<String> qkList = new ArrayList<>();
    ImageView searchIv, finishIv, mainIv;
    LinearLayout mLinearLayout;
    TextView finishTextX;
    Spinner yearSpinner, qingkuangSpinner;
    GridView gridView;
    GridViewAdapter mGridViewAdapter;
    private List<String> gridList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initView();
    }

    private void initView() {
        gridList.add("城市总体规划");
        gridList.add("上位规划");
        gridList.add("专项规划");
        gridList.add("控制性详细规划");
        gridList.add("城市设计");
        gridList.add("研究类规划");
        gridList.add("历史文化名城");
        mListView = (ListView) findViewById(R.id.activity_details_listview);
        gridView = (GridView) findViewById(R.id.activity_details_gd);
        mGridViewAdapter = new GridViewAdapter(DetailsActivity.this,gridList);
        gridView.setAdapter(mGridViewAdapter);
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        yearList.add("2005");
        yearList.add("2008");
        yearList.add("2014");
        yearList.add("2017");
        qkList.add("计划");
        qkList.add("在编");
        qkList.add("调整");
        qkList.add("完成");
        mDetailsAdapter = new DetailsAdapter(this, testList);
        mListView.setAdapter(mDetailsAdapter);
        searchIv = (ImageView) findViewById(R.id.activity_details_search);
        mLinearLayout = (LinearLayout) findViewById(R.id.queryterm);
        finishTextX = (TextView) findViewById(R.id.finish);
        finishIv = (ImageView) findViewById(R.id.activity_details_return);
        yearSpinner = (Spinner) findViewById(R.id.year);
        qingkuangSpinner = (Spinner) findViewById(R.id.kz_qk);
        finishIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearLayout.setVisibility(View.VISIBLE);
            }
        });
        finishTextX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearLayout.setVisibility(View.GONE);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(adapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                TextView tvResult = (TextView) findViewById(R.id.tvResult);
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
//                tvResult.setText(adapter.getItem(position));
            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, qkList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        qingkuangSpinner.setAdapter(adapter1);
        qingkuangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                TextView tvResult = (TextView) findViewById(R.id.tvResult);
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
//                tvResult.setText(adapter.getItem(position));
            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
