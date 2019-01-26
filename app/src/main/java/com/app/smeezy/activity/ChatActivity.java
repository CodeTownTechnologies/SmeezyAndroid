package com.app.smeezy.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.ChatAdapter;
import com.app.smeezy.interfacess.ChatListener;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.ChatData;
import com.app.smeezy.responsemodels.SingleChatItem;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements IRequestView, View.OnClickListener, ChatListener {

    private static final String GET_SINGLE_CHAT = "get_single_chat";
    private static final String GET_UNREAD_CHAT = "get_unread_chat";
    private static final String COMPOSE_MESSAGE = "compose_message";
    private static final String SAVE_ALLOW_USER = "save_allow_user";
    private static final String REMOVE_ALLOW_USER = "remove_allow_user";

    @BindView(R.id.chat_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.et_chat_compose_message)
    EditText et_new_message;

    @BindView(R.id.btn_chat_send_message)
    ImageButton btn_send_message;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    @BindView(R.id.tv_block_desc)
    TextView tv_block_desc;

    @BindView(R.id.ll_chat_message_bar)
    LinearLayout ll_chat_message_bar;

    @BindView(R.id.cb_chat_allow)
    CheckBox cb_allow;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ChatAdapter mAdapter;
    private ArrayList<SingleChatItem> singleChatList = new ArrayList<>();
    private String friendId, friendImage, friendName;

    private boolean noMoreChatItems = false;
    private int page = 1, blockStatus, myAllowed, isFriend;

    private BroadcastReceiver chatRefreshBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getUnreadChat();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent receivedIntent = getIntent();
        friendId = receivedIntent.getStringExtra("friendId");
        friendImage = receivedIntent.getStringExtra("friendImage");
        friendName = receivedIntent.getStringExtra("friendName");
        blockStatus = receivedIntent.getIntExtra("blockStatus", 0);
        myAllowed = receivedIntent.getIntExtra("myAllowed", 0);
        isFriend = receivedIntent.getIntExtra("isFriend", 0);

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        mAdapter = new ChatAdapter(mContext, singleChatList, friendImage, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(-1)) {
                    onBottomReached();
                }
            }
        });

        getSingleChat(page);

        setUpToolbar();

        if (blockStatus == 0) {
            tv_block_desc.setVisibility(View.GONE);
            ll_chat_message_bar.setVisibility(View.VISIBLE);
        } else {
            ll_chat_message_bar.setVisibility(View.GONE);
            tv_block_desc.setVisibility(View.VISIBLE);
        }

        if (isFriend == 1){
            cb_allow.setVisibility(View.GONE);
        }else {
            cb_allow.setVisibility(View.VISIBLE);
            if (myAllowed == 1){
                cb_allow.setChecked(true);
            }else {
                cb_allow.setChecked(false);
            }
        }


    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        ImageView img_logo = (ImageView) findViewById(R.id.img_logo_toolbar);
        Glide.with(mContext)
                .load(PreferenceUtils.getLogoUrl(mContext))
                .asBitmap()
                .into(img_logo);

        TextView activity_title = (TextView) findViewById(R.id.chat_activity_title);
        activity_title.setText(friendName);

        ImageView img_friend = (ImageView) findViewById(R.id.img_chat_toolbar);
        Glide.with(mContext)
                .load(friendImage + StaticData.THUMB_100)
                .asBitmap()
                .placeholder(R.drawable.no_user_white)
                .into(img_friend);

        img_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blockStatus == 0) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("memberId", friendId);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    private void getSingleChat(int page) {
        if (!noMoreChatItems) {
            if (cd.isConnectingToInternet()) {
                startProgressBar();
                requestPresenter.getSingleChat(GET_SINGLE_CHAT, PreferenceUtils.getId(mContext), friendId, page);
            } else {
                Utills.noInternetConnection(mContext);
            }
        }

    }

    private void getUnreadChat() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getUnreadChat(GET_UNREAD_CHAT, PreferenceUtils.getId(mContext), friendId);
        } else {
            Utills.noInternetConnection(mContext);
        }

    }

    private void composeMessage(String message) {
        if (cd.isConnectingToInternet()) {
            startProgressBar();
            requestPresenter.composeMessage(COMPOSE_MESSAGE, PreferenceUtils.getId(mContext), friendId, message);
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onBottomReached() {
        if (!noMoreChatItems) {
            page++;
            getSingleChat(page);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mContext.registerReceiver(chatRefreshBroadcast, new IntentFilter(StaticData.CHAT_REFRESH_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();

        mContext.unregisterReceiver(chatRefreshBroadcast);

    }

    @OnClick({R.id.btn_chat_send_message, R.id.cb_chat_allow})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_chat_send_message:

                String newMessage = et_new_message.getText().toString().trim();

                if (newMessage.isEmpty()) {
                    showAlert(getString(R.string.app_name), getString(R.string.message_required));
                } else {
                    composeMessage(newMessage);
                }

                break;

            case R.id.cb_chat_allow:

                if (myAllowed == 0){
                    if (cd.isConnectingToInternet()){
                        startProgressBar();
                        requestPresenter.saveAllowUser(SAVE_ALLOW_USER, PreferenceUtils.getId(mContext), friendId);
                    }else {
                        Utills.noInternetConnection(mContext);
                    }
                }else {
                    if (cd.isConnectingToInternet()){
                        startProgressBar();
                        requestPresenter.removeAllowUser(REMOVE_ALLOW_USER, PreferenceUtils.getId(mContext), friendId);
                    }else {
                        Utills.noInternetConnection(mContext);
                    }
                }

                break;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
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

            case GET_SINGLE_CHAT:

                showAlert(getString(R.string.app_name), message);

                break;

            case COMPOSE_MESSAGE:

                showAlert(getString(R.string.app_name), message);

                break;

            case SAVE_ALLOW_USER:

                cb_allow.setChecked(false);
                showAlert(getString(R.string.app_name), message);

                break;

            case REMOVE_ALLOW_USER:

                cb_allow.setChecked(true);
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

            case GET_SINGLE_CHAT: {

                JSONArray data = Data.optJSONArray("data");
                int length = data.length();
                Gson gson = new Gson();

                if (length == 0) {

                    noMoreChatItems = true;

                    if (singleChatList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }

                    break;

                }

                if (page == 1) {

                    for (int i = 0; i < length; i++) {

                        JSONObject currentData = data.optJSONObject(i);
                        /*gson.excluder().excludeFieldsWithoutExposeAnnotation();
                        SingleChatItem singleChatItem = gson.fromJson(currentData.toString(), SingleChatItem.class);
*/
                        SingleChatItem singleChatItem = new SingleChatItem()
                                .withThreadId(currentData.optString("thread_id"))
                                .withSenderId(currentData.optString("sender_id"))
                                .withReceiverId(currentData.optString("receiver_id"))
                                .withMessage(currentData.optString("message"))
                                .withAddDate(currentData.optString("add_date"))
                                .withImage(currentData.optString("image"))
                                .withName(currentData.optString("name"));



                        if (!currentData.optString("data").equals("")){
                            JSONObject chatDataObject = currentData.optJSONObject("data");

                            if (!chatDataObject.optString("stuff_type").equals("")){
                                singleChatItem.getData().withStuffId(chatDataObject.optString("stuff_id"))
                                        .withAction(chatDataObject.optString("action"))
                                        .withStuffType(chatDataObject.optString("stuff_type"))
                                        .withStuffName(chatDataObject.optString("stuff_name"))
                                        .withSelectedStuff(chatDataObject.optString("selected_stuff"));
                            }

                        }


                        singleChatList.add(singleChatItem);

                    }
                } else {
                    for (int i = length - 1; i >= 0; i--) {

                        JSONObject currentData = data.optJSONObject(i);
                        SingleChatItem singleChatItem = new SingleChatItem()
                                .withThreadId(currentData.optString("thread_id"))
                                .withSenderId(currentData.optString("sender_id"))
                                .withReceiverId(currentData.optString("receiver_id"))
                                .withMessage(currentData.optString("message"))
                                .withAddDate(currentData.optString("add_date"))
                                .withImage(currentData.optString("image"))
                                .withName(currentData.optString("name"));

                        if (!currentData.optString("data").equals("")){
                            JSONObject chatDataObject = currentData.optJSONObject("data");

                            if (!chatDataObject.optString("stuff_type").equals("")){
                                singleChatItem.getData().withStuffId(chatDataObject.optString("stuff_id"))
                                        .withAction(chatDataObject.optString("action"))
                                        .withStuffType(chatDataObject.optString("stuff_type"))
                                        .withStuffName(chatDataObject.optString("stuff_name"))
                                        .withSelectedStuff(chatDataObject.optString("selected_stuff"));
                            }

                        }

                        singleChatList.add(0, singleChatItem);

                    }
                }

                if (singleChatList.isEmpty()) {
                    showEmptyView();
                } else {
                    hideEmptyView();
                    if (page == 1) {
                        recyclerView.scrollToPosition(singleChatList.size() - 1);
                    } else {

                    }
                }

                mAdapter.notifyDataSetChanged();

                break;
            }

            case COMPOSE_MESSAGE: {

                JSONObject data = Data.optJSONObject("data");
                Gson gson = new Gson();

                SingleChatItem singleChatItem = new SingleChatItem()
                        .withThreadId(data.optString("thread_id"))
                        .withSenderId(data.optString("sender_id"))
                        .withReceiverId(data.optString("receiver_id"))
                        .withMessage(data.optString("message"))
                        .withAddDate(data.optString("add_date"))
                        .withImage(data.optString("image"))
                        .withName(data.optString("name"));

                singleChatList.add(singleChatItem);

                mAdapter.notifyDataSetChanged();
                et_new_message.setText("");
                if (singleChatList.isEmpty()) {
                    showEmptyView();
                } else {
                    hideEmptyView();
                    recyclerView.smoothScrollToPosition(singleChatList.size() - 1);
                }
                break;
            }

            case GET_UNREAD_CHAT: {

                JSONArray data = Data.optJSONArray("data");
                int length = data.length();
                Gson gson = new Gson();

                for (int i = 0; i < length; i++) {

                    JSONObject currentData = data.optJSONObject(i);
                    SingleChatItem singleChatItem = new SingleChatItem()
                            .withThreadId(currentData.optString("thread_id"))
                            .withSenderId(currentData.optString("sender_id"))
                            .withReceiverId(currentData.optString("receiver_id"))
                            .withMessage(currentData.optString("message"))
                            .withAddDate(currentData.optString("add_date"))
                            .withImage(currentData.optString("image"))
                            .withName(currentData.optString("name"));

                    if (!currentData.optString("data").equals("")){
                        JSONObject chatDataObject = currentData.optJSONObject("data");

                        if (!chatDataObject.optString("stuff_type").equals("")){
                            singleChatItem.getData().withStuffId(chatDataObject.optString("stuff_id"))
                                    .withAction(chatDataObject.optString("action"))
                                    .withStuffType(chatDataObject.optString("stuff_type"))
                                    .withStuffName(chatDataObject.optString("stuff_name"))
                                    .withSelectedStuff(chatDataObject.optString("selected_stuff"));
                        }

                    }

                    singleChatList.add(singleChatItem);

                }
                mAdapter.notifyDataSetChanged();
                if (singleChatList.isEmpty()) {
                    showEmptyView();
                } else {
                    hideEmptyView();
                    recyclerView.scrollToPosition(singleChatList.size() - 1);
                }
                break;
            }

            case SAVE_ALLOW_USER:

                myAllowed = 1;
                cb_allow.setChecked(true);

                break;

            case REMOVE_ALLOW_USER:

                myAllowed = 0;
                cb_allow.setChecked(false);

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
