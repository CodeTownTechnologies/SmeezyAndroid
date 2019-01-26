package com.app.smeezy.presenter;


import android.content.Context;

import com.app.smeezy.settingstructure.ApiService;
import com.app.smeezy.settingstructure.Smeezy;


/**
 * Created by kipl145 on 11/22/2016.
 */
public class BasePresenter {

    Smeezy smeezy;

    public BasePresenter(Smeezy smeezy){
        this.smeezy = smeezy;
    }

    public ApiService getApiService(){
        return smeezy.getApiService();
    }

    public Context getAppContext(){
        return smeezy.getAppContext();
    }

}
