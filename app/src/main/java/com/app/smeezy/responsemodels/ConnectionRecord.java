package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConnectionRecord implements Serializable {

    @SerializedName("crq_id")
    @Expose
    private String crqId;
    @SerializedName("from_member_id")
    @Expose
    private String fromMemberId;
    @SerializedName("to_member_id")
    @Expose
    private String toMemberId;
    @SerializedName("request_status")
    @Expose
    private String requestStatus;

    public String getCrqId() {
        return crqId;
    }

    public void setCrqId(String crqId) {
        this.crqId = crqId;
    }

    public ConnectionRecord withCrqId(String crqId) {
        this.crqId = crqId;
        return this;
    }

    public String getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(String fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    public ConnectionRecord withFromMemberId(String fromMemberId) {
        this.fromMemberId = fromMemberId;
        return this;
    }

    public String getToMemberId() {
        return toMemberId;
    }

    public void setToMemberId(String toMemberId) {
        this.toMemberId = toMemberId;
    }

    public ConnectionRecord withToMemberId(String toMemberId) {
        this.toMemberId = toMemberId;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public ConnectionRecord withRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

}