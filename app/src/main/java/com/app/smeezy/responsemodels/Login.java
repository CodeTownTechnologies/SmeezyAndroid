package com.app.smeezy.responsemodels;

import java.io.Serializable;

public class Login implements Serializable {

    public static final String NORMAL = "normal";
    public static final String GOOGLE = "google";
    public static final String FACEBOOK = "facebook";

    private String socialId;
    private String name;
    private String email;
    private String imageUrl;
    private String loginType;

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public Login withSocialId(String socialId){
        this.socialId = socialId;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Login withName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Login withEmail(String email){
        this.email = email;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Login withImageUrl(String  imageUrl){
        this.imageUrl = imageUrl;
        return this;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Login withLoginType(String loginType) {
        this.loginType = loginType;
        return this;
    }
}
