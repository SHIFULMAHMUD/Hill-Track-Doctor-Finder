package com.android.hilltrackdoctorfinder.activity.auth;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.activity.SplashScreenActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.fragment.NoInternetFragment;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements View.OnClickListener{
    ApiInterface apiInterface;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.editTextFirstName)
    TextInputEditText editTextFirstName;
    @BindView(R.id.editTextLastName)
    TextInputEditText editTextLastName;
    @BindView(R.id.editTextPhone)
    TextInputEditText editTextPhone;
    @BindView(R.id.editTextPassword)
    TextInputEditText editTextPassword;
    @BindView(R.id.editTextConfirmPassword)
    TextInputEditText editTextConfirmPassword;
    @BindView(R.id.cardViewRegister)
    CardView cardViewRegister;
    @BindView(R.id.layoutMoveToLogin)
    LinearLayout layoutMoveToLogin;
    @BindView(R.id.errorTextViewFirstName)
    TextView errorTextViewFirstName;
    @BindView(R.id.errorTextViewLastName)
    TextView errorTextViewLastName;
    @BindView(R.id.errorTextViewPhone)
    TextView errorTextViewPhone;
    @BindView(R.id.errorTextViewPassword)
    TextView errorTextViewPassword;
    @BindView(R.id.errorTextViewConfirmPassword)
    TextView errorTextViewConfirmPassword;
    String firebase_token;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    public static final int REQUEST_CHECK_SETTINGS = 99;
    String latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        getFirebaseClientToken();
        //Check Enable Location
        if (ContextCompat.checkSelfPermission(SignUpActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT>=23) //Android MarshMellow Version or above
            {
                createLocationRequest();
            }
        }
        //Runtime permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getLocation();
        }
        cardViewRegister.setOnClickListener(this);
        layoutMoveToLogin.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==cardViewRegister){
            if (checkValitation(editTextFirstName.getText().toString(), editTextFirstName,errorTextViewFirstName, "[First Name must be filled out]"))
                if (checkValitation(editTextLastName.getText().toString(), editTextLastName,errorTextViewLastName, "[Last Name must be filled out]"))
                    if (phoneValidation(editTextPhone.getText().toString(), errorTextViewPhone, "[Phone Number must be valid]"))
                            if (passwordValidation(editTextPassword.getText().toString(), errorTextViewPassword, "[Password must be filled out]"))
                                if (checkValitation(editTextConfirmPassword.getText().toString(), editTextConfirmPassword,errorTextViewConfirmPassword, "[Confirm Password must be filled out]"))
                                    if (checkMatchPassword(editTextPassword.getText().toString(), editTextConfirmPassword.getText().toString(), errorTextViewConfirmPassword))
                                        checkNetwork();
        }else if (view==layoutMoveToLogin){
            Intent intent=new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
        }
        else if (view==imageViewBack){
            Intent intent=new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
        }
    }
    private void checkNetwork() {
            if (CheckStatus.network(SignUpActivity.this)) {
                registrationRequest();
            } else {
                noInternet();
        }
    }

    private void registrationRequest() {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Register> call = apiInterface.registration(editTextFirstName.getText().toString(),editTextLastName.getText().toString(),editTextPhone.getText().toString(),editTextPassword.getText().toString(),latitude,longitude,firebase_token);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.code()==200) {
                    loading.end();
                    Register body=response.body();
                    if (body.getValue().equals("success"))
                    {
                        Tools.setSuccessToast(SignUpActivity.this,body.getMessage());
                        addNotification();
                        sharedprefer.setMobile_number(editTextPhone.getText().toString());
                        sharedprefer.setFirst_name(editTextFirstName.getText().toString());
                        sharedprefer.setLast_name(editTextLastName.getText().toString());
                        Intent intent=new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
                    }else if (body.getValue().equals("exists"))
                    {
                        Tools.setWarningToast(SignUpActivity.this,body.getMessage());
                    }
                    else if (body.getValue().equals("failure"))
                    {
                        Tools.setErrorToast(SignUpActivity.this,body.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                loading.end();
            }
        });
        }

    private boolean checkValitation(String value, TextInputEditText editText , TextView error, String reason) {
        if (value.isEmpty()) {
            error.setVisibility(View.VISIBLE);
            error.setText(reason);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
            editText.requestFocus();
            return false;
        } else {
            error.setVisibility(View.GONE);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            return true;
        }
    }
    private boolean phoneValidation(String value, TextView error, String reason) {
        if (value.isEmpty() || !value.startsWith("01") || value.length()!=11) {
            error.setVisibility(View.VISIBLE);
            error.setText(reason);
            editTextPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
            editTextPhone.requestFocus();
            return false;
        } else {
            error.setVisibility(View.GONE);
            editTextPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            return true;
        }
    }
    private boolean checkMatchPassword(String password, String conform, TextView error) {
        if (password.equals(conform)) {
            error.setVisibility(View.GONE);
            return true;
        } else {
            error.setVisibility(View.VISIBLE);
            error.setText("Password not matched");
            return false;
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
    private void addNotification() {

        Uri urinotification = null;

        try {
            urinotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), urinotification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("RegisterNotification", "RegisterNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "RegisterNotification")
                        .setSmallIcon(R.drawable.ic_notification_logo)
                        .setContentTitle(getString(R.string.registration_successful))
                        .setSound(urinotification)
                        .setContentText(getString(R.string.congratulation) +" "+ editTextFirstName.getText().toString() +" "+ editTextLastName.getText().toString()+"!");

        Intent notificationIntent = new Intent(this, SplashScreenActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

// Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
    private void noInternet() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        NoInternetFragment newFragment = new NoInternetFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }
    private void getFirebaseClientToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {

                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){
                            firebase_token =task.getResult().getToken();
                            Log.e("firebase_token",firebase_token);
                        }
                    }
                });

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
                        resolvable.startResolutionForResult(SignUpActivity.this,
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
        LocationServices.getFusedLocationProviderClient(SignUpActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(SignUpActivity.this).removeLocationUpdates(this);
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
}