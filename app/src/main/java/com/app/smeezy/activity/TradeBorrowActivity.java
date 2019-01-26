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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.InterestListAdapter;
import com.app.smeezy.adapter.TradeStuffListAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.interfacess.InterestListener;
import com.app.smeezy.interfacess.TradeStuffListener;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.InterestItem;
import com.app.smeezy.responsemodels.PaymentOption;
import com.app.smeezy.responsemodels.StuffCategory;
import com.app.smeezy.responsemodels.StuffFeedItem;
import com.app.smeezy.responsemodels.UserStuff;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TradeBorrowActivity extends BaseActivity implements View.OnClickListener, IRequestView,
        TradeStuffListener, InterestListener {

    private BroadcastReceiver stuffRefreshBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tradeStuffList.clear();
            tradeStuffListAdapter.notifyDataSetChanged();
            if (cd.isConnectingToInternet()) {
                startProgressBar();
                getUserTradeStuffList();
            } else {
                Utills.noInternetConnection(mContext);
            }
        }
    };

    private static final String GET_TRADE_STUFF = "get_trade_stuff";
    private static final String GET_INTEREST_LIST = "get_interest_list";
    private static final String GET_STUFF_CATEGORY_LIST = "get_stuff_category";
    private static final String TRADE_SEND_MESSAGE = "trade_send_message";
    private static final String SHARE_SEND_MESSAGE = "share_send_message";
    private static final String GET_PAYMENT_OPTION_LIST = "get_payment_options";

    @BindView(R.id.trade_borrow_stuff_recycler_view)
    RecyclerView stuff_recyclerView;

    @BindView(R.id.trade_borrow_interest_recycler_view)
    RecyclerView interest_recyclerView;

    @BindView(R.id.et_trade_message)
    EditText et_trade_message;

    /*@BindView(R.id.et_share_message)
    EditText et_share_message;*/

    @BindView(R.id.trade_borrow_stuff_empty_view)
    TextView tv_stuff_empty_view;

    @BindView(R.id.trade_borrow_interest_empty_view)
    TextView tv_interest_empty_view;

    @BindView(R.id.ll_action)
    LinearLayout ll_action;

    /*@BindView(R.id.rl_trade)
    RelativeLayout rl_trade;

    @BindView(R.id.ll_other)
    LinearLayout ll_other;*/

    @BindView(R.id.rg_trade_borrow)
    RadioGroup rg_trade_borrow;

    @BindView(R.id.rb_trade)
    RadioButton rb_trade;

    @BindView(R.id.rb_share)
    RadioButton rb_share;

    @BindView(R.id.rb_rent)
    RadioButton rb_rent;

    @BindView(R.id.rb_buy)
    RadioButton rb_buy;

    @BindView(R.id.img_item)
    ImageView img_item;

    @BindView(R.id.tv_item_name)
    TextView tv_item_name;

    @BindView(R.id.tv_trade_borrow_payment_options)
    TextView tv_payment_options;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<UserStuff> tradeStuffList = new ArrayList<>();
    private ArrayList<InterestItem> interestList = new ArrayList<>();
    private ArrayList<StuffCategory> stuffCategoryList = new ArrayList<>();
    private TradeStuffListAdapter tradeStuffListAdapter;
    private InterestListAdapter interestListAdapter;

    private StuffFeedItem stuffFeedItem;

    private TextView activity_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_borrow);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        stuffFeedItem = (StuffFeedItem) intent.getSerializableExtra("stuffFeedItem");
        String from = intent.getStringExtra("from");

        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        setUpToolbar();

        tradeStuffListAdapter = new TradeStuffListAdapter(mContext, tradeStuffList, this);
        stuff_recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        stuff_recyclerView.setAdapter(tradeStuffListAdapter);

        interestListAdapter = new InterestListAdapter(mContext, interestList, this);
        interest_recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        interest_recyclerView.setAdapter(interestListAdapter);

        getPaymentOptions();

        tv_item_name.setText(stuffFeedItem.getStuffText());
        Glide.with(mContext)
                .load(stuffFeedItem.getImage() + StaticData.THUMB_100)
                .asBitmap()
                .placeholder(R.drawable.hand_icon)
                .into(img_item);

        boolean isShare = false, isTrade = false, isBuy = false, isRent = false;


        if (stuffFeedItem.getOptions().contains("trade")) {
            isTrade = true;
        }

        if (stuffFeedItem.getOptions().contains("share")) {
            isShare = true;
        }

        if (stuffFeedItem.getBuy().equals("Yes")) {
            isBuy = true;
            rb_buy.setText(String.format("%s %s", getString(R.string.buy),
                    Utills.moneyConvert(stuffFeedItem.getBuyPrice())));
        }

        if (stuffFeedItem.getRent().equals("Yes")) {
            isRent = true;
            rb_rent.setText(String.format("%s %s", getString(R.string.rent),
                    Utills.moneyConvert(stuffFeedItem.getRentPrice())));
        }

        rg_trade_borrow.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.rb_trade:

                        /*rl_trade.setVisibility(View.VISIBLE);
                        ll_other.setVisibility(View.GONE);*/
                        Utills.hideSoftKeyboard(TradeBorrowActivity.this);
                        et_trade_message.setText(getString(R.string.trade_pre_filled));
                        et_trade_message.setSelection(et_trade_message.getText().toString().length());
                        //activity_title.setText(getString(R.string.offer_trade));

                        break;

                    case R.id.rb_share:

                        /*ll_other.setVisibility(View.VISIBLE);
                        rl_trade.setVisibility(View.GONE);*/
                        et_trade_message.setText(getString(R.string.borrow_pre_filled,
                                stuffFeedItem.getStuffText()));
                        et_trade_message.setSelection(et_trade_message.getText().toString().length());
                        Utills.hideSoftKeyboard(TradeBorrowActivity.this);
                        //activity_title.setText(getString(R.string.borrow));

                        break;

                    case R.id.rb_rent:

                        /*ll_other.setVisibility(View.VISIBLE);
                        rl_trade.setVisibility(View.GONE);*/
                        et_trade_message.setText(getString(R.string.rent_pre_filled,
                                stuffFeedItem.getStuffText()));
                        et_trade_message.setSelection(et_trade_message.getText().toString().length());
                        Utills.hideSoftKeyboard(TradeBorrowActivity.this);

                        break;

                    case R.id.rb_buy:

                       /* ll_other.setVisibility(View.VISIBLE);
                        rl_trade.setVisibility(View.GONE);*/
                        et_trade_message.setText(getString(R.string.buy_pre_filled,
                                stuffFeedItem.getStuffText()));
                        et_trade_message.setSelection(et_trade_message.getText().toString().length());
                        Utills.hideSoftKeyboard(TradeBorrowActivity.this);

                        break;

                }
            }
        });

        if ((isTrade && isShare) || (isTrade && isBuy) || (isTrade && isRent) ||
                (isShare && isBuy) || (isShare && isRent)) {
            ll_action.setVisibility(View.VISIBLE);

            if (from.equals("trade")) {
                rb_trade.setChecked(true);
            } else if (from.equals("share")) {
                rb_share.setChecked(true);
            } else if (from.equals("rent")) {
                rb_rent.setChecked(true);
            } else if (from.equals("buy")) {
                rb_buy.setChecked(true);
            } else {
                rb_trade.setChecked(true);
            }

            if (!isTrade) {
                rb_trade.setVisibility(View.GONE);
            }

            if (!isShare) {
                rb_share.setVisibility(View.GONE);
            }

            if (!isBuy) {
                rb_buy.setVisibility(View.GONE);
            }

            if (!isRent) {
                rb_rent.setVisibility(View.GONE);
            }

            if (isRent || isBuy) {
                if (!stuffFeedItem.getPaymentAccepted().isEmpty()) {
                    tv_payment_options.setVisibility(View.VISIBLE);
                } else {
                    tv_payment_options.setVisibility(View.GONE);
                }
            }

        } else {
            ll_action.setVisibility(View.GONE);

            if (isTrade) {
                /*rl_trade.setVisibility(View.VISIBLE);
                ll_other.setVisibility(View.GONE);*/

                rb_trade.setChecked(true);

                et_trade_message.setText(getString(R.string.trade_pre_filled));
                et_trade_message.setSelection(et_trade_message.getText().toString().length());

                //activity_title.setText(getString(R.string.offer_trade));
            } else {
                /*ll_other.setVisibility(View.VISIBLE);
                rl_trade.setVisibility(View.GONE);*/

                if (from.equals("share")) {
                    rb_share.setChecked(true);
                } else if (from.equals("rent")) {
                    rb_rent.setChecked(true);
                } else if (from.equals("buy")) {
                    rb_buy.setChecked(true);
                }


                //activity_title.setText(getString(R.string.borrow));
            }
        }

        /*rg_trade_borrow.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.rb_trade:

                        rl_trade.setVisibility(View.VISIBLE);
                        ll_other.setVisibility(View.GONE);

                        activity_title.setText(getString(R.string.offer_trade));

                        break;


                    case R.id.rb_share:

                        ll_other.setVisibility(View.VISIBLE);
                        rl_trade.setVisibility(View.GONE);

                        activity_title.setText(getString(R.string.borrow));

                        break;

                }
            }
        });*/


    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(stuffRefreshBroadcast, new IntentFilter(StaticData.STUFF_REFRESH_BROADCAST));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(stuffRefreshBroadcast);
    }


    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.trade_borrow_toolbar);
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

        activity_title = (TextView) findViewById(R.id.activity_title);
        activity_title.setText(getString(R.string.title_activity_trade_borrow));
    }

    private void getPaymentOptions() {
        if (cd.isConnectingToInternet()) {
            startProgressBar();
            requestPresenter.getPaymentOptionsList(GET_PAYMENT_OPTION_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    private void getStuffCategoryList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getStuffList(GET_STUFF_CATEGORY_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }

    }

    private void getUserTradeStuffList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getUserTradeStuffList(GET_TRADE_STUFF, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    private void getMemberInterestList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getMemberInterestList(GET_INTEREST_LIST, PreferenceUtils.getId(mContext),
                    stuffFeedItem.getUserId());
        } else {
            Utills.noInternetConnection(mContext);
        }
    }


    @Override
    public void onInterestClickListener(int position) {
        tradeStuffListAdapter.clearSelection();
    }

    @Override
    public void onStuffClickListener(int position) {
        interestListAdapter.clearSelection();
    }

    @OnClick({R.id.btn_trade_send, R.id.img_stuff_add})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_stuff_add:

                Intent addStuffIntent = new Intent(mContext, AddStuffActivity.class);
                addStuffIntent.putExtra("isManual", true);
                addStuffIntent.putExtra("stuffCategoryList", stuffCategoryList);
                startActivity(addStuffIntent);

                break;

            case R.id.btn_trade_send:

                String tradeMessage = et_trade_message.getText().toString().trim();
                String stuffText = "";

                for (UserStuff stuff : tradeStuffList) {

                    if (stuff.getIsSelected()) {
                        stuffText = stuff.getStuffText();
                        break;
                    }
                }

                for (InterestItem interestItem : interestList) {

                    if (interestItem.getIsSelected()) {
                        stuffText = interestItem.getTitle();
                        break;
                    }
                }


                if (rb_trade.isChecked()) {
                    if (stuffText.isEmpty() && tradeMessage.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.you_must_send_a_message_or_select_an_item_to_trade));
                    } else {
                        if (cd.isConnectingToInternet()) {
                            startProgressBar();
                            requestPresenter.tradeOffer(TRADE_SEND_MESSAGE, PreferenceUtils.getId(mContext),
                                    tradeMessage, "trade", stuffFeedItem.getId(), "trade_offer",
                                    stuffText);
                        } else {
                            Utills.noInternetConnection(mContext);
                        }
                    }
                } else {
                    if (tradeMessage.isEmpty()) {
                        showAlert(getString(R.string.app_name), getString(R.string.message_required));
                    } else {
                        if (cd.isConnectingToInternet()) {
                            if (rb_share.isChecked()) {
                                startProgressBar();
                                requestPresenter.composeMessageStuff(SHARE_SEND_MESSAGE,
                                        PreferenceUtils.getId(mContext),
                                        stuffFeedItem.getUserId(), tradeMessage, "share",
                                        stuffFeedItem.getId(), stuffText);
                            } else if (rb_rent.isChecked()) {
                                startProgressBar();
                                requestPresenter.composeMessageStuff(SHARE_SEND_MESSAGE,
                                        PreferenceUtils.getId(mContext),
                                        stuffFeedItem.getUserId(), tradeMessage, "rent",
                                        stuffFeedItem.getId(), stuffText);
                            } else if (rb_buy.isChecked()) {
                                startProgressBar();
                                requestPresenter.composeMessageStuff(SHARE_SEND_MESSAGE,
                                        PreferenceUtils.getId(mContext),
                                        stuffFeedItem.getUserId(), tradeMessage, "buy",
                                        stuffFeedItem.getId(), stuffText);
                            }

                        } else {
                            Utills.noInternetConnection(mContext);
                        }
                    }
                }


                break;

            /*case R.id.btn_share_send:


                String shareMessage = et_trade_message.getText().toString().trim();
                String stuffText1 = "";

                for (UserStuff stuff : tradeStuffList) {

                    if (stuff.getIsSelected()) {
                        stuffText1 = stuff.getStuffText();
                        break;
                    }
                }

                for (InterestItem interestItem : interestList) {

                    if (interestItem.getIsSelected()) {
                        stuffText1 = interestItem.getTitle();
                        break;
                    }
                }

                if (shareMessage.isEmpty()) {
                    showAlert(getString(R.string.app_name), getString(R.string.message_required));
                } else {

                    if (cd.isConnectingToInternet()) {
                        if (rb_share.isChecked()) {
                            startProgressBar();
                            requestPresenter.composeMessageStuff(SHARE_SEND_MESSAGE,
                                    PreferenceUtils.getId(mContext),
                                    stuffFeedItem.getUserId(), shareMessage, "share",
                                    stuffFeedItem.getId(), stuffText1);
                        } else if (rb_rent.isChecked()) {
                            startProgressBar();
                            requestPresenter.composeMessageStuff(SHARE_SEND_MESSAGE,
                                    PreferenceUtils.getId(mContext),
                                    stuffFeedItem.getUserId(), shareMessage, "rent",
                                    stuffFeedItem.getId(), stuffText1);
                        } else if (rb_buy.isChecked()) {
                            startProgressBar();
                            requestPresenter.composeMessageStuff(SHARE_SEND_MESSAGE,
                                    PreferenceUtils.getId(mContext),
                                    stuffFeedItem.getUserId(), shareMessage, "buy",
                                    stuffFeedItem.getId(), stuffText1);
                        }

                    } else {
                        Utills.noInternetConnection(mContext);
                    }
                }


                break;*/


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

    }

    @Override
    public void Failed(String method, String message) {
        switch (method) {

            case GET_STUFF_CATEGORY_LIST:

                dismissProgressBar();
                showAlert(getString(R.string.app_name), message);

                break;

            case GET_TRADE_STUFF:

                dismissProgressBar();
                showAlert(getString(R.string.app_name), message);

                break;

            case GET_INTEREST_LIST:

                dismissProgressBar();
                showAlert(getString(R.string.app_name), message);

                break;

            case TRADE_SEND_MESSAGE:

                dismissProgressBar();
                showAlert(getString(R.string.app_name), message);

                break;

            case SHARE_SEND_MESSAGE:

                dismissProgressBar();
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

            case GET_STUFF_CATEGORY_LIST:

                try {

                    getMemberInterestList();

                    JSONObject data = Data.getJSONObject("data");


                    JSONArray stuffCategoryArray = data.optJSONArray("category_list");

                    int length1 = stuffCategoryArray.length();

                    stuffCategoryList.add(new StuffCategory().withId("-1")
                            .withTitle(getString(R.string.select_category)));

                    for (int i = 0; i < length1; i++) {

                        Gson gson = new Gson();

                        StuffCategory stuffCategory = gson.fromJson(
                                stuffCategoryArray.getJSONObject(i).toString(), StuffCategory.class);

                        stuffCategoryList.add(stuffCategory);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case GET_INTEREST_LIST:

                try {

                    getUserTradeStuffList();

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        InterestItem interestItem = new InterestItem().withTitle(data.getString(i))
                                .withIsSelected(false);
                        interestList.add(interestItem);

                    }

                    interestListAdapter.notifyDataSetChanged();

                    if (interestList.isEmpty()) {
                        showInterestEmptyView();
                    } else {
                        hideInterestEmptyView();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;

            case GET_TRADE_STUFF:

                try {

                    dismissProgressBar();

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        UserStuff stuff = gson.fromJson(data.getJSONObject(i).toString(), UserStuff.class);
                        stuff.setIsSelected(false);
                        tradeStuffList.add(stuff);

                    }

                    tradeStuffListAdapter.notifyDataSetChanged();

                    if (tradeStuffList.isEmpty()) {
                        showStuffEmptyView();
                    } else {
                        hideStuffEmptyView();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case GET_PAYMENT_OPTION_LIST:

                try {

                    getStuffCategoryList();

                    JSONArray data = Data.getJSONArray("data");

                    int length = data.length();

                    Gson gson = new Gson();

                    List<String> paymentIds = Arrays.asList(stuffFeedItem.getPaymentAccepted().split(","));

                    StringBuilder temp = new StringBuilder();

                    for (int i = 0; i < length; i++) {

                        PaymentOption paymentOption = gson.fromJson(data.getJSONObject(i).toString(), PaymentOption.class);
                        paymentOption.setIsSelected(false);

                        for (String id : paymentIds) {
                            if (paymentOption.getId().equals(id.trim())) {
                                temp.append(String.format("%s, ", paymentOption.getTitle()));
                                break;
                            }
                        }
                    }

                    if (temp.length() > 1) {
                        tv_payment_options.setText(String.format("%s %s!",
                                getString(R.string.this_user_accepts), temp.substring(0, temp.length() - 2)));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


            case TRADE_SEND_MESSAGE:

                dismissProgressBar();
                finish();

                break;

            case SHARE_SEND_MESSAGE:

                dismissProgressBar();
                finish();

                break;

        }

    }

    @Override
    public void Success1(String method, JSONObject Data) {

    }

    private void showStuffEmptyView() {
        stuff_recyclerView.setVisibility(View.GONE);
        tv_stuff_empty_view.setVisibility(View.VISIBLE);
    }

    private void hideStuffEmptyView() {
        tv_stuff_empty_view.setVisibility(View.GONE);
        stuff_recyclerView.setVisibility(View.VISIBLE);
    }

    private void showInterestEmptyView() {
        interest_recyclerView.setVisibility(View.GONE);
        tv_interest_empty_view.setVisibility(View.VISIBLE);
    }

    private void hideInterestEmptyView() {
        tv_interest_empty_view.setVisibility(View.GONE);
        interest_recyclerView.setVisibility(View.VISIBLE);
    }

}
