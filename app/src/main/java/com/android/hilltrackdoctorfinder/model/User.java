package com.android.hilltrackdoctorfinder.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("first_name")
    public String first_name;
    @SerializedName("last_name")
    public String last_name;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("password")
    public String password;
    @SerializedName("address")
    public String address;
    @SerializedName("blood")
    public String blood;
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

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getToken() {
        return token;
    }

    public String getAddress() {
        return address;
    }

    public String getBlood() {
        return blood;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
