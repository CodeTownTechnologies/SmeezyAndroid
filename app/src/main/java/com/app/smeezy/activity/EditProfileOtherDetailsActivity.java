package com.app.smeezy.activity;

import android.content.Context;
import android.content.Intent;
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
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileOtherDetailsActivity extends BaseActivity implements View.OnClickListener, IRequestView {

    private static final String UPDATE_PROFILE_OTHER_DETAILS = "update_profile_other_details";

    @BindView(R.id.et_edit_religious_views)
    EditText et_religious_views;

    @BindView(R.id.et_edit_political_views)
    EditText et_political_views;

    @BindView(R.id.et_edit_work)
    EditText et_work;

    @BindView(R.id.et_edit_school)
    EditText et_school;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_other_details);
        ButterKnife.bind(this);

        user = (User) getIntent().getSerializableExtra("user");

        et_religious_views.setText(user.getReligiousView());

        et_religious_views.setSelection(et_religious_views.getText().toString().length());

        et_political_views.setText(user.getPoliticalView());
        et_work.setText(user.getWork());
        et_school.setText(user.getSchool());

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
        activity_title.setText(getResources().getString(R.string.title_activity_edit_profile_other_settings));
    }



    @OnClick({R.id.btn_edit_other_details_update})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

           /* case R.id.btn_edit_other_details_cancel:

                finish();

                break;*/


            case R.id.btn_edit_other_details_update:

                String religiousViews = et_religious_views.getText().toString().trim();
                String politicalViews = et_political_views.getText().toString().trim();
                String work = et_work.getText().toString().trim();
                String school = et_school.getText().toString().trim();


                if (cd.isConnectingToInternet()) {
                    requestPresenter.editOtherSettings(UPDATE_PROFILE_OTHER_DETAILS, PreferenceUtils.getId(mContext), religiousViews,
                            politicalViews, work, school);
                } else {
                    Utills.noInternetConnection(mContext);
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

            case UPDATE_PROFILE_OTHER_DETAILS:

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

            case UPDATE_PROFILE_OTHER_DETAILS:

                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.putExtra(getString(R.string.activity_tag), getString(R.string.activity_edit_profile_details_tag));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();


                break;


        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
