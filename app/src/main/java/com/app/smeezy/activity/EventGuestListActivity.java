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
import com.app.smeezy.adapter.EventGuestListAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.EventGuest;
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

public class EventGuestListActivity extends BaseActivity implements IRequestView{

    private static final String GET_GUEST_LIST = "get_guest_list";

    @BindView(R.id.event_guest_list_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.event_guest_list_empty_view)
    TextView tv_empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;


    private ArrayList<EventGuest> guestList = new ArrayList<>();
    private EventGuestListAdapter mAdapter;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_guest_list);
        ButterKnife.bind(this);

        eventId = getIntent().getStringExtra("eventId");

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        mAdapter = new EventGuestListAdapter(mContext, guestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getGuestList();

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
        activity_title.setText(getResources().getString(R.string.title_activity_event_guest_list));
    }

    private void getGuestList() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getEventGuestList(GET_GUEST_LIST, PreferenceUtils.getId(mContext), eventId);
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

            case GET_GUEST_LIST:

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

            case GET_GUEST_LIST:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    Gson gson = new Gson();

                    for (int i = 0; i < length; i++){

                        EventGuest guest = gson.fromJson(data.optJSONObject(i).toString(), EventGuest.class);

                        if (guest.getDecision().equals("yes") || guest.getDecision().equals("maybe")){
                            guestList.add(guest);
                        }

                    }

                    mAdapter.notifyDataSetChanged();

                    if (guestList.isEmpty()){
                        showEmptyView();
                    }else {
                        hideEmptyView();
                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }


                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }

    private void showEmptyView(){
        recyclerView.setVisibility(View.GONE);
        tv_empty_view.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView(){
        tv_empty_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
