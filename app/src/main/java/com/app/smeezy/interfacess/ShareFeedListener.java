package com.app.smeezy.interfacess;

import com.app.smeezy.responsemodels.StuffFeedItem;

/**
 * Created by Rahul on 09-02-2018.
 */

public interface ShareFeedListener {

    void onBottomReached();
    void onAskToBorrow(StuffFeedItem stuffFeedItem);
    void onUnfollowUser(int position, StuffFeedItem stuffFeedItem);
    void onChangeCategory(int position, StuffFeedItem stuffFeedItem);
    void onBookmark(int position, StuffFeedItem stuffFeedItem);
    void onRemoveBookmark(int position, StuffFeedItem stuffFeedItem);
    void onReportPost(int position, StuffFeedItem stuffFeedItem);

}
