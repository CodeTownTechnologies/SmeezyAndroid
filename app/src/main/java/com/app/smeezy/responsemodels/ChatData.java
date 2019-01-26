package com.app.smeezy.responsemodels;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatData implements Serializable {

    @SerializedName("stuff_id")
    @Expose
    private String stuffId;
    @SerializedName("stuff_type")
    @Expose
    private String stuffType;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("stuff_name")
    @Expose
    private String stuffName;
    @SerializedName("selected_stuff")
    @Expose
    private String selectedStuff;


    public String getStuffId() {
        return stuffId;
    }

    public void setStuffId(String stuffId) {
        this.stuffId = stuffId;
    }

    public ChatData withStuffId(String stuffId) {
        this.stuffId = stuffId;
        return this;
    }

    public String getStuffType() {
        return stuffType;
    }

    public void setStuffType(String stuffType) {
        this.stuffType = stuffType;
    }

    public ChatData withStuffType(String stuffType) {
        this.stuffType = stuffType;
        return this;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ChatData withAction(String action) {
        this.action = action;
        return this;
    }

    public String getStuffName() {
        return stuffName;
    }

    public void setStuffName(String stuffName) {
        this.stuffName = stuffName;
    }

    public ChatData withStuffName(String stuffName) {
        this.stuffName = stuffName;
        return this;
    }

    public String getSelectedStuff() {
        return selectedStuff;
    }

    public void setSelectedStuff(String selectedStuff) {
        this.selectedStuff = selectedStuff;
    }

    public ChatData withSelectedStuff(String selectedStuff) {
        this.selectedStuff = selectedStuff;
        return this;
    }
}
