package com.app.smeezy.requestmodels;

/**
 * Created by Rahul on 20-12-2017.
 */

import com.app.smeezy.responsemodels.Info;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request {


    @SerializedName("mode")
    @Expose
    private Integer mode;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_password")
    @Expose
    private String userPassword;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("relationship_status")
    @Expose
    private String relationshipStatus;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("location_latitude")
    @Expose
    private String locationLatitude;
    @SerializedName("location_longitude")
    @Expose
    private String locationLongitude;
    @SerializedName("gender")
    @Expose
    private String gender;
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
    @SerializedName("user_facebook_id")
    @Expose
    private String userFacebookId;
    @SerializedName("user_google_id")
    @Expose
    private String userGoogleId;
    @SerializedName("user_image_url")
    @Expose
    private String userImageUrl;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("religious_view")
    @Expose
    private String religiousView;
    @SerializedName("political_view")
    @Expose
    private String politicalView;
    @SerializedName("work")
    @Expose
    private String work;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("media")
    @Expose
    private String media;
    @SerializedName("sub_type")
    @Expose
    private String subType;
    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("password_new")
    @Expose
    private String passwordNew;
    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("activity_id")
    @Expose
    private String activityId;
    @SerializedName("feed_id")
    @Expose
    private String feedId;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("post_privacy")
    @Expose
    private String postPrivacy;
    @SerializedName("search_distance")
    @Expose
    private int searchDistance;
    @SerializedName("notification")
    @Expose
    private int notification;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String[] oldImageArray;
    @SerializedName("task")
    @Expose
    private String task;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("users")
    @Expose
    private String[] users;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("options")
    @Expose
    private String options;
    @SerializedName("stufflist_privacy")
    @Expose
    private String stuffListPrivacy;
    @SerializedName("decision")
    @Expose
    private String decision;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("subject")
    @Expose
    private String subject;
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
    @SerializedName("referral_code")
    @Expose
    private String referralCode;
    @SerializedName("stuff_id")
    @Expose
    private String stuffId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("stuff_text")
    @Expose
    private String stuffText;
    @SerializedName("stuff_privacy")
    @Expose
    private String stuffPrivacy;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("selected_stuff")
    @Expose
    private String selectedStuff;
    @SerializedName("task_type")
    @Expose
    private String taskType;
    @SerializedName("task_id")
    @Expose
    private String taskId;
    @SerializedName("buy")
    @Expose
    private String buy;
    @SerializedName("rent")
    @Expose
    private String rent;
    @SerializedName("buy_price")
    @Expose
    private String buyPrice;
    @SerializedName("rent_price")
    @Expose
    private String rentPrice;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("payment_accepted")
    @Expose
    private String paymentAccepted;
    @SerializedName("sortby")
    @Expose
    private String sortby;






    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Request withMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Request withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Request withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Request withUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Request withUserPassword(String userPassword) {
        this.userPassword = userPassword;
        return this;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Request withDeviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Request withDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Request withName(String name) {
        this.name = name;
        return this;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Request withDob(String dob) {
        this.dob = dob;
        return this;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public Request withRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Request withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Request withLocation(String location) {
        this.location = location;
        return this;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Request withLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
        return this;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Request withLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Request withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Request withCity(String city) {
        this.city = city;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Request withRegion(String region) {
        this.region = region;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Request withCountry(String country) {
        this.country = country;
        return this;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Request withPincode(String pincode) {
        this.pincode = pincode;
        return this;
    }

    public String getUserFacebookId() {
        return userFacebookId;
    }

    public void setUserFacebookId(String userFacebookId) {
        this.userFacebookId = userFacebookId;
    }

    public Request withUserFacebookId(String userFacebookId) {
        this.userFacebookId = userFacebookId;
        return this;
    }

    public String getUserGoogleId() {
        return userGoogleId;
    }

    public void setUserGoogleId(String userGoogleId) {
        this.userGoogleId = userGoogleId;
    }

    public Request withUserGoogleId(String userGoogleId) {
        this.userGoogleId = userGoogleId;
        return this;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public Request withUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
        return this;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Request withMemberId(String memberId) {
        this.memberId = memberId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Request withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Request withType(String type) {
        this.type = type;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Request withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Request withUrl(String url) {
        this.url = url;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Request withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getReligiousView() {
        return religiousView;
    }

    public void setReligiousView(String religiousView) {
        this.religiousView = religiousView;
    }

    public Request withReligiousView(String religiousView) {
        this.religiousView = religiousView;
        return this;
    }

    public String getPoliticalView() {
        return politicalView;
    }

    public void setPoliticalView(String politicalView) {
        this.politicalView = politicalView;
    }

    public Request withPoliticalView(String politicalView) {
        this.politicalView = politicalView;
        return this;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public Request withWork(String work) {
        this.work = work;
        return this;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Request withSchool(String school) {
        this.school = school;
        return this;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public Request withMedia(String media) {
        this.media = media;
        return this;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Request withSubType(String subType) {
        this.subType = subType;
        return this;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Request withInfo(Info info) {
        this.info = info;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Request withPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public Request withPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
        return this;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Request withPage(String page) {
        this.page = page;
        return this;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Request withActivityId(String activityId){
        this.activityId = activityId;
        return this;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public Request withFeedId(String feedId){
        this.feedId = feedId;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Request withParentId(String parentId){
        this.parentId = parentId;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Request withComment(String comment){
        this.comment = comment;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Request withReason(String reason){
        this.reason = reason;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Request withKeyword(String keyword){
        this.keyword = keyword;
        return this;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Request withAction(String action){
        this.action = action;
        return this;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Request withRate(String rate){
        this.rate = rate;
        return this;
    }

    public String getPostPrivacy() {
        return postPrivacy;
    }

    public void setPostPrivacy(String postPrivacy) {
        this.postPrivacy = postPrivacy;
    }

    public Request withPostPrivacy(String postPrivacy){
        this.postPrivacy = postPrivacy;
        return this;
    }

    public int getSearchDistance() {
        return searchDistance;
    }

    public void setSearchDistance(int searchDistance) {
        this.searchDistance = searchDistance;
    }

    public Request withSearchDistance(int searchDistance){
        this.searchDistance = searchDistance;
        return this;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public Request withNotification(int notification){
        this.notification = notification;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Request withId(String id){
        this.id = id;
        return this;
    }

    public String[] getOldImageArray() {
        return oldImageArray;
    }

    public void setOldImageArray(String[] oldImageArray) {
        this.oldImageArray = oldImageArray;
    }

    public Request withOldImageArray(String[] oldImageArray){
        this.oldImageArray = oldImageArray;
        return this;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Request withTask(String task){
        this.task = task;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Request withTitle(String title) {
        this.title = title;
        return this;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Request withStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Request withEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Request withEndDate(String endDate){
        this.endDate = endDate;
        return this;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public Request withUsers(String[] users){
        this.users = users;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Request withGroupId(String groupId){
        this.groupId = groupId;
        return this;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Request withOptions(String options){
        this.options = options;
        return this;
    }

    public String getStuffListPrivacy() {
        return stuffListPrivacy;
    }

    public void setStuffListPrivacy(String stuffListPrivacy) {
        this.stuffListPrivacy = stuffListPrivacy;
    }

    public Request withStuffListPrivacy(String stuffListPrivacy){
        this.stuffListPrivacy = stuffListPrivacy;
        return this;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Request withDecision(String decision){
        this.decision = decision;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Request withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Request withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getEmailPrivacy() {
        return emailPrivacy;
    }

    public void setEmailPrivacy(String emailPrivacy) {
        this.emailPrivacy = emailPrivacy;
    }

    public Request withEmailPrivacy(String emailPrivacy) {
        this.emailPrivacy = emailPrivacy;
        return this;
    }

    public String getPhonePrivacy() {
        return phonePrivacy;
    }

    public void setPhonePrivacy(String phonePrivacy) {
        this.phonePrivacy = phonePrivacy;
    }

    public Request withPhonePrivacy(String phonePrivacy) {
        this.phonePrivacy = phonePrivacy;
        return this;
    }

    public String getLocationPrivacy() {
        return locationPrivacy;
    }

    public void setLocationPrivacy(String locationPrivacy) {
        this.locationPrivacy = locationPrivacy;
    }

    public Request withLocationPrivacy(String locationPrivacy) {
        this.locationPrivacy = locationPrivacy;
        return this;
    }

    public String getSocialConnectionPrivacy() {
        return socialConnectionPrivacy;
    }

    public void setSocialConnectionPrivacy(String socialConnectionPrivacy) {
        this.socialConnectionPrivacy = socialConnectionPrivacy;
    }

    public Request withSocialConnectionPrivacy(String socialConnectionPrivacy) {
        this.socialConnectionPrivacy = socialConnectionPrivacy;
        return this;
    }

    public String getRelationshipPrivacy() {
        return relationshipPrivacy;
    }

    public void setRelationshipPrivacy(String relationshipPrivacy) {
        this.relationshipPrivacy = relationshipPrivacy;
    }

    public Request withRelationshipPrivacy(String relationshipPrivacy) {
        this.relationshipPrivacy = relationshipPrivacy;
        return this;
    }

    public String getDobPrivacy() {
        return dobPrivacy;
    }

    public void setDobPrivacy(String dobPrivacy) {
        this.dobPrivacy = dobPrivacy;
    }

    public Request withDobPrivacy(String dobPrivacy) {
        this.dobPrivacy = dobPrivacy;
        return this;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public Request withReferralCode(String referralCode) {
        this.referralCode = referralCode;
        return this;
    }


    public String getStuffId() {
        return stuffId;
    }

    public void setStuffId(String stuffId) {
        this.stuffId = stuffId;
    }

    public Request withStuffId(String stuffId) {
        this.stuffId = stuffId;
        return this;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Request withCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getStuffText() {
        return stuffText;
    }

    public void setStuffText(String stuffText) {
        this.stuffText = stuffText;
    }

    public Request withStuffText(String stuffText) {
        this.stuffText = stuffText;
        return this;
    }

    public String getStuffPrivacy() {
        return stuffPrivacy;
    }

    public void setStuffPrivacy(String stuffPrivacy) {
        this.stuffPrivacy = stuffPrivacy;
    }

    public Request withStuffPrivacy(String stuffPrivacy) {
        this.stuffPrivacy = stuffPrivacy;
        return this;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Request withCategory(String category) {
        this.category = category;
        return this;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }


    public Request withItem(String item) {
        this.item = item;
        return this;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Request withDistance(String distance) {
        this.distance = distance;
        return this;
    }

    public String getSelectedStuff() {
        return selectedStuff;
    }

    public void setSelectedStuff(String selectedStuff) {
        this.selectedStuff = selectedStuff;
    }


    public Request withSelectedStuff(String selectedStuff) {
        this.selectedStuff = selectedStuff;
        return this;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Request withTastType(String taskType){
        this.taskType = taskType;
        return this;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Request withTaskId(String taskId){
        this.taskId = taskId;
        return this;
    }
    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public Request withBuy(String buy) {
        this.buy = buy;
        return this;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }


    public Request withRent(String rent) {
        this.rent = rent;
        return this;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Request withBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
        return this;
    }

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Request withRentPrice(String rentPrice){
        this.rentPrice = rentPrice;
        return this;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Request withPaymentId(String paymentId){
        this.paymentId = paymentId;
        return this;
    }

    public String getPaymentAccepted() {
        return paymentAccepted;
    }

    public void setPaymentAccepted(String paymentAccepted) {
        this.paymentAccepted = paymentAccepted;
    }

    public Request withPaymentAccepted(String paymentAccepted){
        this.paymentAccepted = paymentAccepted;
        return this;
    }


    public String getSortby() {
        return sortby;
    }

    public void setSortby(String sortby) {
        this.sortby = sortby;
    }

    public Request withSortby(String sortby){
        this.sortby = sortby;
        return this;
    }
}