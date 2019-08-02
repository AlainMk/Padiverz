package com.alain.mk.padiver.message;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.ConvHelper;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.base.BaseActivity;
import com.alain.mk.padiver.models.Conversation;
import com.alain.mk.padiver.models.Message;
import com.alain.mk.padiver.api.MessageHelper;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

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

    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.activity_message_button_back)
    public void onClickBackButton() {
        onBackPressed();
    }

    @OnClick(R.id.activity_message_send_button)
    // Depending if an image is set, we'll send different Message to Firestore
    public void onClickSendMessage() {

        if (!TextUtils.isEmpty(editTextMessage.getText()) && modelCurrentUser != null){
            String uid = getIntent().getStringExtra("uid");
                // SEND A TEXT MESSAGE
            MessageHelper.createFirstMessageForChat(editTextMessage.getText().toString(), this.getCurrentUser().getUid(), uid, modelCurrentUser).addOnFailureListener(this.onFailureListener());
            MessageHelper.createSecondMessageForChat(editTextMessage.getText().toString(), this.getCurrentUser().getUid(), uid, modelCurrentUser).addOnFailureListener(this.onFailureListener());

            ConvHelper.createFirstConv(editTextMessage.getText().toString(), this.getCurrentUser().getUid(), uid, modelReceiveUser).addOnFailureListener(this.onFailureListener());
            ConvHelper.createSecondConv(editTextMessage.getText().toString(), this.getCurrentUser().getUid(), uid, modelCurrentUser).addOnFailureListener(this.onFailureListener());

            this.editTextMessage.setText("");
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
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        // Show TextView in case RecyclerView is empty
        textViewRecyclerViewEmpty.setVisibility(this.messageAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}
