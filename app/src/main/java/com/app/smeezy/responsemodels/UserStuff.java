package com.app.smeezy.responsemodels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserStuff implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
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
    @SerializedName("is_selected")
    @Expose
    private boolean isSelected;
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

    public UserStuff withId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserStuff withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getStuffId() {
        return stuffId;
    }

    public void setStuffId(String stuffId) {
        this.stuffId = stuffId;
    }

    public UserStuff withStuffId(String stuffId) {
        this.stuffId = stuffId;
        return this;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public UserStuff withCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getStuffText() {
        return stuffText;
    }

    public void setStuffText(String stuffText) {
        this.stuffText = stuffText;
    }

    public UserStuff withStuffText(String stuffText) {
        this.stuffText = stuffText;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UserStuff withImage(String image) {
        this.image = image;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserStuff withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public UserStuff withOptions(String options) {
        this.options = options;
        return this;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public UserStuff withAddedOn(String addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public UserStuff withUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserStuff withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public UserStuff withIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public UserStuff withCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public UserStuff withIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        return this;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public UserStuff withBuy(String buy) {
        this.buy = buy;
        return this;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }


    public UserStuff withRent(String rent) {
        this.rent = rent;
        return this;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public UserStuff withBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
        return this;
    }

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public UserStuff withRentPrice(String rentPrice){
        this.rentPrice = rentPrice;
        return this;
    }

    public String getPaymentAccepted() {
        return paymentAccepted;
    }

    public void setPaymentAccepted(String paymentAccepted) {
        this.paymentAccepted = paymentAccepted;
    }

    public UserStuff withPaymentAccepted(String paymentAccepted){
        this.paymentAccepted = paymentAccepted;
        return this;
    }

}

