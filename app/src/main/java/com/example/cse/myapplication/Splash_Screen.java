package com.example.cse.myapplication;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    Animation topAnim;
    ImageView logo;
    TextView slogan;
    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);

        logo = findViewById(R.id.imageView);
        slogan = findViewById(R.id.slogan);

        logo.setAnimation(topAnim);
        slogan.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

                if (isFirstTime) {

                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    Intent intent = new Intent(Splash_Screen.this, OnBoarding.class);

                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(logo, "logo_image");
                    pairs[1] = new Pair<View, String>(slogan, "slogan_tran");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash_Screen.this, pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                } else {
                    Intent intent = new Intent(Splash_Screen.this, LoginActivity.class);

                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(logo, "logo_image");
                    pairs[1] = new Pair<View, String>(slogan, "slogan_tran");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash_Screen.this, pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            }
        }, SPLASH_SCREEN);

    }
}
