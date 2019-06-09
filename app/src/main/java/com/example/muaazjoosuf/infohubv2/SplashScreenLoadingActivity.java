package com.example.muaazjoosuf.infohubv2;

import androidx.appcompat.app.AppCompatActivity;
import gr.net.maroulis.library.EasySplashScreen;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class SplashScreenLoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_loading);

        EasySplashScreen config = new EasySplashScreen(SplashScreenLoadingActivity.this)
                .withFullScreen()
                .withTargetActivity(NewsSectionActivity.class)
                .withBackgroundColor(Color.parseColor("#074E72"))
                .withLogo(R.mipmap.ic_launcher)
                .withFooterText("Copyright 2018")
                .withBeforeLogoText("EDMT DEV Co, LTD")
                .withAfterLogoText("Splash on your bish like water");

        //set text color
      /*  config.getHeaderTextView().setTextColor(getResources().getColor(R.color.white));
        config.getFooterTextView().setTextColor(getResources().getColor(R.color.white));
        config.getAfterLogoTextView().setTextColor(getResources().getColor(R.color.white));
        config.getBeforeLogoTextView().setTextColor(getResources().getColor(R.color.white));*/

        //set to View
        View view = config.create();

        //set view to content view
        setContentView(view);

    }
}
