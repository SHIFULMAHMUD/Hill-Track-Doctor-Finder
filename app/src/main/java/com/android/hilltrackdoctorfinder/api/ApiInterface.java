package com.android.hilltrackdoctorfinder.api;

import com.android.hilltrackdoctorfinder.model.Login;
import com.android.hilltrackdoctorfinder.model.Register;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("register.php")
    Call<Register> registration(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("login.php")
    Call<Login> login(
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("token") String token);
}

