package com.android.hilltrackdoctorfinder.model;

import com.google.gson.annotations.SerializedName;

public class Doctor {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("image")
    public String image;
    @SerializedName("qualification")
    public String qualification;
    @SerializedName("specialist")
    public String specialist;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("address")
    public String address;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;
    @SerializedName("email")
    public String email;
    @SerializedName("availability")
    public String availability;
    @SerializedName("consultation_time")
    public String consultation_time;
    @SerializedName("join_date")
    public String join_date;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getQualification() {
        return qualification;
    }

    public String getSpecialist() {
        return specialist;
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

    public String getEmail() {
        return email;
    }

    public String getAvailability() {
        return availability;
    }

    public String getConsultation_time() {
        return consultation_time;
    }

    public String getJoin_date() {
        return join_date;
    }
}
