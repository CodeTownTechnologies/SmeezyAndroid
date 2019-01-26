package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Group implements Serializable{

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
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
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
    @SerializedName("total_member")
    @Expose
    private String totalMember;
    @SerializedName("total_post")
    @Expose
    private String totalPost;
    @SerializedName("total_likes")
    @Expose
    private Integer totalLikes;
    @SerializedName("total_comments")
    @Expose
    private Integer totalComments;
    @SerializedName("is_likes")
    @Expose
    private String isLikes;
    @SerializedName("group_role")
    @Expose
    private String groupRole;
    @SerializedName("join_request_status")
    @Expose
    private JoinRequestStatus joinRequestStatus;






    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Group withId(String id) {
        this.id = id;
        return this;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Group withActivityId(String activityId) {
        this.activityId = activityId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Group withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Group withType(String type) {
        this.type = type;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Group withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Group withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Group withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Group withImage(String image) {
        this.image = image;
        return this;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public Group withAddedOn(String addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Group withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Group withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group withName(String name) {
        this.name = name;
        return this;
    }

    public String getProfileSlug() {
        return profileSlug;
    }

    public void setProfileSlug(String profileSlug) {
        this.profileSlug = profileSlug;
    }

    public Group withProfileSlug(String profileSlug) {
        this.profileSlug = profileSlug;
        return this;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Group withProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public String getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(String totalMember) {
        this.totalMember = totalMember;
    }

    public Group withTotalMember(String totalMember) {
        this.totalMember = totalMember;
        return this;
    }

    public String getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(String totalPost) {
        this.totalPost = totalPost;
    }

    public Group withTotalPost(String totalPost) {
        this.totalPost = totalPost;
        return this;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Group withTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
        return this;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    public Group withTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
        return this;
    }

    public String getIsLikes() {
        return isLikes;
    }

    public void setIsLikes(String isLikes) {
        this.isLikes = isLikes;
    }

    public Group withIsLikes(String isLikes) {
        this.isLikes = isLikes;
        return this;
    }

    public String getGroupRole() {
        return groupRole;
    }

    public void setGroupRole(String groupRole) {
        this.groupRole = groupRole;
    }

    public Group withGroupRole(String groupRole) {
        this.groupRole = groupRole;
        return this;
    }

    public JoinRequestStatus getJoinRequestStatus() {
        return joinRequestStatus;
    }

    public void setJoinRequestStatus(JoinRequestStatus joinRequestStatus) {
        this.joinRequestStatus = joinRequestStatus;
    }

    public Group withJoinRequestStatus(JoinRequestStatus joinRequestStatus) {
        this.joinRequestStatus = joinRequestStatus;
        return this;
    }
}