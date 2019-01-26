package com.app.smeezy.interfacess;

/**
 * Created by Rahul on 09-02-2018.
 */

public interface EventRequestListener {

    void onYes(int position, String inviteId);
    void onNo(int position, String inviteId);
    void onMaybe(int position, String inviteId);

}
