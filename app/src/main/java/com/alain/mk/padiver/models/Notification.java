package com.alain.mk.padiver.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Notification {

    private String senderUsername;
    private String receiveUsername;
    private String message;
    private String imageUrl;
    private String deviceToken;
    private Date dateCreated;

    public Notification() {
    }

    public Notification(String senderUsername, String receiveUsername, String message, String deviceToken) {
        this.senderUsername = senderUsername;
        this.receiveUsername = receiveUsername;
        this.message = message;
        this.deviceToken = deviceToken;
    }

    public Notification(String senderUsername, String receiveUsername, String message, String imageUrl, String deviceToken) {
        this.senderUsername = senderUsername;
        this.receiveUsername = receiveUsername;
        this.message = message;
        this.imageUrl = imageUrl;
        this.deviceToken = deviceToken;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiveUsername() {
        return receiveUsername;
    }

    public void setReceiveUsername(String receiveUsername) {
        this.receiveUsername = receiveUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
