package com.android.hilltrackdoctorfinder.model;

import com.google.gson.annotations.SerializedName;

public class Center {
    @SerializedName("website")
    private String website;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("cell")
    private String cell;
    @SerializedName("address")
    private String address;
    @SerializedName("facility")
    private String facility;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

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

    public String getFacility() {
        return facility;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getWebsite() {
        return website;
    }
}
