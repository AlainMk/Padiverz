package com.alain.mk.padiver.controllers.activities.auth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.base.BaseActivity;
import com.alain.mk.padiver.controllers.activities.home.HomeActivity;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ActivityEditProfile extends BaseActivity {

    @BindView(R.id.activity_edit_profile_edit_text_username) AppCompatEditText editTextUsername;
    @BindView(R.id.activity_edit_profile_edit_text_about) AppCompatEditText editTextAbout;
    @BindView(R.id.activity_edit_profile_edit_text_hobbies) AppCompatEditText editTextHobbies;
    @BindView(R.id.activity_edit_profile_edit_text_location) AppCompatEditText editTextLocation;
    @BindView(R.id.activity_edit_profile_edit_text_skills) AppCompatEditText editTextSkills;
    @BindView(R.id.activity_edit_profile_edit_text_web_site) AppCompatEditText editTextWebsite;
    @BindView(R.id.activity_edit_profile_edit_text_git_hub_link) AppCompatEditText editTextGithubLink;
    @BindView(R.id.activity_edit_profile_edit_text_phone_number) AppCompatEditText editTextPhone;
    @BindView(R.id.activity_edit_profile_image_view_profile) ImageView editImageView;

    // STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;

    // Uri of image selected by user
    private Uri uriImageSelected;
    private static final int RC_CHOOSE_PHOTO = 200;

    private ProgressDialog progressDialog;

    public static final int REGISTER_MEMBER = 30;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        this.updateUIWhenCreating();
    }

    @Override
    public int getFrameLayout() {
        return R.layout.activity_edit_profile;
    }

    @OnClick(R.id.activity_edit_profile_floating_action_button)
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
                this.updateUserInFirestore();
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

    // --------------------
    // ACTIONS
    // --------------------

    private void updateUserInFirestore() {

        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        progressDialog.setMessage("Publication...");
        progressDialog.show();
        if (this.editImageView.getDrawable() == null) {

            UserHelper.updateInfoUser(this.getCurrentUser().getUid(), editTextUsername.getText().toString(), deviceToken, editTextPhone.getText().toString(),
                                        editTextLocation.getText().toString(), editTextSkills.getText().toString(), editTextAbout.getText().toString(), editTextHobbies.getText().toString(),
                                        editTextWebsite.getText().toString(), editTextGithubLink.getText().toString())
                    .addOnSuccessListener(updateUIAfterRESTRequestsCompleted(REGISTER_MEMBER))
                    .addOnFailureListener(this.onFailureListener());
        } else {

            this.uploadPhotoInStorageAndSendMessage(editTextUsername.getText().toString(), deviceToken, editTextPhone.getText().toString(),
                    editTextLocation.getText().toString(), editTextSkills.getText().toString(), editTextAbout.getText().toString(), editTextHobbies.getText().toString(),
                    editTextWebsite.getText().toString(), editTextGithubLink.getText().toString());
        }

    }

    private void uploadPhotoInStorageAndSendMessage(String username, String deviceToken, String phoneNumber, String address, String language, String bio, String hobbies, String webSite, String githubLink) {

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

        uploadTask.addOnCompleteListener(this,
                task -> {
                    if (task.isSuccessful()) {
                        task.getResult().getMetadata().getReference().getDownloadUrl()
                                .addOnCompleteListener(this,
                                        task1 -> {
                                            if (task1.isSuccessful()) {

                                                String pathImageSavedInFirebase = task1.getResult().toString();
                                                UserHelper.updateInfoUserWithImage(this.getCurrentUser().getUid(), username, pathImageSavedInFirebase, deviceToken, phoneNumber, address, language, bio, hobbies, webSite, githubLink)
                                                        .addOnSuccessListener(updateUIAfterRESTRequestsCompleted(REGISTER_MEMBER))
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
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return aVoid -> {
            switch (origin){
                case REGISTER_MEMBER:
                    progressDialog.dismiss();
                    startActivity(new Intent(this, HomeActivity.class));
                    break;
                default:
                    break;
            }
        };
    }

    private void updateUIWhenCreating() {

        UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {

            User modelCurrentUser = documentSnapshot.toObject(User.class);

            if (modelCurrentUser.getUrlPicture() != null) {
                Glide.with(this)
                        .load(modelCurrentUser.getUrlPicture())
                        .into(editImageView);
            }

            this.editTextPhone.setText(modelCurrentUser.getPhoneNumber());

            this.editTextHobbies.setText(modelCurrentUser.getHobbies());

            this.editTextLocation.setText(modelCurrentUser.getAddress());

            this.editTextGithubLink.setText(modelCurrentUser.getGithubLink());

            this.editTextAbout.setText(modelCurrentUser.getBio());

            this.editTextWebsite.setText(modelCurrentUser.getWebSite());

            this.editTextSkills.setText(modelCurrentUser.getLanguage());

            this.editTextUsername.setText(modelCurrentUser.getUsername());
        });

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
