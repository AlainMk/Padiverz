package com.alain.mk.padiver.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post {

    private String title;
    private String tags;
    private String description;
    private Date dateCreated;
    private User userSender;
    private String urlImage;

    public Post() {
    }

    public Post(String title, String tags, String description, User userSender) {
        this.title = title;
        this.tags = tags;
        this.description = description;
        this.userSender = userSender;
    }

    public Post(String title, String tags, String description, User userSender, String urlImage) {
        this.title = title;
        this.tags = tags;
        this.description = description;
        this.userSender = userSender;
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
