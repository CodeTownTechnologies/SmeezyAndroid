package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentOption {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("is_selected")
    @Expose
    private boolean isSelected;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PaymentOption withId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PaymentOption withTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public PaymentOption withIsSelected(boolean isSelected){
        this.isSelected = isSelected;
        return this;
    }
}