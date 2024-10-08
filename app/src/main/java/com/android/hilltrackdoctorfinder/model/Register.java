package com.android.hilltrackdoctorfinder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Register {
    @SerializedName("first_name")
    public String first_name;
    @SerializedName("last_name")
    public String last_name;
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

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
