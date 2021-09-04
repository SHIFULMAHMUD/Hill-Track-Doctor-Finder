package com.android.hilltrackdoctorfinder.model;

public class ScreenItem {
    String txtTitle;
    int img;

    public ScreenItem(String txtTitle, int img) {
        this.txtTitle = txtTitle;
        this.img = img;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public int getImg() {
        return img;
    }
}
