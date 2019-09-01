package com.alain.mk.padiver.api;

import com.alain.mk.padiver.models.Like;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ArticleHelper {

    public static final String COLLECTION_REFERENCE = "article";
    public static final String DOCUMENT_LIKE = "likes";
    public static final String DOCUMENT_COMMENT = "comments";

    public static CollectionReference getCollectionReference() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_REFERENCE);
    }

    public static CollectionReference getLikeReference(String article) {
        return ArticleHelper.getCollectionReference()
                .document(DOCUMENT_LIKE)
                .collection(article);
    }

    public static Task<Void> createLike(String uid, String articleName) {

        Like like = new Like(uid);
        return ArticleHelper.getLikeReference(articleName)
                .document(uid)
                .set(like);
    }

    public static Task<Void> deleteLike(String uid, String articleName) {

        return ArticleHelper.getLikeReference(articleName)
                .document(uid)
                .delete();
    }
}
