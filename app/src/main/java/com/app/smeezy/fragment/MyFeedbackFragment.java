package com.app.smeezy.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.PostFeedbackActivity;
import com.app.smeezy.adapter.MyFeedbackAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Feedback;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFeedbackFragment extends BaseFragment implements View.OnClickListener, IRequestView{

    private static final String GET_FEEDBACK = "get_feedback";
    private static final int POST_FEEDBACK = 235;

    @BindView(R.id.my_feedback_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.my_feedback_empty_view)
    TextView tv_empty_view;

    @BindView(R.id.btn_post_feedback)
    Button btn_post_feedback;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<Feedback> feedbackList = new ArrayList<>();
    private MyFeedbackAdapter mAdapter;

    private String memberId;
    private boolean ownProfile;
    private User user;

    public MyFeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_feedback, container, false);

        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            memberId = getArguments().getString("memberId");
            ownProfile = getArguments().getBoolean("ownProfile");
            user = (User) getArguments().getSerializable("user");
        }

        if (ownProfile) {
            btn_post_feedback.setVisibility(View.GONE);
            tv_empty_view.setText(getString(R.string.this_user_does_not_have_any_feedback_yet));
        } else {
            if (user.isFeedbackPosted()){
                btn_post_feedback.setVisibility(View.GONE);
            }else {
                btn_post_feedback.setVisibility(View.VISIBLE);
            }
            tv_empty_view.setText(getString(R.string.this_user_does_not_have_any_feedback_yet));
        }



        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        mAdapter = new MyFeedbackAdapter(mContext, feedbackList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getFeedbackList();

        return view;
    }



    private void getFeedbackList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getFeedbackList(GET_FEEDBACK, PreferenceUtils.getId(mContext), memberId);
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @OnClick({R.id.btn_post_feedback})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_post_feedback:

                Intent intent = new Intent(mContext, PostFeedbackActivity.class);
                intent.putExtra("memberId", memberId);
                startActivityForResult(intent, POST_FEEDBACK);

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case POST_FEEDBACK:

                if (resultCode == Activity.RESULT_OK){

                    Feedback feedback = (Feedback) data.getSerializableExtra("feedback");

                    if (feedback != null){
                        feedbackList.add(feedback);
                    }

                    mAdapter.notifyDataSetChanged();

                    if (feedbackList.isEmpty()){
                        showEmptyView();
                    }else {
                        hideEmptyView();
                    }

                }

                break;
        }

    }

    @Override
    public void showLoadingProgressBar() {

    }

    @Override
    public void hideLoadingProgressBar() {

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

            case GET_FEEDBACK:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    JSONArray list = data.getJSONArray("list");

                    int length = list.length();

                    for (int i = 0; i < length; i++) {

                        JSONObject currentFeedback = list.getJSONObject(i);
                        Feedback feedback = new Feedback().withId(currentFeedback.optString("id"))
                                .withUserId(currentFeedback.optString("user_id"))
                                .withMemberId(currentFeedback.optString("member_id"))
                                .withRate(currentFeedback.optString("rate"))
                                .withComment(currentFeedback.optString("comment"))
                                .withName(currentFeedback.optString("name"))
                                .withProfileImage(currentFeedback.optString("profile_image"))
                                .withCoverImage(currentFeedback.optString("cover_image"));


                        String addedOn = currentFeedback.optString("added_on");

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

                        feedbackList.add(feedback);

                    }

                    if (feedbackList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }

                    mAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (feedbackList.isEmpty()) {
                    showEmptyView();
                } else {
                    hideEmptyView();
                }

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
