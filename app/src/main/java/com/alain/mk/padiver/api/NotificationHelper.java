package com.alain.mk.padiver.api;

import com.alain.mk.padiver.models.Message;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class NotificationHelper {

    public static final String COLLECTION_NAME = "notification";

    public static Task<DocumentReference> createMessage(String textmessage, String senderId, String uid) {
        Message message = new Message(textmessage, senderId);

        return UserHelper.getUsersCollection()
                .document(uid)
                .collection(COLLECTION_NAME)
                .add(message);
    }
}
