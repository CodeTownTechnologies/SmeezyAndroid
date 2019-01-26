package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatListItem implements Serializable {

    @SerializedName("thread_id")
    @Expose
    private String threadId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("friend_id")
    @Expose
    private String friendId;
    @SerializedName("modify_date")
    @Expose
    private String modifyDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("stuff_privacy")
    @Expose
    private String stuffPrivacy;
    @SerializedName("allowed_users")
    @Expose
    private String allowedUsers;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("unread_count")
    @Expose
    private String unreadCount;
    @SerializedName("my_allowed")
    @Expose
    private Integer myAllowed;
    @SerializedName("block_status")
    @Expose
    private Integer blockStatus;
    @SerializedName("is_friend")
    @Expose
    private Integer isFriend;


    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public ChatListItem withThreadId(String threadId) {
        this.threadId = threadId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ChatListItem withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public ChatListItem withFriendId(String friendId) {
        this.friendId = friendId;
        return this;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public ChatListItem withModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatListItem withName(String name) {
        this.name = name;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ChatListItem withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getStuffPrivacy() {
        return stuffPrivacy;
    }

    public void setStuffPrivacy(String stuffPrivacy) {
        this.stuffPrivacy = stuffPrivacy;
    }

    public ChatListItem withStuffPrivacy(String stuffPrivacy) {
        this.stuffPrivacy = stuffPrivacy;
        return this;
    }

    public String getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers(String allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    public ChatListItem withAllowedUsers(String allowedUsers) {
        this.allowedUsers = allowedUsers;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ChatListItem withImage(String image) {
        this.image = image;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatListItem withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

    public ChatListItem withUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
        return this;
    }

    public Integer getMyAllowed() {
        return myAllowed;
    }

    public void setMyAllowed(Integer myAllowed) {
        this.myAllowed = myAllowed;
    }

    public ChatListItem withMyAllowed(Integer myAllowed) {
        this.myAllowed = myAllowed;
        return this;
    }

    public Integer getBlockStatus() {
        return blockStatus;
    }

    public void setBlockStatus(Integer blockStatus) {
        this.blockStatus = blockStatus;
    }

    public ChatListItem withBlockStatus(Integer blockStatus) {
        this.blockStatus = blockStatus;
        return this;
    }


    public Integer getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(Integer isFriend) {
        this.isFriend = isFriend;
    }

    public ChatListItem withIsFriend(Integer isFriend){
        this.isFriend = isFriend;
        return this;
    }
}