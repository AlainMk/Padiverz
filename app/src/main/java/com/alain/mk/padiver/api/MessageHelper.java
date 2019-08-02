package com.alain.mk.padiver.api;

import com.alain.mk.padiver.models.Message;
import com.alain.mk.padiver.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;


public class MessageHelper {

    // --- GET---

    public static Query getAllMessageForChat(String senderId, String receiveId){

        return ChatHelper.getChatCollection()
                .document(senderId)
                .collection(receiveId)
                .orderBy("dateCreated");
    }

    // --- CREATE MESSAGE ---

    public static Task<DocumentReference> createFirstMessageForChat(String textMessage, String userSenderId, String userReceiveId, User userSender) {

        Message message = new Message(textMessage, userSenderId, userReceiveId, userSender);

        return ChatHelper.getChatCollection()
                .document(userSenderId)
                .collection(userReceiveId)
                .add(message);
    }

    public static Task<DocumentReference> createSecondMessageForChat(String textMessage, String userSenderId, String userReceiveId, User userSender) {

        Message message = new Message(textMessage, userSenderId, userReceiveId, userSender);

        return ChatHelper.getChatCollection()
                .document(userReceiveId)
                .collection(userSenderId)
                .add(message);
    }

    // --- CREATE MESSAGE WITH IMAGE ---

    public static Task<DocumentReference> createMessageWithImageForChat(String texteMessage, String userSenderId, String userReceiveId, String urlImage, User userSender) {

        Message message = new Message(texteMessage, userSenderId, userReceiveId, urlImage, userSender);

        return ChatHelper.getChatCollection()
                .document(userSenderId)
                .collection(userReceiveId)
                .add(message);
    }

}
