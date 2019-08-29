package com.alain.mk.padiver.api;

import com.alain.mk.padiver.models.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class PostHelper {

    public static final String COLLECTION_NAME = "posts";

    public static CollectionReference getCollectionReference() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Query getPostCollection() {

        return FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated");
    }

    public static Task<DocumentReference> createPost(String title, List<String> tags, String description, String userId) {

        Post post = new Post(title, tags, description, userId);
        return PostHelper.getCollectionReference().add(post);
    }

    public static Task<DocumentReference> createPostWithImage(String title, List<String> tags, String description, String userId, String urlImage) {

        Post post = new Post(title, tags, description, userId, urlImage);
        return PostHelper.getCollectionReference().add(post);
    }
}
