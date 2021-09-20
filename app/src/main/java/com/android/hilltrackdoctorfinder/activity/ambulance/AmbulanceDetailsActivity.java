package com.android.hilltrackdoctorfinder.activity.ambulance;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Ambulance;
import com.android.hilltrackdoctorfinder.model.Center;
import com.android.hilltrackdoctorfinder.utils.Tools;
import com.android.hilltrackdoctorfinder.utils.Urls;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmbulanceDetailsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ambulanceImageView)
    ImageView ambulanceImageView;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.mobileTextView)
    TextView mobileTextView;
    @BindView(R.id.addressTextView)
    TextView addressTextView;
    @BindView(R.id.unionTextView)
    TextView unionTextView;
    @BindView(R.id.facilityTextView)
    TextView facilityTextView;
    @BindView(R.id.phoneImageButton)
    ImageButton phoneImageButton;
    @BindView(R.id.whatsappImageButton)
    ImageButton whatsappImageButton;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    String id;
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_details);
        ButterKnife.bind(this);
        title.setText("Ambulance");
        id = getIntent().getStringExtra("id");
        getAmbulanceInfo(id);
        back.setOnClickListener(this);
        phoneImageButton.setOnClickListener(this);
        whatsappImageButton.setOnClickListener(this);
    }

    private void getAmbulanceInfo(String id) {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Ambulance>> call = apiInterface.getAmbulanceData(id);
        call.enqueue(new Callback<List<Ambulance>>() {
            @Override
            public void onResponse(Call<List<Ambulance>> call, Response<List<Ambulance>> response) {
                if (response.code()==200) {
                    loading.end();
                    List<Ambulance> body=response.body();
                    nameTextView.setText(body.get(0).getName());
                    mobileTextView.setText(body.get(0).getMobile());
                    addressTextView.setText(body.get(0).getAddress());
                    facilityTextView.setText(body.get(0).getFacility());
                    unionTextView.setText(body.get(0).getUnions());
                    Glide.with(context).load(Urls.ImageUrl+body.get(0).getImage()).placeholder(R.drawable.ic_ambulance).into(ambulanceImageView);
                }
            }
            @Override
            public void onFailure(Call<List<Ambulance>> call, Throwable t) {
                loading.end();
            }
        });
    }
    private boolean appInstalledorNot(String path) {
        PackageManager packageManager = AmbulanceDetailsActivity.this.getPackageManager();
        boolean appsInstall;
        try {
            packageManager.getPackageInfo(path, PackageManager.GET_ACTIVITIES);
            appsInstall = true;

        } catch (PackageManager.NameNotFoundException e) {
            appsInstall = false;
        }

        return appsInstall;
    }

    @Override
    public void onClick(View view) {
        if (view==back){
            finish();
        }else if (view==phoneImageButton){
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+mobileTextView.getText().toString()));
            context.startActivity(callIntent);
        }else if (view==whatsappImageButton){
            boolean installed = appInstalledorNot("com.whatsapp");
            if (installed) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + mobileTextView.getText().toString() + "&text="));
                startActivity(intent);

            } else {
                Tools.setErrorToast(AmbulanceDetailsActivity.this, "WhatsApp Not Installed");
                final String appPackageName = "com.whatsapp"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        }
    }
}