package com.app.smeezy.interfacess;

import com.app.smeezy.responsemodels.FeedItem;
import com.app.smeezy.responsemodels.User;

/**
 * Created by Rahul on 26-01-2018.
 */

public interface FriendListListener {

    void onFollowUser(int position, User user);
    void onUnfollowUser(int position, User user);
    void onProfileClick(int position, User user);

}
