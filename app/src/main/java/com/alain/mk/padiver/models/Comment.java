package com.alain.mk.padiver.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Comment {

    private String comment;
    private User userSender;
    private String urlImage;
    private Date dateCreated;

    public Comment() {
    }

    public Comment(String comment, User userSender) {
        this.comment = comment;
        this.userSender = userSender;
    }

    public Comment(String comment, User userSender, String urlImage) {
        this.comment = comment;
        this.userSender = userSender;
        this.urlImage = urlImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
