package com.alain.mk.padiver.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Like {

    private String uid;
    private Date dateCreated;

    public Like() {
    }

    public Like(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
