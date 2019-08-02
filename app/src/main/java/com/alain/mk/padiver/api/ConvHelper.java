package com.alain.mk.padiver.api;

import com.alain.mk.padiver.models.Message;
import com.alain.mk.padiver.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ConvHelper {

    public static final String COLLECTION_NAME = "conv";
    public static final String COLLECTION_CHAT = "chats";

    public static CollectionReference getConvCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Query getAllConversation(String uid) {

        return ConvHelper.getConvCollection()
                .document(uid)
                .collection(COLLECTION_CHAT)
                .orderBy("dateCreated");
    }

    public static Task<Void> createFirstConv(String textMessage, String userSenderId, String userReceiveId, User userSender) {

        Message message = new Message(textMessage, userSenderId, userReceiveId, userSender);

        return ConvHelper.getConvCollection()
                .document(userSenderId)
                .collection(COLLECTION_CHAT)
                .document(userReceiveId)
                .set(message);
    }

    public static Task<Void> createSecondConv(String textMessage, String userSenderId, String userReceiveId, User userSender) {

        Message message = new Message(textMessage, userSenderId, userReceiveId, userSender);
        return ConvHelper.getConvCollection()
                .document(userReceiveId)
                .collection(COLLECTION_CHAT)
                .document(userSenderId)
                .set(message);
    }
}
