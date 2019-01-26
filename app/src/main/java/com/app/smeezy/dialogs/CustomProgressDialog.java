package com.app.smeezy.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.ProgressBar;

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        super(context, android.R.style.Theme_Holo_Dialog_NoActionBar);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ProgressBar v = new ProgressBar(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(v);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
