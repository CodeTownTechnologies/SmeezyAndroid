package com.app.smeezy.interfacess;

import com.app.smeezy.responsemodels.User;

/**
 * Created by Rahul on 25-01-2018.
 */

public interface SuggestedFriendsListener {

    void onAddFriend(int position, User user);

}
