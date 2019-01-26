package com.app.smeezy.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.CreateGroupActivity;
import com.app.smeezy.adapter.MyGroupAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.Group;
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

import static com.app.smeezy.utills.StaticData.CREATE_GROUP;
import static com.app.smeezy.utills.StaticData.GROUP_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroupsFragment extends BaseFragment implements View.OnClickListener, IRequestView {

    private static final String GET_GROUPS = "get_groups";


    @BindView(R.id.group_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.img_group_add)
    ImageView img_add;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    @BindView(R.id.et_group_search)
    EditText et_search;


    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private MyGroupAdapter mAdapter;
    private ArrayList<Group> groupList = new ArrayList<>();
    private ArrayList<Group> tempList = new ArrayList<>();

    public MyGroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_group, container, false);

        ButterKnife.bind(this, view);

        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        mAdapter = new MyGroupAdapter(mContext, groupList);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        img_add.setVisibility(View.VISIBLE);

        getGroupList();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String query = charSequence.toString();

                if (query.isEmpty()) {

                    groupList.clear();
                    groupList.addAll(tempList);
                    mAdapter.notifyDataSetChanged();

                } else {

                    groupList.clear();

                    for (Group group : tempList) {

                        if (group.getTitle().toLowerCase().contains(query.toLowerCase())) {
                            groupList.add(group);
                        }

                    }

                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void getGroupList() {

        if (cd.isConnectingToInternet()) {
            startProgressBar();
            requestPresenter.getGroupList(GET_GROUPS, PreferenceUtils.getId(mContext), "self");
        } else {
            Utills.noInternetConnection(mContext);
        }

    }

    @OnClick({R.id.img_group_add})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_group_add:

                Intent intent = new Intent(mContext, CreateGroupActivity.class);
                startActivityForResult(intent, StaticData.CREATE_GROUP);

                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GROUP_DETAIL:

                if (resultCode == Activity.RESULT_OK) {

                    Group group = (Group) data.getSerializableExtra("group");

                    if (group != null) {

                        int groupLength = groupList.size();

                        for (int i = 0; i < groupLength; i++) {
                            if (group.getId().equals(groupList.get(i).getId())) {
                                if (data.getBooleanExtra("delete", false)) {
                                    groupList.remove(i);
                                } else {
                                    groupList.set(i, group);
                                }
                                break;
                            }
                        }

                        int tempLength = tempList.size();

                        for (int i = 0; i < tempLength; i++) {
                            if (group.getId().equals(tempList.get(i).getId())) {
                                if (data.getBooleanExtra("delete", false)) {
                                    tempList.remove(i);
                                } else {
                                    tempList.set(i, group);
                                }
                                break;
                            }
                        }

                        mAdapter.notifyDataSetChanged();

                        if (groupList.isEmpty()) {
                            showEmptyView();
                        } else {
                            hideEmptyView();
                        }

                    }
                }

                break;


            case CREATE_GROUP:

                if (resultCode == Activity.RESULT_OK) {

                    Group group = (Group) data.getSerializableExtra("group");

                    if (group != null) {
                        groupList.add(group);
                        tempList.add(group);
                        mAdapter.notifyDataSetChanged();

                        if (groupList.isEmpty()){
                            showEmptyView();
                        }else {
                            hideEmptyView();
                        }
                    }
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


    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method) {

            case GET_GROUPS:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        Group group = gson.fromJson(data.getJSONObject(i).toString(), Group.class);

                        groupList.add(group);
                    }

                    tempList.addAll(groupList);

                    mAdapter.notifyDataSetChanged();

                    if (groupList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }


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
