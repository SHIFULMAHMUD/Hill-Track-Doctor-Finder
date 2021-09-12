package com.android.hilltrackdoctorfinder.activity;

import android.content.Context;
import android.os.Bundle;

import com.android.hilltrackdoctorfinder.utils.Loading;
import com.android.hilltrackdoctorfinder.utils.Sharedprefer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public Loading loading;
    public Sharedprefer sharedprefer;
    public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading=new Loading(this,false);
        sharedprefer = new Sharedprefer(this);
        context=this;
    }
}
