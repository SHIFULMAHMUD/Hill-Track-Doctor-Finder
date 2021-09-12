package com.android.hilltrackdoctorfinder.model;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("password")
    public String password;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;
    @SerializedName("token")
    public String token;
    @SerializedName("value")
    public String value;
    @SerializedName("message")
    public String message;

    public Login() {}

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
