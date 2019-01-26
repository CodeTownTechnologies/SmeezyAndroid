package com.app.smeezy.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Login;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements View.OnClickListener, IRequestView {

    public static final String SIGNUP_METHOD = "signup";
    public static final String FACEBOOK_LOGIN_METHOD = "fb_login";
    public static final String GOOGLE_LOGIN_METHOD = "google_login";

    @BindView(R.id.et_signup_name)
    EditText et_name;

    @BindView(R.id.et_signup_email)
    EditText et_email;

    @BindView(R.id.et_signup_password)
    EditText et_password;

    @BindView(R.id.et_signup_confirm_password)
    EditText et_confirm_password;

    @BindView(R.id.et_signup_referral_code)
    EditText et_referral_code;

    @BindView(R.id.cb_singup)
    CheckBox cb_terms;


    @BindView(R.id.signup_logo)
    ImageView img_signup_logo;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        login = (Login) getIntent().getSerializableExtra("login");

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        setUpToolbar();

        if (login.getLoginType().equals(Login.FACEBOOK) ||
                login.getLoginType().equals(Login.GOOGLE)) {

            et_name.setText(login.getName());
            et_email.setText(login.getEmail());
            et_email.setFocusableInTouchMode(false);
            et_email.setLongClickable(false);
            et_email.setEnabled(false);

        }

        /*if (PreferenceUtils.getLogoUrl(mContext) != null) {

            Glide.with(mContext)
                    .load(PreferenceUtils.getLogoUrl(mContext))
                    .asBitmap()
                    .into(img_signup_logo);

        }*/

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

        TextView activity_title = (TextView) findViewById(R.id.activity_title);
        activity_title.setText(getResources().getString(R.string.title_activity_signup));
        ImageView img_logo = (ImageView) findViewById(R.id.img_logo_toolbar);
        img_logo.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_register})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_register:

                String name = et_name.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString();
                String confirmPassword = et_confirm_password.getText().toString();
                String referralCode = et_referral_code.getText().toString();
                boolean termsChecked = cb_terms.isChecked();

                if (name.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.all_fields_required));
                } else if (name.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.name_required));
                } else if (email.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.email_required));
                } else if (password.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.password_required));
                } else if (confirmPassword.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.confirm_password_required));
                } else if (!Utills.isEmailValid(email)) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.provide_a_valid_email_address));
                } else if (!password.equals(confirmPassword)) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.password_not_same));
                } else if (!termsChecked) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.accept_terms_and_conditions));
                } else {
                    if (cd.isConnectingToInternet()) {
                        if (login.getLoginType().equals(Login.FACEBOOK)) {
                            requestPresenter.facebookLogin(FACEBOOK_LOGIN_METHOD, email, password, login.getSocialId(), name,
                                    login.getImageUrl(), referralCode);
                        } else if (login.getLoginType().equals(Login.GOOGLE)) {
                            requestPresenter.googleLogin(GOOGLE_LOGIN_METHOD, email, password, login.getSocialId(), name,
                                    login.getImageUrl(), referralCode);
                        } else {
                            requestPresenter.signUp(SIGNUP_METHOD, name, email, password, referralCode);
                        }
                    } else {
                        Utills.noInternetConnection(mContext);
                    }

                }

                break;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                backButtonAction();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backButtonAction();
    }

    private void backButtonAction() {

        if (com.facebook.AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }

        if (GoogleSignIn.getLastSignedInAccount(mContext) != null) {

            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestId()
                    .requestEmail()
                    .requestProfile()
                    .build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(mContext, googleSignInOptions);
            googleSignInClient.signOut();
        }

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

            case SIGNUP_METHOD:

                showAlert(getString(R.string.app_name), message);

                break;

            case FACEBOOK_LOGIN_METHOD:

                showAlert(getString(R.string.app_name), message);

                break;

            case GOOGLE_LOGIN_METHOD:

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

            case SIGNUP_METHOD:
                try {

                    String message = Data.getString("replyMessage");
                    showAlertForLogin(mContext, getString(R.string.app_name), message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case FACEBOOK_LOGIN_METHOD:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    String id = data.optString("id");
                    String email = data.optString("email");
                    String name = data.optString("name");
                    String verificationStatus = data.optString("verification_status");
                    String stepFirst = data.optString("step_first");
                    String profilePic = data.optString("profile_image");
                    int notification = data.optInt("notification");
                    String feedSearchDistance = data.optString("feed_search_distance");
                    String stuffSearchDistance = data.optString("stuff_search_distance");

                    PreferenceUtils.setIsLogin(mContext, true);
                    PreferenceUtils.setId(mContext, id);
                    PreferenceUtils.setEmail(mContext, email);
                    PreferenceUtils.setName(mContext, name);
                    PreferenceUtils.setProfilePicUrl(mContext, profilePic);
                    PreferenceUtils.setNotificationStatus(mContext, String.valueOf(notification));
                    if (!feedSearchDistance.isEmpty()) {
                        PreferenceUtils.setCommunitySearchDistance(mContext, Integer.parseInt(feedSearchDistance));
                    }

                    if (!stuffSearchDistance.isEmpty()) {
                        PreferenceUtils.setItemSearchDistance(mContext, Integer.parseInt(stuffSearchDistance));
                    }

                    startNextActivity(stepFirst);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case GOOGLE_LOGIN_METHOD:


                try {

                    JSONObject data = Data.getJSONObject("data");

                    String id = data.optString("id");
                    String email = data.optString("email");
                    String name = data.optString("name");
                    String verificationStatus = data.optString("verification_status");
                    String stepFirst = data.optString("step_first");
                    String profilePic = data.optString("profile_image");
                    int notification = data.optInt("notification");
                    String feedSearchDistance = data.optString("feed_search_distance");
                    String stuffSearchDistance = data.optString("stuff_search_distance");

                    PreferenceUtils.setIsLogin(mContext, true);
                    PreferenceUtils.setId(mContext, id);
                    PreferenceUtils.setEmail(mContext, email);
                    PreferenceUtils.setName(mContext, name);
                    PreferenceUtils.setProfilePicUrl(mContext, profilePic);
                    PreferenceUtils.setNotificationStatus(mContext, String.valueOf(notification));
                    if (!feedSearchDistance.isEmpty()) {
                        PreferenceUtils.setCommunitySearchDistance(mContext, Integer.parseInt(feedSearchDistance));
                    }

                    if (!stuffSearchDistance.isEmpty()) {
                        PreferenceUtils.setItemSearchDistance(mContext, Integer.parseInt(stuffSearchDistance));
                    }


                    startNextActivity(stepFirst);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


        }
    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }


    private void startNextActivity(String stepFirst) {
        if (stepFirst.equals("Yes")) {
            PreferenceUtils.setStepFirst(mContext, stepFirst);
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (stepFirst.equals("No")) {
            PreferenceUtils.setStepFirst(mContext, stepFirst);
            Intent intent = new Intent(SignUpActivity.this, MyProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }


    public void showAlertForLogin(final Context context, String title, String message) {
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
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

}
