package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InterestItem implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("isSelected")
    @Expose
    private boolean isSelected;




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public InterestItem withTitle(String title) {
        this.title = title;
        return this;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public InterestItem withIsSelected(Boolean isSelected){
        this.isSelected = isSelected;
        return this;
    }

}