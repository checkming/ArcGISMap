package com.gt.cscity.planning.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.VideoView;

import com.gt.cscity.planning.R;
import com.gt.cscity.planning.utils.spUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by Administrator on 2017/9/29.
 */

public class SplashActivity extends Activity{
    private VideoView tv_Video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 将标题栏隐藏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        tv_Video = (VideoView) findViewById(R.id.tv_video);
        // 设置播放的资源
        VideoView();

    }
    /**
     * 1.设置播放的资源
     */
    private void VideoView() {
        // 设置URI路径
        tv_Video.setVideoURI(Uri.parse("android.resource://" + getPackageName()
                + "/" + R.raw.kr36));
        // 开始播放
        tv_Video.start();
        /**
         * 设置videoView 监听播放
         */
//        tv_Video.setOnCompletionListener(new OnCompletionListener() {
//
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                tv_Video.start();
//            }
//        });
        tv_Video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                tv_Video.start();
            }
        });
    }

    public void enter(View v) {
        // 对于用户是否第一次进入程序进行判断
        if (v.getId() == R.id.btn_enter) {
            Enter();
        }

    }

    /**
     * 对用户进入页面进行判断
     */
    private void Enter() {
        boolean isAppFirstOpen = spUtil.getBoolean(this, spUtil.IS_FIRST_OPEN,
                true);
        // 判断
        if (isAppFirstOpen) {
            // 跳转至进入页面指导
            startActivity(new Intent(this, GuideActivity.class));
        } else {
            //跳转到主页面
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
