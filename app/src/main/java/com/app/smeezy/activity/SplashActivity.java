package com.app.smeezy.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity implements IRequestView {
    private static final int SPLASH_TIME = 1 * 1000;

    private static final String GET_CONFIG = "get_config";

    @BindView(R.id.img_splash_logo)
    ImageView img_splash_logo;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        mContext = this;

        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        PreferenceUtils.setLogoUrl(mContext, R.drawable.top_logo);

        Utills.printHashKey(mContext);

        if (cd.isConnectingToInternet()) {
            requestPresenter.getConfig(GET_CONFIG);
        } else {
            Utills.noInternetConnection(mContext);
        }

        /*if (PreferenceUtils.getLogoUrl(mContext) != null) {

            Glide.with(mContext)
                    .load(PreferenceUtils.getLogoUrl(mContext))
                    .asBitmap()
                    .into(img_splash_logo);

        }*/

    }

    @Override
    public void showLoadingProgressBar() {
        startProgressBar();
    }

    @Override
    public void hideLoadingProgressBar() {
        dismissProgressBar();
    }

    @Override
    public void Failed(String method, String message) {

    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method) {

            case GET_CONFIG:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    String logoUrl = data.optString("logo");
                    String appName = data.optString("site_title");
                    String contactEmail = data.optString("contact_email");
                    String copyright = data.optString("copyright");
                    JSONObject android = data.optJSONObject("android");
                    String playStoreUrl = android.optString("url");
                    String latestVersionName = android.optString("version");


                    //PreferenceUtils.setLogoUrl(mContext, logoUrl);
                    PreferenceUtils.setContactEmail(mContext, contactEmail);

                   /* if (PreferenceUtils.getLogoUrl(mContext) != null) {

                        Glide.with(mContext)
                                .load(PreferenceUtils.getLogoUrl(mContext))
                                .asBitmap()
                                .into(img_splash_logo);

                    }*/

                    boolean updateAvailable = false;


                    PackageManager manager = mContext.getPackageManager();
                    try {
                        PackageInfo info = manager.getPackageInfo(
                                mContext.getPackageName(), 0);
                        String version = info.versionName;
                        float currentVersion = Float.parseFloat(version);
                        float latestVersion = Float.parseFloat(latestVersionName);

                        if (latestVersion > currentVersion) {
                            updateAvailable = true;
                        }

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }


                    if (updateAvailable) {
                        showUpdateDialog(mContext, playStoreUrl);
                    } else {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (PreferenceUtils.getIsLogin(mContext)) {
                                    if (PreferenceUtils.getStepFirst(mContext).equals("Yes")) {
                                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(SplashActivity.this, MyProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }, SPLASH_TIME);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }


    private void showUpdateDialog(final Context context, final String url) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_app_dialog);

        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txt_message = (TextView) dialog.findViewById(R.id.txt_message);
        TextView txt_action1 = (TextView) dialog.findViewById(R.id.txt_action1);
        TextView txt_action2 = (TextView) dialog.findViewById(R.id.txt_action2);
        TextView txt_action3 = (TextView) dialog.findViewById(R.id.txt_action3);
        txt_action3.setVisibility(View.VISIBLE);
        txt_action1.setVisibility(View.GONE);
        txt_action2.setVisibility(View.GONE);
        txt_action3.setText(context.getResources().getString(R.string.update));
        txt_title.setText(context.getResources().getString(R.string.new_version_available));
        txt_message.setText(context.getResources().getString(R.string.there_is_a_newer_version_available));

        dialog.show();


        txt_action3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setPackage("com.android.vending");
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }


            }
        });


    }
}