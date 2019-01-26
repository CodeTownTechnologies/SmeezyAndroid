package com.app.smeezy.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.ProfileActivity;
import com.app.smeezy.adapter.FriendListAdapter;
import com.app.smeezy.interfacess.FriendListListener;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.FeedItem;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendListFragment extends BaseFragment implements IRequestView, FriendListListener {

    private static final String GET_FRIEND_LIST = "get_friend_list";
    private static final String FOLLOW_USER = "follow_user";
    private static final String UNFOLLOW_USER = "unfollow_user";

    private static final int PROFILE_REQUEST = 4566;

    @BindView(R.id.friend_list_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<User> friendList = new ArrayList<>();
    private FriendListAdapter mAdapter;

    private String memberId;
    private boolean ownProfile;

    private int followUserPosition, unfollowUserPosition;

    public FriendListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            memberId = getArguments().getString("memberId");
            ownProfile = getArguments().getBoolean("ownProfile");
        }

        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        mAdapter = new FriendListAdapter(mContext, friendList, ownProfile, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getFriendList();

        return view;
    }

    private void getFriendList() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getFriendList(GET_FRIEND_LIST, memberId);
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onFollowUser(int position, User user) {
        if (cd.isConnectingToInternet()) {
            followUserPosition = position;
            startProgressBar();
            requestPresenter.followUnfollowUser(FOLLOW_USER, PreferenceUtils.getId(mContext),
                    user.getId());
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onUnfollowUser(int position, User user) {

        showUnfollowUserAlert(position, user);

    }

    @Override
    public void onProfileClick(int position, User user) {
        Intent intent = new Intent(mContext, ProfileActivity.class);
        intent.putExtra("memberId", user.getId());
        startActivityForResult(intent, PROFILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PROFILE_REQUEST:

                if (resultCode == Activity.RESULT_OK) {

                    String userId = data.getStringExtra("userId");
                    String isUnfollowUser = data.getStringExtra("isUnfollowUser");

                    for (int i = 0; i < friendList.size(); i++) {

                        if (friendList.get(i).getId().equals(userId)){
                            friendList.get(i).setIsUnfollowUser(isUnfollowUser);
                            break;
                        }

                    }

                    mAdapter.notifyDataSetChanged();
                }
                break;

        }

    }

    @Override
    public void showLoadingProgressBar() {

    }

    @Override
    public void hideLoadingProgressBar() {
        dismissProgressBar();
    }

    @Override
    public void Failed(String method, String message) {

        switch (method) {

            case FOLLOW_USER:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case UNFOLLOW_USER:

                showAlert(mContext, getString(R.string.app_name), message);

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
                                .withIsUnfollowUser(userObject.optString("is_unfollow_user"));

                        friendList.add(friend);
                    }

                    if (friendList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }

                    mAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case FOLLOW_USER:

                friendList.get(followUserPosition).setIsUnfollowUser("0");
                mAdapter.notifyItemChanged(followUserPosition);

                break;

            case UNFOLLOW_USER:

                friendList.get(unfollowUserPosition).setIsUnfollowUser("1");
                mAdapter.notifyItemChanged(unfollowUserPosition);

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

    public void showUnfollowUserAlert(final int position, final User user) {

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
        txt_action1.setText(getResources().getString(R.string.yes));
        txt_action2.setText(getResources().getString(R.string.no));
        txt_title.setText(getResources().getString(R.string.app_name));
        txt_message.setText(getResources().getString(R.string.are_your_sure_you_want_to_unfollow_this_user));

        dialog.show();


        txt_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    unfollowUserPosition = position;
                    startProgressBar();
                    requestPresenter.followUnfollowUser(UNFOLLOW_USER, PreferenceUtils.getId(mContext),
                            user.getId());
                } else {
                    Utills.noInternetConnection(mContext);
                }
                dialog.dismiss();

            }
        });

        txt_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });

    }
}
