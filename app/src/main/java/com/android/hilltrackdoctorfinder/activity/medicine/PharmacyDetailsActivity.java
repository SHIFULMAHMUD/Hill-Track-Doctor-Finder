package com.android.hilltrackdoctorfinder.activity.medicine;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.model.Pharmacy;
import com.android.hilltrackdoctorfinder.utils.Tools;
import com.android.hilltrackdoctorfinder.utils.Urls;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PharmacyDetailsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.mobileTextView)
    TextView mobileTextView;
    @BindView(R.id.addressTextView)
    TextView addressTextView;
    @BindView(R.id.distanceTextView)
    TextView distanceTextView;
    @BindView(R.id.phoneImageButton)
    ImageButton phoneImageButton;
    @BindView(R.id.whatsappImageButton)
    ImageButton whatsappImageButton;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    String id,distance;
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_details);
        ButterKnife.bind(this);
        title.setText(R.string.pharmacy);
        id = getIntent().getStringExtra("id");

        if (id!=null)
        {
            String data[]=id.split(",");
            distance=data[1];
            distanceTextView.setText(distance+" Km");
            getPharmacyInfo(data[0]);
        }

        back.setOnClickListener(this);
        phoneImageButton.setOnClickListener(this);
        whatsappImageButton.setOnClickListener(this);
    }

    private void getPharmacyInfo(String id) {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Pharmacy>> call = apiInterface.getPharmacyDetails(id);
        call.enqueue(new Callback<List<Pharmacy>>() {
            @Override
            public void onResponse(Call<List<Pharmacy>> call, Response<List<Pharmacy>> response) {
                if (response.code()==200) {
                    loading.end();
                    List<Pharmacy> body=response.body();
                    nameTextView.setText(body.get(0).getName());
                    mobileTextView.setText(body.get(0).getMobile());
                    addressTextView.setText(body.get(0).getAddress());
                }
            }
            @Override
            public void onFailure(Call<List<Pharmacy>> call, Throwable t) {
                loading.end();
            }
        });
    }
    private boolean appInstalledorNot(String path) {
        PackageManager packageManager = PharmacyDetailsActivity.this.getPackageManager();
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
                Tools.setErrorToast(PharmacyDetailsActivity.this, "WhatsApp Not Installed");
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