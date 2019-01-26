package com.app.smeezy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Login;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements View.OnClickListener, IRequestView {

    public static final String LOGIN_METHOD = "login";
    public static final String FACEBOOK_LOGIN_METHOD = "fb_login";
    public static final String GOOGLE_LOGIN_METHOD = "google_login";
    public static final String SAVE_REFERRAL_CODE = "save_referral_code";

    public static final int GOOGLE_SIGN_IN_REQUEST = 12;


    @BindView(R.id.et_login_email)
    EditText et_email;

    @BindView(R.id.et_login_password)
    EditText et_password;

    @BindView(R.id.tv_login_forgot_password)
    TextView tv_forgot_password;

    @BindView(R.id.tv_login_signup)
    TextView tv_signup;

    @BindView(R.id.login_logo)
    ImageView img_login_logo;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private CallbackManager callbackManager = null;

    private GoogleSignInClient googleSignInClient;
    private Dialog referralDialog;
    private String stepFirst = "No";
    private String instaUrl;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //fillForm();


        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        /*Intent intent = getIntent();

        if (intent.getStringExtra("url")!= null){

            instaUrl = intent.getStringExtra("url");
            Toast.makeText(mContext, instaUrl, Toast.LENGTH_LONG).show();

        }*/


        setUpToolbar();
        setUpFbLogin();
        setUpGoogleLogin();

       /* if (PreferenceUtils.getLogoUrl(mContext) != null) {

            Glide.with(mContext)
                    .load(PreferenceUtils.getLogoUrl(mContext))
                    .asBitmap()
                    .into(img_login_logo);

        }*/

        tv_forgot_password.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_signup.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        TextView activity_title = (TextView) findViewById(R.id.activity_title);
        activity_title.setText(getResources().getString(R.string.title_activity_login));
        ImageView img_logo = (ImageView) findViewById(R.id.img_logo_toolbar);
        img_logo.setVisibility(View.GONE);
    }


    @OnClick({R.id.btn_login, R.id.tv_login_signup, R.id.tv_login_forgot_password, R.id.img_login_fb,
            R.id.img_login_google, R.id.btn_instagram_login})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_login:

                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString();

                if (email.isEmpty() && password.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.all_fields_required));
                } else if (email.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.email_required));
                } else if (password.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.password_required));
                } else if (!Utills.isEmailValid(email)) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.provide_a_valid_email_address));
                } else {

                    if (cd.isConnectingToInternet()) {
                        requestPresenter.login(LOGIN_METHOD, email, password);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }

                }


                break;

            case R.id.tv_login_signup:

                login = new Login()
                        .withLoginType(Login.NORMAL);

                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.putExtra("login", login);
                startActivity(intent);

                break;

            case R.id.tv_login_forgot_password:

                Intent intent1 = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent1);

                break;


            case R.id.img_login_fb:

                onFbLogin();

                break;

            case R.id.img_login_google:

                onGoogleLogin();

                break;

            case R.id.btn_instagram_login:

                /*Intent instaLogin = new Intent(mContext, InstagramLoginActivity.class);
                startActivity(instaLogin);*/

                break;
        }

    }

    private void setUpFbLogin() {

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

    }

    private void onFbLogin() {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject user1, GraphResponse graphResponse) {

                                try {

                                    Log.d("LoginACT", user1.toString());
                                    String facebookId = user1.getString("id");
                                    String name = user1.optString("name");
                                    String email = user1.optString("email");
                                    JSONObject picture = user1.optJSONObject("picture");
                                    JSONObject pictureData = picture.optJSONObject("data");
                                    //String imageUrl = pictureData.optString("url");
                                    String imageUrl = String.format("http://graph.facebook.com/%s/picture?type=large", facebookId);

                                    login = new Login()
                                            .withLoginType(Login.FACEBOOK)
                                            .withSocialId(facebookId)
                                            .withName(name)
                                            .withEmail(email)
                                            .withImageUrl(imageUrl);


                                    if (cd.isConnectingToInternet()) {
                                        requestPresenter.socialLogin(FACEBOOK_LOGIN_METHOD, email, facebookId,
                                                "", name, imageUrl);
                                    } else {
                                        Utills.noInternetConnection(mContext);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("LoginActivity", exception.toString());
                        if (exception instanceof FacebookAuthorizationException) {
                            if (com.facebook.AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                    }
                });

    }

    private void setUpGoogleLogin() {

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .requestProfile()
                .build();

        googleSignInClient = GoogleSignIn.getClient(mContext, googleSignInOptions);

    }

    private void onGoogleLogin() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_REQUEST && resultCode == Activity.RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                String googleId = account.getId();
                String email = account.getEmail();
                String name = account.getDisplayName();

                String imageUrl = "";

                if (account.getPhotoUrl() != null) {
                    imageUrl = account.getPhotoUrl().toString();
                }

                Log.d("LoginACT", imageUrl);

                login = new Login()
                        .withLoginType(Login.GOOGLE)
                        .withSocialId(googleId)
                        .withName(name)
                        .withEmail(email)
                        .withImageUrl(imageUrl);


                if (cd.isConnectingToInternet()) {
                    requestPresenter.socialLogin(GOOGLE_LOGIN_METHOD, email,"", googleId, name, imageUrl);
                } else {
                    Utills.noInternetConnection(mContext);
                }

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
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
        if (method.equals(LOGIN_METHOD) || method.equals(FACEBOOK_LOGIN_METHOD) || method.equals(GOOGLE_LOGIN_METHOD)
                || method.equals(SAVE_REFERRAL_CODE)) {
            showAlert(getResources().getString(R.string.app_name), message);
        }
    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method) {

            case LOGIN_METHOD:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    String id = data.optString("id");
                    String email = data.optString("email");
                    String name = data.optString("name");
                    String verificationStatus = data.optString("verification_status");
                    stepFirst = data.optString("step_first");
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

            case FACEBOOK_LOGIN_METHOD:

                try {
                    if (Data.has("new_user") && Data.getString("new_user").equals("1")) {
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        intent.putExtra("login", login);
                        startActivity(intent);
                    } else {

                        JSONObject data = Data.getJSONObject("data");

                        String id = data.optString("id");
                        String email = data.optString("email");
                        String name = data.optString("name");
                        String verificationStatus = data.optString("verification_status");
                        stepFirst = data.optString("step_first");
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
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case GOOGLE_LOGIN_METHOD:

                try {

                    if (Data.has("new_user") && Data.getString("new_user").equals("1")) {
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        intent.putExtra("login", login);
                        startActivity(intent);
                    } else {

                    JSONObject data = Data.getJSONObject("data");

                    String id = data.optString("id");
                    String email = data.optString("email");
                    String name = data.optString("name");
                    String verificationStatus = data.optString("verification_status");
                    stepFirst = data.optString("step_first");
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
                    if (!feedSearchDistance.isEmpty()){
                        PreferenceUtils.setCommunitySearchDistance(mContext, Integer.parseInt(feedSearchDistance));
                    }

                    if (!stuffSearchDistance.isEmpty()){
                        PreferenceUtils.setItemSearchDistance(mContext, Integer.parseInt(stuffSearchDistance));
                    }


                        startNextActivity(stepFirst);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case SAVE_REFERRAL_CODE:

                if (referralDialog != null && referralDialog.isShowing()) {
                    referralDialog.dismiss();
                }

                startNextActivity(stepFirst);


                break;
        }


    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }


    private void fillForm() {
        et_email.setText("test2@mailinator.com");
        et_password.setText("123456");
    }

    private void startNextActivity(String stepFirst) {
        if (stepFirst.equals("Yes")) {
            PreferenceUtils.setStepFirst(mContext, stepFirst);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else if (stepFirst.equals("No")) {
            PreferenceUtils.setStepFirst(mContext, stepFirst);
            Intent intent = new Intent(LoginActivity.this, MyProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /*private void show_alert_for_referral_code(final Context context) {

        final Dialog dialog = new Dialog(context, R.style.DialogLeftSlideAnim);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.custom_app_dialog);

        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txt_message = (TextView) dialog.findViewById(R.id.txt_message);
        TextView txt_action1 = (TextView) dialog.findViewById(R.id.txt_action1);
        TextView txt_action2 = (TextView) dialog.findViewById(R.id.txt_action2);
        TextView txt_action3 = (TextView) dialog.findViewById(R.id.txt_action3);
        txt_action3.setVisibility(View.GONE);
        txt_action1.setVisibility(View.VISIBLE);
        txt_action2.setVisibility(View.VISIBLE);
        txt_action1.setText(context.getResources().getString(R.string.yes));
        txt_action2.setText(context.getResources().getString(R.string.no));
        txt_title.setText(context.getResources().getString(R.string.app_name));
        txt_message.setText(context.getResources().getString(R.string.do_you_have_a_referral_code));

        dialog.show();


        txt_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                showReferralDialog();

            }
        });

        txt_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                startNextActivity(stepFirst);

            }
        });

    }

    private void showReferralDialog() {


        referralDialog = new Dialog(mContext, R.style.DialogLeftSlideAnim);
        referralDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        referralDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        referralDialog.setCanceledOnTouchOutside(false);
        referralDialog.setContentView(R.layout.custom_referral_dialog);
        referralDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_submit = (Button) referralDialog.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button) referralDialog.findViewById(R.id.btn_cancel);

        final EditText et_referral_code = (EditText) referralDialog.findViewById(R.id.et_referral_code);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                referralDialog.dismiss();
                startNextActivity(stepFirst);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String referralCode = et_referral_code.getText().toString().trim();

                if (referralCode.isEmpty()) {
                    showAlert(getString(R.string.app_name), getString(R.string.referral_code_required));
                } else {

                    if (cd.isConnectingToInternet()) {
                        requestPresenter.saveReferralCode(SAVE_REFERRAL_CODE, PreferenceUtils.getId(mContext),
                                referralCode);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }

                }

            }
        });

        referralDialog.show();

    }*/
}
