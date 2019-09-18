package com.alain.mk.padiver.api;

import com.alain.mk.padiver.models.Post;
import com.alain.mk.padiver.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PostHelper {

    public static final String COLLECTION_NAME = "posts";

    public static CollectionReference getCollectionReference() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Query getPostCollection() {

        return FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated", Query.Direction.DESCENDING);
    }

    public static Query getPostCollectionOrderByUser(String uid) {

        return FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME)
                .whereEqualTo("uid", uid);
    }

    public static Task<DocumentReference> createPost(String uid, String title, String tags, String description, User userSender) {

        Post post = new Post(uid, title, tags, description, userSender);
        return PostHelper.getCollectionReference().add(post);
    }

    public static Task<DocumentReference> createPostWithImage(String uid, String title, String tags, String description, User userSender, String urlImage) {

        Post post = new Post(uid, title, tags, description, userSender, urlImage);
        return PostHelper.getCollectionReference().add(post);
    }
}
