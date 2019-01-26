package com.app.smeezy.interfacess;

import org.json.JSONObject;

/**
 * Created by kipl145 on 11/22/2016.
 */
public interface IRequestView {

    void showLoadingProgressBar();
    void hideLoadingProgressBar();
    void Failed(String method, String message);
    void Failed1(String message);
    void Success(String method, JSONObject Data);
    void Success1(String method, JSONObject Data);
}
