package com.alain.mk.padiver.post;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.PostHelper;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.base.BaseActivity;
import com.alain.mk.padiver.home.HomeActivity;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PostActivity extends BaseActivity {

    @BindView(R.id.activity_post_edit_text_title) EditText editTitle;
    @BindView(R.id.activity_post_edit_tags) NachoTextView editTags;
    @BindView(R.id.activity_post_edit_text_description) EditText editDescription;
    @BindView(R.id.activity_post_image_view) ImageView editImageView;

    // STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;

    // Uri of image selected by user
    private Uri uriImageSelected;
    private static final int RC_CHOOSE_PHOTO = 200;

    private ProgressDialog progressDialog;
    @Nullable
    private User modelCurrentUser;

    public static final int REGISTER_MEMBER = 30;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        this.configureToolbar();
        this.configureTags();
        this.getCurrentUserFromFirestore();
    }

    @Override
    public int getFrameLayout() {
        return R.layout.activity_post;
    }

    @OnClick(R.id.activity_post_floating_action_button)
    // Ask permission when accessing to this listener
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickAddImage() { this.chooseImageFromPhone(); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_post_check:
                this.createPostInFirestore();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Calling the appropriate method after activity result
        this.handleResponse(requestCode, resultCode, data);
    }

    private void configureTags() {
        List<String> items = new ArrayList<>();
        items.add(editTags.getText().toString());
        editTags.setText(items);
        editTags.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
    }

    // --------------------
    // ACTIONS
    // --------------------

    private void createPostInFirestore() {

        if (TextUtils.isEmpty(editTitle.getText())) {

            editTitle.setError("Ce champ n'est peut être vide");
            editTitle.setFocusable(true);
        } else if (TextUtils.isEmpty(editTags.getText())) {

            editTags.setError("Ce champ n'est peut être vide");
            editTags.setFocusable(true);
        } else if (TextUtils.isEmpty(editDescription.getText())) {

            editDescription.setError("Ce champ n'est peut être vide");
            editDescription.setFocusable(true);
        } else {

            progressDialog.setMessage("Publication...");
            progressDialog.show();
            if (this.editImageView.getDrawable() == null) {
                List<String> items = new ArrayList<>();
                items.add(editTags.getText().toString());
                editTags.setText(items);
                editTags.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);

                PostHelper.createPost(editTitle.getText().toString(), editTags.getText().toString(), editDescription.getText().toString(), modelCurrentUser)
                        .addOnSuccessListener(updateUIAfterRESTRequestsCompleted(REGISTER_MEMBER))
                        .addOnFailureListener(this.onFailureListener());
            } else {

                this.uploadPhotoInStorageAndSendMessage(editTitle.getText().toString(), editTags.getText().toString(), editDescription.getText().toString());
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

    private void uploadPhotoInStorageAndSendMessage(final String title, final String tags, final String desc) {

        String uuid = UUID.randomUUID().toString(); // GENERATE UNIQUE STRING
        // UPLOAD TO GCS
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);

        editImageView.setDrawingCacheEnabled(true);
        editImageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) editImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mImageRef.putBytes(data);

        uploadTask.addOnCompleteListener(PostActivity.this,
            task -> {
                if (task.isSuccessful()) {
                    task.getResult().getMetadata().getReference().getDownloadUrl()
                        .addOnCompleteListener(PostActivity.this,
                            task1 -> {
                                if (task1.isSuccessful()) {

                                    String pathImageSavedInFirebase = task1.getResult().toString();
                                    // SAVE MESSAGE IN FIRESTORE
                                    PostHelper.createPostWithImage(title, tags, desc, modelCurrentUser, pathImageSavedInFirebase)
                                            .addOnSuccessListener(this.updateUIAfterRESTRequestsCompleted(REGISTER_MEMBER))
                                            .addOnFailureListener(this.onFailureListener());
                                }
                            });
                }
            }).addOnFailureListener(this.onFailureListener());
    }


    // --------------------
    // UI
    // --------------------

    // Create OnCompleteListener called after tasks ended
    private OnSuccessListener<DocumentReference> updateUIAfterRESTRequestsCompleted(final int origin){
        return aVoid -> {
            switch (origin){
                case REGISTER_MEMBER:
                    progressDialog.dismiss();
                    startActivity(new Intent(PostActivity.this, HomeActivity.class));
                    break;
                default:
                    break;
            }
        };
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
                        .into(this.editImageView);
            } else {
                Toast.makeText(this, getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
