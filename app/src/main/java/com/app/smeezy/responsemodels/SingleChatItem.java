package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SingleChatItem implements Serializable{

    @SerializedName("thread_id")
    @Expose
    private String threadId;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("receiver_id")
    @Expose
    private String receiverId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("add_date")
    @Expose
    private String addDate;
    @SerializedName("data")
    private ChatData data = new ChatData().withStuffId("")
            .withStuffType("")
            .withAction("")
            .withStuffName("")
            .withSelectedStuff("");
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public SingleChatItem withThreadId(String threadId) {
        this.threadId = threadId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public SingleChatItem withSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public SingleChatItem withReceiverId(String receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SingleChatItem withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public SingleChatItem withAddDate(String addDate) {
        this.addDate = addDate;
        return this;
    }

    public ChatData getData() {
        return data;
    }

    public void setData(ChatData data) {
        this.data = data;
    }

    public SingleChatItem withData(ChatData data) {
        this.data = data;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SingleChatItem withName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public SingleChatItem withImage(String image) {
        this.image = image;
        return this;
    }

}