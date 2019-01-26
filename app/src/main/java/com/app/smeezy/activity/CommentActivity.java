package com.app.smeezy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.CommentAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.ChatListItem;
import com.app.smeezy.responsemodels.Comment;
import com.app.smeezy.responsemodels.FeedItem;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentActivity extends BaseActivity implements View.OnClickListener, IRequestView {

    private static final String GET_COMMENTS = "get_comments";
    private static final String COMMENT = "comment";

    @BindView(R.id.comment_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.et_comment)
    EditText et_comment;

    @BindView(R.id.comment_empty_view)
    TextView tv_empty_view;


    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<Comment> commentList = new ArrayList<>();
    private CommentAdapter mAdapter;

    private FeedItem feedItem;
    private int page = 1;
    private int totalComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);



        feedItem = (FeedItem) getIntent().getSerializableExtra("feedItem");
        totalComments = feedItem.getTotalComments();

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);
        mAdapter = new CommentAdapter(mContext, commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getComments(String.valueOf(page));

        setUpToolbar();
    }

    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.comment_toolbar);
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
        activity_title.setText(getResources().getString(R.string.title_activity_comment));
    }


    private void getComments(String page) {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getCommentList(GET_COMMENTS, PreferenceUtils.getId(mContext), feedItem.getActivityId(),
                    feedItem.getId(), page);
        } else {
            Utills.noInternetConnection(mContext);
        }
    }


    @OnClick({R.id.btn_comment_send})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_comment_send:

                String comment = et_comment.getText().toString().trim();

                if (comment.isEmpty()) {
                    showAlert(getString(R.string.app_name), getString(R.string.comment_required));
                } else {
                    if (cd.isConnectingToInternet()) {
                        requestPresenter.comment(COMMENT, PreferenceUtils.getId(mContext), feedItem.getActivityId(),
                                feedItem.getId(), "0", comment);
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

        Intent intent = new Intent();
        intent.putExtra("totalComments", totalComments);
        setResult(Activity.RESULT_OK, intent);
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

            case GET_COMMENTS:

                showAlert(getString(R.string.app_name), message);

                break;

            case COMMENT:

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

            case GET_COMMENTS:

                try {

                    commentList.clear();

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        JSONObject commentObject = data.getJSONObject(i);

                        Comment comment = new Comment().withACId(commentObject.optString("a_c_id"))
                                .withActivityId(commentObject.optString("activity_id"))
                                .withTaskId(commentObject.optString("task_id"))
                                .withParentId(commentObject.optString("parent_id"))
                                .withUserId(commentObject.optString("user_id"))
                                .withComment(commentObject.optString("comment"))
                                .withName(commentObject.optString("name"))
                                .withProfileImage(commentObject.optString("profile_image"));

                        String addedOn = commentObject.optString("added_on");

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

                        comment.setAddedOn(outputDateTime);


                        commentList.add(comment);
                    }

                    mAdapter.notifyDataSetChanged();

                    if (commentList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                        recyclerView.scrollToPosition(commentList.size() - 1);
                    }

                    et_comment.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case COMMENT:

                try {

                    JSONObject data = Data.getJSONObject("data");
                    totalComments = data.optInt("total_comments");



                }catch (JSONException e){
                    e.printStackTrace();
                }

                getComments(String.valueOf(page));

                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }

    private void showEmptyView() {
        recyclerView.setVisibility(View.GONE);
        tv_empty_view.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        tv_empty_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

}
