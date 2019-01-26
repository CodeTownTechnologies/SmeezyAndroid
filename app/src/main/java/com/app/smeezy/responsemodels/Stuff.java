package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Stuff implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("isSelected")
    @Expose
    private boolean isSelected;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Stuff withId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Stuff withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Stuff withCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Stuff withIsSelected(Boolean isSelected){
        this.isSelected = isSelected;
        return this;
    }

}