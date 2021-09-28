package com.android.hilltrackdoctorfinder.activity.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
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
import android.widget.Toast;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.activity.HomeActivity;
import com.android.hilltrackdoctorfinder.activity.auth.SignInActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.model.Login;
import com.android.hilltrackdoctorfinder.model.User;
import com.android.hilltrackdoctorfinder.utils.Tools;
import com.android.hilltrackdoctorfinder.utils.Urls;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
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
    @BindView(R.id.userImageView)
    CircularImageView userImageView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 9;
    private static final int SELECT_REQUEST_CODE = 6;
    Bitmap bitmap = null;
    Uri imageUri;
    Spinner spinnerBloodGroup;
    String[] blood_group = {"A positive (A+)", "A negative (A-)", "B positive (B+)", "B negative (B-)", "O positive (O+)","O negative (O-)","AB positive (AB+)","AB negative (AB-)"};
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        title.setText(R.string.manage_profile);
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
                    Glide.with(context).load(Urls.ProfileImageUrl+body.get(0).getImage()).placeholder(R.drawable.user).into(userImageView);
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
    private void imageSelectionAction() {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Choose Action")
                    .setCancelable(true)
                    .setPositiveButton(Html.fromHtml("<font color='#A4034E'>Camera</font>"), (dialog, which) -> {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1100);

                    })
                    .setNegativeButton(Html.fromHtml("<font color='#A4034E'>Storage</font>"), (dialog, which) -> {

                        // Perform Your Task Here--When No is pressed
                        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(select, SELECT_REQUEST_CODE);
                        dialog.cancel();
                    }).show();

        } else {
            requestAllPermission();
        }

    }
    private void requestAllPermission() {
        Dexter.withActivity(ProfileActivity.this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CAMERA
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            imageSelectionAction();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        //Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_REQUEST_CODE && data != null) {
            try {
                imageUri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Glide.with(getApplicationContext()).load(imageUri).centerCrop().into(userImageView);
                ImageUpload();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 1100 && resultCode == RESULT_OK && data != null) {

            bitmap = (Bitmap) data.getExtras().get("data");

// CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            imageUri = getImageUri(ProfileActivity.this, bitmap);
            Glide.with(getApplicationContext()).load(imageUri).centerCrop().into(userImageView);
            ImageUpload();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void ImageUpload() {
        File file = new File(getRealPathFromURI(imageUri));
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), sharedprefer.getMobile_number());

        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<User> call = apiInterface.uploadImage(fileToUpload,filename,mobile);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code()==200) {
                    loading.end();
                    User body=response.body();
                    if (body.getValue().equals("success"))
                    {
                        Tools.setSuccessToast(ProfileActivity.this,body.getMessage());
                    }
                    else if (body.getValue().equals("failure"))
                    {
                        Tools.setErrorToast(ProfileActivity.this,body.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loading.end();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view==back){
            finish();
        }else if (view==changePhotoTextView){
            imageSelectionAction();
        }else if (view==editProfileTextView){
            showEditProfileDialog();
        }
    }
}