package com.android.hilltrackdoctorfinder.activity.medicine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.adapter.PagerAdapter;
import com.android.hilltrackdoctorfinder.fragment.MedicineShopFragment;
import com.android.hilltrackdoctorfinder.fragment.NearestPharmacyFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicineActivity extends BaseActivity implements View.OnClickListener {
    ViewPager viewPager;
    TabLayout tabLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        ButterKnife.bind(this);
        title.setText(R.string.get_medicine);
        viewPager = findViewById(R.id.simpleViewPager);
        tabLayout = findViewById(R.id.simpleTabLayout);
        setupViewPager();
        back.setOnClickListener(this);
    }
    private void setupViewPager() {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MedicineShopFragment()); //index 0
        adapter.addFragment(new NearestPharmacyFragment()); //index 1
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).setText(R.string.buy_online);
        tabLayout.getTabAt(1).setText(R.string.nearest_pharmacy);
    }

    @Override
    public void onClick(View view) {
        if (view==back){
            finish();
        }
    }
}