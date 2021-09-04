package com.android.hilltrackdoctorfinder.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.adapter.AdapterPagerIntro;
import com.android.hilltrackdoctorfinder.model.ScreenItem;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


public class IntroScreenActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ViewPager viewPagerIntro;
    DotsIndicator DotsIndicator;
    AdapterPagerIntro adapterPagerIntro;
    TextView textViewSkip;
    List<ScreenItem> screenItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);

        viewPagerIntro = findViewById(R.id.viewpager_intro);
        DotsIndicator = findViewById(R.id.dots_indicator);
        textViewSkip = findViewById(R.id.textView_skip);
        loadData();
        adapterPagerIntro = new AdapterPagerIntro(getApplicationContext(), screenItemList);
        viewPagerIntro.setAdapter(adapterPagerIntro);
        DotsIndicator.setViewPager(viewPagerIntro);
        viewPagerIntro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    textViewSkip.setText(getString(R.string.txt_get_start));
                } else {
                    textViewSkip.setText(getString(R.string.txt_skip));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        textViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void loadData() {
        screenItemList.add(new ScreenItem(getString(R.string.first_text), R.drawable.ic_intro_first));
        screenItemList.add(new ScreenItem(getString(R.string.second_text), R.drawable.ic_intro_second));
        screenItemList.add(new ScreenItem(getString(R.string.third_text), R.drawable.ic_intro_third));
    }
}