package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GroupRequest implements Serializable {

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
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_slug")
    @Expose
    private String profileSlug;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GroupRequest withId(String id) {
        this.id = id;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public GroupRequest withGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GroupRequest withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public GroupRequest withRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public GroupRequest withActionBy(String actionBy) {
        this.actionBy = actionBy;
        return this;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public GroupRequest withAddedOn(String addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public GroupRequest withIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public GroupRequest withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public GroupRequest withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupRequest withName(String name) {
        this.name = name;
        return this;
    }

    public String getProfileSlug() {
        return profileSlug;
    }

    public void setProfileSlug(String profileSlug) {
        this.profileSlug = profileSlug;
    }

    public GroupRequest withProfileSlug(String profileSlug) {
        this.profileSlug = profileSlug;
        return this;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public GroupRequest withProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

}
