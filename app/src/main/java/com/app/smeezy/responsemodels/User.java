package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("location_latitude")
    @Expose
    private String locationLatitude;
    @SerializedName("location_longitude")
    @Expose
    private String locationLongitude;
    @SerializedName("location_address")
    @Expose
    private String locationAddress;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("relationship_status")
    @Expose
    private String relationshipStatus;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("work")
    @Expose
    private String work;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("religious_view")
    @Expose
    private String religiousView;
    @SerializedName("political_view")
    @Expose
    private String politicalView;
    @SerializedName("step_first")
    @Expose
    private String stepFirst;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;
    @SerializedName("is_friend")
    @Expose
    private String isFriend;
    @SerializedName("is_request_sent")
    @Expose
    private String isRequestSent;
    @SerializedName("request_status")
    @Expose
    private String requestStatus;
    @SerializedName("fb_url")
    @Expose
    private String fbUrl;
    @SerializedName("twitter_url")
    @Expose
    private String twitterUrl;
    @SerializedName("linkedin_url")
    @Expose
    private String linkedinUrl;
    @SerializedName("connection_record")
    @Expose
    private ConnectionRecord connectionRecord;
    @SerializedName("stufflist_privacy")
    @Expose
    private String stufflistPrivacy;
    @SerializedName(" stufflist_option")
    @Expose
    private String stufflistOption;
    @SerializedName("is_selected")
    @Expose
    private Boolean isSelected;
    @SerializedName("email_privacy")
    @Expose
    private String emailPrivacy;
    @SerializedName("phone_privacy")
    @Expose
    private String phonePrivacy;
    @SerializedName("location_privacy")
    @Expose
    private String locationPrivacy;
    @SerializedName("social_connection_privacy")
    @Expose
    private String socialConnectionPrivacy;
    @SerializedName("relationship_privacy")
    @Expose
    private String relationshipPrivacy;
    @SerializedName("dob_privacy")
    @Expose
    private String dobPrivacy;
    @SerializedName("notification")
    @Expose
    private String notification;
    @SerializedName("custom_stufflist")
    @Expose
    private String customStufflist;
    @SerializedName("allowed")
    @Expose
    private String allowed;
    @SerializedName("block_status")
    @Expose
    private String blockStatus;
    @SerializedName("stufflist")
    @Expose
    private String stufflist;
    @SerializedName("stuff_privacy")
    @Expose
    private String stuffPrivacy;
    @SerializedName("feedbackPosted")
    @Expose
    private boolean feedbackPosted;
    @SerializedName("is_unfollow_user")
    @Expose
    private String isUnfollowUser;
    @SerializedName("mutual_friends")
    @Expose
    private int mutualFriends;






    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User withId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public User withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User withName(String name) {
        this.name = name;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public User withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public User withDob(String dob) {
        this.dob = dob;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public User withLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
        return this;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public User withLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
        return this;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public User withLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User withCity(String city) {
        this.city = city;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public User withRegion(String region) {
        this.region = region;
        return this;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public User withPincode(String pincode) {
        this.pincode = pincode;
        return this;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public User withRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public User withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public User withWork(String work) {
        this.work = work;
        return this;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public User withSchool(String school) {
        this.school = school;
        return this;
    }

    public String getReligiousView() {
        return religiousView;
    }

    public void setReligiousView(String religiousView) {
        this.religiousView = religiousView;
    }

    public User withReligiousView(String religiousView) {
        this.religiousView = religiousView;
        return this;
    }

    public String getPoliticalView() {
        return politicalView;
    }

    public void setPoliticalView(String politicalView) {
        this.politicalView = politicalView;
    }

    public User withPoliticalView(String politicalView) {
        this.politicalView = politicalView;
        return this;
    }

    public String getStepFirst() {
        return stepFirst;
    }

    public void setStepFirst(String stepFirst) {
        this.stepFirst = stepFirst;
    }

    public User withStepFirst(String stepFirst) {
        this.stepFirst = stepFirst;
        return this;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public User withProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public User withCoverImage(String coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public User withIsFriend(String isFriend){
        this.isFriend = isFriend;
        return this;
    }

    public String getIsRequestSent() {
        return isRequestSent;
    }

    public void setIsRequestSent(String isRequestSent) {
        this.isRequestSent = isRequestSent;
    }

    public User withIsRequestSent(String isRequestSent){
        this.isRequestSent = isRequestSent;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public User withRequestStatus(String requestStatus){
        this.requestStatus = requestStatus;
        return this;
    }

    public String getFbUrl() {
        return fbUrl;
    }

    public void setFbUrl(String fbUrl) {
        this.fbUrl = fbUrl;
    }

    public User withFbUrl(String fbUrl){
        this.fbUrl = fbUrl;
        return this;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public User withTwitterUrl(String twitterUrl){
        this.twitterUrl = twitterUrl;
        return this;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public User withLinkedinUrl(String linkedinUrl){
        this.linkedinUrl = linkedinUrl;
        return this;
    }

    public ConnectionRecord getConnectionRecord() {
        return connectionRecord;
    }

    public void setConnectionRecord(ConnectionRecord connectionRecord) {
        this.connectionRecord = connectionRecord;
    }

    public User withConnectionRecord(ConnectionRecord connectionRecord){
        this.connectionRecord = connectionRecord;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public User withCountry(String country){
        this.country = country;
        return this;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public User withIsSelected(Boolean isSelected){
        this.isSelected = isSelected;
        return this;
    }

    public String getStufflistPrivacy() {
        return stufflistPrivacy;
    }

    public void setStufflistPrivacy(String stufflistPrivacy) {
        this.stufflistPrivacy = stufflistPrivacy;
    }

    public User withStufflistPrivacy(String stufflistPrivacy) {
        this.stufflistPrivacy = stufflistPrivacy;
        return this;
    }

    public String getStufflistOption() {
        return stufflistOption;
    }

    public void setStufflistOption(String stufflistOption) {
        this.stufflistOption = stufflistOption;
    }

    public User withStufflistOption(String stufflistOption) {
        this.stufflistOption = stufflistOption;
        return this;
    }

    public String getEmailPrivacy() {
        return emailPrivacy;
    }

    public void setEmailPrivacy(String emailPrivacy) {
        this.emailPrivacy = emailPrivacy;
    }

    public User withEmailPrivacy(String emailPrivacy) {
        this.emailPrivacy = emailPrivacy;
        return this;
    }

    public String getPhonePrivacy() {
        return phonePrivacy;
    }

    public void setPhonePrivacy(String phonePrivacy) {
        this.phonePrivacy = phonePrivacy;
    }

    public User withPhonePrivacy(String phonePrivacy) {
        this.phonePrivacy = phonePrivacy;
        return this;
    }

    public String getLocationPrivacy() {
        return locationPrivacy;
    }

    public void setLocationPrivacy(String locationPrivacy) {
        this.locationPrivacy = locationPrivacy;
    }

    public User withLocationPrivacy(String locationPrivacy) {
        this.locationPrivacy = locationPrivacy;
        return this;
    }

    public String getSocialConnectionPrivacy() {
        return socialConnectionPrivacy;
    }

    public void setSocialConnectionPrivacy(String socialConnectionPrivacy) {
        this.socialConnectionPrivacy = socialConnectionPrivacy;
    }

    public User withSocialConnectionPrivacy(String socialConnectionPrivacy) {
        this.socialConnectionPrivacy = socialConnectionPrivacy;
        return this;
    }

    public String getRelationshipPrivacy() {
        return relationshipPrivacy;
    }

    public void setRelationshipPrivacy(String relationshipPrivacy) {
        this.relationshipPrivacy = relationshipPrivacy;
    }

    public User withRelationshipPrivacy(String relationshipPrivacy) {
        this.relationshipPrivacy = relationshipPrivacy;
        return this;
    }

    public String getDobPrivacy() {
        return dobPrivacy;
    }

    public void setDobPrivacy(String dobPrivacy) {
        this.dobPrivacy = dobPrivacy;
    }

    public User withDobPrivacy(String dobPrivacy) {
        this.dobPrivacy = dobPrivacy;
        return this;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public User withNotification(String notification) {
        this.notification = notification;
        return this;
    }

    public String getCustomStufflist() {
        return customStufflist;
    }

    public void setCustomStufflist(String customStufflist) {
        this.customStufflist = customStufflist;
    }

    public User withCustomStufflist(String customStufflist) {
        this.customStufflist = customStufflist;
        return this;
    }

    public String getAllowed() {
        return allowed;
    }

    public void setAllowed(String allowed) {
        this.allowed = allowed;
    }

    public User withAllowed(String allowed) {
        this.allowed = allowed;
        return this;
    }

    public String getBlockStatus() {
        return blockStatus;
    }

    public void setBlockStatus(String blockStatus) {
        this.blockStatus = blockStatus;
    }

    public User withBlockStatus(String blockStatus){
        this.blockStatus = blockStatus;
        return this;
    }

    public String getStufflist() {
        return stufflist;
    }

    public void setStufflist(String stufflist) {
        this.stufflist = stufflist;
    }

    public User withStufflist(String stufflist){
        this.stufflist = stufflist;
        return this;
    }


    public String getStuffPrivacy() {
        return stuffPrivacy;
    }

    public void setStuffPrivacy(String stuffPrivacy) {
        this.stuffPrivacy = stuffPrivacy;
    }

    public User withStuffPrivacy(String stuffPrivacy) {
        this.stuffPrivacy = stuffPrivacy;
        return this;
    }


    public boolean isFeedbackPosted() {
        return feedbackPosted;
    }

    public void setFeedbackPosted(boolean feedbackPosted) {
        this.feedbackPosted = feedbackPosted;
    }

    public User withFeedbackPosted(boolean feedbackPosted){
        this.feedbackPosted = feedbackPosted;
        return this;
    }


    public String getIsUnfollowUser() {
        return isUnfollowUser;
    }

    public void setIsUnfollowUser(String isUnfollowUser) {
        this.isUnfollowUser = isUnfollowUser;
    }

    public User withIsUnfollowUser(String isUnfollowUser){
        this.isUnfollowUser = isUnfollowUser;
        return this;
    }

    public int getMutualFriends() {
        return mutualFriends;
    }

    public void setMutualFriends(int mutualFriends) {
        this.mutualFriends = mutualFriends;
    }

    public User withMutualFriends(int mutualFriends){
        this.mutualFriends = mutualFriends;
        return this;
    }
}