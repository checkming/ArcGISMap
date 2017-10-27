package com.gt.cscity.planning.ui.activity;

import android.os.Bundle;

import com.gt.cscity.planning.R;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeConfiguration;
import com.stephentuso.welcome.WelcomeHelper;

/**
 * Created by Administrator on 2017/9/28.
 */

public class WelcomeActivity extends com.stephentuso.welcome.WelcomeActivity{
    WelcomeHelper welcomeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        welcomeHelper = new WelcomeHelper(this,WelcomeActivity.class);
        welcomeHelper.show(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeHelper.onSaveInstanceState(outState);
    }

    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.colorGradle)
                .page(new TitlePage(R.drawable.earth,
                        "Title")
                )
                .page(new BasicPage(R.drawable.china,
                        "Header",
                        "More text.")
                        .background(R.color.colorHuiSe)
                )
                .page(new BasicPage(R.drawable.guihua,
                        "Lorem ipsum",
                        "dolor sit amet.")
                )
                .swipeToDismiss(true)
                .build();
    }
}
