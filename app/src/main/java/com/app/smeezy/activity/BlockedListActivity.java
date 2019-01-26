package com.app.smeezy.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.BlockedListAdapter;
import com.app.smeezy.interfacess.BlockedListListener;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.BlockedUser;
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

public class BlockedListActivity extends BaseActivity implements IRequestView, BlockedListListener{

    private static final String GET_BLOCKED_LIST = "get_blocked_list";
    private static final String UNBLOCK = "unblock";

    @BindView(R.id.blocked_list_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.blocked_list_empty_view)
    TextView tv_empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;


    private ArrayList<BlockedUser> userList = new ArrayList<>();
    private BlockedListAdapter mAdapter;
    private int unblockPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_list);
        ButterKnife.bind(this);


        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        mAdapter = new BlockedListAdapter(mContext, userList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getUserList();

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
        activity_title.setText(getResources().getString(R.string.title_activity_blocked_list));
    }

    private void getUserList() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getBlockList(GET_BLOCKED_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onUnblock(int position, String memberId) {

        showUnblockDialog(position, memberId);

    }

    public  void showUnblockDialog(final int position, final String memberId) {

        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.custom_app_dialog);

        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txt_message = (TextView) dialog.findViewById(R.id.txt_message);
        TextView txt_action1 = (TextView) dialog.findViewById(R.id.txt_action1);
        TextView txt_action2 = (TextView) dialog.findViewById(R.id.txt_action2);
        TextView txt_action3 = (TextView) dialog.findViewById(R.id.txt_action3);
        txt_action3.setVisibility(View.GONE);
        txt_action1.setVisibility(View.VISIBLE);
        txt_action2.setVisibility(View.VISIBLE);
        txt_action1.setText(mContext.getResources().getString(R.string.yes));
        txt_action2.setText(mContext.getResources().getString(R.string.no));
        txt_title.setText(mContext.getResources().getString(R.string.app_name));
        txt_message.setText(mContext.getResources().getString(R.string.are_you_sure_you_want_to_unblock));

        dialog.show();


        txt_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if (cd.isConnectingToInternet()){
                    unblockPosition = position;
                    requestPresenter.manageBlock(UNBLOCK, PreferenceUtils.getId(BlockedListActivity.this.mContext), memberId,
                            "unblock");
                }else {
                    Utills.noInternetConnection(BlockedListActivity.this.mContext);
                }

            }
        });

        txt_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });

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

            case GET_BLOCKED_LIST:

                showAlert(getString(R.string.app_name), message);

                break;

            case UNBLOCK:

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

            case GET_BLOCKED_LIST:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    Gson gson = new Gson();

                    for (int i = 0; i < length; i++){

                        BlockedUser user = gson.fromJson(data.getJSONObject(i).toString(), BlockedUser.class);
                        userList.add(user);

                    }

                    mAdapter.notifyDataSetChanged();

                    if (userList.isEmpty()){
                        showEmptyView();
                    }else {
                        hideEmptyView();
                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }


                break;

            case UNBLOCK:

                userList.remove(unblockPosition);
                mAdapter.notifyDataSetChanged();

                if (userList.isEmpty()){
                    showEmptyView();
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
