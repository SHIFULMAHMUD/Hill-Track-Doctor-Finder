package com.android.hilltrackdoctorfinder.activity.doctor;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.adapter.DoctorPagerAdapter;
import com.android.hilltrackdoctorfinder.fragment.DoctorListFragment;
import com.android.hilltrackdoctorfinder.fragment.NearestDoctorFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorActivity extends BaseActivity implements View.OnClickListener {
    ViewPager viewPager;
    TabLayout tabLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        ButterKnife.bind(this);
        title.setText(R.string.find_doctor);
        viewPager = findViewById(R.id.simpleViewPager);
        tabLayout = findViewById(R.id.simpleTabLayout);
        setupViewPager();
        back.setOnClickListener(this);
    }
    private void setupViewPager() {
        DoctorPagerAdapter adapter = new DoctorPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NearestDoctorFragment()); //index 0
        adapter.addFragment(new DoctorListFragment()); //index 1
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).setText(R.string.nearest_doctor);
        tabLayout.getTabAt(1).setText(R.string.doctor_list);
    }
    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
//        if (view==layoutMoveToRegister){
//            Intent intent=new Intent(SignInActivity.this, SignUpActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.enter, R.anim.exit);
//        }
//        else
        if (view==back){
            finish();
        }
    }
}