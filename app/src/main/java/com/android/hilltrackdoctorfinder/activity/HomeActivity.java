package com.android.hilltrackdoctorfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.auth.SignInActivity;
import com.android.hilltrackdoctorfinder.activity.auth.SignUpActivity;
import com.android.hilltrackdoctorfinder.activity.covid.CovidCenterActivity;
import com.android.hilltrackdoctorfinder.activity.covid.IsolationCenterActivity;
import com.android.hilltrackdoctorfinder.activity.covid.VaccineActivity;
import com.android.hilltrackdoctorfinder.activity.doctor.DoctorActivity;
import com.android.hilltrackdoctorfinder.activity.medicine.MedicineActivity;
import com.android.hilltrackdoctorfinder.adapter.ViewPagerSliderAdapter;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.User;
import com.google.gson.Gson;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.doctorLayout)
    LinearLayout doctorLayout;
    @BindView(R.id.covidCenterLayout)
    LinearLayout covidCenterLayout;
    @BindView(R.id.isolationCenterLayout)
    LinearLayout isolationCenterLayout;
    @BindView(R.id.vaccineLayout)
    LinearLayout vaccineLayout;
    @BindView(R.id.medicineLayout)
    LinearLayout medicineLayout;
    @BindView(R.id.boatLayout)
    LinearLayout boatLayout;
    @BindView(R.id.reminderLayout)
    LinearLayout reminderLayout;
    @BindView(R.id.viewPager_slider)
    ViewPager viewPager_slider;
    @BindView(R.id.dots_indicator)
    WormDotsIndicator dots_indicator;
    ViewPagerSliderAdapter sliderAdapter;
    ArrayList banner_data;
    private Runnable runnable_image = null;
    private Handler handler_image = new Handler();
    private final int sliding_time = 3000;
    private ApiInterface apiInterface;
    String userMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        banner_data = new ArrayList();
        sliderAdapter = new ViewPagerSliderAdapter(this, banner_data);
        setSlidingImages();
        startAutoSliderImage();
        viewPager_slider.setAdapter(sliderAdapter);
        dots_indicator.setViewPager(viewPager_slider);
        userMobile=sharedprefer.getMobile_number();
        getUserLocation(userMobile);
        doctorLayout.setOnClickListener(this);
        boatLayout.setOnClickListener(this);
        reminderLayout.setOnClickListener(this);
        medicineLayout.setOnClickListener(this);
        vaccineLayout.setOnClickListener(this);
        isolationCenterLayout.setOnClickListener(this);
        covidCenterLayout.setOnClickListener(this);
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
    public void getUserLocation(String mobile) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<User>> call = apiInterface.getProfile(mobile);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code()==200) {
                    List<User> body=response.body();
                    Log.e("user location", new Gson().toJson(body));
                    nameTextView.setText("Welcome "+body.get(0).getFirst_name()+" "+body.get(0).getLast_name()+"!");
                    sharedprefer.setLatitude(body.get(0).getLatitude());
                    sharedprefer.setLongitude(body.get(0).getLongitude());
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("user location failure",t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view==doctorLayout){
            Intent intent=new Intent(HomeActivity.this, DoctorActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else if (view==boatLayout){
            Intent intent=new Intent(HomeActivity.this, BoatActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else if (view==medicineLayout){
            Intent intent=new Intent(HomeActivity.this, MedicineActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else if (view==reminderLayout){
            Intent intent=new Intent(HomeActivity.this, ReminderActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else if (view==vaccineLayout){
            Intent intent=new Intent(HomeActivity.this, VaccineActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else if (view==covidCenterLayout){
            Intent intent=new Intent(HomeActivity.this, CovidCenterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else if (view==isolationCenterLayout){
            Intent intent=new Intent(HomeActivity.this, IsolationCenterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }
}