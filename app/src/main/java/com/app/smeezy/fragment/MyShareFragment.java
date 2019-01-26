package com.app.smeezy.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.app.smeezy.R;
import com.app.smeezy.activity.CommentActivity;
import com.app.smeezy.activity.EditPostActivity;
import com.app.smeezy.activity.PostActivity;
import com.app.smeezy.activity.ReportActivity;
import com.app.smeezy.adapter.MyShareAdapter;
import com.app.smeezy.interfacess.FeedListener;
import com.app.smeezy.interfacess.IRequestView;
import com.app.smeezy.presenter.RequestPresenter;
import com.app.smeezy.responsemodels.FeedItem;
import com.app.smeezy.responsemodels.Info;
import com.app.smeezy.utills.ConnectionDetector;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.Utills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyShareFragment extends BaseFragment implements IRequestView, FeedListener, View.OnClickListener {


    private static final String GET_FEED_LIST_WITH_FILTER = "get_filtered_feed_list";
    private static final String LIKE_POST = "like_post";
    private static final String DELETE_POST = "delete_post";
    private static final String FOLLOW_POST = "follow_post";
    private static final String UNFOLLOW_POST = "unfollow_post";

    private static final int COMMENT_REQUEST = 222;
    private static final int REPORT_REQUEST = 4564;


    @BindView(R.id.my_share_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_empty_view)
    TextView tv_empty_view;

    @BindView(R.id.et_my_share_post)
    EditText et_my_share_post;

    private Context mContext;
    private ConnectionDetector cd;
    private RequestPresenter requestPresenter;

    private ArrayList<FeedItem> feedList = new ArrayList<>();

    private MyShareAdapter mAdapter;
    private int page = 1;
    private int likePosition, commentPosition, deletePosition;
    private boolean noMoreFeedItems = false;
    private int followPostPosition, unfollowPostPosition, reportPosition;

    private String memberId;
    private boolean ownProfile;

    public MyShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_share, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            memberId = getArguments().getString("memberId");
            ownProfile = getArguments().getBoolean("ownProfile");
        }

        mContext = getActivity();
        cd = new ConnectionDetector(mContext);
        requestPresenter = new RequestPresenter(getApplicationClass(mContext), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new MyShareAdapter(mContext, recyclerView, feedList, this, ownProfile);

        recyclerView.setAdapter(mAdapter);

        if (ownProfile){
            et_my_share_post.setVisibility(View.VISIBLE);
        }else {
            et_my_share_post.setVisibility(View.GONE);
        }

        getFeedList(String.valueOf(page));

        return view;
    }

    private void getFeedList(String page) {
        if (!noMoreFeedItems) {
            if (cd.isConnectingToInternet()) {
                requestPresenter.getFeedList(GET_FEED_LIST_WITH_FILTER, memberId, "", page);
            } else {
                Utills.noInternetConnection(mContext);
            }
        }
    }

    @Override
    public void onLike(int position, String activityId, String feedId) {
        if (cd.isConnectingToInternet()) {
            this.likePosition = position;
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
        intent.putExtra(getString(R.string.activity_tag), getString(R.string.fragment_profile_tag));
        intent.putExtra("feedItem", feedItem);
        startActivity(intent);
    }

    @Override
    public void onUnfollowUser(int position, FeedItem feedItem) {

    }

    @Override
    public void onFollowPost(int position, FeedItem feedItem) {

        if (cd.isConnectingToInternet()) {
            followPostPosition = position;
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

    @OnClick({R.id.et_my_share_post})
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.et_my_share_post:

                Intent intent = new Intent(mContext, PostActivity.class);
                intent.putExtra("groupPost", false);
                intent.putExtra("postPrivacy", "community");
                intent.putExtra(getString(R.string.activity_tag), getString(R.string.fragment_profile_tag));
                startActivity(intent);

                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       switch (requestCode){
           case COMMENT_REQUEST:

            if (resultCode == Activity.RESULT_OK) {

                int totalComments = data.getIntExtra("totalComments", feedList.get(commentPosition).getTotalComments());

                feedList.get(commentPosition).setTotalComments(totalComments);
                mAdapter.notifyDataSetChanged();

            }

            break;

           case REPORT_REQUEST:

               if (resultCode == Activity.RESULT_OK){

                   feedList.remove(reportPosition);
                   mAdapter.notifyItemRemoved(reportPosition);
               }

               break;
        }

    }

    @Override
    public void onBottomReached() {
        page++;
        getFeedList(String.valueOf(page));
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


            case GET_FEED_LIST_WITH_FILTER:

                showAlert(mContext, getString(R.string.app_name), message);

                break;

            case LIKE_POST:

                showAlert(mContext, getString(R.string.app_name), message);

                break;


            case DELETE_POST:

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
                                .withBaseUrl(baseUrl);


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
