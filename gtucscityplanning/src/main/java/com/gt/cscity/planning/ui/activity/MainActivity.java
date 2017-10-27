package com.gt.cscity.planning.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gt.cscity.planning.R;
import com.gt.cscity.planning.dialog.AppClose;
import com.gt.cscity.planning.ui.fragments.fragment.CityFragment;
import com.gt.cscity.planning.ui.fragments.fragment.GuiHuaFragment;
import com.gt.cscity.planning.ui.fragments.fragment.InquiryFragment;
import com.gt.cscity.planning.ui.fragments.fragment.MapFragment;
import com.gt.cscity.planning.ui.fragments.fragment.PlateFragment;
import com.gt.cscity.planning.ui.fragments.fragment.TownFragment;
import com.gt.cscity.planning.ui.fragments.slimenu.MainFragment;
import com.gt.cscity.planning.ui.fragments.slimenu.SecondFragment;
import com.gt.cscity.planning.ui.fragments.slimenu.SetFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/26.
 * 重构项目-侧滑菜单的UI风格变化
 */

public class MainActivity extends AppCompatActivity {
//    @Bind(R.id.toolbars)
//    Toolbar toolbars;

    //    @Bind(R.id.imageView2)
//    ImageView imageView2;
    @Bind(R.id.fl_fragment_main)
    FrameLayout flFragmentMain;

    //    @Bind(R.id.navigation_view)
//    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private Toolbar toolbar;
    private NavigationView navigationview;
    private DrawerLayout drawerlayout;
    private MapFragment mapFragment;
    /*创建一个Drawerlayout和Toolbar联动的开关*/
    private ActionBarDrawerToggle toggle;
    private Dialog dialog;
    private Fragment[] mFragments;
    private FragmentManager fragmentManager;

    //声明SlideMenu对象
    private TextView mSliSetting;
    private TextView mSliExit;
    private SlidingMenu slidingMenu;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        /*初始化View*/
//        initViews();

        /*隐藏滑动条*/
//        hideScrollBar();

        /*设置ActionBar*/
//        setActionBar();

        /*设置Drawerlayout开关*/
//        setDrawerToggle();

        initDataFragment();

        SlidingMenuDialog();


    }


    public void SlidingMenuDialog() {
        //替换主界面内容
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new MainFragment()).commit();
        //实例化菜单控件
        slidingMenu = new SlidingMenu(this);
        //设置相关属性
        slidingMenu.setMode(SlidingMenu.LEFT);//菜单靠左
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏支持触摸拖拉
        slidingMenu.setBehindOffset(170);//SlidingMenu划出时主页面显示的剩余宽度
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);//不包含ActionBar
        slidingMenu.setMenu(R.layout.right_content);
        //渐入渐出效果值
        slidingMenu.setFadeDegree(0.8f);
        slidingMenu.setBehindScrollScale(0.3f);

        TextView menuText1 = (TextView) findViewById(R.id.menutext1);
        TextView menuText2 = (TextView) findViewById(R.id.menutext2);
        mSliSetting = (TextView) findViewById(R.id.sli_setting);
        mSliExit = (TextView) findViewById(R.id.sli_exit);
        menuText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new MainFragment()).commit();
                slidingMenu.toggle();
            }
        });
        menuText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent,new SecondFragment()).commit();
                slidingMenu.toggle();
            }
        });
        mSliSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new SetFragment()).commit();
                slidingMenu.toggle();
            }
        });
        mSliExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出个弹出框
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                //                final View layout = inflater.inflate(R.layout.dialogview, null);//获取自定义布局

                //    设置Title的图标
                builder.setIcon(R.mipmap.ic_launcher);
                //    设置Title的内容
                builder.setTitle("退出登录?");
                //    设置Content来显示一个信息
                builder.setMessage("退出登录后不会删除任何历史数据");


                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppClose.getInstance().exit();
                        //MainActivity.this.finish();
                    }
                });

                builder.show();
            }
        });

        //一键退出整个应用
        AppClose.getInstance().addActivity(this);
    }



    /*初始化View*/
//    private void initViews() {
//        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        navigationview = (NavigationView) findViewById(R.id.navigation_view);
//        toolbar = (Toolbar) findViewById(R.id.toolbars);
//        setSupportActionBar(toolbar);
//
//
//    }


    /**
     * 初始化数据
     */
    private void initDataFragment() {
        CityFragment cityFragment = new CityFragment();
        TownFragment townFragment = new TownFragment();
        PlateFragment plateFragment = new PlateFragment();
        GuiHuaFragment guiHuaFragment = new GuiHuaFragment();
        InquiryFragment inquiryFragment = new InquiryFragment();
        mapFragment = new MapFragment();
        //添加到数组中
        mFragments = new Fragment[]{cityFragment, townFragment, plateFragment, guiHuaFragment, inquiryFragment, mapFragment};

        //开启事务
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_fragment_main, mapFragment).commit();
        //默认设置第0个
        setIndexSelected(0);
    }

    /**
     * 默认为第一个fragment
     *
     * @param index
     */
    private int mIndex;

    private void setIndexSelected(int index) {
        if (mIndex == index)
            return;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //隐藏
        transaction.hide(mFragments[mIndex]);
        //判断是否添加
        if (!mFragments[index].isAdded()) {
            transaction.add(R.id.fl_fragment_main, mFragments[index]).show(mFragments[index]);
        } else {
            transaction.show(mFragments[index]);
        }

        transaction.commit();
        mIndex = index;

    }

    /**
     * 底部模块的点击事件
     *
     * @param view
     */
    @Bind(R.id.rb_city)
    RadioButton rbCity;
    @Bind(R.id.rb_town)
    RadioButton rbTown;
    @Bind(R.id.rb_plate)
    RadioButton rbPlate;
    @Bind(R.id.rb_guihuai)
    RadioButton rbGuihuai;
    @Bind(R.id.rb_inquiry)
    RadioButton rbInquiry;
    @Bind(R.id.rg_bottom)
    RadioGroup rgBottom;

    @OnClick({R.id.rb_city, R.id.rb_town, R.id.rb_plate, R.id.rb_guihuai, R.id.rb_inquiry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_city:
                setIndexSelected(0);
//                Toast.makeText(this,"点击了市域",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.rb_town:
                setIndexSelected(1);
                Toast.makeText(this, "点击了城镇", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_plate:
                setIndexSelected(2);
                Toast.makeText(this, "点击了板块", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_guihuai:
                setIndexSelected(3);
                Toast.makeText(this, "点击了审批", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_inquiry:
                setIndexSelected(4);
                Toast.makeText(this, "点击了查询", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /*去掉navigation中的滑动条*/
    private void hideScrollBar() {
        navigationview.getChildAt(0).setVerticalScrollBarEnabled(false);
    }

//    /*设置ActionBar*/
//    private void setActionBar() {
//        setSupportActionBar(toolbar);
//        /*显示Home图标*/
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }

//    /*设置Drawerlayout的开关,并且和Home图标联动*/
//    private void setDrawerToggle() {
//        toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, 0, 0);
//        drawerlayout.addDrawerListener(toggle);
//        /*同步drawerlayout的状态*/
//        toggle.syncState();
//    }


//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_layer_control:
                // Toast.makeText(this, "图层管理", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_i_inquiry:
                Toast.makeText(this, "开启i查询", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_near_search:
                Toast.makeText(this, "附近搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_measuring_mapping:
                Toast.makeText(this, "量算绘测", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_add_bookmark:
                Toast.makeText(this, "添加书签", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_bookmark:
                Toast.makeText(this, "添加标记", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_calibration_map:
                Toast.makeText(this, "校准地图", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_remove_operation:
                Toast.makeText(this, "移除操作", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void setGoIndex(EventMessage eventMessage){
//        Log.d(TAG, "setGoIndex: "+eventMessage.getTag());
//        if(eventMessage!=null){
//            int tag = eventMessage.getTag();
//            if(tag== EventMessage.EventMessageAction.TAG_GO_City){
//                rbCity.performClick();
//                setIndexSelected(0);
//            }else if(tag== EventMessage.EventMessageAction.TAG_GO_TOWN){
//                rbTown.performClick();
//                setIndexSelected(1);
//            }else if(tag== EventMessage.EventMessageAction.TAG_GO_PLATE){
//                rbPlate.performClick();
//                setIndexSelected(2);
//            }else if(tag== EventMessage.EventMessageAction.TAG_GO_GUIHUA){
//                rbGuihuai.performClick();
//                setIndexSelected(3);
//            }else if(tag== EventMessage.EventMessageAction.TAG_GO_INQUIRY){
//                rbInquiry.performClick();
//                setIndexSelected(4);
//            }
//
//
//        }
//    }

    private boolean mIsExit;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //重写了Menu监听，实现按下手机Menu键弹出和关闭侧滑菜单
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            slidingMenu.toggle();
        }

        //退出应用
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();
            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}


