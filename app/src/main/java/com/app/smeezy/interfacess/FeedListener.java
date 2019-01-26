package com.app.smeezy.interfacess;

import com.app.smeezy.responsemodels.FeedItem;

/**
 * Created by Rahul on 26-01-2018.
 */

public interface FeedListener {

    void onLike(int position, String activityId, String feedId);
    void onComment(int position, FeedItem feedItem);
    void onDeletePost(int position, String feedId);
    void onEditPost(int position, FeedItem feedItem);
    void onBottomReached();
    void onUnfollowUser(int position, FeedItem feedItem);
    void onFollowPost(int position, FeedItem feedItem);
    void onUnfollowPost(int position, FeedItem feedItem);
    void onReportPost(int position, FeedItem feedItem);

}
