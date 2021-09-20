package com.android.hilltrackdoctorfinder.activity.profile;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.model.User;
import com.android.hilltrackdoctorfinder.utils.Tools;
import com.android.hilltrackdoctorfinder.utils.Urls;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.changePhotoTextView)
    TextView changePhotoTextView;
    @BindView(R.id.editProfileTextView)
    TextView editProfileTextView;
    @BindView(R.id.firstNameTextView)
    TextView firstNameTextView;
    @BindView(R.id.lastNameTextView)
    TextView lastNameTextView;
    @BindView(R.id.mobileTextView)
    TextView mobileTextView;
    @BindView(R.id.addressTextView)
    TextView addressTextView;
    @BindView(R.id.bloodGroupTextView)
    TextView bloodGroupTextView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    Spinner spinnerBloodGroup;
    String[] blood_group = {"Choose Blood Group","A positive (A+)", "A negative (A-)", "B positive (B+)", "B negative (B-)", "O positive (O+)","O negative (O-)","AB positive (AB+)","AB negative (AB-)"};
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        title.setText("Profile Dashboard");
        back.setOnClickListener(this);
        changePhotoTextView.setOnClickListener(this);
        editProfileTextView.setOnClickListener(this);
        getProfileInfo(sharedprefer.getMobile_number());
    }

    public void getProfileInfo(String mobile) {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<User>> call = apiInterface.getProfile(mobile);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code()==200) {
                    loading.end();
                    List<User> body=response.body();
                    firstNameTextView.setText(body.get(0).getFirst_name());
                    lastNameTextView.setText(body.get(0).getLast_name());
                    mobileTextView.setText(body.get(0).getMobile());
                    addressTextView.setText(body.get(0).getAddress());
                    bloodGroupTextView.setText(body.get(0).getBlood());
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                loading.end();
            }
        });
    }

    private void showEditProfileDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.profile_update_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        spinnerBloodGroup=dialog.findViewById(R.id.spinnerBloodGroup);

        final TextInputEditText first_name = dialog.findViewById(R.id.firstNameTextInputEditText);
        final TextInputEditText last_name = dialog.findViewById(R.id.lastNameTextInputEditText);
        final TextInputEditText address = dialog.findViewById(R.id.addressTextInputEditText);
        final TextInputEditText password = dialog.findViewById(R.id.passwordTextInputEditText);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, blood_group);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodGroup.setAdapter(aa);
        (dialog.findViewById(R.id.linearLayoutSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_name.getText().toString().isEmpty()){
                    Tools.setErrorToast(ProfileActivity.this,"Enter First Name");
                }else if (last_name.getText().toString().isEmpty()){
                    Tools.setErrorToast(ProfileActivity.this,"Enter Last Name");
                }else if (address.getText().toString().isEmpty()){
                    Tools.setErrorToast(ProfileActivity.this,"Enter Full Address");
                }else if (password.getText().toString().isEmpty()){
                    Tools.setErrorToast(ProfileActivity.this,"Enter Password");
                }else if (password.getText().toString().length()<3){
                    Tools.setErrorToast(ProfileActivity.this,"Password must be at least 4 characters");
                }
                else if(spinnerBloodGroup.getSelectedItem().toString().equalsIgnoreCase("Choose Blood Group")){
                    spinnerBloodGroup.getSelectedItem().toString();
                }else {
                    apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<User> call = apiInterface.updateProfile(first_name.getText().toString(),last_name.getText().toString(),mobileTextView.getText().toString(),password.getText().toString(),address.getText().toString(),spinnerBloodGroup.getSelectedItem().toString());
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code()==200) {
                                User body=response.body();
                                if (body.getValue().equals("success"))
                                {
                                    Tools.setSuccessToast(ProfileActivity.this,body.getMessage());
                                    dialog.dismiss();
                                    getProfileInfo(sharedprefer.getMobile_number());
                                }
                                else if (body.getValue().equals("failure"))
                                {
                                    Tools.setErrorToast(ProfileActivity.this,body.getMessage());
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
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

    @Override
    public void onClick(View view) {
        if (view==back){
            finish();
        }else if (view==changePhotoTextView){

        }else if (view==editProfileTextView){
            showEditProfileDialog();
        }
    }
}