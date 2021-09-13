package com.android.hilltrackdoctorfinder.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Class that stores fragments for tabs
 */
public class DoctorPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "DoctorPagerAdapter";

    private final List<Fragment> mFragmentList = new ArrayList<>();


    public DoctorPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }

}