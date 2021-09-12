package com.android.hilltrackdoctorfinder.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.android.hilltrackdoctorfinder.R;


public class Loading {

    Context context;
    Dialog dialog;


    public Loading(Context context, Boolean cancelable) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.loading_ultra);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(cancelable);
    }

    public void start() {
        dialog.show();
    }

    public void end() {
        dialog.dismiss();
    }

    public boolean isShow() {
        return dialog.isShowing();
    }
}
