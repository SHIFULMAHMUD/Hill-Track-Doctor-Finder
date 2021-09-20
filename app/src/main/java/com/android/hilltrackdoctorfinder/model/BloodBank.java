package com.android.hilltrackdoctorfinder.model;

import com.google.gson.annotations.SerializedName;

public class BloodBank {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("cell")
    public String cell;
    @SerializedName("address")
    public String address;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;
    @SerializedName("unions")
    public String unions;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCell() {
        return cell;
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

    public String getUnions() {
        return unions;
    }
}
