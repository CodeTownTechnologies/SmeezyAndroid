package com.app.smeezy.activity;

import android.content.Context;
import android.os.Build;
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
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, IRequestView {

    private static final String CHANGE_PASSWORD = "change_password";

    @BindView(R.id.et_change_password_old_password)
    EditText et_old_password;

    @BindView(R.id.et_change_password_new_password)
    EditText et_new_password;

    @BindView(R.id.et_change_password_confirm_password)
    EditText et_confirm_password;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
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

        ImageView img_logo = (ImageView) findViewById(R.id.img_logo_toolbar);
        Glide.with(mContext)
                .load(PreferenceUtils.getLogoUrl(mContext))
                .asBitmap()
                .into(img_logo);

        TextView activity_title = (TextView) findViewById(R.id.activity_title);
        activity_title.setText(getResources().getString(R.string.title_activity_change_password));
    }

    @OnClick({R.id.btn_change_password_submit})
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_change_password_submit:

                String oldPassword = et_old_password.getText().toString();
                String newPassword = et_new_password.getText().toString();
                String confirmPassword = et_confirm_password.getText().toString();

                if (oldPassword.isEmpty() && newPassword.isEmpty() && confirmPassword.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.all_fields_required));
                }else if (oldPassword.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.current_password_required));
                }else if (newPassword.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.new_password_required));
                }else if (confirmPassword.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.confirm_password_required));
                }else if (!newPassword.equals(confirmPassword)){
                    showAlert(getString(R.string.app_name), getString(R.string.new_and_confirm_password_not_same));
                }else {

                    if (cd.isConnectingToInternet()){
                        requestPresenter.changePassword(CHANGE_PASSWORD, PreferenceUtils.getId(mContext), oldPassword, newPassword);
                    }else {
                        Utills.noInternetConnection(mContext);
                    }

                }

                break;


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

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
        switch (method){

            case CHANGE_PASSWORD:

                showAlert(getString(R.string.app_name), message);

                break;

        }
    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method){

            case CHANGE_PASSWORD:

                finish();

                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
