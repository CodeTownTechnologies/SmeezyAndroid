package com.app.smeezy.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.InviteAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Event;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteActivity extends BaseActivity implements View.OnClickListener, IRequestView {

    private static final String GET_FRIEND_LIST = "get_friend_list";
    private static final String SEND_INVITE = "send_invite";

    @BindView(R.id.et_invite_desc)
    EditText et_desc;

    @BindView(R.id.et_invite_friend)
    EditText et_friend_list;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<User> friendList = new ArrayList<>();
    private ArrayList<Integer> selectedUsers = new ArrayList<>();
    private Dialog inviteDialog;

    private InviteAdapter mAdapter;

    private Event event;
    private StringBuilder selectedUserList = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        ButterKnife.bind(this);

        event = (Event) getIntent().getSerializableExtra("event");

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        mAdapter = new InviteAdapter(mContext, friendList, selectedUsers);

        setUpToolbar();

        getFriendList();

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
        activity_title.setText(getResources().getString(R.string.title_activity_invite));
    }

    private void getFriendList() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getFriendList(GET_FRIEND_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @OnClick({R.id.btn_invite_send, R.id.et_invite_friend})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.et_invite_friend:

                if (friendList.isEmpty()) {
                    showAlert(getString(R.string.app_name), getString(R.string.no_friends));
                } else {
                    showInviteDialog();
                }

                break;

            /*case R.id.btn_invite_cancel:

                finish();

                break;*/

            case R.id.btn_invite_send:

                String message = et_desc.getText().toString().trim();

                String[] users = new String[selectedUsers.size()];

                for (int i = 0; i < selectedUsers.size(); i++){

                    users[i] = friendList.get(selectedUsers.get(i)).getId();

                }

                if (selectedUsers.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.no_friends_selected));
                }else {
                    if (cd.isConnectingToInternet()){
                        requestPresenter.sendEventInvite(SEND_INVITE, PreferenceUtils.getId(mContext),
                                event.getId(), users);
                    }else {
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

                finish();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void showInviteDialog() {

        inviteDialog = new Dialog(mContext);
        inviteDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        inviteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        inviteDialog.setCanceledOnTouchOutside(false);
        inviteDialog.setContentView(R.layout.custom_invite_dialog);
        inviteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_ok = (Button) inviteDialog.findViewById(R.id.btn_invite_ok);
        Button btn_cancel = (Button) inviteDialog.findViewById(R.id.btn_invite_cancel);
        RecyclerView recyclerView = (RecyclerView) inviteDialog.findViewById(R.id.invite_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int length = friendList.size();

                for (int i = 0; i < length; i++){

                    friendList.get(i).setIsSelected(false);
                    if (selectedUsers.contains(i)){
                        friendList.get(i).setIsSelected(true);
                    }

                }

                inviteDialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int length = friendList.size();
                selectedUsers.clear();
                selectedUserList = new StringBuilder();

                for (int i = 0; i < length; i++){

                    if (friendList.get(i).getIsSelected()){
                        selectedUsers.add(i);
                        selectedUserList.append(String.format("%s, ", friendList.get(i).getName()));
                    }

                }

                if (selectedUserList.length() > 1) {
                    String temp = selectedUserList.substring(0, selectedUserList.length() - 2);
                    et_friend_list.setText(temp);
                }else {
                    et_friend_list.setText("");
                }

                inviteDialog.dismiss();
            }
        });

        inviteDialog.show();

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

            case GET_FRIEND_LIST:

                showAlert(getString(R.string.app_name), message);

                break;

            case SEND_INVITE:

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

            case GET_FRIEND_LIST:

                try {

                    JSONArray data = Data.getJSONArray("data");
                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        JSONObject userObject = data.getJSONObject(i);

                        User friend = new User().withId(userObject.optString("friend_id"))
                                .withName(userObject.optString("name"))
                                .withProfileImage(userObject.optString("image"))
                                .withCity(userObject.optString("city"))
                                .withRegion(userObject.optString("region"))
                                .withCountry(userObject.optString("country"))
                                .withIsSelected(false);

                        friendList.add(friend);
                    }

                    mAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case SEND_INVITE:

                finish();

                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }
}
