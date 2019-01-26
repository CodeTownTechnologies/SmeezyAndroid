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
import com.app.smeezy.adapter.MutualFriendsAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.ConnectionRecord;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MutualFriendsActivity extends BaseActivity implements IRequestView {

    private static final String GET_MUTUAL_FRIENDS = "get_mutual_friends";


    @BindView(R.id.search_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;


    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;



    private ArrayList<User> userList = new ArrayList<>();
    private MutualFriendsAdapter mAdapter;
    private String friendId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutual_friends);
        ButterKnife.bind(this);

        friendId = getIntent().getStringExtra("friendId");

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        mAdapter = new MutualFriendsAdapter(mContext, userList);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getMutualFriends();

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
        activity_title.setText(getResources().getString(R.string.title_activity_mutual_friends));
    }

    private void getMutualFriends() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getMutualFriendList(GET_MUTUAL_FRIENDS, PreferenceUtils.getId(mContext), friendId);
        } else {
            Utills.noInternetConnection(mContext);
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

        switch (method) {


        }

    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method) {

            case GET_MUTUAL_FRIENDS:

                try {

                    JSONArray data = Data.getJSONArray("data");
                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        JSONObject userObject = data.getJSONObject(i);

                        User friend = new User().withId(userObject.optString("id"))
                                .withName(userObject.optString("name"))
                                .withProfileImage(userObject.optString("image"))
                                .withCity(userObject.optString("city"))
                                .withRegion(userObject.optString("region"))
                                .withCountry(userObject.optString("country"))
                                .withPincode(userObject.optString("pincode"))
                                .withPhoneNumber(userObject.optString("phone_number"))
                                .withLocationAddress(userObject.optString("location_address"));

                        userList.add(friend);
                    }

                    if (userList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }

                    mAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
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
