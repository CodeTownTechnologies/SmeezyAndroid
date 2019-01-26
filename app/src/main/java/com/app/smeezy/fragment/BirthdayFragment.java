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
import com.app.smeezy.adapter.BirthdayAdapter;
import com.app.smeezy.adapter.MyEventAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.BirthdayItem;
import com.app.smeezy.responsemodels.Event;
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

import static com.app.smeezy.utills.StaticData.CREATE_EVENT;
import static com.app.smeezy.utills.StaticData.EVENT_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 */
public class BirthdayFragment extends BaseFragment implements IRequestView {

    private static final String GET_BIRTHDAYS = "get_birthdays";


    @BindView(R.id.birthday_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    @BindView(R.id.et_birthday_search)
    EditText et_search;


    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private BirthdayAdapter mAdapter;
    private ArrayList<BirthdayItem> birthdayList = new ArrayList<>();
    private ArrayList<BirthdayItem> tempList = new ArrayList<>();

    public BirthdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_birthday, container, false);

        ButterKnife.bind(this, view);


        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        mAdapter = new BirthdayAdapter(mContext, birthdayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getBirthdayList();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String query = charSequence.toString();

                if (query.isEmpty()){

                    birthdayList.clear();
                    birthdayList.addAll(tempList);
                    mAdapter.notifyDataSetChanged();

                }else {

                    birthdayList.clear();

                    for (BirthdayItem birthday : tempList){

                        if (birthday.getName().toLowerCase().contains(query.toLowerCase())){
                            birthdayList.add(birthday);
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

    private void getBirthdayList(){

        if (cd.isConnectingToInternet()){
            requestPresenter.getBirthdayList(GET_BIRTHDAYS, PreferenceUtils.getId(mContext));
        }else {
            Utills.noInternetConnection(mContext);
        }
    }


    @Override
    public void showLoadingProgressBar() {

    }

    @Override
    public void hideLoadingProgressBar() {

    }

    @Override
    public void Failed(String method, String message) {

    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method){

            case GET_BIRTHDAYS:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    for (int i = 0; i < length; i++){

                        Gson gson = new Gson();

                        BirthdayItem birthdayItem = gson.fromJson(data.getJSONObject(i).toString(), BirthdayItem.class);

                        birthdayList.add(birthdayItem);
                    }

                    tempList.addAll(birthdayList);

                    mAdapter.notifyDataSetChanged();

                    if (birthdayList.isEmpty()){
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

    private void showEmptyView() {
        recyclerView.setVisibility(View.GONE);
        tv_empty_view.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        tv_empty_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

}
