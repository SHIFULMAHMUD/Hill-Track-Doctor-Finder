package com.android.hilltrackdoctorfinder.model;

import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("details")
    public String details;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }
}
