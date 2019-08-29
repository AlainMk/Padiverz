package com.alain.mk.padiver.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class Post {

    private String title;
    private List<String> tags;
    private String description;
    private Date dateCreated;
    private String userId;
    private String urlImage;

    public Post() {
    }

    public Post(String title, List<String> tags, String description,  String userId) {
        this.title = title;
        this.tags = tags;
        this.description = description;
        this.userId = userId;
    }

    public Post(String title, List<String> tags, String description, String userId, String urlImage) {
        this.title = title;
        this.tags = tags;
        this.description = description;
        this.userId = userId;
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
