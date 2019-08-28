package com.alain.mk.padiver.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {

    private String message;
    private Date dateCreated;
    private User userSender;
    private String userSenderId;
    private String userReceiveId;
    private String urlImage;

    public Message() {
    }

    public Message(String message, String userSenderId) {
        this.message = message;
        this.userSenderId = userSenderId;
    }

    public Message(String message, String userSenderId, String userReceiveId, User userSender) {
        this.message = message;
        this.userSender = userSender;
        this.userSenderId = userSenderId;
        this.userReceiveId = userReceiveId;
    }

    public Message(String message, String userSenderId, String userReceiveId, String urlImage, User userSender) {
        this.message = message;
        this.userSender = userSender;
        this.userSenderId = userSenderId;
        this.userReceiveId = userReceiveId;
        this.urlImage = urlImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUserSenderId() {
        return userSenderId;
    }

    public void setUserSenderId(String userSenderId) {
        this.userSenderId = userSenderId;
    }

    public String getUserReceiveId() {
        return userReceiveId;
    }

    public void setUserReceiveId(String userReceiveId) {
        this.userReceiveId = userReceiveId;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }
}
