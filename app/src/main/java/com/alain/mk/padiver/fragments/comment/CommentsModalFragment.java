package com.alain.mk.padiver.fragments.comment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.alain.mk.padiver.api.ArticleHelper;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.models.Comment;
import com.alain.mk.padiver.models.Post;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

public class CommentsModalFragment extends BottomSheetDialogFragment implements CommentAdapter.Listener{

    @BindView(R.id.modal_fragment_comments_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.modal_fragment_comments_text_view_recycler_view_empty) TextView textViewRecyclerViewEmpty;
    @BindView(R.id.modal_fragment_comments_image_chosen_preview) ImageView imageViewPreview;
    @BindView(R.id.modal_fragment_comments_edit_text) TextInputEditText editTextComment;

    private BottomSheetBehavior behavior;
    private CommentAdapter commentAdapter;

    // STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;

    // Uri of image selected by user
    private Uri uriImageSelected;
    private static final int RC_CHOOSE_PHOTO = 200;

    private Post post;
    @Nullable
    private User modelCurrentUser;

    public void setPost(Post post) {
        this.post = post;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.modal_fragment_comments, null);
        ButterKnife.bind(this, view);

        dialog.setContentView(view);
        behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        this.configureRecyclerView();
        this.getCurrentUserFromFirestore();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
    @OnClick(R.id.modal_fragment_comments_send_button)
    public void onClickSendComment() {

        if (!TextUtils.isEmpty(editTextComment.getText())){
            // SEND A TEXT MESSAGE
            if (this.imageViewPreview.getDrawable() == null) {

                ArticleHelper.createComment(editTextComment.getText().toString(), post.getTitle(), modelCurrentUser).addOnFailureListener(e ->
                    Toast.makeText(getActivity(), "Une erreur inconue " + e, Toast.LENGTH_SHORT).show());
                this.editTextComment.setText("");
            }else {
                // SEND A IMAGE + TEXT IMAGE
                this.uploadPhotoInStorageAndSendMessage(editTextComment.getText().toString());
                this.editTextComment.setText("");
                this.imageViewPreview.setImageDrawable(null);
            }
        }
    }

    @OnClick(R.id.modal_fragment_comments_add_file_button)
    // Ask permission when accessing to this listener
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickAddFile() { this.chooseImageFromPhone(); }

    // --------------------
    // UTILS
    // --------------------

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    // --------------------
    // REST REQUESTS
    // --------------------
    // Get Current User from Firestore
    private void getCurrentUserFromFirestore(){
        UserHelper.getUser(getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> modelCurrentUser = documentSnapshot.toObject(User.class));
    }

    // --------------------
    // UI
    // --------------------
    // Configure RecyclerView with a Query
    private void configureRecyclerView() {

        this.commentAdapter = new CommentAdapter(generateOptionsForAdapter(ArticleHelper.getCommnetsReference(post.getTitle())), Glide.with(this), this);

        commentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(commentAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(this.commentAdapter);
    }

    // Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Comment> generateOptionsForAdapter(Query query){

        return new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .setLifecycleOwner(this)
                .build();
    }

    private void uploadPhotoInStorageAndSendMessage(final String commment) {

        String uuid = UUID.randomUUID().toString(); // GENERATE UNIQUE STRING
        // UPLOAD TO GCS
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);

        this.imageViewPreview.setDrawingCacheEnabled(true);
        this.imageViewPreview.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewPreview.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mImageRef.putBytes(data);

        uploadTask.addOnCompleteListener(getActivity(),
                task -> {
                    if (task.isSuccessful()) {
                        task.getResult().getMetadata().getReference().getDownloadUrl()
                                .addOnCompleteListener(getActivity(),
                                        task1 -> {
                                            if (task1.isSuccessful()) {

                                                String pathImageSavedInFirebase = task1.getResult().toString();
                                                // SAVE MESSAGE IN FIRESTORE
                                                ArticleHelper.createCommentWithImage(commment, post.getTitle(), modelCurrentUser, pathImageSavedInFirebase);
                                            }
                                        });
                    }
                }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), "Une erreur inconnue est survenue", Toast.LENGTH_SHORT).show();
                });
    }

    // --------------------
    // FILE MANAGEMENT
    // --------------------

    private void chooseImageFromPhone(){
        if (!EasyPermissions.hasPermissions(getActivity(), PERMS)) {
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
                        .into(this.imageViewPreview);
            } else {
                Toast.makeText(getActivity(), getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDataChanged() {

        // Show TextView in case RecyclerView is empty
        textViewRecyclerViewEmpty.setVisibility(this.commentAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClickCommentImage(int position) {

        List<String> list = new ArrayList<String>();
        list.add(commentAdapter.getComment(position).getUrlImage());
        new StfalconImageViewer.Builder<>(getActivity(), list, (imageView, imageUrl) -> Glide.with(this).load(imageUrl).into(imageView)).show();
    }
}
