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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Feedback;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostFeedbackActivity extends BaseActivity implements IRequestView, View.OnClickListener {

    private static final String POST_FEEDBACK = "post_feedback";

    @BindView(R.id.et_post_feedback_description)
    EditText et_description;

    @BindView(R.id.img_post_feedback_like)
    ImageView img_like;

    @BindView(R.id.img_post_feedback_dislike)
    ImageView img_dislike;


    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;


    private String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_feedback);

        ButterKnife.bind(this);


        memberId = getIntent().getStringExtra("memberId");

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
        activity_title.setText(getResources().getString(R.string.title_activity_post_feedback));

    }


    @OnClick({R.id.btn_post_feedback_submit, R.id.img_post_feedback_like, R.id.img_post_feedback_dislike})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_post_feedback_like:

                img_like.setActivated(true);
                img_dislike.setActivated(false);

                break;

            case R.id.img_post_feedback_dislike:

                img_dislike.setActivated(true);
                img_like.setActivated(false);

                break;

            case R.id.btn_post_feedback_submit:

                String description = et_description.getText().toString().trim();

                if (img_like.isActivated() || img_dislike.isActivated()) {

                    if (description.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.description_required));
                    } else {
                        String rate = "";

                        if (img_like.isActivated()) {
                            rate = "like";
                        } else if (img_dislike.isActivated()) {
                            rate = "dislike";
                        }

                        if (cd.isConnectingToInternet()) {
                            requestPresenter.postFeedback(POST_FEEDBACK, PreferenceUtils.getId(mContext), memberId, rate, description);
                        } else {
                            Utills.noInternetConnection(mContext);
                        }
                    }
                } else {
                    showAlert(getString(R.string.app_name), getString(R.string.please_select_like_or_dislike));
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

            case POST_FEEDBACK:

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

            case POST_FEEDBACK:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    Feedback feedback = new Feedback().withId(data.optString("id"))
                            .withUserId(data.optString("user_id"))
                            .withMemberId(data.optString("member_id"))
                            .withRate(data.optString("rate"))
                            .withComment(data.optString("comment"))
                            .withName(data.optString("name"))
                            .withProfileImage(data.optString("profile_image"))
                            .withCoverImage(data.optString("cover_image"));


                    String addedOn = data.optString("added_on");

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date inputDate = null;

                    try {
                        inputDate = inputFormat.parse(addedOn);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String outputDate;
                    String outputTime;
                    String outputDateTime = "";

                    if (inputDate != null) {
                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd");
                        SimpleDateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a");
                        outputDate = outputDateFormat.format(inputDate);
                        outputTime = outputTimeFormat.format(inputDate);
                        outputDateTime = String.format("%s, at %s", outputDate, outputTime);
                    }

                    feedback.setAddedOn(outputDateTime);

                    Intent intent = new Intent();
                    intent.putExtra("feedback", feedback);
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                }catch (JSONException e){
                    e.printStackTrace();
                }



                break;

        }
    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
