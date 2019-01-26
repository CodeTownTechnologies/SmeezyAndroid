package com.app.smeezy.responsemodels;

import com.app.smeezy.requestmodels.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FeedItem implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("activity_id")
    @Expose
    private String activityId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("sub_type")
    @Expose
    private String subType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("media")
    @Expose
    private String media;
    @SerializedName("image")
    @Expose
    private ArrayList<String> image;
    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
    @SerializedName("activity_type")
    @Expose
    private String activityType;
    @SerializedName("modify_date")
    @Expose
    private String modifyDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("total_likes")
    @Expose
    private Integer totalLikes;
    @SerializedName("total_comments")
    @Expose
    private Integer totalComments;
    @SerializedName("is_likes")
    @Expose
    private String isLikes;
    @SerializedName("post_privacy")
    @Expose
    private String postPrivacy;
    @SerializedName("base_url")
    @Expose
    private String baseUrl;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("is_follow_post")
    @Expose
    private String isFollowPost;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FeedItem withId(String id) {
        this.id = id;
        return this;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public FeedItem withActivityId(String activityId) {
        this.activityId = activityId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public FeedItem withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FeedItem withType(String type) {
        this.type = type;
        return this;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public FeedItem withSubType(String subType) {
        this.subType = subType;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FeedItem withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FeedItem withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public FeedItem withMedia(String media) {
        this.media = media;
        return this;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public FeedItem withImage(ArrayList<String> image) {
        this.image = image;
        return this;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public FeedItem withInfo(Info info) {
        this.info = info;
        return this;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public FeedItem withAddedOn(String addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public FeedItem withActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public FeedItem withModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FeedItem withName(String name) {
        this.name = name;
        return this;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public FeedItem withProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public FeedItem withTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
        return this;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    public FeedItem withTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
        return this;
    }

    public String getIsLikes() {
        return isLikes;
    }

    public void setIsLikes(String isLikes) {
        this.isLikes = isLikes;
    }

    public FeedItem withIsLikes(String isLikes){
        this.isLikes = isLikes;
        return this;
    }

    public String getPostPrivacy() {
        return postPrivacy;
    }

    public void setPostPrivacy(String postPrivacy) {
        this.postPrivacy = postPrivacy;
    }

    public FeedItem withPostPrivacy(String postPrivacy){
        this.postPrivacy = postPrivacy;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public FeedItem withBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
        return this;
    }


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public FeedItem withUserType(String userType) {
        this.userType = userType;
        return this;
    }

    public String getIsFollowPost() {
        return isFollowPost;
    }

    public void setIsFollowPost(String isFollowPost) {
        this.isFollowPost = isFollowPost;
    }

    public FeedItem withIsFollowPost(String isFollowPost){
        this.isFollowPost = isFollowPost;
        return this;
    }
}