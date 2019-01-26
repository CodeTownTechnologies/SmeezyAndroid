package com.app.smeezy.interfacess;

/**
 * Created by Rahul on 09-02-2018.
 */

public interface FriendRequestListener {

    void onAcceptRequest(int position, String memberId);
    void onRejectRequest(int position, String memberId);

}
