package com.android.hilltrackdoctorfinder.activity.auth;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.activity.SplashScreenActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.fragment.NoInternetFragment;
import com.android.hilltrackdoctorfinder.model.Signup;
import com.android.hilltrackdoctorfinder.utils.CheckStatus;
import com.android.hilltrackdoctorfinder.utils.Tools;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    String latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        getFirebaseClientToken();
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
                            if (passwordValitation(editTextPassword.getText().toString(), errorTextViewPassword, "[Password must be filled out]"))
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
        Call<Signup> call = apiInterface.registration(editTextFirstName.getText().toString(),editTextLastName.getText().toString(),editTextPhone.getText().toString(),editTextPassword.getText().toString(),latitude,longitude,firebase_token);
        call.enqueue(new Callback<Signup>() {
            @Override
            public void onResponse(Call<Signup> call, Response<Signup> response) {
                if (response.code()==200) {
                    loading.end();
                    Signup body=response.body();
                    if (body.getValue().equals("success"))
                    {
                        Tools.setSuccessToast(SignUpActivity.this,body.getMessage());
                        addNotification();
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
            public void onFailure(Call<Signup> call, Throwable t) {
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
            error.setText(R.string.password_not_match);
            return false;
        }
    }

    private boolean passwordValitation(String value, TextView error, String reason) {
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