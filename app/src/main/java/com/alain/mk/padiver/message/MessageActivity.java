package com.alain.mk.padiver.message;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.ConvHelper;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.base.BaseActivity;
import com.alain.mk.padiver.models.Message;
import com.alain.mk.padiver.api.MessageHelper;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MessageActivity extends BaseActivity implements MessageAdapter.Listener{

    // FOR DESIGN
    // Getting all views needed
    @BindView(R.id.activity_message_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.activity_message_edit_text) TextInputEditText editTextMessage;
    @BindView(R.id.activity_message_text_view_recycler_view_empty) TextView textViewRecyclerViewEmpty;
    @BindView(R.id.activity_message_image_chosen_preview) ImageView imageViewPreview;
    @BindView(R.id.activity_message_text_name) TextView textViewUsername;
    @BindView(R.id.activity_message_image_profile) ImageView imageViewProfile;


    public static final String BUNDLE_KEY_PROJECT_ID = "uid";
    public static final String BUNDLE_KEY_PROJECT_IMAGE_URL = "urlPicture";
    public static final String BUNDLE_KEY_PROJECT_USERNAME = "username";

    // STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;

    // Uri of image selected by user
    private Uri uriImageSelected;
    private static final int RC_CHOOSE_PHOTO = 200;

    // FOR DATA
    private MessageAdapter messageAdapter;
    @Nullable
    private User modelCurrentUser;
    @Nullable
    private User modelReceiveUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uid = getIntent().getStringExtra("uid");
        this.getCurrentUserFromFirestore();
        this.getCurrentUserReceiveFromFirestore();
        this.updateUIWhenCreating();
        this.configureRecyclerView(this.getCurrentUser().getUid(), uid);
    }

    @Override
    public int getFrameLayout() {
        return R.layout.activity_message;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Calling the appropriate method after activity result
        this.handleResponse(requestCode, resultCode, data);
    }

    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.activity_message_button_back)
    public void onClickBackButton() {
        onBackPressed();
    }

    @OnClick(R.id.activity_message_add_file_button)
    // Ask permission when accessing to this listener
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickAddFile() { this.chooseImageFromPhone(); }

    @OnClick(R.id.activity_message_send_button)
    // Depending if an image is set, we'll send different Message to Firestore
    public void onClickSendMessage() {

        if (!TextUtils.isEmpty(editTextMessage.getText()) && modelCurrentUser != null){
            String uid = getIntent().getStringExtra("uid");
            // SEND A TEXT MESSAGE
            if (this.imageViewPreview.getDrawable() == null) {
                MessageHelper.createFirstMessageForChat(editTextMessage.getText().toString(), this.getCurrentUser().getUid(), uid, modelCurrentUser).addOnFailureListener(this.onFailureListener());
                MessageHelper.createSecondMessageForChat(editTextMessage.getText().toString(), this.getCurrentUser().getUid(), uid, modelCurrentUser).addOnFailureListener(this.onFailureListener());

                ConvHelper.createFirstConv(editTextMessage.getText().toString(), this.getCurrentUser().getUid(), uid, modelReceiveUser).addOnFailureListener(this.onFailureListener());
                ConvHelper.createSecondConv(editTextMessage.getText().toString(), this.getCurrentUser().getUid(), uid, modelCurrentUser).addOnFailureListener(this.onFailureListener());

                this.editTextMessage.setText("");
            }else {
                // SEND A IMAGE + TEXT IMAGE
                this.uploadPhotoInFirebaseAndSendMessage(editTextMessage.getText().toString());
                this.editTextMessage.setText("");
                this.imageViewPreview.setImageDrawable(null);
            }
        }
    }

    // --------------------
    // REST REQUESTS
    // --------------------
    // Get Current User from Firestore
    private void getCurrentUserFromFirestore(){
        UserHelper.getUser(getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> modelCurrentUser = documentSnapshot.toObject(User.class));
    }

    private void getCurrentUserReceiveFromFirestore() {
        String uid = getIntent().getStringExtra("uid");
        UserHelper.getUser(uid).addOnSuccessListener(documentSnapshot -> modelReceiveUser = documentSnapshot.toObject(User.class));
    }

    private void uploadPhotoInFirebaseAndSendMessage(final String message) {
        String uuid = UUID.randomUUID().toString(); // GENERATE UNIQUE STRING
        String uid = getIntent().getStringExtra("uid");
        // UPLOAD TO GCS
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);

        mImageRef.putFile(this.uriImageSelected).addOnCompleteListener(MessageActivity.this,
            task -> {
                if (task.isSuccessful()) {
                    task.getResult().getMetadata().getReference().getDownloadUrl()
                        .addOnCompleteListener(MessageActivity.this,
                            task1 -> {
                                if (task1.isSuccessful()) {

                                    String pathImageSavedInFirebase = task1.getResult().toString();
                                    // SAVE MESSAGE IN FIRESTORE
                                    MessageHelper.createFirstMessageWithImageForChat(pathImageSavedInFirebase, message, this.getCurrentUser().getUid(), uid, modelCurrentUser).addOnFailureListener(onFailureListener());

                                    MessageHelper.createSecondMessageWithImageForChat(pathImageSavedInFirebase, message, this.getCurrentUser().getUid(), uid, modelCurrentUser).addOnFailureListener(onFailureListener());
                                }
                        });
                }
        }).addOnFailureListener(this.onFailureListener());
    }

    // --------------------
    // UI
    // --------------------
    // Configure RecyclerView with a Query
    private void configureRecyclerView(String userSenderId, String userReceiveId) {

        this.messageAdapter = new MessageAdapter(generateOptionsForAdapter(MessageHelper.getAllMessageForChat(userSenderId, userReceiveId)), Glide.with(this), this, this.getCurrentUser().getUid());

        messageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.messageAdapter);
    }

    // Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query){

        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
    }

    private void updateUIWhenCreating() {

        String name = getIntent().getStringExtra("username");
        String image = getIntent().getStringExtra("urlPicture");

        if (image != null) {
            Glide.with(this)
                    .load(image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewProfile);
        }

        this.textViewUsername.setText(name);
    }

    // --------------------
    // FILE MANAGEMENT
    // --------------------

    private void chooseImageFromPhone(){
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), RC_IMAGE_PERMS, PERMS);
            return;
        }
        // Launch an "Selection Image" Activity
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }

    // Handle activity response (after user has chosen or not a picture)
    private void handleResponse(int requestCode, int resultCode, Intent data){
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                this.uriImageSelected = data.getData();
                Glide.with(this) //SHOWING PREVIEW OF IMAGE
                        .load(this.uriImageSelected)
                        .apply(RequestOptions.circleCropTransform())
                        .into(this.imageViewPreview);
            } else {
                Toast.makeText(this, getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // --------------------
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        // Show TextView in case RecyclerView is empty
        textViewRecyclerViewEmpty.setVisibility(this.messageAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}
