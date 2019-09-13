package com.alain.mk.padiver.api;

import com.alain.mk.padiver.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserHelper {

    public static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String email, String urlPicture, String tokenId, String deviceToken) {
        User userToCreate = new User(uid, username, email, urlPicture, tokenId, deviceToken);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateInfoUser(String uid, String username, String deviceToken, String phoneNumber, String address, String language, String bio, String hobbies, String webSite, String githubLink) {

        Map<String, Object> usertMap = new HashMap<>();

        usertMap.put("username", username);
        usertMap.put("deviceToken", deviceToken);
        usertMap.put("phoneNumber", phoneNumber);
        usertMap.put("address", address);
        usertMap.put("language", language);
        usertMap.put("bio", bio);
        usertMap.put("hobbies", hobbies);
        usertMap.put("webSite", webSite);
        usertMap.put("githubLink", githubLink);

        return UserHelper.getUsersCollection().document(uid).update(usertMap);
    }

    public static Task<Void> updateInfoUserWithImage(String uid, String username, String urlPicture, String deviceToken, String phoneNumber, String address, String language, String bio, String hobbies, String webSite, String githubLink) {

        Map<String, Object> usertMap = new HashMap<>();

        usertMap.put("username", username);
        usertMap.put("urlPicture", urlPicture);
        usertMap.put("deviceToken", deviceToken);
        usertMap.put("phoneNumber", phoneNumber);
        usertMap.put("address", address);
        usertMap.put("language", language);
        usertMap.put("bio", bio);
        usertMap.put("hobbies", hobbies);
        usertMap.put("webSite", webSite);
        usertMap.put("githubLink", githubLink);

        return UserHelper.getUsersCollection().document(uid).update(usertMap);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }
}
