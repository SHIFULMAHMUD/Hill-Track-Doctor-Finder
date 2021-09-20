package com.android.hilltrackdoctorfinder.activity.hospital;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.adapter.PagerAdapter;
import com.android.hilltrackdoctorfinder.fragment.BloodBankListFragment;
import com.android.hilltrackdoctorfinder.fragment.HospitalListFragment;
import com.android.hilltrackdoctorfinder.fragment.NearestBloodBankFragment;
import com.android.hilltrackdoctorfinder.fragment.NearestHospitalFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HospitalActivity extends BaseActivity implements View.OnClickListener {
    ViewPager viewPager;
    TabLayout tabLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        ButterKnife.bind(this);
        title.setText("Hospital");
        viewPager = findViewById(R.id.simpleViewPager);
        tabLayout = findViewById(R.id.simpleTabLayout);
        setupViewPager();
        back.setOnClickListener(this);
    }
    private void setupViewPager() {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HospitalListFragment()); //index 0
        adapter.addFragment(new NearestHospitalFragment()); //index 1
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).setText("Hospital List");
        tabLayout.getTabAt(1).setText("Nearest Hospital");
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