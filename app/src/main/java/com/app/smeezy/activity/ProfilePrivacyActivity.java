package com.app.smeezy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfilePrivacyActivity extends BaseActivity implements IRequestView, View.OnClickListener {

    private static final String SAVE_PROFILE_PRIVACY = "save_profile_privacy";

    @BindView(R.id.cb_profile_privacy_email)
    CheckBox cb_email;

    @BindView(R.id.cb_profile_privacy_dob)
    CheckBox cb_dob;

    @BindView(R.id.cb_profile_privacy_phone)
    CheckBox cb_phone;

    @BindView(R.id.cb_profile_privacy_relationship_status)
    CheckBox cb_relationship;

    @BindView(R.id.cb_profile_privacy_location)
    CheckBox cb_location;

    @BindView(R.id.cb_profile_privacy_social_connection)
    CheckBox cb_social_connection;

    private User user;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private String emailPrivacy, dobPrivacy, phonePrivacy, relationshipPrivacy, locationPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_privacy);
        ButterKnife.bind(this);

        user = (User) getIntent().getSerializableExtra("user");

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        setData();

        setUpToolbar();


    }

    private void setData() {

        if (user.getEmailPrivacy().equals("private")) {
            cb_email.setChecked(true);
        } else {
            cb_email.setChecked(false);
        }

        if (user.getDobPrivacy().equals("private")) {
            cb_dob.setChecked(true);
        } else {
            cb_dob.setChecked(false);
        }

        if (user.getPhonePrivacy().equals("private")) {
            cb_phone.setChecked(true);
        } else {
            cb_phone.setChecked(false);
        }

        if (user.getRelationshipPrivacy().equals("private")) {
            cb_relationship.setChecked(true);
        } else {
            cb_relationship.setChecked(false);
        }

        if (user.getLocationPrivacy().equals("private")) {
            cb_location.setChecked(true);
        } else {
            cb_location.setChecked(false);
        }

        if (user.getSocialConnectionPrivacy().equals("private")) {
            cb_social_connection.setChecked(true);
        } else {
            cb_social_connection.setChecked(false);
        }

    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        ImageView img_logo = (ImageView) findViewById(R.id.img_logo_toolbar);
        Glide.with(mContext)
                .load(PreferenceUtils.getLogoUrl(mContext))
                .asBitmap()
                .into(img_logo);

        TextView activity_title = (TextView) findViewById(R.id.activity_title);
        activity_title.setText(getResources().getString(R.string.title_activity_profile_privacy));
    }


    @OnClick({R.id.btn_profile_privacy_update})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_profile_privacy_update:

                String emailPrivacy = "public";
                String dobPrivacy = "public";
                String phonePrivacy = "public";
                String relationshipPrivacy = "public";
                String locationPrivacy = "public";
                String socialConnectionPrivacy = "public";


                if (cb_email.isChecked()) {
                    emailPrivacy = "private";
                }

                if (cb_dob.isChecked()) {
                    dobPrivacy = "private";
                }

                if (cb_phone.isChecked()) {
                    phonePrivacy = "private";
                }

                if (cb_relationship.isChecked()) {
                    relationshipPrivacy = "private";
                }

                if (cb_location.isChecked()) {
                    locationPrivacy = "private";
                }

                if (cb_social_connection.isChecked()) {
                    socialConnectionPrivacy = "private";
                }

                if (cd.isConnectingToInternet()) {
                    requestPresenter.saveProfilePrivacy(SAVE_PROFILE_PRIVACY, PreferenceUtils.getId(mContext),
                            emailPrivacy, dobPrivacy, phonePrivacy, relationshipPrivacy, locationPrivacy,
                            socialConnectionPrivacy);
                } else {
                    Utills.noInternetConnection(mContext);
                }

                break;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
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
        switch (method) {

            case SAVE_PROFILE_PRIVACY:

                showAlert(getString(R.string.app_name), message);

                break;

        }
    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method) {

            case SAVE_PROFILE_PRIVACY:

                try {

                    JSONObject req = Data.getJSONObject("req");

                    user.withEmailPrivacy(req.optString("email_privacy"))
                            .withDobPrivacy(req.optString("dob_privacy"))
                            .withPhonePrivacy(req.optString("phone_privacy"))
                            .withRelationshipPrivacy(req.optString("relationship_privacy"))
                            .withLocationPrivacy(req.optString("location_privacy"))
                            .withSocialConnectionPrivacy(req.optString("social_connection_privacy"));

                    Intent intent = new Intent();
                    intent.putExtra("user", user);
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
