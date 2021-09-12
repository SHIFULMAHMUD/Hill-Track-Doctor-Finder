package com.android.hilltrackdoctorfinder.activity.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.activity.HomeActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.fragment.NoInternetFragment;
import com.android.hilltrackdoctorfinder.model.Login;
import com.android.hilltrackdoctorfinder.model.Register;
import com.android.hilltrackdoctorfinder.utils.CheckStatus;
import com.android.hilltrackdoctorfinder.utils.Tools;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity implements View.OnClickListener, TextWatcher,
        CompoundButton.OnCheckedChangeListener{
    ApiInterface apiInterface;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.editTextPhone)
    TextInputEditText editTextPhone;
    @BindView(R.id.editTextPassword)
    TextInputEditText editTextPassword;
    @BindView(R.id.errorTextViewPhone)
    TextView errorTextViewPhone;
    @BindView(R.id.errorTextViewPassword)
    TextView errorTextViewPassword;
    @BindView(R.id.checkboxRemember)
    CheckBox checkboxRemember;
    @BindView(R.id.layoutMoveToRegister)
    LinearLayout layoutMoveToRegister;
    @BindView(R.id.cardViewLogin)
    CardView cardViewLogin;
    String firebase_token,latitude,longitude;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_PASSWORD = "password";
    public static final int REQUEST_CHECK_SETTINGS = 99;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        sharedprefer.userlogin(true);
        getFirebaseClientToken();
//Check Enable Location
        if (ContextCompat.checkSelfPermission(SignInActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT>=23) //Android MarshMellow Version or above
            {
                createLocationRequest();
            }
        }
//Runtime permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SignInActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getLocation();
        }

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean(KEY_REMEMBER, false))
            checkboxRemember.setChecked(true);
        else
            checkboxRemember.setChecked(false);

        editTextPhone.setText(sharedPreferences.getString(KEY_MOBILE, ""));
        editTextPassword.setText(sharedPreferences.getString(KEY_PASSWORD, ""));

        cardViewLogin.setOnClickListener(this);
        layoutMoveToRegister.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);
        editTextPhone.addTextChangedListener(this);
        editTextPassword.addTextChangedListener(this);
        checkboxRemember.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==cardViewLogin) {
            if (phoneValidation(editTextPhone.getText().toString(), errorTextViewPhone, "[Phone Number must be valid]"))
                if (passwordValidation(editTextPassword.getText().toString(), errorTextViewPassword, "[Password must be filled out]"))
                        checkNetwork();
        }
        else if (view==layoutMoveToRegister){
            Intent intent=new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }

        else if (view==imageViewBack){
            finish();
        }
    }
    private void checkNetwork() {
        if (CheckStatus.network(SignInActivity.this)) {
            loginRequest();
        } else {
            noInternet();
        }
    }
    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.

            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(SignInActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toasty.error(this, "Permission Denied!", Toasty.LENGTH_SHORT).show();
            }
        }
    }
    private void getLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(SignInActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(SignInActivity.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double lat = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double lng = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    latitude = String.valueOf(lat);
                    longitude = String.valueOf(lng);
                }
            }
        }, Looper.getMainLooper());
    }
    private void loginRequest() {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Login> call = apiInterface.login(editTextPhone.getText().toString(),editTextPassword.getText().toString(),latitude,longitude,firebase_token);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.code()==200) {
                    loading.end();
                    Login body=response.body();
                    if (body.getValue().equals("success"))
                    {
                        Tools.setSuccessToast(SignInActivity.this,body.getMessage());
                        Intent intent=new Intent(SignInActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
                    }
                    else if (body.getValue().equals("failure"))
                    {
                        Tools.setErrorToast(SignInActivity.this,body.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                loading.end();
            }
        });

    }
    private void getFirebaseClientToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {

                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){
                            firebase_token =task.getResult().getToken();
                            Log.e("firebase token",firebase_token);
                        }
                    }
                });
    }
    private boolean phoneValidation(String value, TextView error, String reason) {
        if (value.isEmpty() || !value.startsWith("01") || value.length()!=11) {
            error.setVisibility(View.VISIBLE);
            error.setText(reason);
            editTextPhone.requestFocus();
            editTextPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
            return false;
        } else {
            error.setVisibility(View.GONE);
            editTextPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            return true;
        }
    }

    private boolean passwordValidation(String value, TextView error, String reason) {
        if (value.isEmpty()) {
            error.setVisibility(View.VISIBLE);
            error.setText(reason);
            editTextPassword.requestFocus();
            return false;
        } else if (value.length() < 4) {
            error.setVisibility(View.VISIBLE);
            error.setText("Minimum-4 digit");
            editTextPassword.requestFocus();
            return false;
        }else {
            error.setVisibility(View.GONE);
            return true;
        }
    }

    private void noInternet() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        NoInternetFragment newFragment = new NoInternetFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        managePrefs();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        managePrefs();
    }

    private void managePrefs(){
        if(checkboxRemember.isChecked()){
            editor.putString(KEY_MOBILE, editTextPhone.getText().toString().trim());
            editor.putString(KEY_PASSWORD, editTextPassword.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
        }else{
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASSWORD);
            editor.remove(KEY_MOBILE);
        }
        editor.apply();
    }
}
