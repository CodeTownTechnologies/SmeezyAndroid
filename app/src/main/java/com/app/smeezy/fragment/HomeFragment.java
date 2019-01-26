package com.app.smeezy.fragment;


import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.CommentActivity;
import com.app.smeezy.activity.EditPostActivity;
import com.app.smeezy.activity.HomeActivity;
import com.app.smeezy.activity.NotificationsActivity;
import com.app.smeezy.activity.PostActivity;
import com.app.smeezy.activity.ProfileActivity;
import com.app.smeezy.activity.ReportActivity;
import com.app.smeezy.activity.SearchActivity;
import com.app.smeezy.adapter.HomeAdapter;
import com.app.smeezy.interfacess.HomeFeedListener;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.FeedItem;
import com.app.smeezy.responsemodels.Info;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.app.smeezy.utills.Utills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, IRequestView, HomeFeedListener {


    private static final String GET_FEED_LIST_WITH_FILTER = "get_filtered_feed_list";
    private static final String LIKE_POST = "like_post";
    private static final String SAVE_SEARCH_DISTANCE = "save_search_distance";
    private static final String DELETE_POST = "delete_post";
    private static final String UNFOLLOW_USER = "unfollow_user";
    private static final String FOLLOW_POST = "follow_post";
    private static final String UNFOLLOW_POST = "unfollow_post";


    private static final int COMMENT_REQUEST = 221;
    private static final int PROFILE_REQUEST = 2201;
    private static final int REPORT_REQUEST = 1020;

    @BindView(R.id.et_home_message)
    EditText et_home_message;

    @BindView(R.id.tv_home_set_distance)
    TextView tv_home_set_distance;

    @BindView(R.id.home_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    @BindView(R.id.ll_set_distance)
    LinearLayout ll_set_distance;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<FeedItem> feedList = new ArrayList<>();

    private HomeAdapter mAdapter;
    private Dialog filterDialog;
    private int page = 1;
    private String feedType = "";
    private int likePosition, commentPosition, deletePosition, unfollowUserPosition,
            followPostPosition, unfollowPostPosition, reportPosition;
    private boolean noMoreFeedItems = false;
    private HashMap<String, Boolean> filterValues = new HashMap<>();
    private String postPrivacy = "";
    private Dialog searchDistanceDialog;

    private BroadcastReceiver unfollowReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String userId = intent.getStringExtra("userId");

            ListIterator<FeedItem> iterator = feedList.listIterator();

            while (iterator.hasNext()) {
                if (iterator.next().getUserId().equals(userId)) {
                    iterator.remove();
                }
            }

            mAdapter.notifyDataSetChanged();
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            postPrivacy = getArguments().getString("postPrivacy");
        }

        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new HomeAdapter(mContext, recyclerView, feedList, this, postPrivacy);

        recyclerView.setAdapter(mAdapter);

        if (postPrivacy.equals("community")) {
            et_home_message.setHint(getString(R.string.whats_happening_in_community));
            ll_set_distance.setVisibility(View.VISIBLE);
            /*if (PreferenceUtils.getIsFirstTime(mContext)) {
                showSetDistanceDialog();
                PreferenceUtils.setIsFirstTime(mContext, false);
            }*/
        } else if (postPrivacy.equals("friends")) {
            et_home_message.setHint(getString(R.string.post_things_here_just_for_fun));
            ll_set_distance.setVisibility(View.GONE);
        }

        resetFilterValues();

        getFeedListWithFilter(feedType, String.valueOf(page));

        getActivity().registerReceiver(unfollowReceiver, new IntentFilter(StaticData.UNFOLLOW_BROADCAST));

        return view;
    }

    private void getFeedListWithFilter(String type, String page) {
        if (!noMoreFeedItems) {
            if (cd.isConnectingToInternet()) {
                if (postPrivacy.equals("community")) {
                    requestPresenter.getCommunityFeedList(GET_FEED_LIST_WITH_FILTER, PreferenceUtils.getId(mContext), type, page);
                } else if (postPrivacy.equals("friends")) {
                    requestPresenter.getFriendFeedList(GET_FEED_LIST_WITH_FILTER, PreferenceUtils.getId(mContext), type, page);
                }
            } else {
                Utills.noInternetConnection(mContext);
            }
        }
    }

    @Override
    public void onLike(int position, String activityId, String feedId) {
        if (cd.isConnectingToInternet()) {
            this.likePosition = position;
            startProgressBar();
            requestPresenter.likeUnlikePost(LIKE_POST, PreferenceUtils.getId(mContext), activityId, feedId);
        } else {
            Utills.noInternetConnection(mContext);
        }
    }

    @Override
    public void onComment(int position, FeedItem feedItem) {
        commentPosition = position;
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra("feedItem", feedItem);
        startActivityForResult(intent, COMMENT_REQUEST);
    }

    @Override
    public void onDeletePost(int position, String feedId) {
        showDeleteAlert(position, feedId);
    }

    @Override
    public void onEditPost(int position, FeedItem feedItem) {
        Intent intent = new Intent(mContext, EditPostActivity.class);
        intent.putExtra(getString(R.string.activity_tag), getString(R.string.fragment_home_tag));
        intent.putExtra("feedItem", feedItem);
        startActivity(intent);
    }

    @Override
    public void onBottomReached() {
        page++;
        getFeedListWithFilter(feedType, String.valueOf(page));
    }


    @Override
    public void onUnfollowUser(int position, FeedItem feedItem) {

        showUnfollowUserAlert(position, feedItem);

    }

    @Override
    public void onFollowPost(int position, FeedItem feedItem) {

        if (cd.isConnectingToInternet()) {
            followPostPosition = position;
            startProgressBar();
            requestPresenter.followUnfollowPost(FOLLOW_POST, PreferenceUtils.getId(mContext),
                    feedItem.getActivityId(), feedItem.getId());
        } else {
            Utills.noInternetConnection(mContext);
        }

    }

    @Override
    public void onUnfollowPost(int position, FeedItem feedItem) {

        showUnfollowPostAlert(position, feedItem);

    }

    @Override
    public void onReportPost(int position, FeedItem feedItem) {
        reportPosition = position;
        Intent intent = new Intent(mContext, ReportActivity.class);
        intent.putExtra("feedItem", feedItem);
        intent.putExtra("taskType", "post");
        startActivityForResult(intent, REPORT_REQUEST);
    }

    @Override
    public void onProfileClick(int position, FeedItem feedItem) {
        if (feedItem.getUserType().equals(StaticData.TYPE_MEMBER)) {

            if (PreferenceUtils.getId(mContext).equals(feedItem.getUserId())) {

                ((HomeActivity) mContext).loadProfileFragment();

            } else {

                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("memberId", feedItem.getUserId());
                startActivityForResult(intent, PROFILE_REQUEST);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case COMMENT_REQUEST:
                if (resultCode == Activity.RESULT_OK) {

                    int totalComments = data.getIntExtra("totalComments",
                            feedList.get(commentPosition).getTotalComments());

                    feedList.get(commentPosition).setTotalComments(totalComments);
                    mAdapter.notifyDataSetChanged();

                }

                break;

            case PROFILE_REQUEST:

                if (resultCode == Activity.RESULT_OK) {

                    String userId = data.getStringExtra("userId");
                    String isUnfollowUser = data.getStringExtra("isUnfollowUser");

                    if (isUnfollowUser.equals("1")) {

                        ListIterator<FeedItem> iterator = feedList.listIterator();

                        while (iterator.hasNext()) {
                            if (iterator.next().getUserId().equals(userId)) {
                                iterator.remove();
                            }
                        }

                        mAdapter.notifyDataSetChanged();

                    }

                }

                break;


            case REPORT_REQUEST:

                if (resultCode == Activity.RESULT_OK) {

                    feedList.remove(reportPosition);
                    mAdapter.notifyItemRemoved(reportPosition);
                }

                break;
        }

    }


    @OnClick({R.id.et_home_message, R.id.tv_home_set_distance, R.id.tv_home_filter})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.et_home_message:

                Intent intent = new Intent(mContext, PostActivity.class);
                intent.putExtra("groupPost", false);
                intent.putExtra("postPrivacy", postPrivacy);
                intent.putExtra(getString(R.string.activity_tag), getString(R.string.fragment_home_tag));
                startActivity(intent);

                break;

            case R.id.tv_home_set_distance:

                showSetDistanceDialog();

                break;


            case R.id.tv_home_filter:

                showFilterDialog();

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
    }

    private void showFilterDialog() {

        filterDialog = new Dialog(mContext);
        filterDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setCanceledOnTouchOutside(false);
        filterDialog.setContentView(R.layout.custom_home_filter_dialog);
        filterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button btn_submit = (Button) filterDialog.findViewById(R.id.btn_filter_submit);
        ImageView img_cross = (ImageView) filterDialog.findViewById(R.id.img_filter_cross);

        final CheckBox cb_image = (CheckBox) filterDialog.findViewById(R.id.cb_filter_image);
        final CheckBox cb_video = (CheckBox) filterDialog.findViewById(R.id.cb_filter_video);
        final CheckBox cb_link = (CheckBox) filterDialog.findViewById(R.id.cb_filter_link);
        final CheckBox cb_all = (CheckBox) filterDialog.findViewById(R.id.cb_filter_all);

        /*List<String> feedTypeList = Arrays.asList(feedType.split(","));

        for (String type : feedTypeList) {

            if (type.equals("image")) {
                cb_image.setChecked(true);
            } else if (type.equals("video")) {
                cb_video.setChecked(true);
            } else if (type.equals("link")) {
                cb_link.setChecked(true);
            } else if (type.equals("text")) {
                cb_all.setChecked(true);
            }

        }*/

        if (filterValues.get("image")) {
            cb_image.setChecked(true);
        }

        if (filterValues.get("video")) {
            cb_video.setChecked(true);
        }

        if (filterValues.get("link")) {
            cb_link.setChecked(true);
        }

        if (filterValues.get("all")) {
            cb_all.setChecked(true);
        }

        filterDialog.show();

        /*cb_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cb_image.setChecked(true);
                    cb_video.setChecked(true);
                    cb_link.setChecked(true);
                }else if (cb_image.isChecked() && cb_video.isChecked() && cb_link.isChecked()){
                    cb_image.setChecked(false);
                    cb_video.setChecked(false);
                    cb_link.setChecked(false);
                    cb_all.setChecked(false);
                }
            }
        });

        cb_image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && cb_video.isChecked() && cb_link.isChecked()){
                    cb_all.setChecked(true);
                }else {
                    cb_all.setChecked(false);
                }
            }
        });

        cb_video.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && cb_image.isChecked() && cb_link.isChecked()){
                    cb_all.setChecked(true);
                }else {
                    cb_all.setChecked(false);
                }
            }
        });

        cb_link.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && cb_image.isChecked() && cb_video.isChecked()){
                    cb_all.setChecked(true);
                }else {
                    cb_all.setChecked(false);
                }
            }
        });*/

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filterDialog.dismiss();

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                feedType = "";

                resetFilterValues();

                if (cb_image.isChecked()) {
                    feedType = String.format("%s", "image");
                    filterValues.put("image", true);
                }

                if (cb_video.isChecked()) {
                    if (feedType.isEmpty()) {
                        feedType = String.format("%s", "video");
                    } else {
                        feedType = String.format("%s,%s", feedType, "video");
                    }
                    filterValues.put("video", true);
                }

                if (cb_link.isChecked()) {
                    if (feedType.isEmpty()) {
                        feedType = String.format("%s", "link");
                    } else {
                        feedType = String.format("%s,%s", feedType, "link");
                    }
                    filterValues.put("link", true);
                }

                if (cb_all.isChecked()) {
                    feedType = "text,image,video,link";
                    filterValues.put("all", true);
                }


                feedList.clear();
                mAdapter.notifyDataSetChanged();
                page = 1;
                noMoreFeedItems = false;
                if (cd.isConnectingToInternet()) {
                    startProgressBar();
                    getFeedListWithFilter(feedType, String.valueOf(page));
                } else {
                    Utills.noInternetConnection(mContext);
                }


                filterDialog.dismiss();
            }
        });

    }

    private void resetFilterValues() {
        filterValues.put("image", false);
        filterValues.put("video", false);
        filterValues.put("link", false);
        filterValues.put("all", false);
    }

    @Override
    public void showLoadingProgressBar() {
        //startProgressBar();
    }

    @Override
    public void hideLoadingProgressBar() {
        dismissProgressBar();
    }

    @Override
    public void Failed(String method, String message) {

        switch (method) {


            case GET_FEED_LIST_WITH_FILTER:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case LIKE_POST:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case DELETE_POST:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case SAVE_SEARCH_DISTANCE:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case UNFOLLOW_USER:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case FOLLOW_POST:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case UNFOLLOW_POST:

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

            case LIKE_POST: {

                if (feedList.get(likePosition).getIsLikes().equals("1")) {
                    feedList.get(likePosition).setIsLikes("0");
                    feedList.get(likePosition).setTotalLikes(feedList.get(likePosition).getTotalLikes() - 1);
                } else {
                    feedList.get(likePosition).setIsLikes("1");
                    feedList.get(likePosition).setTotalLikes(feedList.get(likePosition).getTotalLikes() + 1);
                }

                mAdapter.notifyDataSetChanged();


                break;
            }

            case DELETE_POST:

                feedList.remove(deletePosition);
                mAdapter.notifyDataSetChanged();

                if (feedList.isEmpty()) {
                    showEmptyView();
                }

                break;


            case GET_FEED_LIST_WITH_FILTER:

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

                    for (int i = 0; i < length; i++) {

                        JSONObject currentFeedItem = data.getJSONObject(i);

                        String id = currentFeedItem.optString("id");
                        String activityId = currentFeedItem.optString("activity_id");
                        String userId = currentFeedItem.optString("user_id");
                        String postPrivacy = currentFeedItem.optString("post_privacy");
                        String type = currentFeedItem.optString("type");
                        String subType = currentFeedItem.optString("sub_type");
                        String title = currentFeedItem.optString("title");
                        String description = currentFeedItem.optString("description");
                        String media = currentFeedItem.optString("media");
                        String addedOn = currentFeedItem.optString("added_on");
                        String activityType = currentFeedItem.optString("activity_type");
                        String modifyDate = currentFeedItem.optString("modify_date");
                        String name = currentFeedItem.optString("name");
                        String profileImage = currentFeedItem.optString("profile_image");
                        int totalLikes = currentFeedItem.optInt("total_likes");
                        int totalComments = currentFeedItem.optInt("total_comments");
                        String isLikes = currentFeedItem.optString("is_likes");
                        String baseUrl = currentFeedItem.optString("base_url");
                        String userType = currentFeedItem.optString("user_type");
                        String isFollowPost = currentFeedItem.optString("is_follow_post");

                        ArrayList<String> imageUrlList = new ArrayList<>();

                        JSONObject image = currentFeedItem.optJSONObject("image");

                        if (image != null) {

                            Iterator<String> imageKeys = image.keys();
                            while (imageKeys.hasNext()) {
                                String imageUrl = image.optString(imageKeys.next());
                                imageUrlList.add(imageUrl);
                            }
                        }

                        String infoString = currentFeedItem.optString("info");

                        Info info = new Info();

                        if (!infoString.isEmpty()) {

                            JSONObject infoObject = new JSONObject(infoString);

                            info.withImage(infoObject.optString("image"))
                                    .withDescription(infoObject.optString("description"))
                                    .withHost(infoObject.optString("host"))
                                    .withSiteName(infoObject.optString("site_name"))
                                    .withTitle(infoObject.optString("title"))
                                    .withType(infoObject.optString("type"))
                                    .withUrl(infoObject.optString("url"))
                                    .withVideoType(infoObject.optString("video:type"))
                                    .withVideoUrl(infoObject.optString("video:url"))
                                    .withVideoSecureUrl(infoObject.optString("video:secure_url"))
                                    .withVideoWidth(infoObject.optString("video:width"))
                                    .withVideoHeight(infoObject.optString("video:height"))
                                    .withVideoTag(infoObject.optString("video:tag"));

                        }


                        FeedItem feedItem = new FeedItem().withId(id)
                                .withActivityId(activityId)
                                .withUserId(userId)
                                .withPostPrivacy(postPrivacy)
                                .withActivityType(activityType)
                                .withDescription(description)
                                .withInfo(info)
                                .withMedia(media)
                                .withModifyDate(modifyDate)
                                .withName(name)
                                .withProfileImage(profileImage)
                                .withType(type)
                                .withTotalLikes(totalLikes)
                                .withTotalComments(totalComments)
                                .withSubType(subType)
                                .withTitle(title)
                                .withImage(imageUrlList)
                                .withIsLikes(isLikes)
                                .withBaseUrl(baseUrl)
                                .withUserType(userType)
                                .withIsFollowPost(isFollowPost);


                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date inputDate = null;

                        try {
                            inputDate = inputFormat.parse(addedOn);
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
                            outputDateTime = String.format("%s at %s", outputDate, outputTime);
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

            case SAVE_SEARCH_DISTANCE:

                try {

                    JSONObject request = Data.getJSONObject("req");

                    int distance = request.optInt("search_distance");
                    PreferenceUtils.setCommunitySearchDistance(mContext, distance);

                    if (searchDistanceDialog != null && searchDistanceDialog.isShowing()) {
                        searchDistanceDialog.dismiss();
                    }

                    feedList.clear();
                    page = 1;
                    noMoreFeedItems = false;

                    if (cd.isConnectingToInternet()) {
                        startProgressBar();
                        getFeedListWithFilter(feedType, String.valueOf(page));
                    } else {
                        Utills.noInternetConnection(mContext);
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

                ListIterator<FeedItem> iterator = feedList.listIterator();

                while (iterator.hasNext()) {
                    if (iterator.next().getUserId().equals(userId)) {
                        iterator.remove();
                    }
                }

                mAdapter.notifyDataSetChanged();

                break;

            case FOLLOW_POST:

                feedList.get(followPostPosition).setIsFollowPost("1");

                mAdapter.notifyItemChanged(followPostPosition);

                break;

            case UNFOLLOW_POST:

                feedList.get(unfollowPostPosition).setIsFollowPost("0");

                mAdapter.notifyItemChanged(unfollowPostPosition);

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

    public void showSetDistanceDialog() {

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

        et_distance.setText(String.valueOf(PreferenceUtils.getCommunitySearchDistance(mContext)));
        et_distance.setSelection(et_distance.getText().toString().length());

        /*TextView tv_min = (TextView) searchDistanceDialog.findViewById(R.id.tv_search_distance_min);
        TextView tv_max = (TextView) searchDistanceDialog.findViewById(R.id.tv_search_distance_max);
        final TextView tv_value = (TextView) searchDistanceDialog.findViewById(R.id.tv_search_distance);

        CrystalSeekbar seekbar = (CrystalSeekbar) searchDistanceDialog.findViewById(R.id.search_distance_seekbar);

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


        seekbar.setMinStartValue((float) PreferenceUtils.getCommunitySearchDistance(mContext)).apply();
        tv_value.setText(String.valueOf(PreferenceUtils.getCommunitySearchDistance(mContext)));*/



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

                String distance = et_distance.getText().toString().trim();

                if (distance.isEmpty()) {
                    showAlert(mContext, getString(R.string.app_name), getString(R.string.distance_required));
                } else if (cd.isConnectingToInternet()) {
                    //int searchDistance = Integer.parseInt(tv_value.getText().toString().trim());
                    Utills.hideSoftKeyboard((HomeActivity) mContext);
                    int searchDistance = Integer.parseInt(distance);
                    startProgressBar();
                    requestPresenter.saveSearchDistance(SAVE_SEARCH_DISTANCE, PreferenceUtils.getId(mContext), searchDistance);
                } else {
                    Utills.noInternetConnection(mContext);
                }

            }
        });

        if (postPrivacy.equals("community")) {
            searchDistanceDialog.show();
        }

    }

    public void showDeleteAlert(final int position, final String feedId) {

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
        txt_message.setText(getResources().getString(R.string.are_your_sure_you_want_to_delete_this_post));

        dialog.show();


        txt_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    deletePosition = position;
                    startProgressBar();
                    requestPresenter.deletePost(DELETE_POST, PreferenceUtils.getId(mContext), feedId);
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

    public void showUnfollowUserAlert(final int position, final FeedItem feedItem) {

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
                            feedItem.getUserId());
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


    public void showUnfollowPostAlert(final int position, final FeedItem feedItem) {

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
        txt_message.setText(getResources().getString(R.string.are_your_sure_you_want_to_unfollow_this_post));

        dialog.show();


        txt_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    unfollowPostPosition = position;
                    startProgressBar();
                    requestPresenter.followUnfollowPost(UNFOLLOW_POST, PreferenceUtils.getId(mContext),
                            feedItem.getActivityId(), feedItem.getId());
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
