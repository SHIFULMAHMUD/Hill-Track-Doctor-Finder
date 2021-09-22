package com.android.hilltrackdoctorfinder.model;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("doc_id")
    public String doc_id;
    @SerializedName("rating")
    public String rating;
    @SerializedName("review")
    public String review;
    @SerializedName("reviewer")
    public String reviewer;
    @SerializedName("value")
    public String value;
    @SerializedName("message")
    public String message;

    public String getDoc_id() {
        return doc_id;
    }

    public String getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public String getReviewer() {
        return reviewer;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
