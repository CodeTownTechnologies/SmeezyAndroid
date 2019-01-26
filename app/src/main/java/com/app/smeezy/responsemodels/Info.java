package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Info implements Serializable {

    @SerializedName("site_name")
    @Expose
    private String siteName;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("video:url")
    @Expose
    private String videoUrl;
    @SerializedName("video:secure_url")
    @Expose
    private String videoSecureUrl;
    @SerializedName("video:type")
    @Expose
    private String videoType;
    @SerializedName("video:width")
    @Expose
    private String videoWidth;
    @SerializedName("video:height")
    @Expose
    private String videoHeight;
    @SerializedName("video:tag")
    @Expose
    private String videoTag;
    @SerializedName("host")
    @Expose
    private String host;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Info withSiteName(String siteName) {
        this.siteName = siteName;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Info withUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Info withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Info withImage(String image) {
        this.image = image;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Info withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Info withType(String type) {
        this.type = type;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Info withVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public String getVideoSecureUrl() {
        return videoSecureUrl;
    }

    public void setVideoSecureUrl(String videoSecureUrl) {
        this.videoSecureUrl = videoSecureUrl;
    }

    public Info withVideoSecureUrl(String videoSecureUrl) {
        this.videoSecureUrl = videoSecureUrl;
        return this;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public Info withVideoType(String videoType) {
        this.videoType = videoType;
        return this;
    }

    public String getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(String videoWidth) {
        this.videoWidth = videoWidth;
    }

    public Info withVideoWidth(String videoWidth) {
        this.videoWidth = videoWidth;
        return this;
    }

    public String getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(String videoHeight) {
        this.videoHeight = videoHeight;
    }

    public Info withVideoHeight(String videoHeight) {
        this.videoHeight = videoHeight;
        return this;
    }

    public String getVideoTag() {
        return videoTag;
    }

    public void setVideoTag(String videoTag) {
        this.videoTag = videoTag;
    }

    public Info withVideoTag(String videoTag) {
        this.videoTag = videoTag;
        return this;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Info withHost(String host) {
        this.host = host;
        return this;
    }

}