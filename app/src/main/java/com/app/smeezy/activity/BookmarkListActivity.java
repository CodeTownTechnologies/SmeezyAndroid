package com.app.smeezy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.adapter.BookmarkListAdapter;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.interfacess.TradeFeedListener;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.FeedItem;
import com.app.smeezy.responsemodels.StuffCategory;
import com.app.smeezy.responsemodels.StuffFeedItem;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.ItemOffsetDecoration;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarkListActivity extends BaseActivity implements TradeFeedListener, IRequestView {

    private static final String GET_BOOKMARK_LIST_WITH_FILTER = "get_bookmark_list";
    private static final String GET_CATEGORY_LIST = "get_category_list";
    private static final String UNFOLLOW_USER = "unfollow_user";
    private static final String REMOVE_BOOKMARK = "remove_bookmark";
    private static final String CHANGE_CATEGORY = "change_category";

    private static final int REPORT_REQUEST = 4572;

    @BindView(R.id.bookmark_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private Dialog changeCategoryDialog;
    private ArrayList<StuffFeedItem> feedList = new ArrayList<>();
    private ArrayList<StuffCategory> stuffChangeCategoryList = new ArrayList<>();
    private ArrayAdapter<StuffCategory> changeCategorySpinnerAdapter;
    private BookmarkListAdapter mAdapter;
    private int page = 1;
    private boolean noMoreFeedItems = false;
    private String categorySlug = "", searchKeyword = "", distance = "";
    private int unfollowUserPosition, removeBookmarkPosition,
            changeCategoryPosition, reportPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_list);
        ButterKnife.bind(this);


        mContext = this;
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(), this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == feedList.size()){
                    return 2;
                }else {
                    return 1;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.dimen_5dp);
        recyclerView.addItemDecoration(itemDecoration);

        mAdapter = new BookmarkListAdapter(mContext, recyclerView, feedList, this);

        recyclerView.setAdapter(mAdapter);

        getFeedListWithFilter(String.valueOf(page));


        getCategoryList();

        setUpToolbar();
    }



    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        TextView activity_title = (TextView) findViewById(R.id.activity_title);
        activity_title.setText(getResources().getString(R.string.title_activity_bookmark_list));
    }

    private void getFeedListWithFilter(String page) {
        if (!noMoreFeedItems) {
            if (cd.isConnectingToInternet()) {
                requestPresenter.getBookmarkList(GET_BOOKMARK_LIST_WITH_FILTER,
                        PreferenceUtils.getId(mContext), page, categorySlug,
                        searchKeyword, distance);
            } else {
                Utills.noInternetConnection(mContext);
            }
        }
    }

    private void getCategoryList() {

        if (cd.isConnectingToInternet()) {
            requestPresenter.getStuffList(GET_CATEGORY_LIST, PreferenceUtils.getId(mContext));
        } else {
            Utills.noInternetConnection(mContext);
        }

    }

    @Override
    public void onBottomReached() {
        page++;
        getFeedListWithFilter(String.valueOf(page));
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

    }

    @Override
    public void onRemoveBookmark(int position, StuffFeedItem stuffFeedItem) {
        if (cd.isConnectingToInternet()){
            removeBookmarkPosition = position;
            startProgressBar();
            requestPresenter.addRemoveBookmark(REMOVE_BOOKMARK, PreferenceUtils.getId(mContext),
                    stuffFeedItem.getActivityId(), stuffFeedItem.getId());
        }else {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case REPORT_REQUEST:

                if (resultCode == Activity.RESULT_OK){

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
        switch (method){


            case UNFOLLOW_USER:

                showAlert( getString(R.string.app_name), message);

                break;


            case REMOVE_BOOKMARK:

                showAlert(getString(R.string.app_name), message);

                break;

            case CHANGE_CATEGORY:

                showAlert(getString(R.string.app_name), message);

                break;

        }
    }

    @Override
    public void Failed1(String message) {

    }

    @Override
    public void Success(String method, JSONObject Data) {

        switch (method){

            case GET_BOOKMARK_LIST_WITH_FILTER:

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


            case GET_CATEGORY_LIST:

                try {

                    JSONObject data = Data.getJSONObject("data");

                    JSONArray stuffCategoryArray = data.optJSONArray("category_list");

                    int length = stuffCategoryArray.length();


                    for (int i = 0; i < length; i++) {

                        Gson gson = new Gson();

                        StuffCategory stuffCategory = gson.fromJson(
                                stuffCategoryArray.getJSONObject(i).toString(), StuffCategory.class);

                        stuffChangeCategoryList.add(stuffCategory);

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


            case REMOVE_BOOKMARK:

                feedList.remove(removeBookmarkPosition);
                mAdapter.notifyItemRemoved(removeBookmarkPosition);

                break;

            case CHANGE_CATEGORY:

                if (changeCategoryDialog != null && changeCategoryDialog.isShowing()){
                    changeCategoryDialog.dismiss();
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

                if (message.isEmpty()){
                    showAlert(getString(R.string.app_name), getString(R.string.message_required));
                }else if (cd.isConnectingToInternet()) {
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
}
