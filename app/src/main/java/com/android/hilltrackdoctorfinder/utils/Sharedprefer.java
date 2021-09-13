package com.android.hilltrackdoctorfinder.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Sharedprefer {
    Context context;
    public SharedPreferences default_prefence;

    public Sharedprefer(Context context) {
        this.context = context;
        default_prefence = context.getSharedPreferences("health_guardian", Context.MODE_PRIVATE);
    }


    public String getUsername() {
        return default_prefence.getString("username", "");
    }

    public void setUsername(String username) {
        default_prefence.edit().putString("username", username).apply();
    }

    public String getFirst_name() {
        return default_prefence.getString("user_first_name", "");
    }

    public void setFirst_name(String first_name) {
        default_prefence.edit().putString("user_first_name", first_name).apply();
    }

    public String getLast_name() {
        return default_prefence.getString("user_last_name", "");
    }

    public void setLast_name(String last_name) {
        default_prefence.edit().putString("user_last_name", last_name).apply();
    }

    public String getMobile_number() {
        return default_prefence.getString("user_mobile_number", "");
    }

    public void setMobile_number(String mobile_number) {
        default_prefence.edit().putString("user_mobile_number", mobile_number).apply();
    }

    public String getLatitude() {
        return default_prefence.getString("user_latitude", "");
    }

    public void setLatitude(String user_latitude) {
        default_prefence.edit().putString("user_latitude", user_latitude).apply();
    }
    public String getLongitude() {
        return default_prefence.getString("user_longitude", "");
    }

    public void setLongitude(String user_longitude) {
        default_prefence.edit().putString("user_longitude", user_longitude).apply();
    }

    public String getRecentNoInternet_page() {

        return default_prefence.getString("internet_error_last_page", "");
    }

    public void setRecentNoInternet_page(String activity) {
        default_prefence.edit().putString("internet_error_last_page", activity).apply();
    }

    public String getUserId() {

        return default_prefence.getString("user_ID", "");
    }

    public void setUserId(String activity) {
        default_prefence.edit().putString("user_ID", activity).apply();
    }


    public String getUser_profile_picture() {
        return default_prefence.getString("user_profile_picture", "");
    }

    public void setUser_profile_picture(String activity) {
        default_prefence.edit().putString("user_profile_picture", activity).apply();
    }

    public Boolean userlogin() {
        return default_prefence.getBoolean("userslogin", false);
    }

    public void userlogin(Boolean userlogin) {
        default_prefence.edit().putBoolean("userslogin", userlogin).apply();
    }

    public String getLanguage() {
        return default_prefence.getString("Locale.Helper.Selected.Language", "en");
    }

    public void setLanguage(String language) {
        default_prefence.edit().putString("Locale.Helper.Selected.Language", language).apply();
    }
}
