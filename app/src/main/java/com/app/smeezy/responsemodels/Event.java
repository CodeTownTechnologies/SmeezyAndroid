package com.app.smeezy.responsemodels;

import com.app.smeezy.requestmodels.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable{

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
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("location_latitude")
    @Expose
    private String locationLatitude;
    @SerializedName("location_longitude")
    @Expose
    private String locationLongitude;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private Object endDate;
    @SerializedName("ended_on")
    @Expose
    private String endedOn;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("guest_coming")
    @Expose
    private String guestComing;
    @SerializedName("guest_not_coming")
    @Expose
    private String guestNotComing;
    @SerializedName("guest_maybe")
    @Expose
    private String guestMaybe;
    @SerializedName("total_likes")
    @Expose
    private Integer totalLikes;
    @SerializedName("total_comments")
    @Expose
    private Integer totalComments;
    @SerializedName("is_likes")
    @Expose
    private String isLikes;
    @SerializedName("invite_id")
    @Expose
    private String inviteId;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event withId(String id) {
        this.id = id;
        return this;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Event withActivityId(String activityId) {
        this.activityId = activityId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Event withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Event withType(String type) {
        this.type = type;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Event withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Event withImage(String image) {
        this.image = image;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Event withLocation(String location) {
        this.location = location;
        return this;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Event withLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
        return this;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Event withLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Event withStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = endDate;
    }

    public Event withEndDate(Object endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getEndedOn() {
        return endedOn;
    }

    public void setEndedOn(String endedOn) {
        this.endedOn = endedOn;
    }

    public Event withEndedOn(String endedOn) {
        this.endedOn = endedOn;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Event withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Event withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Event withName(String name) {
        this.name = name;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Event withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Event withProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public String getGuestComing() {
        return guestComing;
    }

    public void setGuestComing(String guestComing) {
        this.guestComing = guestComing;
    }

    public Event withGuestComing(String guestComing) {
        this.guestComing = guestComing;
        return this;
    }

    public String getGuestNotComing() {
        return guestNotComing;
    }

    public void setGuestNotComing(String guestNotComing) {
        this.guestNotComing = guestNotComing;
    }

    public Event withGuestNotComing(String guestNotComing) {
        this.guestNotComing = guestNotComing;
        return this;
    }

    public String getGuestMaybe() {
        return guestMaybe;
    }

    public void setGuestMaybe(String guestMaybe) {
        this.guestMaybe = guestMaybe;
    }

    public Event withGuestMaybe(String guestMaybe) {
        this.guestMaybe = guestMaybe;
        return this;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Event withTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
        return this;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    public Event withTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
        return this;
    }

    public String getIsLikes() {
        return isLikes;
    }

    public void setIsLikes(String isLikes) {
        this.isLikes = isLikes;
    }

    public Event withIsLikes(String isLikes) {
        this.isLikes = isLikes;
        return this;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public Event withInviteId(String inviteId) {
        this.inviteId = inviteId;
        return this;
    }
}