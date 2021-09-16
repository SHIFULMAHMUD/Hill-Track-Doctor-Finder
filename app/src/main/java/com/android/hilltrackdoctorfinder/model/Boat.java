package com.android.hilltrackdoctorfinder.model;

import com.google.gson.annotations.SerializedName;

public class Boat {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("address")
    public String address;
    @SerializedName("unions")
    public String unions;

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

    public String getUnions() {
        return unions;
    }
}
