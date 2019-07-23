package com.alain.mk.padiver.fragments.user;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersModalFragment extends BottomSheetDialogFragment {

    @BindView(R.id.modal_fragment_users_username) TextView username;
    @BindView(R.id.modal_fragment_users_image_user) ImageView imageProfile;
    @BindView(R.id.modal_fragment_users_email) TextView userMail;
    @BindView(R.id.modal_fragment_users_location) TextView location;
    @BindView(R.id.modal_fragment_users_phone_number) TextView phoneNumber;
    @BindView(R.id.modal_fragment_users_bio) TextView userBio;
    @BindView(R.id.modal_fragment_users_github_link) TextView userGithubLink;
    @BindView(R.id.modal_fragment_users_langage) TextView userLanguage;
    @BindView(R.id.modal_fragment_users_web_site) TextView userWebSite;

    private BottomSheetBehavior behavior;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.modal_fragment_users, null);
        ButterKnife.bind(this, view);

        this.updateUIWhenCreating();
        dialog.setContentView(view);
        behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void updateUIWhenCreating() {

        if (user.getUrlPicture() != null) {
            Glide.with(this)
                    .load(user.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageProfile);
        }

        String phoneNumber = TextUtils.isEmpty(user.getPhoneNumber()) ? getString(R.string.info_no_phone_number) : this.user.getPhoneNumber();
        this.phoneNumber.setText(phoneNumber);

        String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : this.user.getEmail();
        this.userMail.setText(email);

        String address = TextUtils.isEmpty(user.getAddress()) ? getString(R.string.info_no_address_location) : this.user.getAddress();
        this.location.setText(address);

        String githubLink = TextUtils.isEmpty(user.getGithubLink()) ? getString(R.string.info_no_github_link) : this.user.getGithubLink();
        this.userGithubLink.setText(githubLink);

        String bio = TextUtils.isEmpty(user.getBio()) ? getString(R.string.info_no_bio) : this.user.getBio();
        this.userBio.setText(bio);

        String webSite = TextUtils.isEmpty(user.getWebSite()) ? getString(R.string.info_no_web_site) : this.user.getWebSite();
        this.userWebSite.setText(webSite);

        String language = TextUtils.isEmpty(user.getLanguage()) ? getString(R.string.info_no_language) : this.user.getLanguage();
        this.userLanguage.setText(language);

        // 7 - Get additional data from Firestore (isMentor & Username)
        UserHelper.getUser(user.getUid()).addOnSuccessListener(documentSnapshot -> {
            User currentUser = documentSnapshot.toObject(User.class);
            String username = TextUtils.isEmpty(currentUser.getUsername()) ? getString(R.string.info_no_username_found) : currentUser.getUsername();
            this.username.setText(username);
        });
    }

}
