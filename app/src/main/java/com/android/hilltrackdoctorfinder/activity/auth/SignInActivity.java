package com.android.hilltrackdoctorfinder.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.utils.CheckStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity implements View.OnClickListener{
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
    @BindView(R.id.textViewForgetPassword)
    TextView textViewForgetPassword;
    @BindView(R.id.layoutMoveToRegister)
    LinearLayout layoutMoveToRegister;
    @BindView(R.id.cardViewLogin)
    CardView cardViewLogin;
    String firebase_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
//        getFirebaseClientToken();
        cardViewLogin.setOnClickListener(this);
        layoutMoveToRegister.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);
        textViewForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==cardViewLogin) {
            if (phoneValidation(editTextPhone.getText().toString(), errorTextViewPhone, "[Phone Number must be valid]"))
                if (passwordValitation(editTextPassword.getText().toString(),editTextPassword, errorTextViewPassword, "[Password must be filled out]"))
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
//            loginRequest();
        } else {
//            noInternet();
        }

    }
//    private void loginRequest() {
//
//        Login login = new Login();
//
//        login.setMobile(editTextPhone.getText().toString());
//        login.setPassword(editTextPassword.getText().toString());
//        login.setFirebase_token(firebase_token);
//
//        loading.start();
//        Call<Signin> signinCall = ApiClient.getInstance(this).getApi().login(login);
//        signinCall.enqueue(new Callback<Signin>() {
//            @Override
//            public void onResponse(Call<Signin> call, Response<Signin> response) {
//                loading.end();
//                if (response.code() == 200) {
//                    Signin signin = response.body();
//                    if (signin.getStatus()) {
//                        Tools.sweetAlertSuccessDialog(context,signin.getMessage());
//                        sharedprefer.userlogin(true);
//                        sharedprefer.setApiToken(signin.getAuth_token());
//                        Intent a = new Intent(SignInActivity.this, HomeActivity.class);
//                        startActivity(a);
//                        overridePendingTransition(R.anim.enter, R.anim.exit);
//                        finish();
//                    } else {
//                        Tools.sweetAlertErrorDialog(context,signin.getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Signin> call, Throwable t) {
//                Tools.setErrorToast(SignInActivity.this, "Something Wrong. Try Again");
//                call.cancel();
//            }
//        });
//    }
//    private void getFirebaseClientToken(){
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (task.isSuccessful()){
//                            firebase_token =task.getResult().getToken();
//                            Log.e("firebase token",firebase_token);
//                        }
//                    }
//                });
//    }
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

    private boolean passwordValitation(String value, TextInputEditText editText, TextView error, String reason) {
        if (value.isEmpty()) {
            error.setVisibility(View.VISIBLE);
            error.setText(reason);
            editText.requestFocus();
            return false;
        }
        else{
            error.setVisibility(View.GONE);
            return true;
        }

//        else {
//
//            if (validPassword1(value)) {
//                error.setVisibility(View.GONE);
//                if (validPassword(value,error)) {
//                    return true;
//                } else {
//
//                    return false;
//                }
//            } else {
//                error.setVisibility(View.VISIBLE);
//                error.setText("Minimum-8 digit");
//                return false;
//            }
//        }
    }

    public static boolean validPassword(final String password,TextView error) {

        if (!Pattern.compile("^(?=.*[A-Z]).{8,}$").matcher(password).matches()){
            error.setVisibility(View.VISIBLE);
            error.setText("Must have a capital letter");
            return false;
        }else if (!Pattern.compile("^(?=.*[a-z]).{8,}$").matcher(password).matches()){
            error.setVisibility(View.VISIBLE);
            error.setText("Must have a small letter");
            return false;
        }else if (!Pattern.compile("^(?=.*[@#$%^&;+=.!_]).{8,}$").matcher(password).matches()){
            error.setVisibility(View.VISIBLE);
            error.setText("Must have a special character");
            return false;
        }else if (!Pattern.compile("^(?=.*[0-9]).{8,}$").matcher(password).matches()){
            error.setVisibility(View.VISIBLE);
            error.setText("Must have a number");
            return false;
        }else if (!Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.!_])(?=\\S+$).{8,}$").matcher(password).matches()){
            error.setVisibility(View.INVISIBLE);
            error.setText("Wrong pattern");
            return false;
        }else {
            return true;
        }

    }


    public static boolean validPassword1(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^.{8,}$";
//        final String PASSWORD_PATTERN = "^.{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

//    private void noInternet() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        NoInternetFragment newFragment = new NoInternetFragment();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
//    }
}