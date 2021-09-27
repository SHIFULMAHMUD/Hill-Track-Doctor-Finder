package com.android.hilltrackdoctorfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.auth.SignInActivity;
import com.android.hilltrackdoctorfinder.utils.Tools;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.appcompat.app.AppCompatActivity;


public class SplashScreenActivity extends BaseActivity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Tools.setSystemBarColor(SplashScreenActivity.this,R.color.purple_action_bar);
        FirebaseMessaging.getInstance().subscribeToTopic("HealthGuardian");
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
        Intent intent;
        if (sharedprefer.userlogin()) {
            intent = new Intent(getApplicationContext(), SignInActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), IntroScreenActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
            }
        }, 3000);
    }
    }

