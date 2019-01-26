package com.app.smeezy.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.app.smeezy.activity.CreateEventActivity;
import com.app.smeezy.adapter.MyEventAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
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
public class PastEventFragment extends BaseFragment implements View.OnClickListener, IRequestView {

    private static final String GET_EVENTS = "get_events";


    @BindView(R.id.event_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.img_event_add)
    ImageView img_add;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    @BindView(R.id.et_event_search)
    EditText et_search;


    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private MyEventAdapter mAdapter;
    private ArrayList<Event> eventList = new ArrayList<>();
    private ArrayList<Event> tempList = new ArrayList<>();

    public PastEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_event, container, false);

        ButterKnife.bind(this, view);


        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        mAdapter = new MyEventAdapter(mContext, eventList);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getEventList();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String query = charSequence.toString();

                if (query.isEmpty()) {

                    eventList.clear();
                    eventList.addAll(tempList);
                    mAdapter.notifyDataSetChanged();

                } else {

                    eventList.clear();

                    for (Event event : tempList) {

                        if (event.getTitle().toLowerCase().contains(query.toLowerCase())) {
                            eventList.add(event);
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

    private void getEventList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getEventList(GET_EVENTS, PreferenceUtils.getId(mContext), "past");
        } else {
            Utills.noInternetConnection(mContext);
        }

    }

    @OnClick({R.id.img_event_add})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            /*case R.id.img_event_add:

                Intent intent = new Intent(mContext, CreateEventActivity.class);
                startActivity(intent);

                break;*/

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case EVENT_DETAIL:

                if (resultCode == Activity.RESULT_OK) {

                    Event event = (Event) data.getSerializableExtra("event");

                    if (event != null) {

                        int eventLength = eventList.size();

                        for (int i = 0; i < eventLength; i++) {
                            if (event.getId().equals(eventList.get(i).getId())) {
                                if (data.getBooleanExtra("delete", false)) {
                                    eventList.remove(i);
                                } else {
                                    eventList.set(i, event);
                                }
                                break;
                            }
                        }

                        int tempLength = tempList.size();

                        for (int i = 0; i < tempLength; i++) {
                            if (event.getId().equals(tempList.get(i).getId())) {
                                if (data.getBooleanExtra("delete", false)) {
                                    tempList.remove(i);
                                } else {
                                    tempList.set(i, event);
                                }
                                break;
                            }
                        }

                        mAdapter.notifyDataSetChanged();

                        if (eventList.isEmpty()){
                            showEmptyView();
                        }else {
                            hideEmptyView();
                        }

                    }
                }

                break;

            case CREATE_EVENT:

                if (resultCode == Activity.RESULT_OK) {

                    Event event = (Event) data.getSerializableExtra("event");

                    if (event != null) {
                        eventList.add(event);
                        tempList.add(event);
                        mAdapter.notifyDataSetChanged();

                        if (eventList.isEmpty()){
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

            case GET_EVENTS:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        Event event = gson.fromJson(data.getJSONObject(i).toString(), Event.class);

                        eventList.add(event);
                    }

                    tempList.addAll(eventList);

                    mAdapter.notifyDataSetChanged();

                    if (eventList.isEmpty()) {
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
