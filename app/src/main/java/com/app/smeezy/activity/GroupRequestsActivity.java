package com.app.smeezy.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.GroupRequestListAdapter;
import com.app.smeezy.interfacess.GroupRequestListener;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.GroupRequest;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupRequestsActivity extends BaseActivity implements IRequestView,
        GroupRequestListener {

    private static final String GET_GROUP_REQUEST_LIST = "get_group_requests_list";
    private static final String ACCEPT_REQUEST = "accept_request";
    private static final String REJECT_REQUEST = "reject_request";

    @BindView(R.id.group_requests_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;


    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<GroupRequest> groupRequestList = new ArrayList<>();
    private GroupRequestListAdapter mAdapter;
    private String groupId;
    private int approvePosition, rejectPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_requests);
        ButterKnife.bind(this);


        groupId = getIntent().getStringExtra("groupId");

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);
        mAdapter = new GroupRequestListAdapter(mContext, groupRequestList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getGroupRequestList();

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
        activity_title.setText(getResources().getString(R.string.title_activity_group_requests));
    }


    private void getGroupRequestList() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getGroupRequestList(GET_GROUP_REQUEST_LIST, PreferenceUtils.getId(mContext), groupId);
        } else {
            Utills.noInternetConnection(mContext);
        }
    }


    @Override
    public void onApprove(int position, String requestId) {
        if (cd.isConnectingToInternet()) {
            approvePosition = position;
            requestPresenter.manageGroupRequest(ACCEPT_REQUEST, PreferenceUtils.getId(mContext),
                    requestId, "approve");
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onReject(int position, String requestId) {
        if (cd.isConnectingToInternet()) {
            rejectPosition = position;
            requestPresenter.manageGroupRequest(ACCEPT_REQUEST, PreferenceUtils.getId(mContext),
                    requestId, "reject");
        } else {
            Utills.noInternetConnection(mContext);
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

            case GET_GROUP_REQUEST_LIST:

                showAlert(getString(R.string.app_name), message);

                break;

            case ACCEPT_REQUEST:

                showAlert(getString(R.string.app_name), message);

                break;

            case REJECT_REQUEST:

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

            case GET_GROUP_REQUEST_LIST:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        GroupRequest groupRequest =
                                gson.fromJson(data.getJSONObject(i).toString(), GroupRequest.class);

                        groupRequestList.add(groupRequest);
                    }

                    mAdapter.notifyDataSetChanged();

                    if (groupRequestList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case ACCEPT_REQUEST:

                groupRequestList.remove(approvePosition);
                mAdapter.notifyItemRemoved(approvePosition);
                if (groupRequestList.isEmpty()) {
                    showEmptyView();
                } else {
                    hideEmptyView();
                }

                break;

            case REJECT_REQUEST:

                groupRequestList.remove(rejectPosition);
                mAdapter.notifyItemRemoved(rejectPosition);
                if (groupRequestList.isEmpty()) {
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
