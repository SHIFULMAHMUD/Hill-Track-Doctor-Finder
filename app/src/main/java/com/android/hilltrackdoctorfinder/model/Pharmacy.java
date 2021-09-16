package com.android.hilltrackdoctorfinder.model;

import com.google.gson.annotations.SerializedName;

public class Pharmacy {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("address")
    public String address;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
