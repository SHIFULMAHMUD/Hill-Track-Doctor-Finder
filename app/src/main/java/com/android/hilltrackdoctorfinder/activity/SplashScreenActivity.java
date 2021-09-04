package com.android.hilltrackdoctorfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.utils.Tools;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.appcompat.app.AppCompatActivity;


public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean loggedIn = false;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Tools.setSystemBarColor(SplashScreenActivity.this,R.color.color_blue);
//        FirebaseMessaging.getInstance().subscribeToTopic("Emedilife");
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
        Intent intent;
        if (loggedIn) {
            intent = new Intent(getApplicationContext(), HomeActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), IntroScreenActivity.class);
        }
        startActivity(intent);
        finish();
            }
        }, 2000);
    }

//    private void isLoginFirstTime() {
//        sharedPreferences = getSharedPreferences(getString(R.string.Details), Context.MODE_PRIVATE);
//        editor=sharedPreferences.edit();
//        loggedIn = sharedPreferences.getBoolean("login", false);
//        Intent intent;
//        if (loggedIn) {
//            intent = new Intent(getApplicationContext(), HomeActivity.class);
//        } else {
//            intent = new Intent(getApplicationContext(), IntroScreenActivity.class);
//        }
//        startActivity(intent);
//        finish();
//    }

}