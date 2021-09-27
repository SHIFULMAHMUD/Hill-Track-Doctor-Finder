package com.android.hilltrackdoctorfinder.activity.doctor;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.activity.HomeActivity;
import com.android.hilltrackdoctorfinder.activity.ReminderActivity;
import com.android.hilltrackdoctorfinder.activity.profile.ProfileActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.model.Review;
import com.android.hilltrackdoctorfinder.model.User;
import com.android.hilltrackdoctorfinder.model.Wishlist;
import com.android.hilltrackdoctorfinder.utils.Tools;
import com.android.hilltrackdoctorfinder.utils.Urls;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.DecimalFormat;
import java.util.List;

public class DoctorDetailsActivity extends BaseActivity implements View.OnClickListener {
    RatingBar ratingbar;
    String rating;
    @BindView(R.id.ratingBarProfile)
    RatingBar ratingBarProfile;
    @BindView(R.id.userImageView)
    CircularImageView userImageView;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.ratingTv)
    TextView ratingTv;
    @BindView(R.id.seeReviewTextView)
    TextView seeReviewTextView;
    @BindView(R.id.joinDateTextView)
    TextView joinDateTextView;
    @BindView(R.id.consultTimeTextView)
    TextView consultTimeTextView;
    @BindView(R.id.availabilityTextView)
    TextView availabilityTextView;
    @BindView(R.id.emailTextView)
    TextView emailTextView;
    @BindView(R.id.qualificationTextView)
    TextView qualificationTextView;
    @BindView(R.id.specialistTextView)
    TextView specialistTextView;
    @BindView(R.id.mobileTextView)
    TextView mobileTextView;
    @BindView(R.id.addressTextView)
    TextView addressTextView;
    @BindView(R.id.distanceTextView)
    TextView distanceTextView;
    @BindView(R.id.headingDistanceTv)
    TextView headingDistanceTv;
    @BindView(R.id.phoneImageButton)
    ImageButton phoneImageButton;
    @BindView(R.id.whatsappImageButton)
    ImageButton whatsappImageButton;
    @BindView(R.id.emailImageButton)
    ImageButton emailImageButton;
    @BindView(R.id.reviewImageButton)
    ImageButton reviewImageButton;
    @BindView(R.id.favouriteImageButton)
    ImageButton favouriteImageButton;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    String id,doctor_id,distance;
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        ButterKnife.bind(this);
        title.setText(R.string.doctor_profile);
        id = getIntent().getStringExtra("id");
        doctor_id = getIntent().getStringExtra("doctor_id");

        if (id!=null)
        {
            reviewImageButton.setVisibility(View.GONE);
            favouriteImageButton.setVisibility(View.GONE);
            String data[]=id.split(",");
            distance=data[1];
            distanceTextView.setText(distance+" Km");
            getDoctorProfileInfo(data[0]);
            getReviewInfo(data[0]);
            getWishListInfo(data[0],sharedprefer.getMobile_number());
        }
        if (doctor_id!=null)
        {
            headingDistanceTv.setVisibility(View.GONE);
            distanceTextView.setVisibility(View.GONE);
            getDoctorProfileInfo(doctor_id);
            getReviewInfo(doctor_id);
            getWishListInfo(doctor_id,sharedprefer.getMobile_number());
        }
        back.setOnClickListener(this);
        phoneImageButton.setOnClickListener(this);
        whatsappImageButton.setOnClickListener(this);
        emailImageButton.setOnClickListener(this);
        reviewImageButton.setOnClickListener(this);
        seeReviewTextView.setOnClickListener(this);
        favouriteImageButton.setOnClickListener(this);
    }

    private void getDoctorProfileInfo(String id) {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Doctor>> call = apiInterface.getDoctorDetails(id);
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.code()==200) {
                    loading.end();
                    List<Doctor> body=response.body();
                    Glide.with(context).load(Urls.ImageUrl+body.get(0).getImage()).placeholder(R.drawable.user).into(userImageView);
                    nameTextView.setText(body.get(0).getName());
                    qualificationTextView.setText(body.get(0).getQualification());
                    specialistTextView.setText(body.get(0).getSpecialist());
                    mobileTextView.setText(body.get(0).getMobile());
                    addressTextView.setText(body.get(0).getAddress());
                    availabilityTextView.setText(body.get(0).getAvailability());
                    emailTextView.setText(body.get(0).getEmail());
                    consultTimeTextView.setText(body.get(0).getConsultation_time());
                    joinDateTextView.setText(body.get(0).getJoin_date());
                }
            }
            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                loading.end();
            }
        });
    }
    private void getReviewInfo(String id) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Review>> call = apiInterface.getDoctorReview(id);
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.code()==200) {
                    List<Review> body=response.body();
                    if (!body.isEmpty())
                    {
                        float rating=0;
                        for (int i=0;i<body.size();i++)
                        {
                            rating+=Float.parseFloat(body.get(i).getRating());
                        }
                        ratingBarProfile.setRating(rating/body.size());
                        ratingTv.setText(Float.toString(Float.parseFloat(new DecimalFormat("#.#").format(rating/body.size()))));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {}
        });
    }
    private boolean appInstalledorNot(String path) {
        PackageManager packageManager = DoctorDetailsActivity.this.getPackageManager();
        boolean appsInstall;
        try {
            packageManager.getPackageInfo(path, PackageManager.GET_ACTIVITIES);
            appsInstall = true;

        } catch (PackageManager.NameNotFoundException e) {
            appsInstall = false;
        }

        return appsInstall;
    }
    public static void sendEmail(Context context, String[] recipientList,
                                 String title, String subject, String body) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipientList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, title));
    }
    private void showRatingDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.rating_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        ratingbar=dialog.findViewById(R.id.ratingBar);

        final TextInputEditText review = dialog.findViewById(R.id.reviewEditText);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar arg0, float rateValue, boolean arg2) {
                rating = String.valueOf(rateValue);
            }
        });

        (dialog.findViewById(R.id.linearLayoutSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review.getText().toString().isEmpty()){
                    Tools.setErrorToast(DoctorDetailsActivity.this,"Write a Review");
                }else {
                    loading.start();
                    apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<Review> call = apiInterface.review(doctor_id,review.getText().toString(),rating,sharedprefer.getFirst_name(),sharedprefer.getMobile_number());
                    call.enqueue(new Callback<Review>() {
                        @Override
                        public void onResponse(Call<Review> call, Response<Review> response) {
                            loading.end();
                            if (response.code()==200) {
                                Review body=response.body();
                                if (body.getValue().equals("success"))
                                {
                                    Tools.setSuccessToast(DoctorDetailsActivity.this,body.getMessage());
                                    dialog.dismiss();
                                }
                                else if (body.getValue().equals("failure"))
                                {
                                    Tools.setErrorToast(DoctorDetailsActivity.this,body.getMessage());
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<Review> call, Throwable t) {
                            loading.end();
                        }
                    });
                }
            }
        });


        (dialog.findViewById(R.id.id_btn_purchase_voucher_dialog_cross)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void getWishListInfo(String id,String mobile) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Wishlist>> call = apiInterface.getWishlistInfo(id,mobile);
        call.enqueue(new Callback<List<Wishlist>>() {
            @Override
            public void onResponse(Call<List<Wishlist>> call, Response<List<Wishlist>> response) {
                if (response.code()==200) {
                    List<Wishlist> body=response.body();
                    if (!body.isEmpty()){
                        if (body.get(0).getStatus().equals("1")){
                            favouriteImageButton.setImageResource(R.drawable.ic_favourite);
                        }else {favouriteImageButton.setImageResource(R.drawable.ic_favourite_outline);}
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Wishlist>> call, Throwable t) {

            }
        });
    }

    private void addWishListInfo() {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Wishlist> call = apiInterface.addToWishList(doctor_id,nameTextView.getText().toString(),specialistTextView.getText().toString(),sharedprefer.getMobile_number(),ratingTv.getText().toString(),"1");
        call.enqueue(new Callback<Wishlist>() {
            @Override
            public void onResponse(Call<Wishlist> call, Response<Wishlist> response) {
                if (response.code()==200) {
                    loading.end();
                    Wishlist body=response.body();
                    if (body.getValue().equals("success"))
                    {
                        favouriteImageButton.setImageResource(R.drawable.ic_favourite);
                        Tools.setSuccessToast(DoctorDetailsActivity.this,body.getMessage());
                    }
                    else if (body.getValue().equals("failure"))
                    {
                        Tools.setErrorToast(DoctorDetailsActivity.this,body.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<Wishlist> call, Throwable t) {
                loading.end();
            }
        });
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
                Tools.setErrorToast(DoctorDetailsActivity.this, "WhatsApp Not Installed");
                final String appPackageName = "com.whatsapp"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        }else if (view==emailImageButton){
            sendEmail(context, new String[]{emailTextView.getText().toString()}, "",
                    "", "");
        }else if (view==reviewImageButton){
            showRatingDialog();
        }else if (view==seeReviewTextView){
            if (id!=null)
            {
                String data[]=id.split(",");
                Intent intent=new Intent(DoctorDetailsActivity.this, DoctorReviewActivity.class);
                intent.putExtra("id",data[0]);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
            if (doctor_id!=null)
            {
                Intent intent=new Intent(DoctorDetailsActivity.this, DoctorReviewActivity.class);
                intent.putExtra("id",doctor_id);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        }else if (view==favouriteImageButton){
            addWishListInfo();
        }
    }
}