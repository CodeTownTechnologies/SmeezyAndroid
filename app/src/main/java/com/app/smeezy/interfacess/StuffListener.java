package com.app.smeezy.interfacess;

import com.app.smeezy.responsemodels.FeedItem;
import com.app.smeezy.responsemodels.UserStuff;

/**
 * Created by Rahul on 26-01-2018.
 */

public interface StuffListener {

    void onDeleteStuff(int position, String id);
    void onEditStuff (int position, UserStuff userStuff);
    void onBottomReached();

}
