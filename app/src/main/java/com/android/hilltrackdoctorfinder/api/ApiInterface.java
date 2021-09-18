package com.android.hilltrackdoctorfinder.api;

import com.android.hilltrackdoctorfinder.model.Boat;
import com.android.hilltrackdoctorfinder.model.Center;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.model.Login;
import com.android.hilltrackdoctorfinder.model.Pharmacy;
import com.android.hilltrackdoctorfinder.model.Register;
import com.android.hilltrackdoctorfinder.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("profile.php")
    Call<List<User>> getProfile(@Query("mobile") String mobile);

    @GET("get_nearest_doctor.php")
    Call<List<Doctor>> getNearestDoctor();

    @GET("covid_center.php")
    Call<List<Center>> getNearestCovidCenter();

    @GET("isolation_center.php")
    Call<List<Center>> getNearestIsolationCenter();

    @GET("get_nearest_pharmacy.php")
    Call<List<Pharmacy>> getNearestPharmacy();

    @GET("get_boats.php")
    Call<List<Boat>> getAllBoats();

    @GET("get_searched_union_boat.php")
    Call<List<Boat>> getSearchedUnionBoat(@Query("text") String text);

    @GET("get_searched_doctor.php")
    Call<List<Doctor>> getSearchedDoctor(@Query("text") String text);

    @GET("get_doctor_details.php")
    Call<List<Doctor>> getDoctorDetails(@Query("id") String id);

    @GET("get_pharmacy_details.php")
    Call<List<Pharmacy>> getPharmacyDetails(@Query("id") String id);

    @GET("get_covid_center.php")
    Call<List<Center>> getCovidCenterInfo(@Query("id") String id);

    @GET("get_isolation_center.php")
    Call<List<Center>> getIsolationCenter(@Query("id") String id);
}

