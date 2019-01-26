package com.app.smeezy.fragment;


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
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.PrivateChatListAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.ChatListItem;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateChatListFragment extends BaseFragment implements IRequestView {

    private static final String GET_CHAT_LIST = "get_private_chat_list";

    @BindView(R.id.private_chat_list_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.private_chat_list_empty_view)
    TextView empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<ChatListItem> chatList = new ArrayList<>();
    private ArrayList<ChatListItem> tempChatList = new ArrayList<>();
    private PrivateChatListAdapter mAdapter;

    private BroadcastReceiver chatRefreshBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            chatList.clear();
            getChatList();
        }
    };

    public PrivateChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_private_chat_list, container, false);
        ButterKnife.bind(this, view);

        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);
        mAdapter = new PrivateChatListAdapter(mContext, chatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);


        return view;
    }

    private void getChatList() {
        if (cd.isConnectingToInternet()) {
            requestPresenter.getChatList(GET_CHAT_LIST, PreferenceUtils.getId(mContext), "private");
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mContext.registerReceiver(chatRefreshBroadcast, new IntentFilter(StaticData.CHAT_REFRESH_BROADCAST));

        chatList.clear();
        mAdapter.notifyDataSetChanged();
        if (cd.isConnectingToInternet()){
            startProgressBar();
            getChatList();
        }else {
            Utills.noInternetConnection(mContext);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        mContext.unregisterReceiver(chatRefreshBroadcast);

    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_private_chat_list_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.private_chat_list_search).getActionView();
        menu.findItem(R.id.private_chat_list_search).expandActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {



                return false;
            }
        });

    }*/

    public void onQueryChange(String newText){

        chatList.clear();

        if (newText.isEmpty()) {
            chatList.addAll(tempChatList);
            mAdapter.notifyDataSetChanged();
        } else {

            for (int i = 0; i < tempChatList.size(); i++) {

                if (tempChatList.get(i).getName().toLowerCase().contains(newText.toLowerCase())) {
                    chatList.add(tempChatList.get(i));
                }

            }
            mAdapter.notifyDataSetChanged();
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

            case GET_CHAT_LIST:

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

            case GET_CHAT_LIST:

                JSONArray data = Data.optJSONArray("data");
                int length = data.length();

                Gson gson = new Gson();

                for (int i = 0; i < length; i++) {

                    JSONObject currentData = data.optJSONObject(i);
                    ChatListItem private_chatListItem = gson.fromJson(currentData.toString(), ChatListItem.class);
                    chatList.add(private_chatListItem);
                }

                tempChatList.addAll(chatList);
                mAdapter.notifyDataSetChanged();
                if (chatList.isEmpty()) {
                    showEmptyView();
                } else {
                    hideEmptyView();
                    recyclerView.scrollToPosition(chatList.size() - 1);
                }
                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }

    private void showEmptyView() {

        recyclerView.setVisibility(View.GONE);
        empty_view.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {

        empty_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
