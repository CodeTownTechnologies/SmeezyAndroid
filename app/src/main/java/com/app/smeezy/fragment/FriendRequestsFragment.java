package com.app.smeezy.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.FriendRequestListAdapter;
import com.app.smeezy.interfacess.FriendRequestListener;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.ConnectionRecord;
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
public class FriendRequestsFragment extends BaseFragment implements IRequestView, FriendRequestListener {


    private static final String GET_FRIEND_REQUEST_LIST = "get_friend_request_list";
    private static final String ACCEPT_REQUEST = "accept_request";
    private static final String REJECT_REQUEST = "reject_request";

    @BindView(R.id.friend_request_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<User> friendRequestList = new ArrayList<>();
    private FriendRequestListAdapter mAdapter;
    private int acceptPosition, rejectPosition;

    public FriendRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_requests, container, false);

        ButterKnife.bind(this, view);

        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        mAdapter = new FriendRequestListAdapter(mContext, friendRequestList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getFriendRequestList();

        return view;
    }private void getFriendRequestList() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getFriendRequestList(GET_FRIEND_REQUEST_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onAcceptRequest(int position, String memberId) {
        if (cd.isConnectingToInternet()){
            this.acceptPosition = position;
            requestPresenter.manageFriendRequest(ACCEPT_REQUEST, PreferenceUtils.getId(mContext), memberId, "accept");
        }else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onRejectRequest(int position, String memberId) {
        if (cd.isConnectingToInternet()){
            this.rejectPosition = position;
            requestPresenter.manageFriendRequest(REJECT_REQUEST, PreferenceUtils.getId(mContext), memberId, "reject");
        }else {
            Utills.noInternetConnection(mContext);
        }
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

            case GET_FRIEND_REQUEST_LIST:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case ACCEPT_REQUEST:

                showAlert(mContext,getString(R.string.app_name), message);

                break;

            case REJECT_REQUEST:

                showAlert(mContext,getString(R.string.app_name), message);

                break;

        }
    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method) {

            case GET_FRIEND_REQUEST_LIST:

                try {

                    JSONArray data = Data.getJSONArray("data");
                    int length = data.length();

                    for (int i = 0; i < length; i++){

                        JSONObject userObject = data.getJSONObject(i);

                        ConnectionRecord connectionRecord = new ConnectionRecord()
                                .withCrqId(userObject.optString("crq_id"))
                                .withFromMemberId(userObject.optString("from_member_id"))
                                .withToMemberId(userObject.optString("to_member_id"))
                                .withRequestStatus(userObject.optString("request_status"));

                        User friend = new User().withId(userObject.optString("from_member_id"))
                                .withName(userObject.optString("name"))
                                .withProfileImage(userObject.optString("profile_image"))
                                .withCity(userObject.optString("city"))
                                .withRegion(userObject.optString("region"))
                                .withCountry(userObject.optString("country"))
                                .withConnectionRecord(connectionRecord);

                        friendRequestList.add(friend);
                    }

                    if (friendRequestList.isEmpty()){
                        showEmptyView();
                    }else {
                        hideEmptyView();
                    }

                    mAdapter.notifyDataSetChanged();


                }catch (JSONException e){
                    e.printStackTrace();
                }

                break;


            case ACCEPT_REQUEST:

                friendRequestList.remove(acceptPosition);
                mAdapter.notifyDataSetChanged();

                if (friendRequestList.isEmpty()){
                    showEmptyView();
                }

                break;


            case REJECT_REQUEST:

                friendRequestList.remove(rejectPosition);
                mAdapter.notifyDataSetChanged();

                if (friendRequestList.isEmpty()){
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
