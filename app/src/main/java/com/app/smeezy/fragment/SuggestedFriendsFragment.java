package com.app.smeezy.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.SuggestedFriendsAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.interfacess.SuggestedFriendsListener;
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
public class SuggestedFriendsFragment extends BaseFragment implements IRequestView, SuggestedFriendsListener{

    private static final String GET_SUGGESTED_FRIENDS = "get_suggested_friends";
    private static final String SEND_FRIEND_REQUEST = "send_friend_request";


    @BindView(R.id.suggested_friends_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<User> userList = new ArrayList<>();
    private SuggestedFriendsAdapter mAdapter;
    private int addFriendPosition;

    public SuggestedFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggested_friends, container, false);
        ButterKnife.bind(this, view);


        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        mAdapter = new SuggestedFriendsAdapter(mContext, userList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getSuggestedFriends();

        return view;
    }

    private void getSuggestedFriends() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getSuggestedFriendsList(GET_SUGGESTED_FRIENDS, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onAddFriend(int position, User user) {
        if (cd.isConnectingToInternet()) {
            addFriendPosition = position;
            requestPresenter.sendFriendRequest(SEND_FRIEND_REQUEST, PreferenceUtils.getId(mContext),
                    user.getId());
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.no_menu, menu);

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

            case SEND_FRIEND_REQUEST:

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

            case GET_SUGGESTED_FRIENDS:

                try {


                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        JSONObject currentData = data.getJSONObject(i);
                        String id = currentData.optString("id");
                        String email = currentData.optString("email");
                        String name = currentData.optString("name");
                        String gender = currentData.optString("gender");
                        String dob = currentData.optString("dob");
                        String address = currentData.optString("location_address");
                        String city = currentData.optString("city");
                        String region = currentData.optString("region");
                        String country = currentData.optString("country");
                        String pincode = currentData.optString("pincode");
                        String phone = currentData.optString("phone_number");
                        String profileImage = currentData.optString("profile_image");
                        int mutualFriends = currentData.optInt("mutual_friends");
                        JSONObject connectionRecordObject = currentData.optJSONObject("connection_record");

                        String isRequestSent;

                        ConnectionRecord connectionRecord = new ConnectionRecord();

                        if (connectionRecordObject != null) {
                            connectionRecord.withCrqId(connectionRecordObject.optString("crq_id"))
                                    .withFromMemberId(connectionRecordObject.optString("from_member_id"))
                                    .withToMemberId(connectionRecordObject.optString("to_member_id"))
                                    .withRequestStatus(connectionRecordObject.optString("request_status"));
                            isRequestSent = "1";
                        }else {
                            isRequestSent = "0";
                        }


                        User user = new User().withId(id)
                                .withEmail(email)
                                .withName(name)
                                .withGender(gender)
                                .withDob(dob)
                                .withLocationAddress(address)
                                .withCity(city)
                                .withRegion(region)
                                .withCountry(country)
                                .withPincode(pincode)
                                .withPhoneNumber(phone)
                                .withProfileImage(profileImage)
                                .withIsRequestSent(isRequestSent)
                                .withMutualFriends(mutualFriends)
                                .withConnectionRecord(connectionRecord);


                        userList.add(user);

                    }

                    mAdapter.notifyDataSetChanged();

                    if (userList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case SEND_FRIEND_REQUEST:

                userList.get(addFriendPosition).setIsRequestSent("1");
                userList.get(addFriendPosition).getConnectionRecord().withFromMemberId(PreferenceUtils.getId(mContext))
                        .withToMemberId(userList.get(addFriendPosition).getId());
                mAdapter.notifyItemChanged(addFriendPosition);

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
