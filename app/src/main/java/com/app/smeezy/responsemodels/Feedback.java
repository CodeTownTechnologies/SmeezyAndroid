package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Feedback implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("rate")
    @Expose
    private String rate;
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
    @SerializedName("cover_image")
    @Expose
    private String coverImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Feedback withId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Feedback withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Feedback withMemberId(String memberId) {
        this.memberId = memberId;
        return this;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Feedback withRate(String rate) {
        this.rate = rate;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Feedback withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public Feedback withAddedOn(String addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Feedback withName(String name) {
        this.name = name;
        return this;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Feedback withProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Feedback withCoverImage(String coverImage) {
        this.coverImage = coverImage;
        return this;
    }

}