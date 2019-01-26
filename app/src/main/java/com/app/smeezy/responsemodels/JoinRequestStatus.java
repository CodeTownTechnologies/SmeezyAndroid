package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JoinRequestStatus implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("request_status")
    @Expose
    private String requestStatus;
    @SerializedName("action_by")
    @Expose
    private String actionBy;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
    @SerializedName("ip")
    @Expose
    private String ip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JoinRequestStatus withId(String id) {
        this.id = id;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public JoinRequestStatus withGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public JoinRequestStatus withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public JoinRequestStatus withRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public JoinRequestStatus withActionBy(String actionBy) {
        this.actionBy = actionBy;
        return this;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public JoinRequestStatus withAddedOn(String addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public JoinRequestStatus withIp(String ip) {
        this.ip = ip;
        return this;
    }

}