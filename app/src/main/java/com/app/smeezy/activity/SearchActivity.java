package com.app.smeezy.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.SearchItemAdapter;
import com.app.smeezy.adapter.SearchUserAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.interfacess.SearchListener;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.ConnectionRecord;
import com.app.smeezy.responsemodels.StuffFeedItem;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements View.OnClickListener, IRequestView,
        SearchListener {

    private static final String SEARCH_USERS = "search_users";
    private static final String SEARCH_ITEMS = "search_items";
    private static final String PRIVATE_MESSAGE = "private_message";
    private static final String SEND_FRIEND_REQUEST = "send_friend_request";


    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.search_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    @BindView(R.id.rg_search)
    RadioGroup rg_search;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private SearchUserAdapter searchUserAdapter;
    private SearchItemAdapter searchItemAdapter;


    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<StuffFeedItem> stuffList = new ArrayList<>();
    private String type = "stuff", keyword = "";
    private int page = 1, addFriendPosition;
    private boolean noMoreItems = false;
    private Dialog messageDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        searchUserAdapter = new SearchUserAdapter(mContext, userList, this);
        searchItemAdapter = new SearchItemAdapter(mContext, stuffList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(searchItemAdapter);

        rg_search.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rb_search_users) {
                    type = "normal";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rb_search_stuff) {
                    type = "stuff";
                }

                noMoreItems = false;
                page = 1;
                //userList.clear();
                searchUsersAndItems();
            }
        });


        et_search.requestFocus();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyword = charSequence.toString();
                noMoreItems = false;
                page = 1;
                //userList.clear();
                requestPresenter.cancelSearch();
                if (!keyword.isEmpty()) {
                    searchUsersAndItems();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchUsersAndItems() {
        if (cd.isConnectingToInternet()) {
            if (type.equals("normal")) {
                requestPresenter.searchUsers(SEARCH_USERS, PreferenceUtils.getId(mContext), keyword, type, page);
            } else if (type.equals("stuff")) {
                //requestPresenter.searchUsers(SEARCH_ITEMS, PreferenceUtils.getId(mContext), keyword, type, page);
                requestPresenter.getStuffFeedListForSearch(SEARCH_ITEMS,
                        PreferenceUtils.getId(mContext), "", String.valueOf(page), "",
                       keyword, String.valueOf(PreferenceUtils.getItemSearchDistance(mContext)));
            }
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    public void onBottomReached() {
        if (!noMoreItems) {
            page++;
            searchUsersAndItems();
        }
    }

    @Override
    public void onAddFriend(int position, User user) {
        if (cd.isConnectingToInternet()) {
            startProgressBar();
            addFriendPosition = position;
            requestPresenter.sendFriendRequest(SEND_FRIEND_REQUEST, PreferenceUtils.getId(mContext),
                    user.getId());
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    public String getUserType() {
        return type;
    }

    @OnClick({R.id.img_search_close})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_search_close:

                finish();

                break;


        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void showPrivateMessageDialog(final String memberId) {

        messageDialog = new Dialog(mContext);
        messageDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        messageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        messageDialog.setCanceledOnTouchOutside(false);
        messageDialog.setContentView(R.layout.custom_message_dialog);
        messageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_send = (Button) messageDialog.findViewById(R.id.btn_message_send);
        Button btn_cancel = (Button) messageDialog.findViewById(R.id.btn_message_cancel);

        final EditText et_message = (EditText) messageDialog.findViewById(R.id.et_message_text);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageDialog.dismiss();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = et_message.getText().toString().trim();

                if (message.isEmpty()) {
                    showAlert(getString(R.string.app_name), getString(R.string.message_required));
                } else {

                    if (cd.isConnectingToInternet()) {
                        startProgressBar();
                        requestPresenter.composePrivateMessage(PRIVATE_MESSAGE, PreferenceUtils.getId(mContext), memberId, message);
                    } else {
                        Utills.noInternetConnection(mContext);
                    }

                }

            }
        });

        messageDialog.show();

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

            case PRIVATE_MESSAGE:

                showAlert(getString(R.string.app_name), message);

                break;

            case SEND_FRIEND_REQUEST:

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

            case SEARCH_USERS:

                try {

                    if (page == 1) {
                        userList.clear();
                        recyclerView.setAdapter(searchUserAdapter);
                        searchUserAdapter.notifyDataSetChanged();
                    }

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    if (length == 0) {

                        noMoreItems = true;

                        if (userList.isEmpty()) {
                            showEmptyView();
                        } else {
                            hideEmptyView();
                        }
                        searchUserAdapter.notifyDataSetChanged();

                        break;

                    }

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
                        String pincode = currentData.optString("pincode");
                        String phone = currentData.optString("phone_number");
                        String profileImage = currentData.optString("profile_image");
                        String coverImage = currentData.optString("cover_image");
                        String stufflistPrivacy = currentData.optString("stufflist_privacy");
                        String isFriend = currentData.optString("is_friend");
                        String isRequestSent = currentData.optString("is_request_sent");
                        String requestStatus = currentData.optString("request_status");
                        int mutualFriends = currentData.optInt("mutual_friends");
                        JSONObject connectionRecordObject = currentData.optJSONObject("connection_record");


                        ConnectionRecord connectionRecord = new ConnectionRecord();

                        if (connectionRecordObject != null) {
                            connectionRecord.withCrqId(connectionRecordObject.optString("crq_id"))
                                    .withFromMemberId(connectionRecordObject.optString("from_member_id"))
                                    .withToMemberId(connectionRecordObject.optString("to_member_id"))
                                    .withRequestStatus(connectionRecordObject.optString("request_status"));

                            if (connectionRecord.getRequestStatus().equals("Accept")){
                                isFriend = "1";
                            }else {
                                isRequestSent = "1";
                                isFriend = "0";
                            }
                        }

                        User user = new User().withId(id)
                                .withEmail(email)
                                .withName(name)
                                .withGender(gender)
                                .withDob(dob)
                                .withLocationAddress(address)
                                .withCity(city)
                                .withRegion(region)
                                .withPincode(pincode)
                                .withPhoneNumber(phone)
                                .withProfileImage(profileImage)
                                .withCoverImage(coverImage)
                                .withStufflistPrivacy(stufflistPrivacy)
                                .withIsFriend(isFriend)
                                .withIsRequestSent(isRequestSent)
                                .withRequestStatus(requestStatus)
                                .withMutualFriends(mutualFriends)
                                .withConnectionRecord(connectionRecord);


                        userList.add(user);

                    }

                    searchUserAdapter.notifyDataSetChanged();

                    if (userList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case SEARCH_ITEMS:

                try {

                    if (page == 1) {
                        stuffList.clear();
                        recyclerView.setAdapter(searchItemAdapter);
                        searchItemAdapter.notifyDataSetChanged();
                    }

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    if (length == 0) {

                        noMoreItems = true;

                        if (stuffList.isEmpty()) {
                            showEmptyView();
                        } else {
                            hideEmptyView();
                        }
                        searchItemAdapter.notifyDataSetChanged();

                        break;

                    }

                    Gson gson = new Gson();

                    for (int i = 0; i < length; i++) {

                        StuffFeedItem feedItem = gson.fromJson(data.getJSONObject(i).toString(),
                                StuffFeedItem.class);

                        stuffList.add(feedItem);
                    }


                    searchItemAdapter.notifyDataSetChanged();

                    if (stuffList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            case PRIVATE_MESSAGE:


                if (messageDialog != null && messageDialog.isShowing()) {
                    messageDialog.dismiss();
                }


                break;

            case SEND_FRIEND_REQUEST:

                userList.get(addFriendPosition).setIsRequestSent("1");
                userList.get(addFriendPosition).getConnectionRecord().withFromMemberId(PreferenceUtils.getId(mContext))
                        .withToMemberId(userList.get(addFriendPosition).getId());
                searchUserAdapter.notifyItemChanged(addFriendPosition);

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
