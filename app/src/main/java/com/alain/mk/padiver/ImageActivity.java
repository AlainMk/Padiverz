package com.alain.mk.padiver;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.base.BaseActivity;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ImageActivity extends BaseActivity {

    @BindView(R.id.fragment_image_container_image_sent_cardview_image)
    ImageView imageViewProfile;


    public static final String BUNDLE_KEY_PROJECT_ID = "uid";
    public static final String BUNDLE_KEY_PROJECT_IMAGE_URL = "urlPicture";
    public static final String BUNDLE_KEY_PROJECT_USERNAME = "username";

    @Nullable
    private User modelCurrentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolbar();
        this.getCurrentUserFromFirestore();
        this.updateUIWhenCreating();
    }

    @Override
    public int getFrameLayout() {
        return R.layout.activity_image;
    }

    @OnClick(R.id.fragment_image_container_image_sent_cardview_image)
    public void onClickImageProfile() {

        this.loadImage();
    }

/*    private void updateUIWhenCreating() {

        String image = getIntent().getStringExtra("urlPicture");

        if (image != null) {
            Glide.with(this)
                    .load(image)
                    .into(imageViewProfile);
        }
    }*/

    private void loadImage() {
        List<String> list = new ArrayList<String>();
        list.add(modelCurrentUser.getUrlPicture());
        new StfalconImageViewer.Builder<>(this, list, (imageView, imageUrl) -> Glide.with(this).load(imageUrl).into(imageView)).show();
    }

    // --------------------
    // REST REQUESTS
    // --------------------
    // Get Current User from Firestore
    private void getCurrentUserFromFirestore(){
        UserHelper.getUser(getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> modelCurrentUser = documentSnapshot.toObject(User.class));
    }

    // 1 - Update UI when activity is creating
    private void updateUIWhenCreating(){

        if (this.getCurrentUser() != null){

            UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
                User currentUser = documentSnapshot.toObject(User.class);
                if (currentUser.getUrlPicture() != null) {
                    Glide.with(this)
                            .load(currentUser.getUrlPicture())
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageViewProfile);
                }
            });
        }
    }
}
