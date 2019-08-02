package com.alain.mk.padiver.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Conversation {

    private String uid;
    @ServerTimestamp
    private Date date;
    private String username;
    private String lastMessage;
    private String urlImage;

    public Conversation(String uid, Date date, String username, String lastMessage, String urlImage) {
        this.uid = uid;
        this.date = date;
        this.username = username;
        this.lastMessage = lastMessage;
        this.urlImage = urlImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
