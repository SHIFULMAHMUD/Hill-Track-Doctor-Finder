package com.android.hilltrackdoctorfinder.model;

import com.google.gson.annotations.SerializedName;

public class Wishlist {
    @SerializedName("doc_id")
    public String doc_id;
    @SerializedName("doc_name")
    public String doc_name;
    @SerializedName("doc_specialty")
    public String doc_specialty;
    @SerializedName("user_mobile")
    public String user_mobile;
    @SerializedName("status")
    public String status;
    @SerializedName("rating")
    public String rating;
    @SerializedName("value")
    public String value;
    @SerializedName("message")
    public String message;

    public String getDoc_id() {
        return doc_id;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public String getDoc_specialty() {
        return doc_specialty;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public String getStatus() {
        return status;
    }

    public String getRating() {
        return rating;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
