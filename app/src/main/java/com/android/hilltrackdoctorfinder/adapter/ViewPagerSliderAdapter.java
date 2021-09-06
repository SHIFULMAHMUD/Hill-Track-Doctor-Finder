package com.android.hilltrackdoctorfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.hilltrackdoctorfinder.R;
import com.bumptech.glide.Glide;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerSliderAdapter extends PagerAdapter {
    private Context context;
    private ArrayList mSliderItems;

    public ViewPagerSliderAdapter(Context context, ArrayList mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_slider_image, null);
        ImageView img_slider_image = view.findViewById(R.id.img_slider_image);
        Glide.with(context).load(mSliderItems.get(position)).into(img_slider_image);
//        Glide.with(context).load(mSliderItems.get(position)).placeholder(R.drawable.ic_placeholder).into(img_slider_image);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
