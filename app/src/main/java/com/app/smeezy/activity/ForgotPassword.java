package com.app.smeezy.activity;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.Utills;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPassword extends BaseActivity implements View.OnClickListener, IRequestView {

    public static final String FORGOT_PASSWORD_METHOD = "forgot_password";

    @BindView(R.id.et_forgot_password_email)
    EditText et_email;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        setUpToolbar();
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
        activity_title.setText(getResources().getString(R.string.title_activity_forgot_password));
        ImageView img_logo = (ImageView) findViewById(R.id.img_logo_toolbar);
        img_logo.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_forgot_password})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_forgot_password:

                String email = et_email.getText().toString().trim();

                if (email.isEmpty()) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.email_required));
                } else if (!Utills.isEmailValid(email)) {
                    showAlert(getResources().getString(R.string.app_name), getResources().getString(R.string.provide_a_valid_email_address));
                } else {

                    if (cd.isConnectingToInternet()) {
                        requestPresenter.forgotPassword(FORGOT_PASSWORD_METHOD, email);
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

        if (method.equals(FORGOT_PASSWORD_METHOD)) {
            showAlert(getResources().getString(R.string.app_name), message);
        }

    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {
        if (method.equals(FORGOT_PASSWORD_METHOD)) {

            String message = Data.optString("replyMessage");
            show_custom_alert_for_finish(mContext, getResources().getString(R.string.app_name), message);

        }
    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }

}
