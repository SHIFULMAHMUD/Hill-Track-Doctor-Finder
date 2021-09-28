package com.android.hilltrackdoctorfinder.api;

import com.android.hilltrackdoctorfinder.model.Ambulance;
import com.android.hilltrackdoctorfinder.model.BloodBank;
import com.android.hilltrackdoctorfinder.model.Boat;
import com.android.hilltrackdoctorfinder.model.Center;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.model.Hospital;
import com.android.hilltrackdoctorfinder.model.Login;
import com.android.hilltrackdoctorfinder.model.Notification;
import com.android.hilltrackdoctorfinder.model.Pharmacy;
import com.android.hilltrackdoctorfinder.model.Register;
import com.android.hilltrackdoctorfinder.model.Review;
import com.android.hilltrackdoctorfinder.model.User;
import com.android.hilltrackdoctorfinder.model.Wishlist;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @FormUrlEncoded
    @POST("update_profile.php")
    Call<User> updateProfile(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("address") String address,
            @Field("blood") String blood);

    @FormUrlEncoded
    @POST("review.php")
    Call<Review> review(
            @Field("doc_id") String doc_id,
            @Field("review") String review,
            @Field("rating") String rating,
            @Field("reviewer") String reviewer,
            @Field("reviewer_mobile") String reviewer_mobile);

    @FormUrlEncoded
    @POST("add_to_wishlist.php")
    Call<Wishlist> addToWishList(
            @Field("doc_id") String doc_id,
            @Field("doc_name") String doc_name,
            @Field("doc_specialty") String doc_specialty,
            @Field("user_mobile") String user_mobile,
            @Field("rating") String rating,
            @Field("status") String status);

    @FormUrlEncoded
    @POST("update_wishlist.php")
    Call<Wishlist> updateWishList(
            @Field("doc_id") String doc_id,
            @Field("user_mobile") String user_mobile,
            @Field("status") String status);

    @GET("get_nearest_doctor.php")
    Call<List<Doctor>> getNearestDoctor();

    @GET("get_hospital.php")
    Call<List<Hospital>> getNearestHospital();

    @GET("get_bloodbank.php")
    Call<List<BloodBank>> getNearestBloodBank();

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

    @GET("get_searched_union_ambulance.php")
    Call<List<Ambulance>> getSearchedUnionAmbulance(@Query("text") String text);

    @GET("get_searched_doctor.php")
    Call<List<Doctor>> getSearchedDoctor(@Query("text") String text);

    @GET("get_doctor_details.php")
    Call<List<Doctor>> getDoctorDetails(@Query("id") String id);

    @GET("get_wishlist_info.php")
    Call<List<Wishlist>> getWishlistInfo(@Query("doc_id") String doc_id, @Query("user_mobile") String user_mobile);

    @GET("get_wishlist.php")
    Call<List<Wishlist>> getWishList(@Query("user_mobile") String user_mobile);

    @GET("get_notification.php")
    Call<List<Notification>> getNotifications();

    @GET("get_notification_details.php")
    Call<List<Notification>> getNotificationDetails(@Query("id") String id);

    @GET("get_doctor_review.php")
    Call<List<Review>> getDoctorReview(@Query("doc_id") String doc_id);

    @GET("get_pharmacy_details.php")
    Call<List<Pharmacy>> getPharmacyDetails(@Query("id") String id);

    @GET("get_covid_center.php")
    Call<List<Center>> getCovidCenterInfo(@Query("id") String id);

    @GET("get_isolation_center.php")
    Call<List<Center>> getIsolationCenter(@Query("id") String id);

    @GET("get_ambulance_info.php")
    Call<List<Ambulance>> getAmbulanceData(@Query("id") String id);

    @GET("get_bloodbank_info.php")
    Call<List<BloodBank>> getBloodBankInfo(@Query("id") String id);

    @GET("get_searched_union_bloodbank.php")
    Call<List<BloodBank>> getSearchedBloodBank(@Query("text") String text);

    @GET("get_searched_union_hospital.php")
    Call<List<Hospital>> getSearchedHospital(@Query("text") String text);

    @GET("get_hospital_info.php")
    Call<List<Hospital>> getHospitalInfo(@Query("id") String id);

    @Multipart
    @POST("upload_profile_image.php")
    Call<User> uploadImage(
            @Part MultipartBody.Part file,
            @Part("file") RequestBody name,
            @Part("mobile") RequestBody mobile);
}

