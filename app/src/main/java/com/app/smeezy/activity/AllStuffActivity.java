package com.app.smeezy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.AllStuffAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Stuff;
import com.app.smeezy.responsemodels.StuffCategory;
import com.app.smeezy.responsemodels.User;
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
import butterknife.OnClick;

public class AllStuffActivity extends BaseActivity implements IRequestView {

    private static final String GET_STUFF_LIST = "get_stuff";

    @BindView(R.id.all_stuff_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<Stuff> stuffList = new ArrayList<>();
    private ArrayList<StuffCategory> stuffCategoryList = new ArrayList<>();
    private AllStuffAdapter mAdapter;

    private String memberId;
    private boolean ownProfile;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stuff);
        ButterKnife.bind(this);


        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        Intent intent = getIntent();

        memberId = intent.getStringExtra("memberId");
        ownProfile = intent.getBooleanExtra("ownProfile", false);
        user = (User) intent.getSerializableExtra("user");

        mAdapter = new AllStuffAdapter(mContext, stuffList, stuffCategoryList);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getStuffList();

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
        activity_title.setText(getResources().getString(R.string.title_activity_all_stuff));
    }


    private void getStuffList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getStuffList(GET_STUFF_LIST, PreferenceUtils.getId(mContext));
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
        switch (method){

            case GET_STUFF_LIST:

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

            case GET_STUFF_LIST:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    JSONArray stuffArray = data.optJSONArray("stuff_list");

                    int length = stuffArray.length();

                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        Stuff stuff = gson.fromJson(stuffArray.getJSONObject(i).toString(), Stuff.class);

                        stuff.setIsSelected(false);

                        stuffList.add(stuff);

                    }

                    JSONArray stuffCategoryArray = data.optJSONArray("category_list");

                    int length1 = stuffCategoryArray.length();

                    stuffCategoryList.add(new StuffCategory().withId("-1")
                            .withTitle(getString(R.string.select_category)));

                    for (int i = 0; i < length1; i++) {

                        Gson gson = new Gson();

                        StuffCategory stuffCategory = gson.fromJson(
                                stuffCategoryArray.getJSONObject(i).toString(), StuffCategory.class);

                        stuffCategoryList.add(stuffCategory);

                    }

                    mAdapter.notifyDataSetChanged();

                    /*if (stuffList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }*/


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
