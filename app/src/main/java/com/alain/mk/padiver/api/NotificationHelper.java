package com.alain.mk.padiver.api;

import com.alain.mk.padiver.models.Notification;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class NotificationHelper {

    public static final String COLLECTION_NAME = "notification";

    public static Task<DocumentReference> createMessage(String textmessage, String senderUsername, String receiveUsername, String token, String uid) {
        Notification notification = new Notification(senderUsername, receiveUsername, textmessage, token);

        return UserHelper.getUsersCollection()
                .document(uid)
                .collection(COLLECTION_NAME)
                .add(notification);
    }

    public static Task<DocumentReference> createMessageWithImage(String imageUrl, String textmessage, String senderUsername, String receiveUsername, String token, String uid) {
        Notification notification = new Notification(senderUsername, receiveUsername, textmessage, imageUrl, token);

        return UserHelper.getUsersCollection()
                .document(uid)
                .collection(COLLECTION_NAME)
                .add(notification);
    }
}
