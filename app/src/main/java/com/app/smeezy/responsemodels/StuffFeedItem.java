package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StuffFeedItem implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("activity_id")
    @Expose
    private String activityId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("stuff_id")
    @Expose
    private String stuffId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("stuff_text")
    @Expose
    private String stuffText;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("options")
    @Expose
    private String options;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
    @SerializedName("updated_on")
    @Expose
    private String updatedOn;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_slug")
    @Expose
    private String categorySlug;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("location_address")
    @Expose
    private String locationAddress;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("city_code")
    @Expose
    private String cityCode;
    @SerializedName("region_code")
    @Expose
    private String regionCode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("stuff_share_privacy")
    @Expose
    private String stuffSharePrivacy;
    @SerializedName("stuff_trade_privacy")
    @Expose
    private String stuffTradePrivacy;
    @SerializedName("allowed_users")
    @Expose
    private String allowedUsers;
    @SerializedName("distance_display")
    @Expose
    private String distanceDisplay;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("friend")
    @Expose
    private Integer friend;
    @SerializedName("allowed")
    @Expose
    private Integer allowed;
    @SerializedName("total_comments")
    @Expose
    private Integer totalComments;
    @SerializedName("is_bookmark")
    @Expose
    private String isBookmark;
    @SerializedName("location_latitude")
    @Expose
    private String locationLatitude;
    @SerializedName("location_longitude")
    @Expose
    private String locationLongitude;
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
    @SerializedName("payment_accepted")
    @Expose
    private String paymentAccepted;






    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StuffFeedItem withId(String id) {
        this.id = id;
        return this;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public StuffFeedItem withActivityId(String activityId) {
        this.activityId = activityId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public StuffFeedItem withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getStuffId() {
        return stuffId;
    }

    public void setStuffId(String stuffId) {
        this.stuffId = stuffId;
    }

    public StuffFeedItem withStuffId(String stuffId) {
        this.stuffId = stuffId;
        return this;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public StuffFeedItem withCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getStuffText() {
        return stuffText;
    }

    public void setStuffText(String stuffText) {
        this.stuffText = stuffText;
    }

    public StuffFeedItem withStuffText(String stuffText) {
        this.stuffText = stuffText;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public StuffFeedItem withImage(String image) {
        this.image = image;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StuffFeedItem withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public StuffFeedItem withOptions(String options) {
        this.options = options;
        return this;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public StuffFeedItem withAddedOn(String addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public StuffFeedItem withUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StuffFeedItem withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public StuffFeedItem withIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public StuffFeedItem withCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public StuffFeedItem withCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StuffFeedItem withName(String name) {
        this.name = name;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public StuffFeedItem withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public StuffFeedItem withLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public StuffFeedItem withCity(String city) {
        this.city = city;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public StuffFeedItem withRegion(String region) {
        this.region = region;
        return this;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public StuffFeedItem withCityCode(String cityCode) {
        this.cityCode = cityCode;
        return this;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public StuffFeedItem withRegionCode(String regionCode) {
        this.regionCode = regionCode;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public StuffFeedItem withCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public StuffFeedItem withCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StuffFeedItem withPincode(String pincode) {
        this.pincode = pincode;
        return this;
    }

    public String getStuffSharePrivacy() {
        return stuffSharePrivacy;
    }

    public void setStuffSharePrivacy(String stuffSharePrivacy) {
        this.stuffSharePrivacy = stuffSharePrivacy;
    }

    public StuffFeedItem withStuffSharePrivacy(String stuffSharePrivacy) {
        this.stuffSharePrivacy = stuffSharePrivacy;
        return this;
    }

    public String getStuffTradePrivacy() {
        return stuffTradePrivacy;
    }

    public void setStuffTradePrivacy(String stuffTradePrivacy) {
        this.stuffTradePrivacy = stuffTradePrivacy;
    }

    public StuffFeedItem withStuffTradePrivacy(String stuffTradePrivacy) {
        this.stuffTradePrivacy = stuffTradePrivacy;
        return this;
    }

    public String getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers(String allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    public StuffFeedItem withAllowedUsers(String allowedUsers) {
        this.allowedUsers = allowedUsers;
        return this;
    }

    public String getDistanceDisplay() {
        return distanceDisplay;
    }

    public void setDistanceDisplay(String distanceDisplay) {
        this.distanceDisplay = distanceDisplay;
    }

    public StuffFeedItem withDistanceDisplay(String distanceDisplay) {
        this.distanceDisplay = distanceDisplay;
        return this;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public StuffFeedItem withDistance(String distance) {
        this.distance = distance;
        return this;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public StuffFeedItem withProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public Integer getFriend() {
        return friend;
    }

    public void setFriend(Integer friend) {
        this.friend = friend;
    }

    public StuffFeedItem withFriend(Integer friend) {
        this.friend = friend;
        return this;
    }

    public Integer getAllowed() {
        return allowed;
    }

    public void setAllowed(Integer allowed) {
        this.allowed = allowed;
    }

    public StuffFeedItem withAllowed(Integer allowed) {
        this.allowed = allowed;
        return this;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    public StuffFeedItem withTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
        return this;
    }

    public String getIsBookmark() {
        return isBookmark;
    }

    public void setIsBookmark(String isBookmark) {
        this.isBookmark = isBookmark;
    }

    public StuffFeedItem withIsBookmark(String isBookmark){
        this.isBookmark = isBookmark;
        return this;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public StuffFeedItem withLocationLatitude(String locationLatitude){
        this.locationLatitude = locationLatitude;
        return this;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public StuffFeedItem withLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
        return this;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public StuffFeedItem withBuy(String buy) {
        this.buy = buy;
        return this;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }


    public StuffFeedItem withRent(String rent) {
        this.rent = rent;
        return this;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public StuffFeedItem withBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
        return this;
    }

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public StuffFeedItem withRentPrice(String rentPrice){
        this.rentPrice = rentPrice;
        return this;
    }

    public String getPaymentAccepted() {
        return paymentAccepted;
    }

    public void setPaymentAccepted(String paymentAccepted) {
        this.paymentAccepted = paymentAccepted;
    }

    public StuffFeedItem withPaymentAccepted(String paymentAccepted){
        this.paymentAccepted = paymentAccepted;
        return this;
    }
}