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
import com.app.smeezy.adapter.EventRequestListAdapter;
import com.app.smeezy.interfacess.EventRequestListener;
import com.app.smeezy.interfacess.FriendRequestListener;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class EventRequestsFragment extends BaseFragment implements IRequestView, EventRequestListener {


    private static final String GET_EVENT_REQUEST_LIST = "get_event_request_list";
    private static final String ACCEPT_REQUEST = "accept_request";
    private static final String REJECT_REQUEST = "reject_request";
    private static final String MAYBE = "may_be";

    @BindView(R.id.event_request_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<Event> eventRequestList = new ArrayList<>();
    private EventRequestListAdapter mAdapter;
    private int acceptPosition, rejectPosition, maybePosition;

    public EventRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_requests, container, false);

        ButterKnife.bind(this, view);

        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        mAdapter = new EventRequestListAdapter(mContext, eventRequestList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        getEventRequestList();

        return view;
    }

    private void getEventRequestList() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getEventRequestList(GET_EVENT_REQUEST_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onYes(int position, String inviteId) {
        if (cd.isConnectingToInternet()) {
            this.acceptPosition = position;
            requestPresenter.manageEventRequest(ACCEPT_REQUEST, PreferenceUtils.getId(mContext),
                    inviteId, "yes");
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onNo(int position, String inviteId) {
        if (cd.isConnectingToInternet()) {
            this.rejectPosition = position;
            requestPresenter.manageEventRequest(REJECT_REQUEST, PreferenceUtils.getId(mContext),
                    inviteId, "no");
        } else {
            Utills.noInternetConnection(mContext);
        }
    }


    @Override
    public void onMaybe(int position, String inviteId) {
        if (cd.isConnectingToInternet()) {
            this.maybePosition = position;
            requestPresenter.manageEventRequest(MAYBE, PreferenceUtils.getId(mContext),
                    inviteId, "maybe");
        } else {
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
        switch (method) {

            case GET_EVENT_REQUEST_LIST:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case ACCEPT_REQUEST:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case REJECT_REQUEST:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case MAYBE:

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

            case GET_EVENT_REQUEST_LIST:

                try {

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        Event event = gson.fromJson(data.getJSONObject(i).toString(), Event.class);

                        eventRequestList.add(event);
                    }

                    mAdapter.notifyDataSetChanged();

                    if (eventRequestList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


            case ACCEPT_REQUEST:

                eventRequestList.remove(acceptPosition);
                mAdapter.notifyDataSetChanged();

                if (eventRequestList.isEmpty()) {
                    showEmptyView();
                }

                break;


            case REJECT_REQUEST:

                eventRequestList.remove(rejectPosition);
                mAdapter.notifyDataSetChanged();

                if (eventRequestList.isEmpty()) {
                    showEmptyView();
                }

                break;

            case MAYBE:

                eventRequestList.remove(maybePosition);
                mAdapter.notifyDataSetChanged();

                if (eventRequestList.isEmpty()) {
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
}
