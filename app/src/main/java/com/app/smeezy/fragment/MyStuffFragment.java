package com.app.smeezy.fragment;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.AllStuffActivity;
import com.app.smeezy.activity.EditStuffActivity;
import com.app.smeezy.adapter.MyStuffAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.interfacess.StuffListener;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.User;
import com.app.smeezy.responsemodels.UserStuff;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.smeezy.utills.StaticData.ADD_STUFF;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyStuffFragment extends BaseFragment implements View.OnClickListener, IRequestView, StuffListener {

    private static final String GET_USER_STUFF_LIST = "get_user_stuff";
    private static final String GET_OTHER_USER_STUFF_LIST = "get_other_user_stuff";
    private static final String DELETE_USER_STUFF = "delete_user_stuff";

    @BindView(R.id.my_stuff_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    @BindView(R.id.tv_privacy_view)
    TextView tv_privacy_view;

    @BindView(R.id.img_stuff_add)
    ImageView img_stuff_add;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<UserStuff> stuffList = new ArrayList<>();
    private MyStuffAdapter mAdapter;

    private String memberId;
    private boolean ownProfile;
    private User user;

    private int deletePosition;

    private BroadcastReceiver stuffRefreshBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stuffList.clear();
            mAdapter.notifyDataSetChanged();
            getUserStuffList();
        }
    };

    public MyStuffFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_stuff, container, false);

        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            memberId = getArguments().getString("memberId");
            ownProfile = getArguments().getBoolean("ownProfile");
            user = (User) getArguments().getSerializable("user");
        }

        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        mAdapter = new MyStuffAdapter(mContext, stuffList, this, ownProfile);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        if (ownProfile) {
            tv_privacy_view.setVisibility(View.GONE);
            img_stuff_add.setVisibility(View.VISIBLE);
            getUserStuffList();
        } else if (user.getIsFriend().equals("0")
                && user.getStuffPrivacy().equals("private")) {
            img_stuff_add.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            tv_empty_view.setVisibility(View.GONE);
            tv_privacy_view.setVisibility(View.VISIBLE);
        } else {
            img_stuff_add.setVisibility(View.GONE);
            tv_privacy_view.setVisibility(View.GONE);
            getOtherUserStuffList();
        }

        getActivity().registerReceiver(stuffRefreshBroadcast,
                new IntentFilter(StaticData.STUFF_REFRESH_BROADCAST));

        return view;
    }

    private void getUserStuffList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getUserStuffList(GET_USER_STUFF_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }

    }

    private void getOtherUserStuffList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getOtherUserStuffList(GET_OTHER_USER_STUFF_LIST, PreferenceUtils.getId(mContext),
                    user.getId());
        } else {
            Utills.noInternetConnection(mContext);
        }

    }


    @OnClick({R.id.img_stuff_add})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_stuff_add:

                Intent allStuffIntent = new Intent(mContext, AllStuffActivity.class);
                allStuffIntent.putExtra("memberId", memberId);
                allStuffIntent.putExtra("ownProfile", ownProfile);
                allStuffIntent.putExtra("user", user);
                startActivity(allStuffIntent);

                break;

        }

    }

    @Override
    public void onDeleteStuff(int position, String id) {

        showDeleteAlert(position, id);

    }

    @Override
    public void onEditStuff(int position, UserStuff userStuff) {

        Intent intent = new Intent(mContext, EditStuffActivity.class);
        intent.putExtra("userStuff", userStuff);
        startActivity(intent);

    }

    @Override
    public void onBottomReached() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(stuffRefreshBroadcast);
    }

    public void checkPrivacy(String stuffListPrivacy) {

        if (stuffListPrivacy.equals("private") && !ownProfile) {
            recyclerView.setVisibility(View.GONE);
            tv_empty_view.setVisibility(View.GONE);
            tv_privacy_view.setVisibility(View.VISIBLE);
        } else {
            tv_privacy_view.setVisibility(View.GONE);
            if (stuffList.isEmpty()) {
                showEmptyView();
            } else {
                hideEmptyView();
            }
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

            case DELETE_USER_STUFF:

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

            case GET_USER_STUFF_LIST:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        UserStuff stuff = gson.fromJson(data.getJSONObject(i).toString(), UserStuff.class);

                        stuffList.add(stuff);

                    }


                    mAdapter.notifyDataSetChanged();

                    if (stuffList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case GET_OTHER_USER_STUFF_LIST:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        UserStuff stuff = gson.fromJson(data.getJSONObject(i).toString(), UserStuff.class);

                        stuffList.add(stuff);

                    }


                    mAdapter.notifyDataSetChanged();

                    if (stuffList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case DELETE_USER_STUFF:

                stuffList.remove(deletePosition);
                mAdapter.notifyDataSetChanged();

                if (stuffList.isEmpty()) {
                    showEmptyView();
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


    public void showDeleteAlert(final int position, final String id) {

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
        txt_message.setText(getResources().getString(R.string.are_your_sure_you_want_to_delete_this_item));

        dialog.show();


        txt_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    deletePosition = position;
                    startProgressBar();
                    requestPresenter.deleteStuff(DELETE_USER_STUFF, PreferenceUtils.getId(mContext), id);
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
