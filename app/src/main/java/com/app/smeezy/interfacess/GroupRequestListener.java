package com.app.smeezy.interfacess;

/**
 * Created by Rahul on 09-02-2018.
 */

public interface GroupRequestListener {

    void onApprove(int position, String requestId);
    void onReject(int position, String requestId);

}
