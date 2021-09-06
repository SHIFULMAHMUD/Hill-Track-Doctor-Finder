package com.android.hilltrackdoctorfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.adapter.ViewPagerSliderAdapter;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ViewPager viewPager_slider;
    ViewPagerSliderAdapter sliderAdapter;
    WormDotsIndicator dots_indicator;
    ArrayList banner_data;
    private Runnable runnable_image = null;
    private Handler handler_image = new Handler();
    private final int sliding_time = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager_slider = findViewById(R.id.viewPager_slider);
        dots_indicator = findViewById(R.id.dots_indicator);
        banner_data = new ArrayList();
        sliderAdapter = new ViewPagerSliderAdapter(this, banner_data);
        setSlidingImages();
        startAutoSliderImage();
        viewPager_slider.setAdapter(sliderAdapter);
        dots_indicator.setViewPager(viewPager_slider);
    }
    private void setSlidingImages(){
        banner_data.clear();
        banner_data.add("https://www.umvuzohealth.co.za/assets/images/content/find-a-doctor.jpg");
        banner_data.add("https://houstonemergency.org/wp-content/uploads/tybs-banner-vaccine-1681x492-a.jpg");
        banner_data.add("https://s3.ap-south-1.amazonaws.com/carousals/MEDICINES.png");
        banner_data.add("https://miro.medium.com/max/1200/0*C926KEmsIP5VmR3J.png");
        banner_data.add("https://d1m5p6svuhetcy.cloudfront.net/wp-content/uploads/2019/11/appointment-reminder-logo.jpg");
        sliderAdapter.notifyDataSetChanged();
    }

    private void startAutoSliderImage() {
        runnable_image = () -> {
            int pos = viewPager_slider.getCurrentItem();
            pos = pos + 1;
            if (pos >= sliderAdapter.getCount()) pos = 0;
            viewPager_slider.setCurrentItem(pos);
            handler_image.postDelayed(runnable_image, sliding_time);
        };
        handler_image.postDelayed(runnable_image, sliding_time);
    }
}