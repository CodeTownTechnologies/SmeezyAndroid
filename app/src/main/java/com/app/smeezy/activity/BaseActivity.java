package com.app.smeezy.activity;

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
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
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


/**
 * Created by GAURAV on 9/14/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private String LOGTAG = BaseActivity.class.getName();
    private CustomProgressDialog mProgressDialog;
    public Uri mCameraImageUri;
    public Uri mCameraVideoUri;

    // Toolbar toolbar;
//    public void intialize_toolbar(boolean b, boolean b1, boolean b2, boolean b3, boolean b4, String msg) {
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayShowTitleEnabled(b);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(b1);
//        getSupportActionBar().setDisplayShowHomeEnabled(b2);
//        getSupportActionBar().setHomeButtonEnabled(b3);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//
//            if (b4) {
//                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
//            } else {
//                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//            }
//
//        TextView activity_title = (TextView) findViewById(R.id.activity_title);
//        activity_title.setText(msg);
//
//    }

    public void onHomeClick(View view){

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public void showAlert(String title, String message) {

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("action3", this.getResources().getString(R.string.ok));

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
                actionDialogFragment.show(BaseActivity.this.getSupportFragmentManager(), "normalAlert");
            }
        });
    }

    public void show_custom_alert_for_finish(final Context context, String title, String message){
        final Dialog dialog = new Dialog(context, R.style.AppTheme);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_app_dialog);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txt_message = (TextView) dialog.findViewById(R.id.txt_message);
        TextView txt_action1 = (TextView) dialog.findViewById(R.id.txt_action1);
        TextView txt_action2 = (TextView) dialog.findViewById(R.id.txt_action2);
        TextView txt_action3 = (TextView) dialog.findViewById(R.id.txt_action3);
        txt_action1.setVisibility(View.GONE);
        txt_action2.setVisibility(View.GONE);
        txt_action3.setVisibility(View.VISIBLE);

        txt_action3.setText(getResources().getString(R.string.ok));
        txt_title.setText(title);
        txt_message.setText(message);

        dialog.show();

        txt_action3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });


    }

    public void show_custom_alert_for_refresh(final Context context, String title, String message, final Class nextActivity){
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_app_dialog);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txt_message = (TextView) dialog.findViewById(R.id.txt_message);
        TextView txt_action1 = (TextView) dialog.findViewById(R.id.txt_action1);
        TextView txt_action2 = (TextView) dialog.findViewById(R.id.txt_action2);
        TextView txt_action3 = (TextView) dialog.findViewById(R.id.txt_action3);
        txt_action1.setVisibility(View.GONE);
        txt_action2.setVisibility(View.GONE);
        txt_action3.setVisibility(View.VISIBLE);

        txt_action3.setText(getResources().getString(R.string.ok));
        txt_title.setText(title);
        txt_message.setText(message);

        dialog.show();

        txt_action3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(context, nextActivity);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                finish();
            }
        });


    }


    public void startProgressBar() {
        if (mProgressDialog == null) {
            mProgressDialog = new CustomProgressDialog(this);
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


    public Smeezy getApplicationClass() {
        return (Smeezy) getApplicationContext();
    }

    public void selectImagePopUp() {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.upload_biddialog);

        TextView tv_camera = (TextView) dialog.findViewById(R.id.tv_camera);
        TextView tv_gallery = (TextView) dialog.findViewById(R.id.tv_gallery);
        final TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);

        dialog.show();

        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                checkForCameraPermissions();


            }
        });

        tv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                checkForStoragePermissions();

            }
        });



        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

    }

    public void selectVideoPopUp() {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.upload_biddialog);

        TextView tv_camera = (TextView) dialog.findViewById(R.id.tv_camera);
        TextView tv_gallery = (TextView) dialog.findViewById(R.id.tv_gallery);
        final TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);

        dialog.show();

        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                checkForCameraVideoPermissions();


            }
        });

        tv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                checkForStorageVideoPermissions();

            }
        });



        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

    }

    private void checkForStoragePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                    (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, StaticData.PERMISSIONS_STORAGE, StaticData.PERMISSIONS_REQUEST_STORAGE);

            } else {
                actionGallery();
            }
        } else {
            actionGallery();
        }
    }

    private void checkForStorageVideoPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                    (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, StaticData.PERMISSIONS_STORAGE, StaticData.PERMISSIONS_REQUEST_STORAGE_VIDEO);

            } else {
                actionGalleryVideo();
            }
        } else {
            actionGalleryVideo();
        }
    }

    private void checkForCameraPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, StaticData.PERMISSIONS_CAMERA, StaticData.PERMISSIONS_REQUEST_CAMERA);
            } else {
                actionCamera();
            }
        } else {
            actionCamera();
        }
    }

    private void checkForCameraVideoPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, StaticData.PERMISSIONS_CAMERA, StaticData.PERMISSIONS_REQUEST_CAMERA_VIDEO);
            } else {
                actionCameraVideo();
            }
        } else {
            actionCameraVideo();
        }
    }

    public void checkLocationPermissions(){
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                    (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(StaticData.PERMISSIONS_LOCATION, StaticData.PERMISSIONS_REQUEST_LOCATION);
            } else {
                actionPlacePicker();
            }
        } else {
            actionPlacePicker();
        }


    }

    private void actionPlacePicker(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();


        try {
            startActivityForResult(builder.build(this), StaticData.PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handleRequestPermissionResults(requestCode, permissions, grantResults);

        switch (requestCode) {
            case StaticData.PERMISSIONS_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    actionGallery();
                } else {
                    //code for deny
                }
                break;

            case StaticData.PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    actionCamera();
                } else {
                    //code for deny
                }
                break;

            case StaticData.PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    actionPlacePicker();

                } else {
                    //code for deny
                }
                break;

            case StaticData.PERMISSIONS_REQUEST_CAMERA_VIDEO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    actionCameraVideo();
                } else {
                    //code for deny
                }
                break;

            case StaticData.PERMISSIONS_REQUEST_STORAGE_VIDEO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    actionGalleryVideo();
                } else {
                    //code for deny
                }
                break;


        }
    }

    protected void handleRequestPermissionResults(int requestCode, String[] permissions, int[] grantResults) {
        log("handleRequestPermissionResults");
    }

    public void log(String message) {
        Log.e(getClass().getSimpleName(), message);
    }



    /*To pic image from camera*/
    private void actionCamera() {

        mCameraImageUri = Utills.getOutputMediaFileUri();

        if (mCameraImageUri != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
            } else {
                File file = new File(mCameraImageUri.getPath());
                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, StaticData.CAMERA_IMAGE_REQUEST);
            } else {
                showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.camera_unavailable));
            }
        } else {
            showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.file_save_error));
        }
    }

    /*To pic video from camera*/
    private void actionCameraVideo() {

        mCameraVideoUri = Utills.getOutputMediaVideoFileUri();

        if (mCameraVideoUri != null) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraVideoUri);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120);
            } else {
                File file = new File(mCameraVideoUri.getPath());
                Uri videoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120);
            }

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, StaticData.CAMERA_VIDEO_REQUEST);
            } else {
                showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.camera_unavailable));
            }
        } else {
            showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.file_save_error));
        }
    }



    /*To get image from gallery*/
    private void actionGallery() {

        /*if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image*//*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), StaticData.GALLERY_IMAGE_REQUEST);
            } else {
                showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.gallery_unavailable));
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image*//*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, StaticData.GALLERY_IMAGE_REQUEST);
            } else {
                showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.gallery_unavailable));
            }

        }*/

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), StaticData.GALLERY_IMAGE_REQUEST);
        } else {
            showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.gallery_unavailable));
        }
    }


    /*To get video from gallery*/
    private void actionGalleryVideo() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("video/mp4");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_video)), StaticData.GALLERY_VIDEO_REQUEST);
            } else {
                showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.gallery_unavailable));
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("video/mp4");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, StaticData.GALLERY_VIDEO_REQUEST);
            } else {
                showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.gallery_unavailable));
            }
        }

        /*Intent intent = new Intent();
        intent.setType("video/mp4");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_video)), StaticData.GALLERY_VIDEO_REQUEST);
        } else {
            showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.gallery_unavailable));
        }*/
    }


//    public void show_alert(final Context mcontext) {
//
//        final Dialog dialog = new Dialog(mcontext);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setContentView(R.layout.custom_activation_dialog);
//
//        Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
//     /*   TextView txt_message = (TextView) dialog.findViewById(R.id.txt_message);
//        TextView txt_action1 = (TextView) dialog.findViewById(R.id.txt_action1);
//        TextView txt_action2 = (TextView) dialog.findViewById(R.id.txt_action2);
//        TextView txt_action3 = (TextView) dialog.findViewById(R.id.txt_action3);
//        txt_action3.setVisibility(View.GONE);
//        txt_action1.setVisibility(View.GONE);
//        txt_action2.setVisibility(View.GONE);*/
//     /*   txt_title.setText(title);
//        txt_message.setText(message);*/
//
//        dialog.show();
//
//        btn_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//
//            }
//        });
//
//    }

}