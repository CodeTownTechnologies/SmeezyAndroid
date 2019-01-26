package com.app.smeezy.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.dialogs.CustomActionDialog;
import com.app.smeezy.dialogs.CustomProgressDialog;
import com.app.smeezy.settingstructure.Smeezy;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    private CustomProgressDialog mProgressDialog;
    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        return textView;
    }

    public void startProgressBar() {
        if (mProgressDialog == null) {
            mProgressDialog = new CustomProgressDialog(getActivity());
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void dismissProgressBar() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }
    }

    public Smeezy getApplicationClass(Context context) {
        return (Smeezy) context.getApplicationContext();
    }

    public static void showAlert(final Context context, String title, String message) {

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("action3", context.getResources().getString(R.string.ok));

        final CustomActionDialog actionDialogFragment = new CustomActionDialog();
        actionDialogFragment.setArguments(bundle);
        actionDialogFragment.setIActionSelectionListener(new CustomActionDialog.IActionSelectionListener() {
            @Override
            public void onActionSelected(int actionType) {

            }
        });
        actionDialogFragment.setCancelable(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                actionDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "normalAlert");
            }
        });
    }

}
