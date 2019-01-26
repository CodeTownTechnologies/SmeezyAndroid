package com.app.smeezy.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.HomeActivity;
import com.app.smeezy.activity.NotificationsActivity;
import com.app.smeezy.activity.ReportActivity;
import com.app.smeezy.activity.SearchActivity;
import com.app.smeezy.activity.TradeBorrowActivity;
import com.app.smeezy.adapter.AllItemsFeedAdapter;
import com.app.smeezy.interfacess.AllItemsFeedListener;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.FeedItem;
import com.app.smeezy.responsemodels.OrderByItem;
import com.app.smeezy.responsemodels.StuffCategory;
import com.app.smeezy.responsemodels.StuffFeedItem;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.ItemOffsetDecoration;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */

public class AllItemsFeedFragment extends BaseFragment implements View.OnClickListener, IRequestView,
        AllItemsFeedListener {

    private static final String GET_STUFF_FEED_LIST_WITH_FILTER = "get_filtered_feed_list";
    private static final String COMPOSE_MESSAGE = "compose_message";
    private static final String GET_CATEGORY_LIST = "get_category_list";
    private static final String UNFOLLOW_USER = "unfollow_user";
    private static final String BOOKMARK = "add_bookmark";
    private static final String REMOVE_BOOKMARK = "remove_bookmark";
    private static final String CHANGE_CATEGORY = "change_category";
    private static final String SAVE_STUFF_SEARCH_DISTANCE = "save_stuff_search_distance";

    private static final int REPORT_REQUEST = 4564;


    @BindView(R.id.trade_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<StuffFeedItem> feedList = new ArrayList<>();

    private AllItemsFeedAdapter mAdapter;
    private Dialog filterDialog, changeCategoryDialog, orderByDialog;
    private int page = 1;
    private boolean noMoreFeedItems = false;
    private Dialog searchDistanceDialog, messageDialog;
    private String categorySlug = "", searchKeyword = "", orderBy = "date";
    private ArrayList<StuffCategory> stuffCategoryList = new ArrayList<>();
    private ArrayList<OrderByItem> orderByList = new ArrayList<>();
    private ArrayList<StuffCategory> stuffChangeCategoryList = new ArrayList<>();
    private ArrayAdapter<StuffCategory> filterSpinnerAdapter, changeCategorySpinnerAdapter;
    private ArrayAdapter<OrderByItem> orderByAdapter;
    private int unfollowUserPosition, bookmarkPosition, removeBookmarkPosition,
            changeCategoryPosition, reportPosition;
    private HashMap<String, Boolean> optionValues = new HashMap<>();
    private String options = "";

    private BroadcastReceiver unfollowReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String userId = intent.getStringExtra("userId");

            ListIterator<StuffFeedItem> iterator = feedList.listIterator();

            while (iterator.hasNext()) {
                if (iterator.next().getUserId().equals(userId)) {
                    iterator.remove();
                }
            }

            mAdapter.notifyDataSetChanged();
        }
    };

    private BroadcastReceiver stuffDistanceChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            feedList.clear();
            page = 1;
            noMoreFeedItems = false;

            if (cd.isConnectingToInternet()) {
                startProgressBar();
                getFeedListWithFilter(String.valueOf(page));
            } else {
                Utills.noInternetConnection(mContext);
            }
        }
    };

    public AllItemsFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trade_feed, container, false);
        ButterKnife.bind(this, view);


        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == feedList.size()) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.dimen_5dp);
        recyclerView.addItemDecoration(itemDecoration);

        mAdapter = new AllItemsFeedAdapter(mContext, recyclerView, feedList, this);

        recyclerView.setAdapter(mAdapter);

        setOrderByList();
        resetOptionValues();

        getFeedListWithFilter(String.valueOf(page));

        stuffCategoryList.add(new StuffCategory().withId("-1")
                .withTitle(getString(R.string.all_categories))
                .withSlug(""));

        getCategoryList();

        getActivity().registerReceiver(unfollowReceiver,
                new IntentFilter(StaticData.UNFOLLOW_BROADCAST));

        getActivity().registerReceiver(stuffDistanceChangedReceiver,
                new IntentFilter(StaticData.STUFF_SEARCH_DISTANCE_CHANGED));


        return view;
    }

    private void getFeedListWithFilter(String page) {
        if (!noMoreFeedItems) {
            if (cd.isConnectingToInternet()) {
                requestPresenter.getStuffFeedList(GET_STUFF_FEED_LIST_WITH_FILTER,
                        PreferenceUtils.getId(mContext), "", page, categorySlug,
                        searchKeyword, String.valueOf(PreferenceUtils.getItemSearchDistance(mContext)),
                        orderBy, options);
            } else {
                Utills.noInternetConnection(mContext);
            }
        }
    }


    @Override
    public void onBottomReached() {
        page++;
        getFeedListWithFilter(String.valueOf(page));
    }


    @Override
    public void onAskToBorrow(StuffFeedItem stuffFeedItem) {

        Intent intent = new Intent(mContext, TradeBorrowActivity.class);
        intent.putExtra("stuffFeedItem", stuffFeedItem);
        intent.putExtra("from", "share");
        startActivity(intent);
    }

    @Override
    public void onOfferTrade(StuffFeedItem stuffFeedItem) {

        Intent intent = new Intent(mContext, TradeBorrowActivity.class);
        intent.putExtra("stuffFeedItem", stuffFeedItem);
        intent.putExtra("from", "trade");
        startActivity(intent);

    }


    @Override
    public void onUnfollowUser(int position, StuffFeedItem stuffFeedItem) {

        showUnfollowUserAlert(position, stuffFeedItem);

    }

    @Override
    public void onChangeCategory(int position, StuffFeedItem stuffFeedItem) {

        showChangeCategoryDialog(position, stuffFeedItem);

    }

    @Override
    public void onBookmark(int position, StuffFeedItem stuffFeedItem) {

        if (cd.isConnectingToInternet()) {
            bookmarkPosition = position;
            startProgressBar();
            requestPresenter.addRemoveBookmark(BOOKMARK, PreferenceUtils.getId(mContext),
                    stuffFeedItem.getActivityId(), stuffFeedItem.getId());
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onRemoveBookmark(int position, StuffFeedItem stuffFeedItem) {
        if (cd.isConnectingToInternet()) {
            removeBookmarkPosition = position;
            startProgressBar();
            requestPresenter.addRemoveBookmark(REMOVE_BOOKMARK, PreferenceUtils.getId(mContext),
                    stuffFeedItem.getActivityId(), stuffFeedItem.getId());
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onReportPost(int position, StuffFeedItem stuffFeedItem) {

        reportPosition = position;

        Intent intent = new Intent(mContext, ReportActivity.class);
        FeedItem feedItem = new FeedItem().withActivityId(stuffFeedItem.getActivityId())
                .withId(stuffFeedItem.getId());
        intent.putExtra("feedItem", feedItem);
        intent.putExtra("taskType", "stuff");
        startActivityForResult(intent, REPORT_REQUEST);

    }

    private void getCategoryList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getStuffList(GET_CATEGORY_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }

    }


    @OnClick({R.id.tv_trade_set_distance, R.id.tv_trade_filter, R.id.tv_trade_order_by})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_trade_set_distance:

                showSetDistanceDialog();

                break;

            case R.id.tv_trade_filter:

                showFilterDialog();

                break;

            case R.id.tv_trade_order_by:

                showOrderByDialog();

                break;

        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_home_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.home_search:

                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);

                break;

            case R.id.friend_request:

                Intent intent1 = new Intent(mContext, NotificationsActivity.class);
                startActivity(intent1);

                break;

            /*case R.id.home_filter:

                showFilterDialog();

                break;*/


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getActivity().unregisterReceiver(unfollowReceiver);
        getActivity().unregisterReceiver(stuffDistanceChangedReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REPORT_REQUEST:

                if (resultCode == Activity.RESULT_OK) {

                    feedList.remove(reportPosition);
                    mAdapter.notifyItemRemoved(reportPosition);
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

        switch (method) {


            case GET_STUFF_FEED_LIST_WITH_FILTER:

                showAlert(mContext, getString(R.string.app_name), message);

                break;


            case COMPOSE_MESSAGE:

                showAlert(mContext, getString(R.string.app_name), message);

                break;


            case UNFOLLOW_USER:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case BOOKMARK:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case REMOVE_BOOKMARK:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case CHANGE_CATEGORY:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case SAVE_STUFF_SEARCH_DISTANCE:

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


            case GET_STUFF_FEED_LIST_WITH_FILTER:

                try {

                    JSONArray data = Data.getJSONArray("data");
                    int length = data.length();

                    if (length == 0) {

                        noMoreFeedItems = true;
                        mAdapter.stopLoader();
                        mAdapter.notifyDataSetChanged();

                        if (feedList.isEmpty()) {
                            showEmptyView();
                        } else {
                            hideEmptyView();
                        }

                        break;

                    }

                    Gson gson = new Gson();

                    for (int i = 0; i < length; i++) {


                        StuffFeedItem feedItem = gson.fromJson(data.getJSONObject(i).toString(),
                                StuffFeedItem.class);

                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date inputDate = null;


                        try {
                            inputDate = inputFormat.parse(feedItem.getAddedOn());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String outputDate;
                        String outputTime;
                        String outputDateTime = "";

                        if (inputDate != null) {
                            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd");
                            SimpleDateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a");
                            outputDate = outputDateFormat.format(inputDate);
                            outputTime = outputTimeFormat.format(inputDate);
                            outputDateTime = String.format("%s, at %s", outputDate, outputTime);
                        }

                        feedItem.setAddedOn(outputDateTime);
                        feedList.add(feedItem);
                    }

                    mAdapter.notifyDataSetChanged();
                    mAdapter.setLoaded();

                    if (feedList.isEmpty()) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case COMPOSE_MESSAGE:

                if (messageDialog != null && messageDialog.isShowing()) {
                    messageDialog.dismiss();
                }

                break;

            case GET_CATEGORY_LIST:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    JSONArray stuffCategoryArray = data.optJSONArray("category_list");

                    int length = stuffCategoryArray.length();


                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        StuffCategory stuffCategory = gson.fromJson(
                                stuffCategoryArray.getJSONObject(i).toString(), StuffCategory.class);

                        stuffCategoryList.add(stuffCategory);
                        stuffChangeCategoryList.add(stuffCategory);

                    }

                    if (filterDialog != null && filterDialog.isShowing()) {
                        filterSpinnerAdapter.notifyDataSetChanged();
                    }

                    if (changeCategoryDialog != null && changeCategoryDialog.isShowing()) {
                        changeCategorySpinnerAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


            case UNFOLLOW_USER:

                String userId = feedList.get(unfollowUserPosition).getUserId();

                Intent unfollowBroadcastIntent = new Intent(StaticData.UNFOLLOW_BROADCAST);
                unfollowBroadcastIntent.putExtra("userId", userId);

                mContext.sendBroadcast(unfollowBroadcastIntent);

                ListIterator<StuffFeedItem> iterator = feedList.listIterator();

                while (iterator.hasNext()) {
                    if (iterator.next().getUserId().equals(userId)) {
                        iterator.remove();
                    }
                }

                mAdapter.notifyDataSetChanged();


                break;

            case BOOKMARK:

                feedList.get(bookmarkPosition).setIsBookmark("1");
                mAdapter.notifyItemChanged(bookmarkPosition);

                break;

            case REMOVE_BOOKMARK:

                feedList.get(removeBookmarkPosition).setIsBookmark("0");
                mAdapter.notifyItemChanged(removeBookmarkPosition);

                break;

            case CHANGE_CATEGORY:

                if (changeCategoryDialog != null && changeCategoryDialog.isShowing()) {
                    changeCategoryDialog.dismiss();
                }

                break;

            case SAVE_STUFF_SEARCH_DISTANCE:

                try {

                    JSONObject request = Data.getJSONObject("req");

                    int distance = request.optInt("search_distance");
                    PreferenceUtils.setItemSearchDistance(mContext, distance);

                    if (searchDistanceDialog != null && searchDistanceDialog.isShowing()) {
                        searchDistanceDialog.dismiss();
                    }

                    mContext.sendBroadcast(new Intent(StaticData.STUFF_SEARCH_DISTANCE_CHANGED));

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

    private void showMessageDialog(final StuffFeedItem stuffFeedItem) {

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
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.message_required));
                } else {

                    if (cd.isConnectingToInternet()) {
                        /*startProgressBar();
                        requestPresenter.composeMessageStuff(COMPOSE_MESSAGE, PreferenceUtils.getId(mContext),
                                stuffFeedItem.getUserId(), message, "trade",
                                stuffFeedItem.getId());*/
                    } else {
                        Utills.noInternetConnection(mContext);
                    }

                }

            }
        });

        messageDialog.show();

    }

    private void showSetDistanceDialog() {

        searchDistanceDialog = new Dialog(mContext);
        searchDistanceDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        searchDistanceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchDistanceDialog.setCanceledOnTouchOutside(false);
        searchDistanceDialog.setContentView(R.layout.custom_set_distance_dialog);
        searchDistanceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_submit = (Button) searchDistanceDialog.findViewById(R.id.btn_search_distance_submit);
        Button btn_cancel = (Button) searchDistanceDialog.findViewById(R.id.btn_search_distance_cancel);

        final EditText et_distance = (EditText) searchDistanceDialog.findViewById(R.id.et_distance);

        et_distance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchDistanceDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        et_distance.requestFocus();

        et_distance.setText(String.valueOf(PreferenceUtils.getItemSearchDistance(mContext)));
        et_distance.setSelection(String.valueOf(PreferenceUtils.getItemSearchDistance(mContext)).length());

        /*TextView tv_min = (TextView) searchDistanceDialog.findViewById(R.id.tv_search_distance_min);
        TextView tv_max = (TextView) searchDistanceDialog.findViewById(R.id.tv_search_distance_max);
        final TextView tv_value = (TextView) searchDistanceDialog.findViewById(R.id.tv_search_distance);

        final CrystalSeekbar seekbar = (CrystalSeekbar) searchDistanceDialog.findViewById(R.id.search_distance_seekbar);

        seekbar.setSteps(1);

        seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                tv_value.setText(value.toString());
            }
        });

        int minValue = Math.round(seekbar.getMinValue());
        int maxValue = Math.round(seekbar.getMaxValue());

        tv_min.setText(String.valueOf(minValue));
        tv_max.setText(String.valueOf(maxValue));


        seekbar.setMinStartValue((float) Integer.parseInt(distance)).apply();
        tv_value.setText(distance);*/

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utills.hideSoftKeyboard((HomeActivity) mContext);
                searchDistanceDialog.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //distance = tv_value.getText().toString().trim();
                String distance = et_distance.getText().toString().trim();
                if (distance.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.distance_required));
                } else if (Integer.parseInt(distance) <= 0) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.distance_must_be_greater_than_0));
                } else if (cd.isConnectingToInternet()) {
                    Utills.hideSoftKeyboard((HomeActivity) mContext);
                    startProgressBar();
                    requestPresenter.saveStuffSearchDistance(SAVE_STUFF_SEARCH_DISTANCE,
                            PreferenceUtils.getId(mContext), Integer.parseInt(distance));
                } else {
                    Utills.noInternetConnection(mContext);
                }

            }
        });

        searchDistanceDialog.show();

    }

    private void showFilterDialog() {

        filterDialog = new Dialog(mContext);
        filterDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setCanceledOnTouchOutside(false);
        filterDialog.setContentView(R.layout.custom_trade_share_filter_dialog);
        filterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_submit = (Button) filterDialog.findViewById(R.id.btn_filter_submit);
        ImageView img_cross = (ImageView) filterDialog.findViewById(R.id.img_filter_cross);

        final Spinner spinner_category = (Spinner) filterDialog.findViewById(R.id.spinner_stuff_category);
        final EditText et_search = (EditText) filterDialog.findViewById(R.id.et_search);

        final CheckBox cb_borrow = (CheckBox) filterDialog.findViewById(R.id.cb_filter_borrow);
        final CheckBox cb_trade = (CheckBox) filterDialog.findViewById(R.id.cb_filter_trade);
        final CheckBox cb_buy = (CheckBox) filterDialog.findViewById(R.id.cb_filter_buy);
        final CheckBox cb_rent = (CheckBox) filterDialog.findViewById(R.id.cb_filter_rent);

        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    filterDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        et_search.requestFocus();

        et_search.setText(searchKeyword);
        et_search.setSelection(searchKeyword.length());

        filterSpinnerAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, stuffCategoryList);

        spinner_category.setAdapter(filterSpinnerAdapter);

        int selection = 0;

        for (int i = 0; i < stuffCategoryList.size(); i++) {
            if (stuffCategoryList.get(i).getSlug().equals(categorySlug)) {
                selection = i;
                break;
            }
        }

        spinner_category.setSelection(selection);

        if (optionValues.get("share")) {
            cb_borrow.setChecked(true);
        }

        if (optionValues.get("trade")) {
            cb_trade.setChecked(true);
        }

        if (optionValues.get("buy")) {
            cb_buy.setChecked(true);
        }

        if (optionValues.get("rent")) {
            cb_rent.setChecked(true);
        }

        filterDialog.show();

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filterDialog.dismiss();

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StuffCategory stuffCategory = (StuffCategory) spinner_category.getSelectedItem();

                if (stuffCategory.getId().equals("-1")) {
                    categorySlug = "";
                } else {
                    categorySlug = stuffCategory.getSlug();
                }

                searchKeyword = et_search.getText().toString().trim();

                feedList.clear();
                mAdapter.notifyDataSetChanged();
                page = 1;
                noMoreFeedItems = false;

                options = "";

                resetOptionValues();

                if (cb_borrow.isChecked()) {
                    options = String.format("%s", "share");
                    optionValues.put("share", true);
                }

                if (cb_trade.isChecked()) {
                    if (options.isEmpty()) {
                        options = String.format("%s", "trade");
                    } else {
                        options = String.format("%s,%s", options, "trade");
                    }
                    optionValues.put("trade", true);
                }

                if (cb_buy.isChecked()) {
                    if (options.isEmpty()) {
                        options = String.format("%s", "buy");
                    } else {
                        options = String.format("%s,%s", options, "buy");
                    }
                    optionValues.put("buy", true);
                }

                if (cb_rent.isChecked()) {
                    if (options.isEmpty()) {
                        options = String.format("%s", "rent");
                    } else {
                        options = String.format("%s,%s", options, "rent");
                    }
                    optionValues.put("rent", true);
                }
                filterDialog.dismiss();

                if (cd.isConnectingToInternet()) {
                    startProgressBar();
                    getFeedListWithFilter(String.valueOf(page));
                } else {
                    Utills.noInternetConnection(mContext);
                }
            }
        });

    }

    private void setOrderByList() {

        orderByList.add(new OrderByItem().withId("trending").withTitle("Trending Items"));
        orderByList.add(new OrderByItem().withId("rating").withTitle("Most Reviewed"));
        orderByList.add(new OrderByItem().withId("date").withTitle("Sort: Newest items"));
        orderByList.add(new OrderByItem().withId("name-asc").withTitle("Item Name: A to Z"));
        orderByList.add(new OrderByItem().withId("name-desc").withTitle("Item Name: Z to A"));
        orderByList.add(new OrderByItem().withId("distance-asc").withTitle("Distance: low to high"));
        orderByList.add(new OrderByItem().withId("distance-desc").withTitle("Distance: high to low"));

    }


    private void showOrderByDialog() {

        orderByDialog = new Dialog(mContext);
        orderByDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        orderByDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        orderByDialog.setCanceledOnTouchOutside(false);
        orderByDialog.setContentView(R.layout.custom_trade_share_order_by_dialog);
        orderByDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_submit = (Button) orderByDialog.findViewById(R.id.btn_order_by_submit);
        ImageView img_cross = (ImageView) orderByDialog.findViewById(R.id.img_filter_cross);

        final Spinner spinner_order_by = (Spinner) orderByDialog.findViewById(R.id.spinner_order_by);

        orderByAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, orderByList);

        spinner_order_by.setAdapter(orderByAdapter);

        int selection = 0;

        for (int i = 0; i < orderByList.size(); i++) {
            if (orderByList.get(i).getId().equals(orderBy)) {
                selection = i;
                break;
            }
        }

        spinner_order_by.setSelection(selection);

        orderByDialog.show();

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                orderByDialog.dismiss();

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderByItem orderByItem = (OrderByItem) spinner_order_by.getSelectedItem();

                orderBy = orderByItem.getId();

                feedList.clear();
                mAdapter.notifyDataSetChanged();
                page = 1;
                noMoreFeedItems = false;

                orderByDialog.dismiss();

                if (cd.isConnectingToInternet()) {
                    startProgressBar();
                    getFeedListWithFilter(String.valueOf(page));
                } else {
                    Utills.noInternetConnection(mContext);
                }
            }
        });

    }

    public void showUnfollowUserAlert(final int position, final StuffFeedItem stuffFeedItem) {

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
        txt_message.setText(getResources().getString(R.string.are_your_sure_you_want_to_unfollow_this_user));

        dialog.show();


        txt_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    unfollowUserPosition = position;
                    startProgressBar();
                    requestPresenter.followUnfollowUser(UNFOLLOW_USER, PreferenceUtils.getId(mContext),
                            stuffFeedItem.getUserId());

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

    private void showChangeCategoryDialog(final int position, final StuffFeedItem stuffFeedItem) {

        changeCategoryDialog = new Dialog(mContext);
        changeCategoryDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        changeCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeCategoryDialog.setCanceledOnTouchOutside(false);
        changeCategoryDialog.setContentView(R.layout.custom_change_category_dialog);
        changeCategoryDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_submit = (Button) changeCategoryDialog.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button) changeCategoryDialog.findViewById(R.id.btn_cancel);

        final Spinner spinner_category = (Spinner) changeCategoryDialog.findViewById(R.id.spinner_change_category);
        final EditText et_message = (EditText) changeCategoryDialog.findViewById(R.id.et_message_text);

        et_message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    changeCategoryDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        et_message.requestFocus();

        changeCategorySpinnerAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, stuffChangeCategoryList);

        spinner_category.setAdapter(changeCategorySpinnerAdapter);

        int selection = 0;

        for (int i = 0; i < stuffChangeCategoryList.size(); i++) {
            if (stuffChangeCategoryList.get(i).getSlug().equals(stuffFeedItem.getCategorySlug())) {
                selection = i;
                break;
            }
        }

        spinner_category.setSelection(selection);

        changeCategoryDialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeCategoryDialog.dismiss();

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StuffCategory stuffCategory = (StuffCategory) spinner_category.getSelectedItem();

                String message = et_message.getText().toString().trim();

                if (message.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.message_required));
                } else if (cd.isConnectingToInternet()) {
                    changeCategoryPosition = position;
                    startProgressBar();
                    requestPresenter.changeFeedCategory(CHANGE_CATEGORY, PreferenceUtils.getId(mContext),
                            stuffFeedItem.getActivityId(), stuffFeedItem.getId(),
                            stuffCategory.getId(), message);
                } else {
                    Utills.noInternetConnection(mContext);
                }
            }
        });

    }

    private void resetOptionValues() {
        optionValues.put("share", false);
        optionValues.put("trade", false);
        optionValues.put("buy", false);
        optionValues.put("rent", false);
    }

}
