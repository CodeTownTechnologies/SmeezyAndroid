package com.app.smeezy.responsemodels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("a_c_id")
    @Expose
    private String aCId;
    @SerializedName("activity_id")
    @Expose
    private String activityId;
    @SerializedName("task_id")
    @Expose
    private String taskId;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("childs")
    @Expose
    private List<Object> childs = null;

    public String getACId() {
        return aCId;
    }

    public void setACId(String aCId) {
        this.aCId = aCId;
    }

    public Comment withACId(String aCId) {
        this.aCId = aCId;
        return this;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Comment withActivityId(String activityId) {
        this.activityId = activityId;
        return this;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Comment withTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Comment withParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Comment withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comment withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public Comment withAddedOn(String addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Comment withName(String name) {
        this.name = name;
        return this;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Comment withProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public List<Object> getChilds() {
        return childs;
    }

    public void setChilds(List<Object> childs) {
        this.childs = childs;
    }

    public Comment withChilds(List<Object> childs) {
        this.childs = childs;
        return this;
    }

}