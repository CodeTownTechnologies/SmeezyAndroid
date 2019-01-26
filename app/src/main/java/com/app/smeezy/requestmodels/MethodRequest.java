package com.app.smeezy.requestmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rahul on 20-12-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MethodRequest {

    @SerializedName("methodName")
    @Expose
    private String methodName;
    @SerializedName("data")
    @Expose
    private Request data;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public MethodRequest withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Request getData() {
        return data;
    }

    public void setData(Request data) {
        this.data = data;
    }

    public MethodRequest withData(Request data) {
        this.data = data;
        return this;
    }



}